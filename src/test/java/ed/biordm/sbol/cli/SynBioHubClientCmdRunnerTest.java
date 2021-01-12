/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.cli;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author jhay
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SynBioHubClientCmdRunner.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SynBioHubClientCmdRunnerTest {

    @BeforeEach
    public void setUp() throws Exception {
        
        System.setProperty("file.encoding", "UTF-8");
    }

    @Test
    public void contextLoads() throws Exception {
    }

    @Test
    public void test() throws Exception {
        SynBioHubClientCmdRunner.main(new String[]{
            "--spring.main.web-environment=false",
            "-Dfile.encoding=UTF-8"
            // Override any other environment properties according to your needs
        });
    }

    @Test
    public void test2() throws Exception {
        assertTrue(Boolean.TRUE.toString(), true);
    }
}