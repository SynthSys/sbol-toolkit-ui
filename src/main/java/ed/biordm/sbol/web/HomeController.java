/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.web;

import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
public class HomeController {

    @Autowired
    private RestTemplate restTemplate;

    public HomeController(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        var mav = new ModelAndView();

        String message = "Greetings from Spring Boot!";

        mav.setViewName("home");
        mav.addObject("message", message);
        mav.getModelMap().addAttribute("message", message);

        return mav;
    }
}
