/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.web;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author jhay
 */
@TestConfiguration
public class SynBioHubClientTestConfiguration {

    @Bean
    RestTemplateBuilder restTemplateBuilder(){
        return new RestTemplateBuilder();
    }
}
