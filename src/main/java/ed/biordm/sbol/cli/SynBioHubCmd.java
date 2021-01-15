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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Command(name = "synBioHubCmd", mixinStandardHelpOptions = true, version = "1.0")
public class SynBioHubCmd implements Callable<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SynBioHubClientCmdRunner.class);

    @Autowired
    private SynBioHubClientService synBioHubClientService;

    private static final String CWD = System.getProperty("user.dir");

    // @Option(names = {"-u", "--username"}, description = "Username", interactive = true, required = true)
    // char[] username;

    @Option(names = {"-u", "--username"}, description = "Username", interactive = true, arity = "0..1", required = true)
    String username;

    @Option(names = {"-p", "--password"}, description = "Passphrase", interactive = true, arity = "0..1", required = true)
    char[] password;

    @Option(names = {"-c", "--collection-id"}, description = "Collection ID to deposit into", interactive = true, arity = "0..1", required = true)
    long collectionId;

    @Option(names = {"-s", "--server-url"}, description = "Base URL of SynBioHub API target. Default value is ${DEFAULT-VALUE}", descriptionKey="serverUrl", interactive = true, arity = "0..1", required = false)
    String serverUrl;

    @Option(names = {"-d", "--dir-path"}, description = "Directory path to the folder containing the SBOL files to submit. Default value is ${DEFAULT-VALUE}", descriptionKey="dirPath", interactive = true, arity = "0..1", required = false)
    String dirPath;

    @Option(names = {"-f", "--file-ext-filter"}, description = "File extension filter to refine the types of file that will be deposited. Default value is ${DEFAULT-VALUE}", descriptionKey="fileExtFilter", interactive = true, arity = "0..1", required = false)
    String fileExtFilter;

    @Option(names = {"-o", "--overwrite"}, description = "If overwrite is specified, any files with the same name in the designated collection ID will be overwritten. Default value is ${DEFAULT-VALUE}", descriptionKey="overwrite", interactive = true, arity = "0..1", required = false)
    boolean overwrite;

    /*@Autowired
    public SynBioHubCmd(@Value("${synBioHubClientService}") SynBioHubClientService synBioHubClientService) {
        
    }*/
    
    public Integer call() throws Exception {
        LOGGER.info("synBioHubCmd was called with --username={}", username);
        
        byte[] bytes = new byte[password.length];
        for (int i = 0; i < bytes.length; i++) { bytes[i] = (byte) password[i]; }

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bytes);

        LOGGER.info("Hi {}, your password is hashed to {}.", username, Base64.getEncoder().encodeToString(md.digest()));

        LOGGER.debug("SynBioHubClientService failed to initialise: {}", synBioHubClientService == null);

        LOGGER.debug("Specified server URL: {}", serverUrl);
        if(!serverUrl.equals(synBioHubClientService.getServerUrl())) {
            LOGGER.debug("Setting new server URL");
            // synBioHubClientService.setServerUrl(serverUrl);
            synBioHubClientService = new SynBioHubClientServiceImpl(synBioHubClientService, serverUrl);
        }

        synBioHubClientService.submitSBOLFiles(username, new String(password),
                collectionId, dirPath, fileExtFilter, overwrite);

        // null out the arrays when done
        Arrays.fill(bytes, (byte) 0);
        Arrays.fill(password, ' ');

        return 0;
    }
}