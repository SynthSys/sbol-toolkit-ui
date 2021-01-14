/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author jhay
 */
@SpringBootTest
@AutoConfigureMockMvc
// @WebMvcTest(SynBioHubClientController.class)
//@ExtendWith(SpringExtension.class)
public class SynBioHubClientControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${synbiohub.client.user}")
    private String synBioHubUser;

    @Value("${synbiohub.client.email}")
    private String synBioHubEmail;

    @Value("${synbiohub.client.pass}")
    private String synBioHubPass;

    private final String synBioHubBaseUrl;

    private final String LOGIN_URL;
    private final String USER_API;
    private final String SUBMIT_URL;
    private final String UPDATE_URL;

    @Autowired
    public SynBioHubClientControllerTest(@Value("${synbiohub.client.baseUrl}") String synBioHubBaseUrl) {
        this.synBioHubBaseUrl = synBioHubBaseUrl;
        LOGIN_URL = synBioHubBaseUrl.concat("login");
        USER_API = synBioHubBaseUrl.concat("users");
        SUBMIT_URL = synBioHubBaseUrl.concat("submit");
        UPDATE_URL = synBioHubBaseUrl.concat("edit");
    }

    HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            final String basicAuth = HttpHeaders.encodeBasicAuth(username, password, StandardCharsets.UTF_8);
            setBasicAuth(basicAuth);
        }};
    }

    @Test
    public void postSubmit() throws Exception {       
        mvc.perform(MockMvcRequestBuilders.post("/submit").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("submit-result"))
                .andExpect(model().attribute("message", "Greetings from Spring Boot!"));
    }

    @Test
    public void postLogin() throws Exception {
        /*final ResponseEntity<String> responseEntity = restTemplate
                .exchange(LOGIN_URL, HttpMethod.POST, new HttpEntity<Void>(createHeaders(synBioHubEmail, synBioHubPass)), String.class);*/
        
        /*HttpEntity<Void> requestEntity = new HttpEntity<>(createHeaders(synBioHubEmail, synBioHubPass));
        final ResponseEntity<String> responseEntity = restTemplate.postForEntity(LOGIN_URL, requestEntity, String.class);
        System.out.println(responseEntity.getBody());
        System.out.println(responseEntity.getHeaders());*/

        JSONObject requestObject = new JSONObject();
        requestObject.put("email", "j.hay@epcc.ed.ac.uk");
        requestObject.put("password", "admin");

        HttpHeaders headers = new HttpHeaders();
        /*headers.setContentType(MediaType.APPLICATION_JSON);
        headers = createHeaders(synBioHubEmail, synBioHubPass);*/
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.TEXT_PLAIN}));
        
        // HttpEntity<String> requestEntity = new HttpEntity<String>(requestObject.toString(), headers);
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
        
        /*final ResponseEntity<String> responseEntity = restTemplate.postForEntity(LOGIN_URL, requestEntity, String.class);
        System.out.println(responseEntity.getBody());
        System.out.println(responseEntity.getHeaders());*/
        
        Map<String, String> params = new HashMap<>();
        params.put("email", synBioHubEmail);
        params.put("password", synBioHubPass);

        // final ResponseEntity<String> responseEntity = restTemplate.execute(LOGIN_URL, HttpMethod.GET, null, null, params);
        final ResponseEntity<String> responseEntity = restTemplate.exchange(LOGIN_URL, HttpMethod.GET, requestEntity, String.class, params);

        System.out.println(responseEntity.getBody());
        //mav.setViewName("submit-result");
    }

    @Test
    public void testSynBioHubLogin() throws JsonProcessingException, UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.TEXT_PLAIN}));

        // https://synbiohub.github.io/api-docs/?plaintext#login
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("email", synBioHubEmail);
        params.add("password", synBioHubPass);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(LOGIN_URL, request, String.class);
        System.out.println("SynBioHub Auth Token: ".concat(response.getBody()));
        System.out.println(response);//<200,[Content-Length:"0", Date:"Fri, 31 May 2019 09:26:24 GMT"]>
    }

    @Test
    public void testSynBioHubSubmit() throws JsonProcessingException, UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.TEXT_PLAIN}));
        // run the login test above to retrieve the token used here:
        headers.add("X-authorization", "02d60ce2-268f-4953-ac8f-d1306561862a");

        // https://synbiohub.github.io/api-docs/?plaintext#submission-endpoints
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("id", "test_id");
        params.add("version", "1.0.0");
        params.add("name", "test_name");
        params.add("description", "test_description");
        params.add("overwrite_merge", "0");

        // Prepare SBOL file resource for upload
        File sbolFile = new File("D:/Temp/sbol/cyano_full_template.xml");
        params.add("file", new FileSystemResource(sbolFile));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(SUBMIT_URL, request, String.class);
        System.out.println(response.getBody());
        //<200,Successfully uploaded,[X-Powered-By:"Express", Content-Type:"text/plain; charset=utf-8", Content-Length:"21", ETag:"W/"15-7awNgShFQs1A/x/gRTR2xR8k+YA"", Date:"Fri, 06 Nov 2020 12:33:44 GMT", Connection:"keep-alive"]>
        System.out.println(response);
    }

    @Test
    public void testSynBioHubUpdate() throws JsonProcessingException, UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.TEXT_PLAIN}));
        // run the login test above to retrieve the token used here:
        headers.add("X-authorization", "02d60ce2-268f-4953-ac8f-d1306561862a");

        String updateUrl = UPDATE_URL.concat("sll00199_left_seq");
        // https://synbiohub.github.io/api-docs/?plaintext#edit-citations
        // title, description, role, wasDerivedFrom, type, annotation
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("previous", "test_id"); // The previous value of the field.
        params.add("object", "1.0.0");     // The new value of the field.
        params.add("pred", "test_name");   // A predicate for an annotation.

        // Prepare SBOL file resource for upload
        File sbolFile = new File("D:/Temp/sbol/cyano_full_template.xml");
        params.add("file", new FileSystemResource(sbolFile));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(SUBMIT_URL, request, String.class);
        System.out.println(response.getBody());
        //<200,Successfully uploaded,[X-Powered-By:"Express", Content-Type:"text/plain; charset=utf-8", Content-Length:"21", ETag:"W/"15-7awNgShFQs1A/x/gRTR2xR8k+YA"", Date:"Fri, 06 Nov 2020 12:33:44 GMT", Connection:"keep-alive"]>
        System.out.println(response);
    }
}
