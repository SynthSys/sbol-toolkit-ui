/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.cli;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.Callable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import ed.biordm.sbol.service.SynBioHubClientService;

/**
 *
 * @author jhay
 */
@Component 
@Command(name = "synBioHubCmd")
public class SynBioHubCmd implements Callable<Integer> {

    // @Autowired
    private SynBioHubClientService synBioHubClientService; 

    @Option(names = {"-s", "--server-url"}, description = "Base URL of SynBioHub API target", required = false)
    String serverURL;

    @Option(names = {"-u", "--username"}, description = "Username", interactive = true, required = true)
    char[] username;

    @Option(names = {"-p", "--password"}, description = "Passphrase", interactive = true, required = true)
    char[] password;

    public Integer call() throws Exception {
        // mailService.sendMessage(to, subject, String.join(" ", body)); 
        System.out.printf("synBioHubCmd was called with --username=%s", username);
        
        byte[] bytes = new byte[password.length];
        for (int i = 0; i < bytes.length; i++) { bytes[i] = (byte) password[i]; }

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bytes);

        System.out.printf("Hi %s, your password is hashed to %s.%n", username, Base64.getEncoder().encodeToString(md.digest()));

        // null out the arrays when done
        Arrays.fill(bytes, (byte) 0);
        Arrays.fill(password, ' ');

        return 0;
    }
}