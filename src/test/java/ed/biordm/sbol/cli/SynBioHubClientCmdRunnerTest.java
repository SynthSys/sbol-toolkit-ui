/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.cli;

import java.util.stream.Stream;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author jhay
 */
// @SpringBootTest(classes = SynBioHubClientCmdRunner.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
// @SpringBootTest
// @AutoConfigureMockMvc
public class SynBioHubClientCmdRunnerTest {
    @BeforeEach
    public void setUp() throws Exception {
        
        System.setProperty("file.encoding", "UTF-8");
    }

    @Test
    public void test() throws Exception {
        String[] userArgs = { "--username", "johnnyH" };
        String[] passwordArgs = { "--password", "mysupersecurepassword" };
        String[] allArgs = Stream.of(userArgs, passwordArgs).flatMap(Stream::of).toArray(String[]::new);
        SynBioHubClientCmdRunner.main(allArgs);
    }

    @Test
    public void test2() throws Exception {
        assertTrue(Boolean.TRUE.toString(), true);
    }
}