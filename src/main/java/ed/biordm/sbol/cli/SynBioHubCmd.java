/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.cli;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import ed.biordm.sbol.service.SynBioHubClientService;
import ed.biordm.sbol.service.SynBioHubClientServiceImpl;
import java.net.URI;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import picocli.CommandLine.ArgGroup;

// @Component
@Command(name = "synBioHubCmd", mixinStandardHelpOptions = true, version = "1.0")
public class SynBioHubCmd implements Callable<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SynBioHubCmd.class);

    @Autowired
    private SynBioHubClientService synBioHubClientService;

    public void setSynBioHubClientService(SynBioHubClientService synBioHubClientService) {
        this.synBioHubClientService = synBioHubClientService;
    }

    public SynBioHubClientService getSynBioHubClientService() {
        return synBioHubClientService;
    }

    private static final String CWD = System.getProperty("user.dir");
    
    private static final Pattern Y_N_PATTERN = Pattern.compile("[Y|N]{1}");

    // @Option(names = {"-u", "--username"}, description = "Username", interactive = true, required = true)
    // char[] username;

    @Option(names = {"-u", "--username"}, description = "Username", interactive = true, arity = "0..1"/*, required = true*/)
    String username;

    @Option(names = {"-p", "--password"}, description = "Passphrase", interactive = true, arity = "0..1"/*, required = true*/)
    char[] password;

    @ArgGroup(exclusive = true/*, multiplicity = "1"*/)
    ExclusiveURLArgs exclusiveUrlArgs;

    /* @ArgGroup(exclusive = false)
    DependentURLArgs dependentURLArgs; */

    @Option(names = {"-n", "--collection-name"}, description = "Name for the new collection", descriptionKey="collectionName", interactive = true, arity = "0..1", required = false)
    String collectionName;

    @Option(names = {"-d", "--dir-path"}, description = "Directory path to the folder containing the SBOL files to submit. Default value is ${DEFAULT-VALUE}", descriptionKey="dirPath", interactive = true, arity = "0..1", required = false)
    String dirPath;

    @Option(names = {"-f", "--file-ext-filter"}, description = "File extension filter to refine the types of file that will be deposited. Default value is ${DEFAULT-VALUE}", descriptionKey="fileExtFilter", interactive = true, arity = "0..1", required = false)
    String fileExtFilter;

    @Option(names = {"-o", "--overwrite"}, description = "If overwrite is specified, any files with the same name in the designated collection ID will be overwritten. Default value is ${DEFAULT-VALUE}", descriptionKey="overwrite", interactive = true, arity = "0..1", required = false)
    boolean overwrite;

    /*@Autowired
    public SynBioHubCmd(@Value("${synBioHubClientService}") SynBioHubClientService synBioHubClientService) {
        
    }*/

    static class ExclusiveURLArgs {
        @Option(names = {"-c", "--collection-url"}, description = "Collection URL to deposit into", descriptionKey = "collectionUrl", interactive = true, arity = "0..1", required = true)
        String collectionUrl;

        @Option(names = {"-s", "--server-url"}, description = "Base URL of SynBioHub API target. Default value is ${DEFAULT-VALUE}", descriptionKey="serverUrl", interactive = true, arity = "0..1", required = true)
        String serverUrl;
    }

    /*static class DependentURLArgs {
        @Option(names = {"-s", "--server-url"}, description = "Base URL of SynBioHub API target. Default value is ${DEFAULT-VALUE}", descriptionKey="serverUrl", interactive = true, arity = "0..1", required = true)
        String serverUrl;

        @Option(names = {"-n", "--collection-name"}, description = "Name for the new collection", descriptionKey="collectionName", interactive = true, arity = "0..1", required = true)
        String collectionName;
    }*/
    
    public Integer call() throws Exception {
        LOGGER.info("synBioHubCmd was called with --username={}", username);

        verifyInput();
        
        byte[] bytes = new byte[password.length];
        for (int i = 0; i < bytes.length; i++) { bytes[i] = (byte) password[i]; }

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bytes);

        LOGGER.info("Hi {}, your password is hashed to {}.", username, Base64.getEncoder().encodeToString(md.digest()));

        LOGGER.debug("SynBioHubClientService failed to initialise: {}", synBioHubClientService == null);

        LOGGER.debug("Specified server URL: {}", exclusiveUrlArgs.serverUrl);
        LOGGER.debug("Specified collection URL: {}", exclusiveUrlArgs.collectionUrl);

        if(exclusiveUrlArgs.serverUrl != null) {
            LOGGER.debug("Specified Collection Name: {}", collectionName);
            if(!exclusiveUrlArgs.serverUrl.equals(synBioHubClientService.getServerUrl())) {
                LOGGER.debug("Setting new server URL");

                RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
                restTemplateBuilder = restTemplateBuilder.rootUri(exclusiveUrlArgs.serverUrl);
                // synBioHubClientService.setServerUrl(serverUrl);
                synBioHubClientService = new SynBioHubClientServiceImpl();
                synBioHubClientService.setServerUrl(exclusiveUrlArgs.serverUrl);
                synBioHubClientService.setRestTemplateBuilder(restTemplateBuilder);
                RestTemplate restTemplate = restTemplateBuilder.build();
                synBioHubClientService.setRestTemplate(restTemplate);
            }

            synBioHubClientService.submitSBOLFiles(username, new String(password),
                collectionName, dirPath, fileExtFilter, overwrite);
        } else if(exclusiveUrlArgs.collectionUrl != null) {
            URI collectionUri = new URI(exclusiveUrlArgs.collectionUrl);
            String scheme = collectionUri.getScheme();
            String domain = collectionUri.getHost();
            int port = collectionUri.getPort();
            String collServerUrl = scheme.concat("://").concat(domain);

            if (port > 0) {
                collServerUrl = collServerUrl.concat(":").concat(String.valueOf(port));
            }
            if(!collServerUrl.equals(synBioHubClientService.getServerUrl())) {
                LOGGER.debug("Setting new server URL");

                RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
                restTemplateBuilder = restTemplateBuilder.rootUri(collServerUrl);
                // synBioHubClientService.setServerUrl(serverUrl);
                synBioHubClientService = new SynBioHubClientServiceImpl();
                synBioHubClientService.setServerUrl(collServerUrl);
                synBioHubClientService.setRestTemplateBuilder(restTemplateBuilder);
                RestTemplate restTemplate = restTemplateBuilder.build();
                synBioHubClientService.setRestTemplate(restTemplate);
            }

            synBioHubClientService.submitSBOLFiles(username, new String(password),
                collectionUri, dirPath, fileExtFilter, overwrite);
        }

        // null out the arrays when done
        Arrays.fill(bytes, (byte) 0);
        Arrays.fill(password, ' ');

        return 0;
    }

    protected boolean verifyInput() {
        if (username == null) {
            username = new String(System.console().readLine("Please enter your SynBioHub username (email address): "));
        }

        if (password == null) {
            password = System.console().readPassword("Please enter your SynBioHub password: ");
        }

        if (exclusiveUrlArgs == null) {
            exclusiveUrlArgs = new ExclusiveURLArgs();
        }

        if (exclusiveUrlArgs.collectionUrl == null && exclusiveUrlArgs.serverUrl == null) {
            String response = new String(System.console().readLine("Do you want to upload SBOL files to an existing collection [Y | N]: ")).trim();

            while(!Y_N_PATTERN.matcher(response).matches()) {
                response = new String(System.console().readLine("Do you want to upload SBOL files to an existing collection [Y | N]: ")).trim();
            }

            if(response.equals("Y")) {
                exclusiveUrlArgs.collectionUrl = new String(System.console().readLine("Please enter the URL of the collection you wish to upload to: "));
            } else {
                exclusiveUrlArgs.serverUrl = new String(System.console().readLine("Please enter the URL of the SynBioHub server you wish to upload to: "));

                if (collectionName == null) {
                    collectionName = new String(System.console().readLine("Please enter a name for the new collection to create: "));
                }
            }
        }

        if (dirPath == null) {
            dirPath = new String(System.console().readLine("Please enter the path to the directory or SBOL file you wish to upload: "));
        }

        return true;
    }
}