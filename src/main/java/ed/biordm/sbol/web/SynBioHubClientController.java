/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.web;

import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
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

    private final String LOGIN_URL = "https://synbiohub.org/login";
    private final String USER_API = "https://synbiohub.org/users";
    private final String SUBMIT_API = "https://synbiohub.org/submit";

    public SynBioHubClientController(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
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

    @RequestMapping(path = "/submit", method = RequestMethod.POST)
    public ModelAndView doSubmits() {
        var mav = new ModelAndView();

        System.out.println("WTTTTFFFFFF!!!!!!!!");
        logger.warn("WTTTTFFFFFF!!!!!!!!");
        final ResponseEntity<String> responseEntity = restTemplate
                .exchange(SUBMIT_API, HttpMethod.POST, new HttpEntity<Void>(createHeaders("admin", "password")), String.class);
        System.out.println(responseEntity.getBody());
        mav.setViewName("submit-result");

        return mav;
    }
    
}
