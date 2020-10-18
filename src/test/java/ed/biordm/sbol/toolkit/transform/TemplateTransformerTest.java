/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.toolkit.transform;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.sbolstandard.core2.Component;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.Location;
import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLReader;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.Sequence;
import org.sbolstandard.core2.SequenceAnnotation;

/**
 *
 * @author jhay
 */
public class TemplateTransformerTest {

    TemplateTransformer templateTransformer = new TemplateTransformer();
    SBOLDocument doc;
    
    @Before
    public void generateSBOLDocument() throws IOException, SBOLValidationException, SBOLConversionException {
        String fName = "cyano_template.xml";
        File file = new File(getClass().getResource(fName).getFile());

        try {
            doc = SBOLReader.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        doc.setDefaultURIprefix("http://bio.ed.ac.uk/a_mccormick/cyano_source/");
        doc.setComplete(true);
        doc.setCreateDefaults(true);
    }

    /**
     * Test of instantiateFromTemplate method, of class TemplateTransformer.
     */
    @Test
    public void testInstantiateFromTemplate() throws Exception {
        SBOLDocument doc = new SBOLDocument();
        String defaultURIPrefix = "http://bio.ed.ac.uk/a_mccormick/cyano_source/";

        doc.setDefaultURIprefix(defaultURIPrefix);
        doc.setComplete(true);
        doc.setCreateDefaults(true);

        // Test creation of new component definition based on DNA_REGION template
        ComponentDefinition region = doc.createComponentDefinition("region1", "1.0.0", ComponentDefinition.DNA_REGION);
        //TopLevel tl = doc.createCopy(region, "region2");

        String newName = "region2";
        String newVersion = "1.0.1";
        String newDescription = "Deep copy of DNA_REGION component";
        URI expTypeURI = URI.create(defaultURIPrefix.concat(region.getDisplayId())
                .concat("/").concat(region.getVersion()));
        Set<URI> expTypesSet = new HashSet<>();
        expTypesSet.add(expTypeURI);

        ComponentDefinition newCmp = templateTransformer.instantiateFromTemplate(region,
                newName, newVersion, newDescription, doc);

        assertEquals(newName, newCmp.getDisplayId());
        assertEquals(newVersion, newCmp.getVersion());
        assertEquals(newDescription, newCmp.getDescription());
        assertEquals(expTypesSet, newCmp.getTypes());
        assertEquals(region.getRoles(), newCmp.getRoles());
    }

    @Test
    public void instantiateFromTemplateCreatesNewDefinitionWithGivenAttributesPreservingExistingFeatures() throws Exception {
        
        assertNotNull(doc);

        ComponentDefinition org = doc.getComponentDefinition("ampr_origin", "1.0.0");
        org.createAnnotation(new QName("https://ed.ac.uk/", "bio"), "tomek");
        
        assertNotNull(org);
        
        String newName = "escape! Me";
        String newVersion = "1.0.1";
        String newDescription = "Deep copy of DNA_REGION component";

        ComponentDefinition newCmp = templateTransformer.instantiateFromTemplate(org,
                newName, newVersion, newDescription, doc);
        
        assertNotSame(org, newCmp);

        assertEquals(newName, newCmp.getName());
        assertEquals("escape__Me", newCmp.getDisplayId());
        assertEquals(newVersion, newCmp.getVersion());
        assertEquals(newDescription, newCmp.getDescription());
        assertEquals(org.getTypes(), newCmp.getTypes());
        assertEquals(org.getRoles(), newCmp.getRoles());
        assertEquals(org.getSequences(), newCmp.getSequences());
        assertEquals(org.getAnnotations(), newCmp.getAnnotations());
        
        assertEquals(org.getSequenceAnnotations().size(), newCmp.getSequenceAnnotations().size());
        for (SequenceAnnotation orgAnn : org.getSequenceAnnotations()) {
            SequenceAnnotation cpy = newCmp.getSequenceAnnotation(orgAnn.getDisplayId());
            assertNotNull(cpy);
            assertEquals(orgAnn.getRoles(), cpy.getRoles());
            assertEquals(orgAnn.getLocations().size(), cpy.getLocations().size());
            if (orgAnn.getComponent() != null) {
                Component orgComp = orgAnn.getComponent();
                Component cpyComp = cpy.getComponent();
                assertNotNull(cpyComp);
                assertEquals(orgComp.getDefinitionIdentity(), cpyComp.getDefinitionIdentity());
            }
        }
        
        assertEquals(org.getComponents().size(), newCmp.getComponents().size());
        for (Component orgComp: org.getComponents()) {
            Component cpy = newCmp.getComponent(orgComp.getDisplayId());
            assertNotNull(cpy);
            assertEquals(orgComp.getDefinitionIdentity(), cpy.getDefinitionIdentity());
        }
    }
    
    @Test
    public void concreatizeComponentReplacesComponentWithANewConcreteDefinition() throws Exception {
        
        assertNotNull(doc);
        ComponentDefinition parent = doc.getComponentDefinition("cyano_codA_Km", "1.0.0");
        assertNotNull(parent);
        
        String genericComponentId = "right";
        Component replaced = parent.getComponent(genericComponentId); 
        assertNotNull(replaced);
        ComponentDefinition replacedDef = doc.getComponentDefinition(replaced.getDefinitionIdentity());
        assertNotNull(replacedDef);
        
        String newName = "right!/new";
        String newSequence = "GATTACA";
        
        ComponentDefinition newDeff = templateTransformer.concreatizePart(parent, genericComponentId, newName, newSequence, doc);
        assertNotNull(newDeff);
        
        //it is being replaced
        assertNull(parent.getComponent(genericComponentId));
        String newDisplayId = templateTransformer.sanitizeName(newName);
        
        Component newComp = parent.getComponent(newDisplayId);
        assertNotNull(newComp);
        
        assertEquals(newName, newComp.getName());
        assertEquals(parent.getVersion(),newComp.getVersion());
        assertEquals(newDeff.getIdentity(),newComp.getDefinitionIdentity());
        assertEquals(replaced.getRoles(), newComp.getRoles());
        
        //check if newDefinition is correct        
        assertEquals(newName, newDeff.getName());
        assertEquals(newDisplayId, newDeff.getDisplayId());
        
        assertTrue(newDeff.getSequences().stream().findFirst().isPresent());
        Sequence seq = newDeff.getSequences().stream().findFirst().get();
        assertEquals(newSequence, seq.getElements());
        assertEquals(Sequence.IUPAC_DNA, seq.getEncoding());
        
        assertEquals(replacedDef.getTypes(), newDeff.getTypes());
        assertEquals(replacedDef.getRoles(), newDeff.getRoles());
        
        //write teest if the sequences constraints have been replaced with new one
        //that points to newComp instead to the replaced
        
        
    }
    
    
    /**
     * Test of instantiateFromTemplate method, of class TemplateTransformer.
     */
    @Test
    public void testConcreatizeComponent() throws Exception {
        Set<ComponentDefinition> cmpDefs = doc.getComponentDefinitions();
        
        for(ComponentDefinition cmpDef: cmpDefs) {
            System.out.println(cmpDef.getDisplayId());
            System.out.println(cmpDef.getIdentity());
            
            String cmpDefId = cmpDef.getDisplayId();
            
            if(cmpDefId.equals("sll00199_codA_Km") || cmpDefId.equals("cyano_codA_Km")) {
                // Create new sub-component belonging to one of these parent CDs
                Component subCmp = cmpDef.getComponent("left");
                String genericComponentId = subCmp.getIdentity().toString();
                System.out.println(genericComponentId);
                String newName = "test_left";
                String newSequence = "test_sequence";
                
                ComponentDefinition newParent = templateTransformer.concreatizePart(cmpDef, genericComponentId,
                    newName, newSequence, doc);

                // Get child components and verify they match in new component
                List<Component> origCmps = cmpDef.getSortedComponents();
                List<Component> newCmps = newParent.getSortedComponents();
                int count = 0;
                
                for(Component child: origCmps) {
                    Component newCmp = newCmps.get(count);
                    
                    System.out.println(child.getDisplayId());
                    System.out.println(newCmp.getDisplayId());
                }
                // Get sequence constraints and verify they match in new component
                
                
                // Get sequence annos and verify they match in new component
            }
        }
        
        /*
        ComponentDefinition concreatizePart(ComponentDefinition parent, String genericComponentId,
            String newName, String newSequence, SBOLDocument doc)*/
    }
}
