/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.toolkit.transform;

import java.net.URISyntaxException;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.Sequence;
import org.sbolstandard.core2.SequenceOntology;

/**
 *
 * @author tzielins
 */
public class TemplateTransformer {
    
    /**
     * Creates new instance of component definition using the provided template.
     * @param template component to be copied
     * @param newName name of the component (will be transformed into displayId)
     * @param version 
     * @param description description to be added to the definition
     * @param doc including sbol document
     * @return new component definition which is a deep copy of the template with the given properties set
     * @throws SBOLValidationException
     * @throws URISyntaxException 
     */
    public ComponentDefinition instantiateFromTemplate(ComponentDefinition template, 
            String newName, String version, String description, SBOLDocument doc) throws SBOLValidationException, URISyntaxException {

        // name shoudl be sanitize for conversion into display id as alphanumeric with _ (replace all not alphanumeri caracters with _)
        // it should be deep copy, i.e. the owned object must be copied like component, sequenceanotations, sequenceConstraints
        // that should be already handled by doc.crateCopy method.
        throw new UnsupportedOperationException("Not supported yet.");
    }    
    
    /**
     * Replaces sub-component with a new one having similar properties but a given sequence.
     * @param parent component definition which is going to be altered to point to new subcomponent
     * @param genericComponentId displayId of the component which is going to be replaced by a new link
     * @param newName name of the new component instance / component definiton 
     * @param newSequence DNA sequence to be added to the new component definition if provided
     * @param doc including sbol document
     * @return defintion of the new sub component that has been linked to the parent
     * @throws SBOLValidationException
     * @throws URISyntaxException 
     */
    public ComponentDefinition concreatizePart(ComponentDefinition parent, String genericComponentId, 
            String newName, String newSequence, SBOLDocument doc) throws SBOLValidationException, URISyntaxException {
        
        // name shoudl be sanitize for conversion into display id as alphanumeric with _ (replace all not alphanumeri caracters with _)
        // parent has a sub component of the genericComponentId which has to be replaced by the new definiton
        // the CompomonentDefinition of the genericComponentId instance has to be found in the doc, 
        // a deep copy made with the newName (doc.createCopy should handle it)
        // a sequence has to be added if present
        // derived from should be set to the generic-definition
        // the genericComponentId has to be removed and a new component instance pointing the created new component defintion created
        // in the parent component defition the sequenceAnotations and sequeceConstraints have to be updated to point
        // to new component instead of genericComponentId
        // it returns the new sub component definion not the parent so it can be further customized if needed
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Creates new component definiton which contains a flattened sequence from its subcomponents.
     * The new component has its sequence annotated using its subocmponents annotations
     * @param template component for which a sequence should be generated
     * @param newName name (converted to diplayid) for the new component defintion
     * @param doc including sbol document
     * @return new component definition with explicit sequence
     * @throws SBOLValidationException
     * @throws URISyntaxException 
     */
    public ComponentDefinition flattenSequences(ComponentDefinition template, String newName, SBOLDocument doc) throws SBOLValidationException, URISyntaxException {
        
        // template has parts which all have concrete sequences (we can assume it, throw exceptino if not??)
        // the sequences from the parts have to be joined into one, following the correct order 
        // order may come from sequence annotations that map to components or from the sequenceconstrains that define 
        // respective locations. (ther may be API methods in the lib to have those parts ordered correctly, as the synbio hub renders the
        // parts on the graphs in the correct order
        // to be usable for the user (see the features), the new sequence has to be annotated with featues from the subcomponents
        // lets assume one level down as it is our case (if not it can be always called recursively)
        // the tricky part will be that subcomponents sequences are annotted from 1 for each
        // in the joined sequence the annotations has to be shifted depending where the componante instance has been located (how long
        // was the sequence that came from other parts).
        
        // check the code for template.getImpliedNucleicAcidSequence()
        // it may already generate the correct sequence
        // then only creating new sequence annotations in the new locations needs implementation.
        
        // for subcomponents whose annotations were pointing to components not having own role
        // a new component instance has to be created in the top component using the same component definition
        // and then it has to be set in the sequence annotations (grandfather cannot use its granchilids components directly
        // in the sequence annotations.
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
