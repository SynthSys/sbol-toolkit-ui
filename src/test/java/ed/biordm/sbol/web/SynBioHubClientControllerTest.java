/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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

    @Test
    public void postSubmit() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/submit").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("submit-result"))
                .andExpect(model().attribute("message", "Greetings from Spring Boot!"));
    }
}
