/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.service;

import ed.biordm.sbol.web.SynBioHubClientTestConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author jhay
 */
@Import(SynBioHubClientTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@SpringBootConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = {
    "synbiohub.client.baseUrl=http://localhost:7777/",
    "synbiohub.client.username=johnnyH",
    "synbiohub.client.password=mysupersecretpassword"
})
public class SynBioHubClientServiceImplTest {

    // @InjectMocks
    SynBioHubClientServiceImpl synBioHubClientService;

    // @Mock
    @Autowired
    RestTemplateBuilder restTemplateBuilder;

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
    }

    /*@BeforeEach
    void init(@Mock RestTemplateBuilder restTemplateBuilder) {
        synBioHubClientService = new SynBioHubClientServiceImpl(restTemplateBuilder, synBioHubBaseUrl);
    }*/

    @Test
    public void testSubmitFiles() throws Exception {
        String dirPath = "D://temp//sbol";
        String fileExtFilter = "xml";

        synBioHubClientService.submitSBOLFiles(username, password, 2, dirPath, fileExtFilter, true);
    }
}
