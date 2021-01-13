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
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import ed.biordm.sbol.service.SynBioHubClientService;
import java.util.Properties;
import picocli.CommandLine.PropertiesDefaultProvider;

@Command(name = "synBioHubCmd", mixinStandardHelpOptions = true, version = "1.0")
public class SynBioHubCmd implements Callable<Integer> {

    // @Autowired
    private SynBioHubClientService synBioHubClientService;

    private static final String CWD = System.getProperty("user.dir");

    // @Option(names = {"-u", "--username"}, description = "Username", interactive = true, required = true)
    // char[] username;

    @Option(names = {"-u", "--username"}, description = "Username", interactive = true, arity = "0..1", required = true)
    String username;

    @Option(names = {"-p", "--password"}, description = "Passphrase", interactive = true, arity = "0..1", required = true)
    char[] password;

    @Option(names = {"-c", "--collection-id"}, description = "Collection ID to upload into", interactive = true, arity = "0..1", required = true)
    long collectionId;

    @Option(names = {"-d", "--dir-path"}, defaultValue = ".", description = "Directory path to the folder containing the SBOL files to submit. Default value is ${DEFAULT-VALUE}", interactive = true, arity = "0..1", required = false)
    String dirPath;

    @Option(names = {"-s", "--server-url"}, description = "Base URL of SynBioHub API target", interactive = true, arity = "0..1", required = false)
    String serverURL;
    
    public Integer call() throws Exception {
        System.out.printf("synBioHubCmd was called with --username=%s%n", username);
        
        byte[] bytes = new byte[password.length];
        for (int i = 0; i < bytes.length; i++) { bytes[i] = (byte) password[i]; }

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bytes);

        System.out.printf("Hi %s, your password is hashed to %s.%n", username, Base64.getEncoder().encodeToString(md.digest()));

        synBioHubClientService.submitSBOLFiles(username, serverURL, serverURL, username, serverURL, true);

        // null out the arrays when done
        Arrays.fill(bytes, (byte) 0);
        Arrays.fill(password, ' ');

        return 0;
    }
}