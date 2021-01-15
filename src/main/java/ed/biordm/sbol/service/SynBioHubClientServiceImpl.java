/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.service;

import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    @Value("${synbiohub.client.user}")
    private String synBioHubUser;

    @Value("${synbiohub.client.email}")
    private String synBioHubEmail;

    @Value("${synbiohub.client.pass}")
    private String synBioHubPass;

    private final String synBioHubBaseUrl;

    private final String LOGIN_URL;
    private final String USER_API;
    private final String SUBMIT_API;

    HttpHeaders headers = new HttpHeaders();

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
        SUBMIT_API = synBioHubBaseUrl.concat("submit");
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
        SUBMIT_API = synBioHubBaseUrl.concat("submit");
    }

    HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            final String basicAuth = HttpHeaders.encodeBasicAuth(username, password, StandardCharsets.UTF_8);
            setBasicAuth(basicAuth);
        }};
    }

    @Override
    public void doLogin(String username, String password) {
        /*String credentials = synBioHubUser+":"+synBioHubPass;
        byte[] credentialBytes = credentials .getBytes();
        byte[] base64CredentialBytes = Base64.encodeBase64(credentialBytes);
        String base64Credentials = new String(base64CredentialBytes);
        headers.add("Authorization", "Basic " + base64Credentials );*/
        final ResponseEntity<String> responseEntity = restTemplate
                .exchange(LOGIN_URL, HttpMethod.POST, new HttpEntity<Void>(createHeaders(username, password)), String.class);
        System.out.println(responseEntity.getBody());
    }

    @Override
    public void submitSBOLFiles(String username, String password, String collectionId,
            String dirPath, String fileExtFilter, boolean isOverwrite) throws Exception {
        LOGGER.debug("Username is: {}", username);
        LOGGER.debug("Password is: {}", password);
        LOGGER.debug("Collection ID is: {}", collectionId);
        LOGGER.debug("Directory path is: {}", dirPath);
        LOGGER.debug("File extension filter is: {}", fileExtFilter);
        LOGGER.debug("Overwrite is: {}", isOverwrite);

        if(!headers.containsKey("Authorization")) {
            doLogin(username, password);
        }

        HttpEntity<String> request = new HttpEntity<String>(headers);

        final ResponseEntity<String> responseEntity = restTemplate
                .exchange(SUBMIT_API, HttpMethod.POST, new HttpEntity<Void>(createHeaders(synBioHubUser, synBioHubPass)), String.class);
        System.out.println(responseEntity.getBody());       
        

        /*SimpleMailMessage message = new SimpleMailMessage(); // create message
        message.setFrom(NOREPLY_ADDRESS);                    // compose message
        for (String recipient : to) { message.setTo(recipient); }
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);                           // send message
        */
        //LOGGER.info("Mail to {} sent! Subject: {}, Body: {}", to, subject, text); 
    }
}
