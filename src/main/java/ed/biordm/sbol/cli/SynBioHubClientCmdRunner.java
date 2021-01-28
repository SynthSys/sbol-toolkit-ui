/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.cli;

import ed.biordm.sbol.service.SynBioHubClientService;
import ed.biordm.sbol.service.SynBioHubClientServiceImpl;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;
import java.util.function.Supplier;
import org.fusesource.jansi.AnsiConsole;
import org.jline.console.SystemRegistry;
import org.jline.console.impl.Builtins;
import org.jline.console.impl.SystemRegistryImpl;
import org.jline.keymap.KeyMap;
import org.jline.reader.Binding;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.MaskingCallback;
import org.jline.reader.Parser;
import org.jline.reader.Reference;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.widget.TailTipWidgets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;
import picocli.shell.jline3.PicocliCommands;
import picocli.shell.jline3.PicocliCommands.PicocliCommandsFactory;


/**
 * See https://picocli.info/, https://picocli.info/#_spring_boot_example and
 * https://github.com/remkop/picocli/tree/master/picocli-spring-boot-starter
 * 
 * @author jhay
 */
@SpringBootApplication
// @ComponentScan({ "ed.biordm.sbol.cli", "ed.biordm.sbol.service", "ed.biordm.sbol.web" })
//@ComponentScan(basePackages={ "ed.biordm.sbol.cli", "ed.biordm.sbol.service" })
// @ComponentScan({ "ed.biordm.sbol.cli"})
// @ComponentScan({ "ed.biordm.sbol.service" })
public class SynBioHubClientCmdRunner implements CommandLineRunner, ExitCodeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SynBioHubClientCmdRunner.class);

    @Autowired
    private IFactory factory;

    @Autowired
    private SynBioHubClientService synBioHubClientService;

    @Autowired
    private CommandLine cmd;

    /*private final IFactory factory;
    private final SynBioHubCmd synBioHubCmd; 
    private final CommandLine cmd;*/
    private int exitCode;

    private String propertiesFilename;

    private static final boolean IS_TERMINAL_PROMPT = true;
    // constructor injection
    /*SynBioHubClientCmdRunner(IFactory factory, SynBioHubCmd synBioHubCmd,
            @Value("${synbiohub.cmd.properties}") String propertiesFilename) {
        this.factory = factory;
        this.synBioHubCmd = synBioHubCmd;
        this.cmd = new CommandLine(this.synBioHubCmd, this.factory);
        this.propertiesFilename = propertiesFilename;

        Properties defaults = getProperties(this.propertiesFilename);
        this.cmd.setDefaultValueProvider(new SynBioHubCmdDefaultProvider(defaults));
    }*/

    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        new SpringApplicationBuilder(SynBioHubClientCmdRunner.class).web(WebApplicationType.NONE).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.debug("Running with args:");
        LOGGER.debug(Arrays.toString(args));
        
        if (IS_TERMINAL_PROMPT) {
            // use jline3 to create an interactive terminal for the user
            PicocliCommands picocliCommands = new PicocliCommands(cmd);
            PicocliCommandsFactory cmdFactory = new PicocliCommandsFactory(factory);

            runTerminal(picocliCommands, cmdFactory);
        } else {
            // let picocli parse command line args and run the business logic
            exitCode = cmd.execute(args); 
        }

        // From Apache Commons CLI...
        /*Option help = Option.builder("h").required(false).hasArg(false).longOpt("help").build();
        Option guest = Option.builder("g").required(false).hasArg(true).argName("Guest user").longOpt("guest").build();

        Options options = new Options();
        options.addOption(help);
        options.addOption(guest);

        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);

        if (line.hasOption("h") || args.length == 0) {

                HelpFormatter formatter = new HelpFormatter();
                // automatically generate the help statement
                formatter.printHelp("java -jar springboot-apache-cli.jar", options, true);
                return;
        }*/
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

    protected static Properties getProperties(String propertiesFilename) {
        Properties prop = new Properties();
        System.out.println(propertiesFilename);
 
        try (InputStream stream = ClassLoader.getSystemResourceAsStream(propertiesFilename)) {
            if (stream == null) {
                throw new FileNotFoundException();
            }
            prop.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    /*@Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }*/

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        // RestTemplateBuilder restTemplateBuilder = RestTemplateBuilder.rootUri("http://localhost:7777");
        restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:7777");
        return restTemplateBuilder;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        // RestTemplateBuilder restTemplateBuilder = RestTemplateBuilder.rootUri("http://localhost:7777");
        restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:7777");
        return restTemplateBuilder.build();
    }

    @Bean
    public SynBioHubClientService synBioHubClientService() {
        return new SynBioHubClientServiceImpl();
    }

    @Bean
    public CommandLine cmd() {
        SynBioHubCmd synBioHubCmd = new SynBioHubCmd();
        synBioHubCmd.setSynBioHubClientService(synBioHubClientService);
        cmd = new CommandLine(synBioHubCmd, this.factory);
        return cmd;
    }

    /*@Bean
    public SynBioHubClientService synBioHubClientService() {
        return new SynBioHubClientServiceImpl(new RestTemplateBuilder());
    }*/

    protected void runTerminal(PicocliCommands picocliCommands,
            PicocliCommandsFactory cmdFactory) throws Exception {
        AnsiConsole.systemInstall();

        Supplier<Path> workDir = () -> Paths.get(System.getProperty("user.dir"));
        // set up JLine built-in commands
        Builtins builtins = new Builtins(workDir, null, null);
        builtins.rename(Builtins.Command.TTOP, "top");
        builtins.alias("zle", "widget");
        builtins.alias("bindkey", "keymap");

        Parser parser = new DefaultParser();
        try (Terminal terminal = TerminalBuilder.builder().build()) {
            SystemRegistry systemRegistry = new SystemRegistryImpl(parser, terminal, workDir, null);
            systemRegistry.setCommandRegistries(builtins, picocliCommands);
            systemRegistry.register("help", picocliCommands);

            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(systemRegistry.completer())
                    .parser(parser)
                    .variable(LineReader.LIST_MAX, 50)   // max tab completion candidates
                    .build();
            builtins.setLineReader(reader);
            cmdFactory.setTerminal(terminal);
            TailTipWidgets widgets = new TailTipWidgets(reader, systemRegistry::commandDescription, 5, TailTipWidgets.TipType.COMPLETER);
            widgets.enable();
            KeyMap<Binding> keyMap = reader.getKeyMaps().get("main");
            keyMap.bind(new Reference("tailtip-toggle"), KeyMap.alt("s"));

            String prompt = "SynBioHub-Client> ";
            String rightPrompt = null;

            // start the shell and process input until the user quits with Ctrl-D
            String line;
            while (true) {
                try {
                    systemRegistry.cleanUp();
                    line = reader.readLine(prompt, rightPrompt, (MaskingCallback) null, null);
                    systemRegistry.execute(line);
                } catch (UserInterruptException e) {
                    // Ignore
                } catch (EndOfFileException e) {
                    return;
                } catch (Exception e) {
                    systemRegistry.trace(e);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            AnsiConsole.systemUninstall();
        }
    }
}