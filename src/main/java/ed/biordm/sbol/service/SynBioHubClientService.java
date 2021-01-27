/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.service;

import java.net.URI;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;

/**
 *
 * @author jhay
 */
public interface SynBioHubClientService {

    String getServerUrl();

    /*void setServerUrl(String synBioHubBaseUrl);

    RestTemplate getRestTemplate();*/

    RestTemplateBuilder getRestTemplateBuilder();

    HttpHeaders doLogin(String email, String password);

    void submitSBOLFiles(String email, String password, String collectionName,
            String dirPath, String fileExtFilter, boolean isOverwrite) throws Exception;

    void submitSBOLFiles(String email, String password, URI collectionUrl,
            String dirPath, String fileExtFilter, boolean isOverwrite) throws Exception;
}
