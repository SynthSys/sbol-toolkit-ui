/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.cli;

import ed.biordm.sbol.service.SynBioHubClientService;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Stream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * See https://picocli.info/#_testing_the_output
 * 
 * @author jhay
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Configuration
// @Profile("test")
@RunWith(MockitoJUnitRunner.class)
// @RunWith(SpringRunner.class)
public class SynBioHubCmdTest {   
    final PrintStream originalOut = System.out;
    final PrintStream originalErr = System.err;
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final ByteArrayOutputStream err = new ByteArrayOutputStream();

    @Rule public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Mock
    private SynBioHubClientService synBioHubClientService;

    @InjectMocks
    private SynBioHubCmd app;

    // SynBioHubCmd app = new SynBioHubCmd();
    // CommandLine cmd = new CommandLine(app);
    private CommandLine cmd;

    //@MockBean
    /*@Autowired
    private SynBioHubClientService synBioHubClientService;*/

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        cmd = new CommandLine(app);
    }

    /*@Before
    public void setUpStreams() {     
        out.reset();
        err.reset();
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }*/

    @Test
    public void testMinimalArgs() {
        StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        System.out.println(synBioHubClientService == null);
        String[] userArgs = { "--username", "johnnyH" };
        String[] passwordArgs = { "--password", "mysupersecurepassword" };
        String[] collectionIdArgs = { "--collection-url", "http://localhost:7777/user/Johnny/a_random_id/a_random_id_collection/1" };
        String[] allArgs = Stream.of(userArgs, passwordArgs, collectionIdArgs).flatMap(Stream::of).toArray(String[]::new);

        cmd.execute(allArgs);
        assertEquals("", sw.toString());
    }

    @Test
    public void testAllArgs() {
        StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        System.out.println(synBioHubClientService == null);
        String[] userArgs = { "--username", "johnnyH" };
        String[] passwordArgs = { "--password", "mysupersecurepassword" };
        String[] collectionIdArgs = { "--collection-url", "http://localhost:7777/user/Johnny/a_random_id/a_random_id_collection/1" };
        String[] serverUrlArgs = { "--server-url", "http://localhost:7777/" };
        String[] dirPathArgs = { "--dir-path", "D://Temp//sbol/" };
        String[] fileExtFilterArgs = { "--file-ext-filter", "sbol" };
        String[] overwriteArgs = { "--overwrite", "true" };

        String[] allArgs = Stream.of(userArgs,
                passwordArgs, collectionIdArgs, serverUrlArgs,
                dirPathArgs, fileExtFilterArgs, overwriteArgs).flatMap(Stream::of).toArray(String[]::new);

        cmd.execute(allArgs);
        assertEquals("", sw.toString());
    }

    @Test
    public void testExistingCollectionArgs() {
        StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        System.out.println(synBioHubClientService == null);
        // String[] userArgs = { "--username", "johnnyH" };
        String[] userArgs = { "--username", "j.hay@epcc.ed.ac.uk" };
        // String[] passwordArgs = { "--password", "mysupersecurepassword" };
        String[] passwordArgs = { "--password", "admin" };
        String[] collectionIdArgs = { "--collection-url", "http://localhost:7777/user/Johnny/a_random_id/a_random_id_collection/1" };
        String[] dirPathArgs = { "--dir-path", "D://Temp//sbol//codA_Km_0081_slr1130.xml" };
        String[] fileExtFilterArgs = { "--file-ext-filter", "xml" };
        String[] overwriteArgs = { "--overwrite", "true" };

        String[] allArgs = Stream.of(userArgs,
                passwordArgs, collectionIdArgs, dirPathArgs, fileExtFilterArgs,
                overwriteArgs).flatMap(Stream::of).toArray(String[]::new);

        cmd.execute(allArgs);
        assertEquals("", sw.toString());
    }

    @Test
    public void testNewCollectionArgs() {
        StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        System.out.println(synBioHubClientService == null);
        // String[] userArgs = { "--username", "johnnyH" };
        String[] userArgs = { "--username", "j.hay@epcc.ed.ac.uk" };
        // String[] passwordArgs = { "--password", "mysupersecurepassword" };
        String[] passwordArgs = { "--password", "admin" };
        String[] collectionIdArgs = { "--collection-name", "Johnny New Random Collection" };
        String[] serverUrlArgs = { "--server-url", "http://localhost:7777" };
        String[] dirPathArgs = { "--dir-path", "D://temp//sbol//codA_Km_0081_slr1130.xml" };
        String[] fileExtFilterArgs = { "--file-ext-filter", "xml" };
        String[] overwriteArgs = { "--overwrite", "true" };
        String[] allArgs = Stream.of(userArgs, passwordArgs, collectionIdArgs,
                serverUrlArgs, dirPathArgs, fileExtFilterArgs, overwriteArgs)
                .flatMap(Stream::of).toArray(String[]::new);

        cmd.execute(allArgs);
        assertEquals("", sw.toString());
    }

    /*@Test
    public void whiteBoxTest() {






        // white box testing
        assertEquals("expectedValue1", app.getState1());
        assertEquals("expectedValue2", app.getState2());
    }*/

    @Test
    public void testHelpRequest() {
        StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        int exitCode = cmd.execute("--help");
        assertEquals(0, exitCode);
        System.out.print("Help request output:\n".concat(sw.toString()));
        assertTrue(sw.toString().startsWith("Usage: synBioHubCmd [-hV] -u[=<username>] -p[=<password>] [-p[=<password>]"));
        assertTrue(sw.toString().strip().endsWith("  -V, --version   Print version information and exit."));
    }

    @Test
    public void testVersionRequest() {
        StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        int exitCode = cmd.execute("--version");
        assertEquals(0, exitCode);
        System.out.print("Version request output: ".concat(sw.toString()));
        assertEquals("1.0", sw.toString().strip());
    }

    @Test
    public void testIncorrectArgs() {
        StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        // black box testing
        int exitCode = cmd.execute("-x", "-y=123");
        assertEquals(2, exitCode);
        assertEquals("", sw.toString());
    }

    @Test
    public void testExitCode() {
        @Command class App implements Runnable {
            public void run() {
                System.exit(23);
            }
        }
        exit.expectSystemExitWithStatus(23);
        new CommandLine(new App()).execute();
    }
}