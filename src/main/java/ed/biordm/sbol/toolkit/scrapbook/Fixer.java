/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.toolkit.scrapbook;

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
import org.sbolstandard.core2.SequenceOntology;

/**
 *
 * @author Tomasz Zielinski
 */
public class Fixer {
    
    static    QName description = new QName("http://wiki.synbiohub.org/wiki/Terms/synbiohub#", 
            "mutableDescription"); 
    
    static     QName notes = new QName("http://wiki.synbiohub.org/wiki/Terms/synbiohub#", 
            "mutableNotes");    

    // does not work as contains :
    static    QName selection = new QName(SequenceOntology.NAMESPACE.toString(), 
            "SO:0002232","so");
        
    static     QName origin = new QName(SequenceOntology.NAMESPACE.toString(), 
            "SO:0000296","so");     
    
    
    public void fix(SBOLDocument doc, MetaReader meta, String version) throws SBOLValidationException {
        
        for (String id: meta.ids()) {
            
            ComponentDefinition comp = doc.getComponentDefinition(id, version);
            if (comp == null)
                throw new IllegalArgumentException("Missing component id: "+id);
            
            comp.createAnnotation(description, meta.description(id));
            comp.createAnnotation(notes, meta.notes(id));
            // sequence ontology terms do not pass
            //comp.createAnnotation(selection, meta.selection(id));
            //comp.createAnnotation(origin, meta.originOfRep(id));
        }
    }
    

}
