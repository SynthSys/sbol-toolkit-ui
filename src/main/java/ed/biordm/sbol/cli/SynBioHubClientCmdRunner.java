/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.cli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 *
 * @author jhay
 */
//@SpringBootApplication
public class SynBioHubClientCmdRunner implements CommandLineRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SynBioHubClientCmdRunner.class).web(WebApplicationType.NONE).run(args);
    }

    @Override
    public void run(String... arg0) throws Exception {
        System.out.println("Run");

    }
}