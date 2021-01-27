/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.cli;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;


/**
 * See https://picocli.info/, https://picocli.info/#_spring_boot_example and
 * https://github.com/remkop/picocli/tree/master/picocli-spring-boot-starter
 * 
 * @author jhay
 */
@SpringBootApplication
// @ComponentScan({ "ed.biordm.sbol.cli", "ed.biordm.sbol.service", "ed.biordm.sbol.web" })
@ComponentScan({ "ed.biordm.sbol.cli", "ed.biordm.sbol.service" })
// @ComponentScan({ "ed.biordm.sbol.cli"})
public class SynBioHubClientCmdRunner implements CommandLineRunner, ExitCodeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SynBioHubClientCmdRunner.class);

    private final IFactory factory;
    private final SynBioHubCmd synBioHubCmd; 
    private final CommandLine cmd;
    private int exitCode;

    private String propertiesFilename;

    // constructor injection
    SynBioHubClientCmdRunner(IFactory factory, SynBioHubCmd synBioHubCmd,
            @Value("${synbiohub.cmd.properties}") String propertiesFilename) {
        this.factory = factory;
        this.synBioHubCmd = synBioHubCmd;
        this.cmd = new CommandLine(this.synBioHubCmd, this.factory);
        this.propertiesFilename = propertiesFilename;

        Properties defaults = getProperties(this.propertiesFilename);
        this.cmd.setDefaultValueProvider(new SynBioHubCmdDefaultProvider(defaults));
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        new SpringApplicationBuilder(SynBioHubClientCmdRunner.class).web(WebApplicationType.NONE).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.debug("Running with args:");
        LOGGER.debug(Arrays.toString(args));
        // let picocli parse command line args and run the business logic
        exitCode = cmd.execute(args);

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

    /*@Bean
    public SynBioHubClientService synBioHubClientService() {
        return new SynBioHubClientServiceImpl(new RestTemplateBuilder());
    }*/
}