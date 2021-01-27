/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author jhay
 */
public class SynBioHubClientServiceImplIT {

    @TestConfiguration
    static class SynBioHubClientServiceImplTestContextConfiguration {
 
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();

        @Bean
        public SynBioHubClientServiceImpl synBioHubClientService() {           
            String url = "http://localhost:7777";
            return new SynBioHubClientServiceImpl(restTemplateBuilder, url);
        }

        @Bean
        public RestTemplate restTemplate() {           
            return restTemplateBuilder.build();
        }
    }

    @Autowired
    private SynBioHubClientService synBioHubClientService;

    @Autowired
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

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

    private static final String SUBMIT_URL = "http://localhost:7777/submit";

    @Before
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testSubmitFiles() throws Exception {
        String pass = "admin";

        synBioHubClientService.submitSBOLFiles(email, pass, collUrl,
                dirPath, fileExtFilter, isOverwrite);
    }

    @Test                                                                                          
    public void givenMockingIsDoneByMockRestServiceServer_whenPostIsCalled_thenReturnsUploaded() throws Exception {   
        //Employee emp = new Employee("E001", "Eric Simmons");
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI(SUBMIT_URL)))
          .andExpect(method(HttpMethod.POST))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.TEXT_PLAIN)
          .body(mapper.writeValueAsString("Successfully uploaded"))
        );                                   

        String pass = "admin";
        //Employee employee = empService.getEmployee(id);
        synBioHubClientService.submitSBOLFiles(email, pass, collUrl,
                dirPath, fileExtFilter, isOverwrite);
        mockServer.verify();
        //Assert.assertEquals(emp, employee);                                                        
    }

}
