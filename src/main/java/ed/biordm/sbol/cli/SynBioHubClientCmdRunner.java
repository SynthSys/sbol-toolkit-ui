/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.cli;

import ed.biordm.sbol.service.SynBioHubClientService;
import ed.biordm.sbol.service.SynBioHubClientServiceImpl;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(SynBioHubClientCmdRunner.class);

    private IFactory factory;
    private SynBioHubCmd synBioHubCmd; 
    private int exitCode;

    // constructor injection
    SynBioHubClientCmdRunner(IFactory factory, SynBioHubCmd synBioHubCmd) {
        this.factory = factory;
        this.synBioHubCmd = synBioHubCmd;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        new SpringApplicationBuilder(SynBioHubClientCmdRunner.class).web(WebApplicationType.NONE).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Run");
        System.out.println(Arrays.toString(args));
        // let picocli parse command line args and run the business logic
        exitCode = new CommandLine(synBioHubCmd, factory).execute(args);

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

    /*@Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }*/

    /* @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    } */

    /*@Bean
    public SynBioHubClientService synBioHubClientService() {
        return new SynBioHubClientServiceImpl(new RestTemplateBuilder());
    }*/
}