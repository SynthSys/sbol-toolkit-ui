/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Stream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * See https://picocli.info/#_testing_the_output
 * 
 * @author jhay
 */
public class SynBioHubCmdTest {   
    final PrintStream originalOut = System.out;
    final PrintStream originalErr = System.err;
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final ByteArrayOutputStream err = new ByteArrayOutputStream();

    @Rule public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    SynBioHubCmd app = new SynBioHubCmd();
    CommandLine cmd = new CommandLine(app);

    @BeforeEach
    public void setUpStreams() {
        out.reset();
        err.reset();
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void test() {
        StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        String[] userArgs = { "--username", "johnnyH" };
        String[] passwordArgs = { "--password", "mysupersecurepassword" };
        String[] allArgs = Stream.of(userArgs, passwordArgs).flatMap(Stream::of).toArray(String[]::new);

        cmd.execute(allArgs);
        assertEquals("", sw.toString());
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