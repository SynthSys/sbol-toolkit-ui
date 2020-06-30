/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.fixer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import javax.xml.namespace.QName;
import org.sbolstandard.core2.Component;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLReader;
import org.sbolstandard.core2.SBOLValidationException;

/**
 *
 * @author Tomasz Zielinski
 */
public class Fixer {
    
    
    public void tryIt(Path file) throws SBOLValidationException, IOException, SBOLConversionException {
        
        SBOLDocument doc = SBOLReader.read(file.toFile());
        
        System.out.println(doc.getComponentDefinitions().size());
        //System.out.println(doc.getCollections().size());
        
        doc.getComponentDefinitions().forEach( (ComponentDefinition compd) -> {
            
            //Set<Component> comps = compd.getComponents();
            System.out.println(compd.getDisplayId());
            if (compd.getDisplayId().equals("IBMc050")) {
                compd.getAnnotations().forEach( a -> {
                    System.out.println(a.getQName());
                    if (a.isStringValue()) {
                        System.out.println(a.getStringValue());
                    }
                    System.out.println(a.getQName().getNamespaceURI());       
                });
            }
            
            if (compd.getDisplayId().equals("IBMc202")) {
                String value = "Name: IBMc202\n" +
"Summary: pSB3T5(BsaI-)-PBAD(SapI-)-B30-mCherry*(1-192)-5aa-SYNZIP17-BCD1-SYNZIP18-5aa-mCherry*(193-236)-H6-Ter\n" +
"Creator: Trevor Y. H. Ho\n" +
"Principal Investigator: Baojun Wang\n" +
"\n" +
"BioSafety Level: Level 1\n" +
"Backbone: pSB3T5\n" +
"Origin of replication: p15A\n" +
"Selection Markers: Tetracycline";
                
                QName qName = new QName("http://wiki.synbiohub.org/wiki/Terms/synbiohub#", 
                        "mutableDescription");
                
                try {
                    compd.createAnnotation(qName, value);
                } catch (SBOLValidationException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            
            if (compd.getDisplayId().equals("IBMc202")) {
                compd.getAnnotations().forEach( a -> {
                    System.out.println(a.getQName());
                    if (a.isStringValue()) {
                        System.out.println(a.getStringValue());
                    }
                            
                });
            }            
            
        });
    }
}
