/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.cli;

import java.util.List;
import java.util.concurrent.Callable;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 *
 * @author jhay
 */
@Component 
@Command(name = "synBioHubCmd")
public class SynBioHubCmd implements Callable<Integer> {

    // @Autowired
    // private IMailService mailService; 

    @Option(names = "--to", description = "email(s) of recipient(s)", required = true)
    List<String> to;

    @Option(names = "--subject", description = "Subject")
    String subject;

    @Parameters(description = "Message to be sent")
    String[] body = {};

    public Integer call() throws Exception {
        // mailService.sendMessage(to, subject, String.join(" ", body)); 
        System.out.printf("synBioHubCmd was called with --to=%s and subject: %s%n", to, subject);
        return 0;
    }
}