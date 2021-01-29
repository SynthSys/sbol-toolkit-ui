/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.cli;

/**
 *
 * @author jhay
 */
public class SynBioHubCmdArgs {

    String username;

    char[] password;

    String collectionName;

    String dirPath;

    String fileExtFilter;

    boolean overwrite;

    String url;

    String collDescription;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getFileExtFilter() {
        return fileExtFilter;
    }

    public void setFileExtFilter(String fileExtFilter) {
        this.fileExtFilter = fileExtFilter;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCollDescription() {
        return collDescription;
    }

    public void setCollDescription(String collDescription) {
        this.collDescription = collDescription;
    }
}
