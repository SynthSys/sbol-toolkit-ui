/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.service;

/**
 *
 * @author jhay
 */
public interface SynBioHubClientService {
    
    void doLogin();

    void submitSBOLFiles(String username, String password, String collectionId,
            String dirPath, String fileExtFilter, boolean isOverwrite) throws Exception;
}
