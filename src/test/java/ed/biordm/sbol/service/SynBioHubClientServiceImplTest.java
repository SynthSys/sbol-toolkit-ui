/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.service;

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

    // @InjectMocks
    /*SynBioHubClientServiceImpl synBioHubClientService;

    @Value("${synbiohub.client.baseUrl}")
    private String synBioHubBaseUrl;

    @Value("${synbiohub.client.username}")
    private String username;

    @Value("${synbiohub.client.password}")
    private String password;

    @Before
    public void init() {
        // MockitoAnnotations.initMocks(this);
        System.out.println(synBioHubBaseUrl);
        synBioHubClientService = new SynBioHubClientServiceImpl(restTemplateBuilder, synBioHubBaseUrl);
    }*/

    /*@BeforeEach
    void init(@Mock RestTemplateBuilder restTemplateBuilder) {
        synBioHubClientService = new SynBioHubClientServiceImpl(restTemplateBuilder, synBioHubBaseUrl);
    }*/

    @Test
    public void testSubmitFiles() throws Exception {
        String dirPath = "D://temp//sbol";
        String fileExtFilter = "xml";
        String username = "JohnnyH";
        String password = "mysupersecretpassword";
        long collId = 2;
        boolean isOverwrite = Boolean.TRUE;

        synBioHubClientService.submitSBOLFiles(username, password, collId, dirPath, fileExtFilter, isOverwrite);
    }
}
