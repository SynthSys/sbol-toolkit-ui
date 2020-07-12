/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.fixer;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tomasz Zielinski
 */
public class MetaReaderTest {
    
    MetaReader reader;
    
    public MetaReaderTest() {
    }
    
    @Before
    public void setUp() {
        
        reader = new MetaReader();
    }

    @Test
    public void readReads() {
        File file = testFile("IBM constructs by ID.csv");
        
        List<String[]> rows = reader.read(file.toPath(), 1);
        assertEquals(74, rows.size());
    }
    
    @Test
    public void parses() {
        File file = testFile("IBM constructs by ID.csv");
        
        Map<String,String[]> rows = reader.parse(file.toPath(), 1);
        assertEquals(74, rows.size());
        assertNotNull(rows.get("IBMc052"));
    }

    @Test
    public void selection() {
       init();
       String id = "IBMc050";
       assertEquals("Ampicillin / Chloramphenicol",reader.selection(id));
    }    

    @Test
    public void origin() {
       init();
       String id = "IBMc050";
       assertEquals("pMB1",reader.originOfRep(id));
    }     

    @Test
    public void backbone() {
       init();
       String id = "IBMc050";
       assertEquals("pSB1A3",reader.backbone(id));
    } 

    @Test
    public void description() {
       init();
       String id = "IBMc050";
       assertEquals("Plasmid carrying Golden Gate substitution insert for IBM, using Plux2 as C-lobe promoter",reader.description(id));
    } 

    @Test
    public void notes() {
       init();
       String id = "IBMc050";
       String exp ="Summary: pSB1A3-BbsI-M86N-cat-Plux2-B32-M86C-SapI\n" +
"Creator: Trevor Y. H. Ho\n" +
"Principal Investigator: Baojun Wang\n" +
"BioSafety Level: Level 1\n" +
"Backbone: pSB1A3\n" +
"Origin of replication: pMB1\n" +
"Selection Markers: Ampicillin / Chloramphenicol\n";
       assertEquals(exp,reader.notes(id));
    } 

     
    
    void init() {
        File file = testFile("IBM constructs by ID.csv");
        reader.init(file.toPath(), 1);
    }
    
    
    
    public File testFile(String name) {
        try {
            return new File(this.getClass().getResource(name).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
                
    }    
    
}
