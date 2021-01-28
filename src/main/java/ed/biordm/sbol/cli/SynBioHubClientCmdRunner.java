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
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${synbiohub.cmd.properties}")
    private String propertiesFilename;

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
        new SpringApplicationBuilder(SynBioHubClientCmdRunner.class).web(WebApplicationType.NONE).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.debug("Running with args:");
        LOGGER.debug(Arrays.toString(args));

        LOGGER.debug(propertiesFilename);
        Properties defaults = getProperties(this.propertiesFilename);
        cmd.setDefaultValueProvider(new SynBioHubCmdDefaultProvider(defaults));

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
 
        // printClassLoaderProps();
        // ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // try (InputStream stream = classLoader.getSystemResourceAsStream(propertiesFilename)) {
        try (InputStream stream = SynBioHubClientCmdRunner.class.getResourceAsStream(propertiesFilename)) {
            if (stream == null) {
                throw new FileNotFoundException();
            }
            prop.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    protected static void printClassLoaderProps() {
        try {
            SynBioHubClientCmdRunner runner = new SynBioHubClientCmdRunner();
            Class clazz = runner.getClass();
            ClassLoader cl = clazz.getClassLoader();
            System.out.println("ClassLoader=" + cl);

            // Where does the servlet itself live?
            String resourceNameForClass = clazz.getName().replaceAll("\\.", "/") + ".class";
            System.out.println("resourceNameForClass=" + resourceNameForClass);
            for (Enumeration<URL> e = cl.getResources(resourceNameForClass); e.hasMoreElements();) {
                System.out.println("resource=" + e.nextElement());
            }

            // What about a class that lives in a jar in WEB-INF/lib
            clazz = Class.forName("org.slf4j.Logger");
            resourceNameForClass = clazz.getName().replaceAll("\\.", "/") + ".class";
            System.out.println("resourceNameForClass=" + resourceNameForClass);
            for (Enumeration<URL> e = cl.getResources(resourceNameForClass); e.hasMoreElements();) {
                System.out.println("resource=" + e.nextElement());
            }

            if (cl instanceof URLClassLoader) {
                URLClassLoader ucl = (URLClassLoader) cl;
                URL[] urls = ucl.getURLs();
                for (int i = 0; i < urls.length; i++) {
                    System.out.println("url[" + i + "]=" + urls[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}