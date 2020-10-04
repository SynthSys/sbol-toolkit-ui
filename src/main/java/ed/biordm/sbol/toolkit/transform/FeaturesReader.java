/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.toolkit.transform;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tzielins
 */
public class FeaturesReader {

    /**
     * Reads features to be used by transformations from excel file. 
     * First column contains object id, second is the feature value. 
     * It can ignore header rows and read from particular sheet
     * @param file
     * @param skipRows
     * @param sheetNr
     * @return map with id, value pairs from the read rows
     */
    public Map<String,String> readSimpleFeatures(Path file, int skipRows, int sheetNr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    

    /**
     * Reads multiple features represented as multiple values in consecutive columns.
     * First column contains object id, the following the values. 
     * It can ignore header rows and read from particular sheet
     * @param file
     * @param skipRows
     * @param sheetNr
     * @return map with id and the list of read features values
     */
    public Map<String, List<String>> readMultiFeatures(Path file, int skipRows, int sheetNr) {
        throw new UnsupportedOperationException("Not supported yet.");
        
    }
}
