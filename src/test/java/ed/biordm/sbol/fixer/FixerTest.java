/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.fixer;

import java.io.File;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tomasz Zielinski
 */
public class FixerTest {
    
    
    Fixer fixer;
    
    public FixerTest() {
    }

    @Before
    public void setUp() {
        fixer = new Fixer();
        
    }

    @Test
    public void readsDoc() throws Exception {
        String fName = "Inteinassistedbisectionmapping_collection-fixed.sbol";
        fixer.tryIt(testFile(fName).toPath());
        
        fail("to see out");
    }
    
    
    @Test
    public void testFilesReads() {
        
        File f = testFile("Inteinassistedbisectionmapping_collection-fixed.sbol");
        assertNotNull(f);
        
    }
    
    public File testFile(String name) {
        try {
            return new File(this.getClass().getResource(name).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
                
    }
    
}
