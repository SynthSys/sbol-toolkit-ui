/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.service;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author jhay
 */
@RunWith(SpringRunner.class)
public class SynBioHubClientServiceImplTest {

    @TestConfiguration
    static class SynBioHubClientServiceImplTestContextConfiguration {
 
        @Bean
        public SynBioHubClientServiceImpl synBioHubClientService() {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
            String url = "http://localhost:7777";
            return new SynBioHubClientServiceImpl(restTemplateBuilder, url);
        }
    }

    @Autowired
    private SynBioHubClientService synBioHubClientService;

    // String dirPath = "D://temp//sbol";
    String dirPath = "D://temp//sbol//codA_Km_0081_slr1130.xml";
    String fileExtFilter = "xml";
    String email = "j.hay@epcc.ed.ac.uk";
    String password = "mysupersecretpassword";
    String collUrl = "http://localhost:7777/user/Johnny/a_random_id/a_random_id_collection/1";
    String collId = "a_random_id";
    long version = 1;
    String collName = "A Random Name";
    boolean isOverwrite = Boolean.TRUE;

    @Test
    public void testSubmitFiles() throws Exception {
        String pass = "admin";

        synBioHubClientService.submitSBOLFiles(email, pass, collUrl,
                dirPath, fileExtFilter, isOverwrite);
    }

    @Test
    public void testSubmitFilesBadEmail() throws Exception {
        String pass = "admin";
        String badEmail = "johnnyH";
        try {
            synBioHubClientService.submitSBOLFiles(badEmail, password, collUrl,
                dirPath, fileExtFilter, isOverwrite);
        } catch (Exception e) {
            assertEquals("401 Unauthorized: [Your e-mail address was not recognized.]", e.getMessage());
        }
    }

    @Test
    public void testSubmitFilesBadPassword() throws Exception {
        try {
            synBioHubClientService.submitSBOLFiles(email, password, collUrl,
                    dirPath, fileExtFilter, isOverwrite);
        } catch (Exception e) {
            assertEquals("401 Unauthorized: [Your password was not recognized.]", e.getMessage());
        }
    }
}
