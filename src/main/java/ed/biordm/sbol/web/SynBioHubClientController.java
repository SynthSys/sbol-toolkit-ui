/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.web;

import java.nio.charset.StandardCharsets;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author jhay
 */
@RestController
public class SynBioHubClientController {

    private static final Logger logger = LoggerFactory.getLogger(SynBioHubClientController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${synbiohub.client.user}")
    private String synBioHubUser;

    @Value("${synbiohub.client.email}")
    private String synBioHubEmail;

    @Value("${synbiohub.client.pass}")
    private String synBioHubPass;

    // @Value("${synbiohub.client.baseUrl}")
    private String synBioHubBaseUrl;

    /*private final String LOGIN_URL = "http://localhost:7777/login";
    private final String USER_API = "http://localhost:7777/users";
    private final String SUBMIT_URL = "http://localhost:7777/submit";
    private final String UPDATE_URL = "http://localhost:7777/edit/";*/

    /* private final String LOGIN_URL = synBioHubBaseUrl.concat("login");
    private final String USER_API = synBioHubBaseUrl.concat("users");
    private final String SUBMIT_URL = synBioHubBaseUrl.concat("submit");
    private final String UPDATE_URL = synBioHubBaseUrl.concat("edit"); */

    private final String LOGIN_URL;
    private final String USER_API;
    private final String SUBMIT_URL;
    private final String UPDATE_URL;

    HttpHeaders headers = new HttpHeaders();

    @Autowired
    public SynBioHubClientController(RestTemplateBuilder restTemplateBuilder,
            @Value("${synbiohub.client.baseUrl}") String synBioHubBaseUrl) {
        restTemplate = restTemplateBuilder.build();
        
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

    @GetMapping("/users/{id}")
    public User consumeUser(@PathVariable Integer id) {
        String url = USER_API + "/{id}";
        return restTemplate.getForObject(url, User.class, id);
    }

    @GetMapping("/users")
    public User[] consumeAllUser() {
        return restTemplate.getForObject(USER_API, User[].class);
    }

    public void doLogin() {
        /*String credentials = synBioHubUser+":"+synBioHubPass;
        byte[] credentialBytes = credentials .getBytes();
        byte[] base64CredentialBytes = Base64.encodeBase64(credentialBytes);
        String base64Credentials = new String(base64CredentialBytes);
        headers.add("Authorization", "Basic " + base64Credentials );*/
        final ResponseEntity<String> responseEntity = restTemplate
                .exchange(LOGIN_URL, HttpMethod.POST, new HttpEntity<Void>(createHeaders(synBioHubEmail, synBioHubPass)), String.class);
        System.out.println(responseEntity.getBody());
    }

    @RequestMapping(path = "/submit", method = RequestMethod.POST)
    public ModelAndView doSubmits() {
        var mav = new ModelAndView();

        if(!headers.containsKey("Authorization")) {
            doLogin();
        }

        logger.debug("WTTTTFFFFFF?!");

        HttpEntity<String> request = new HttpEntity<String>(headers);

        final ResponseEntity<String> responseEntity = restTemplate
                .exchange(SUBMIT_URL, HttpMethod.POST, new HttpEntity<Void>(createHeaders(synBioHubUser, synBioHubPass)), String.class);
        System.out.println(responseEntity.getBody());
        mav.setViewName("submit-result");

        return mav;
    }
    
}
