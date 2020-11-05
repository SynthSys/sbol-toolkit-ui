/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.web;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author jhay
 */
@Component
public class BasicAuthClient {

    private final RestTemplate restTemplate;

    public BasicAuthClient(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                    .basicAuthentication("admin", "password")
                    .build();
    }

    public void invokeProtectedResource() {
        final ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:8080/secured/hello", String.class);
        final String body = responseEntity.getBody();
        System.out.println("body = " + body);
    }
}
