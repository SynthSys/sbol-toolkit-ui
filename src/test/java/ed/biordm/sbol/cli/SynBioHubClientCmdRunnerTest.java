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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author jhay
 */
// @SpringBootTest(classes = SynBioHubClientCmdRunner.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
// @SpringBootTest
// @AutoConfigureMockMvc
// @RunWith(SpringRunner.class)
public class SynBioHubClientCmdRunnerTest {
    @BeforeEach
    public void setUp() throws Exception {
        // Reflection was only requires if the property was autowired with @value...
        // ReflectionTestUtils.setField(SynBioHubClientCmdRunner.class, "propertiesFilename", "blah");
        System.setProperty("file.encoding", "UTF-8");
    }

    @Test
    public void testMandatoryArgs() throws Exception {
        String[] userArgs = { "--username", "johnnyH" };
        String[] passwordArgs = { "--password", "mysupersecurepassword" };
        String[] collectionIdArgs = { "--collection-id", "1" };
        String[] allArgs = Stream.of(userArgs, passwordArgs, collectionIdArgs).flatMap(Stream::of).toArray(String[]::new);
        // SynBioHubClientCmdRunner.main(allArgs);
        // PowerMockito.mockStatic(SpringApplication.class);
        SpringApplication.run(SynBioHubClientCmdRunner.class, allArgs);
    }

    @Test
    public void testAllArgs() throws Exception {
        String[] userArgs = { "--username", "johnnyH" };
        String[] passwordArgs = { "--password", "mysupersecurepassword" };
        String[] collectionIdArgs = { "--collection-id", "1" };
        String[] serverUrlArgs = { "--server-url", "http://localhost:7890/" };
        String[] dirPathArgs = { "--dir-path", "D://Temp//sbol/" };
        String[] fileExtFilterArgs = { "--file-ext-filter", "sbol" };
        String[] overwriteArgs = { "--overwrite", "true" };

        String[] allArgs = Stream.of(userArgs,
                passwordArgs, collectionIdArgs, serverUrlArgs,
                dirPathArgs, fileExtFilterArgs, overwriteArgs).flatMap(Stream::of).toArray(String[]::new);
        // SynBioHubClientCmdRunner.main(allArgs);
        // PowerMockito.mockStatic(SpringApplication.class);
        SpringApplication.run(SynBioHubClientCmdRunner.class, allArgs);
    }

    @Test
    public void test2() throws Exception {
        assertTrue(Boolean.TRUE.toString(), true);
    }
}