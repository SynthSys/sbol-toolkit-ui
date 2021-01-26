/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author jhay
 */
@Service("synBioHubClientService")
public class SynBioHubClientServiceImpl implements SynBioHubClientService {
    // private static final ch.qos.logback.classic.Logger LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(SynBioHubClientServiceImpl.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(SynBioHubClientServiceImpl.class);

    // @Autowired
    private final RestTemplate restTemplate;

    private final RestTemplateBuilder restTemplateBuilder;

    /*@Value("${synbiohub.client.user}")
    private String synBioHubUser;

    @Value("${synbiohub.client.email}")
    private String synBioHubEmail;

    @Value("${synbiohub.client.pass}")
    private String synBioHubPass;*/

    private final String synBioHubBaseUrl;

    private final String LOGIN_URL;
    private final String USER_API;
    private final String SUBMIT_URL;

    HttpHeaders headers = new HttpHeaders();

    // typical format: 422b6bbf-9a1a-4003-ad20-81f7bf32d1cf
    private static final Pattern AUTH_TOKEN_PATTERN = Pattern.compile("[\\w]{8}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{12}");

    @Override
    public String getServerUrl() {
        return this.synBioHubBaseUrl;
    }

    /* @Override
    public RestTemplate getRestTemplate() {
        return this.restTemplate;
    }*/

    @Override
    public RestTemplateBuilder getRestTemplateBuilder() {
        return this.restTemplateBuilder;
    }

    /*@Override
    public void setServerUrl(String synBioHubBaseUrl) {
        this.synBioHubBaseUrl = synBioHubBaseUrl;
    }*/

    @Autowired
    public SynBioHubClientServiceImpl(RestTemplateBuilder restTemplateBuilder,
            @Value("${synbiohub.client.baseUrl}") String synBioHubBaseUrl) {
        System.out.println("Service is init'd!");
        System.out.println(synBioHubBaseUrl);
        this.restTemplateBuilder = restTemplateBuilder;
        this.restTemplate = this.restTemplateBuilder.build();

        this.synBioHubBaseUrl = synBioHubBaseUrl;
        LOGIN_URL = synBioHubBaseUrl.concat("login");
        USER_API = synBioHubBaseUrl.concat("users");
        SUBMIT_URL = synBioHubBaseUrl.concat("submit");
    }

    // Overloaded constructor to allow a new service impl to be instantiated
    // if the specified server URL is different than the autowired default
    public SynBioHubClientServiceImpl(SynBioHubClientService synBioHubClientService,
            String synBioHubBaseUrl) {
        this.restTemplateBuilder = synBioHubClientService.getRestTemplateBuilder();
        this.restTemplate = this.restTemplateBuilder.build();

        this.synBioHubBaseUrl = synBioHubBaseUrl;
        LOGIN_URL = synBioHubBaseUrl.concat("login");
        USER_API = synBioHubBaseUrl.concat("users");
        SUBMIT_URL = synBioHubBaseUrl.concat("submit");
    }

    protected HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            final String basicAuth = HttpHeaders.encodeBasicAuth(username, password, StandardCharsets.UTF_8);
            setBasicAuth(basicAuth);
        }};
    }

    protected HttpHeaders createLoginHeaders() {
        return new HttpHeaders() {{
            this.setAccept(Collections.singletonList(MediaType.TEXT_PLAIN));
            this.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
        }};
    }

    protected HttpHeaders createAuthHeaders(ResponseEntity<?> responseEntity) {
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setAccept(Collections.singletonList(MediaType.TEXT_PLAIN));
        authHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));

        HttpStatus resStatus = responseEntity.getStatusCode();
        LOGGER.info("Response status for login request: {}", responseEntity.getStatusCodeValue());
        LOGGER.info("Received response for login request: {}", responseEntity.getBody());

        if (resStatus.is2xxSuccessful()) {
            Object resBody = responseEntity.getBody();
            if (resBody != null && resBody.getClass().equals(String.class)) {
                String authToken = resBody.toString();
                LOGGER.debug("Auth Token: {}", authToken);
                if (AUTH_TOKEN_PATTERN.matcher(authToken).matches()) {
                    //authHeaders.setBearerAuth(authToken);
                    authHeaders.set("X-authorization", authToken);
                    //authHeaders.setBasicAuth(authToken);
                }
            }
        } else {
            throw new ResponseStatusException(resStatus, resStatus.getReasonPhrase());
        }

        return authHeaders;
    }

    @Override
    public HttpHeaders doLogin(String email, String password) {
        LOGGER.debug("Attempting login with user email: {}", email);

        MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("email", email);
        requestMap.add("password", password);

        HttpHeaders headers = createLoginHeaders();
        final ParameterizedTypeReference<String> typeReference = new ParameterizedTypeReference<String>() {};
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(requestMap, headers);

        final ResponseEntity<String> responseEntity = restTemplate.exchange(LOGIN_URL, HttpMethod.POST, entity, typeReference);

        HttpStatus resStatus = responseEntity.getStatusCode();
        int resStatusVal = responseEntity.getStatusCodeValue();
        LOGGER.info("Response status for login request: {}", resStatusVal);
        LOGGER.info("Received response for login request: {}", responseEntity.getBody());

        HttpHeaders authHeaders = new HttpHeaders();

        if (resStatus.is2xxSuccessful()) {
            authHeaders = createAuthHeaders(responseEntity);
        } else {
            throw new ResponseStatusException(resStatus, resStatus.getReasonPhrase());
        }

        return authHeaders;
    }

    @Override
    public void submitSBOLFiles(String email, String password, long collectionId,
            String dirPath, String fileExtFilter, boolean isOverwrite) throws Exception {
        LOGGER.debug("Email is: {}", email);
        LOGGER.debug("Password is: {}", password);
        LOGGER.debug("Collection ID is: {}", collectionId);
        LOGGER.debug("Directory path is: {}", dirPath);
        LOGGER.debug("File extension filter is: {}", fileExtFilter);
        LOGGER.debug("Overwrite is: {}", isOverwrite);

 
        /*if(!headers.containsKey("Authorization")) {
            doLogin(username, password);
        }*/
        HttpHeaders authHeaders = doLogin(email, password);

        // SynBioHubClientUploader uploader = new SynBioHubClientUploader();
        List<String> fileNamesList = retrieveFiles(dirPath, fileExtFilter);

        for (String filename: fileNamesList) {
            LOGGER.debug("Current file for upload: {}", filename);

            uploadFile(authHeaders, filename, collectionId);
        }
        /*SimpleMailMessage message = new SimpleMailMessage(); // create message
        message.setFrom(NOREPLY_ADDRESS);                    // compose message
        for (String recipient : to) { message.setTo(recipient); }
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);                           // send message
        */
        //LOGGER.info("Mail to {} sent! Subject: {}, Body: {}", to, subject, text); 
    }

    /**
     * 
     * @param dirPath
     * @param fileExtFilter 
     * @return  
     */
    protected List<String> retrieveFiles(String dirPath, String fileExtFilter) {
        // File directory = new File(dirPath);
        List<String> fileNamesList = new ArrayList<>();

        Predicate<String> fileExtCondition = (String filename) -> {
            if (filename.toLowerCase().endsWith(".".concat(fileExtFilter))) {
                return true;
            }
            return false;
        };

        // Reading the folder and getting Stream.
        try (Stream<Path> walk = Files.walk(Paths.get(dirPath))) {

            // Filtering the paths by a regular file and adding into a list.
            fileNamesList = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).filter(fileExtCondition)
                    .collect(Collectors.toList());

            // printing the file nams
            //fileNamesList.forEach(System.out::println);
        } catch (IOException e) {
            LOGGER.error("Error locating files for upload", e);
        }

        return fileNamesList;
    }

    /**
     * 
     * @param filename
     * @param collectionId
     * @param username
     * @param password
     * @return 
     */
    /*protected boolean uploadFile(String filename, long collectionId, String username,
            String password) {
        if(!headers.containsKey("Authorization")) {
            doLogin(username, password);
        }

        HttpEntity<String> request = new HttpEntity<String>(headers);

        final ResponseEntity<String> responseEntity = restTemplate
                .exchange(SUBMIT_API, HttpMethod.POST, new HttpEntity<Void>(createHeaders(synBioHubUser, synBioHubPass)), String.class);
        System.out.println(responseEntity.getBody());

        return false;
    }*/

    /**
     * 
     * @param headers
     * @param filename
     * @param collectionId
     * @return 
     */
    protected boolean uploadFile(HttpHeaders headers, String filename, long collectionId) {
        boolean success = false;
        HttpEntity<String> request = new HttpEntity(headers);

        File file = new File(filename);
        byte[] fileContent = getFileContent(file);

        HttpHeaders parts = new HttpHeaders();
        parts.setContentType(MediaType.APPLICATION_XML);
        final ByteArrayResource byteArrayResource = new ByteArrayResource(fileContent) {
            @Override
            public String getFilename() {
                return filename;
            }
        };

        final HttpEntity<ByteArrayResource> partsEntity = new HttpEntity<>(byteArrayResource, parts);

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("file", partsEntity);

        final ParameterizedTypeReference<String> typeReference = new ParameterizedTypeReference<String>() {};
        final HttpEntity<MultiValueMap<String, Object>> reqEntity = new HttpEntity<>(requestMap, headers);

        final ResponseEntity<String> responseEntity = restTemplate.exchange(SUBMIT_URL, HttpMethod.POST, reqEntity, typeReference);
        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            // System.out.println("File uploaded = " + responseEntity.getBody().isSuccess());
            success = true;
        }
        /*final ResponseEntity<String> responseEntity = restTemplate
                .exchange(SUBMIT_API, HttpMethod.POST, new HttpEntity<Void>(headers), String.class);
        System.out.println(responseEntity.getBody());*/

        return success;
    }

    /**
     * 
     * @param file
     * @return 
     */
    protected byte[] getFileContent(File file) {
        byte[] fileBytes = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(fileBytes);
        } catch (FileNotFoundException e) {
            LOGGER.error("Unable to find file: {}", file.getAbsolutePath(), e);
        } catch (IOException e) {
            LOGGER.error("Unable to read file: {}", file.getAbsolutePath(), e);
        }
        
        return fileBytes;
    }
}
