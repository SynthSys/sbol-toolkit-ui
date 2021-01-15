/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author jhay
 */
public interface SynBioHubClientService {

    String getServerUrl();

    /*void setServerUrl(String synBioHubBaseUrl);

    RestTemplate getRestTemplate();*/

    RestTemplateBuilder getRestTemplateBuilder();

    void doLogin(String username, String password);

    void submitSBOLFiles(String username, String password, long collectionId,
            String dirPath, String fileExtFilter, boolean isOverwrite) throws Exception;
}
