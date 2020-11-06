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
//@WebMvcTest(SynBioHubClientController.class)
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

    private final String LOGIN_URL = "http://localhost:7777/login";
    private final String USER_API = "http://localhost:7777/users";
    private final String SUBMIT_URL = "http://localhost:7777/submit";
    
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
        System.out.println("WTTTTFFFFFF!!!!!!!!");

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
        params.put("email", "j.hay@epcc.ed.ac.uk");
        params.put("password", "admin");

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

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("email", "j.hay@epcc.ed.ac.uk");
        params.add("password", "admin");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(LOGIN_URL, request, String.class);
        System.out.println(response.getBody());
        System.out.println(response);//<200,[Content-Length:"0", Date:"Fri, 31 May 2019 09:26:24 GMT"]>
    }

    @Test
    public void testSynBioHubSubmit() throws JsonProcessingException, UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.TEXT_PLAIN}));
        // run the login test above to retrieve the token used here:
        headers.add("X-authorization", "394d9e21-49ce-433d-acbc-20effb81cef7");

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
}
