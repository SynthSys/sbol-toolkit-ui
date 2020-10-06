/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.toolkit.transform;

import ed.biordm.sbol.toolkit.transform.ExcelFormatException;
import ed.biordm.sbol.toolkit.transform.FeaturesReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author jhay
 */
public class FeaturesReaderTest {
    FeaturesReader featuresReader = new FeaturesReader();

    /**
     * Test of readSimpleFeatures method, of class FeaturesReader.
     */
    @Test
    public void testReadSimpleFeatures() throws Exception {
        File file = new File(getClass().getResource("flank-list.xlsx").getFile()); 
        
        Map<String, String> features = new HashMap<>();
        
        try {
            String expKey = "0684_slr2015_left";
            String expValue = "TGCGATCAACAAATCTTTCGATATAGACCGAAAAAAAGAAATCTCACTGGCCAACTGTTTAAGTTGTTTTGTTGACGGCTTTTGTGGTGCCATGACAATATATCCAAACAATAACTAAATTCTCCCCTAGACCCTCTCAAACCCCAGTTTCAGGGAAAATTATAGAAACAAGTAAACTGTTTTGGTGTTTATTGTCACAGGTTTTGGTGAGCTGGCTAACAATCATTTGTTGTAAATTAACTTATCATCTAGGTGATGTTAGTTGTCACCCATCCTATGACAATATGTGTTTAATTTGACTAGCAATAGTCCAGAGATGTTCTCTAAAACTTCGTTTTTAGGCCCATACTTCACCTTCCTACAATCCCCCTCCCCCCCATCTATTCCCTGTGACTTGAACCCCTTTGTTTGCCTTTAAGAGAAATTTAAAATCCCTTTATTTGAGCTTTAAGCACTTAGCTATTAACCTGAAATTTGACCCAGAAAAACCAATAAAAGACTTCCCCACGGTCTTTGCTGACTTCCCAAAAACCCCAATCGACC";
            
            Map<String, String> result = featuresReader.readSimpleFeatures(file.toPath(), 0, 0);
            assertEquals(expValue, result.get(expKey));

            expKey = "0002_slr0612_left";
            expValue = "AACATTTGGGGTTAGCGTTCCAGATTGTGGACGATATTTTAGATTTCACTTCCCCCACGGAGGTTTTGGGGAAACCGGCCGGGTCAGATTTAATCAGCGGCAACATCACCGCCCCAGCCCTATTTGCCATGGAAAAATATCCCCTACTTGGTAAATTAATTGAACGGGAATTTGCCCAGGCGGGGGATTTGGAACAGGCCCTGGAATTGGTAGAACAGGGGGATGGTATCCGGCGATCAAGGGAATTGGCCGCGAACCAAGCGCAACTGGCCCGGCAACATCTGAGTGTGCTGGAAATGTCCGCTCCGAGAGAATCTCTGTTGGAATTAGTTGATTATGTGCTTGGTCGTCTCCATTAGGTTTTCCCGTAGATTTTTTCCCAGCGGGCTTGATTGCGTTGAATAAAACTCCCCAAACCATTGTTTTTTACAAACCCTACGGAGTTCTGTGTCAATTTACCGATAATTCTGCCCATCCCCGGCCGACGTTGAAGGATTATATTAATTTGCCAGATTTATATCCC";
            
            result = featuresReader.readSimpleFeatures(file.toPath(), 1, 0);
            assertEquals(expValue, result.get(expKey));

        } catch(IOException e) {
            
        } catch(ExcelFormatException e) {
            
        }
    }
}
