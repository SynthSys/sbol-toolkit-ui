/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.fixer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
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
                            
                });
            }
            
        });
    }
}
