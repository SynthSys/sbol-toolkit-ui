/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.toolkit.transform;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.sbolstandard.core2.Component;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLReader;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.Sequence;
import org.sbolstandard.core2.SequenceAnnotation;
import org.sbolstandard.core2.SequenceConstraint;

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

        ComponentDefinition newCmp = templateTransformer.instantiateFromTemplate(region,
                newName, newVersion, newDescription, doc);

        assertEquals(newName, newCmp.getDisplayId());
        assertEquals(newVersion, newCmp.getVersion());
        assertEquals(newDescription, newCmp.getDescription());
        assertEquals(region.getTypes(), newCmp.getTypes());
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
        for (Component orgComp : org.getComponents()) {
            Component cpy = newCmp.getComponent(orgComp.getDisplayId());
            assertNotNull(cpy);
            assertEquals(orgComp.getDefinitionIdentity(), cpy.getDefinitionIdentity());
        }
    }

    @Test
    public void testInstantiateFromTemplatePreservingSequenceConstraints() throws Exception {
        assertNotNull(doc);

        ComponentDefinition org = doc.getComponentDefinition("ampr_origin", "1.0.0");
        org.createAnnotation(new QName("https://ed.ac.uk/", "bio"), "johnny");

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

        assertEquals(org.getSequenceConstraints().size(), newCmp.getSequenceConstraints().size());

        for (SequenceConstraint orgCon : org.getSequenceConstraints()) {
            SequenceConstraint cpy = newCmp.getSequenceConstraint(orgCon.getDisplayId());
            assertNotNull(cpy);
            /*assertEquals(orgCon.getRoles(), cpy.getRoles());
            assertEquals(orgCon.getLocations().size(), cpy.getLocations().size());
            if (orgCon.getComponent() != null) {
                Component orgComp = orgCon.getComponent();
                Component cpyComp = cpy.getComponent();
                assertNotNull(cpyComp);
                assertEquals(orgComp.getDefinitionIdentity(), cpyComp.getDefinitionIdentity());
            }*/
        }

        assertEquals(org.getComponents().size(), newCmp.getComponents().size());
        for (Component orgComp : org.getComponents()) {
            Component cpy = newCmp.getComponent(orgComp.getDisplayId());
            assertNotNull(cpy);
            assertEquals(orgComp.getDefinitionIdentity(), cpy.getDefinitionIdentity());
        }
    }

    @Test
    public void concretizeComponentReplacesComponentWithANewConcreteDefinition() throws Exception {

        assertNotNull(doc);
        ComponentDefinition parent = doc.getComponentDefinition("cyano_codA_Km", "1.0.0");
        assertNotNull(parent);

        String genericComponentId = "right";
        Component replaced = parent.getComponent(genericComponentId);
        System.out.println("Replaced component displayId: " + replaced.getDisplayId());
        assertNotNull(replaced);
        ComponentDefinition replacedDef = doc.getComponentDefinition(replaced.getDefinitionIdentity());
        assertNotNull(replacedDef);

        String newName = "right!/new";
        String newSequence = "GATTACA";

        ComponentDefinition newDeff = templateTransformer.concretizePart(parent, genericComponentId, newName, newSequence, doc);
        assertNotNull(newDeff);

        //it is being replaced
        assertNull(parent.getComponent(genericComponentId));

        // Shouldn't the genericComponentId return the newly created replacement
        // component? Won't the new component and the old component share the
        // same genericComponentId i.e. the String returned from 'getIdentity',
        // but they will simply have different names (i.e. 'getName') and/or
        // display IDs (i.e. 'getDisplayId')?
        //assertNotNull(newDeff.getComponent(genericComponentId));
        String newDisplayId = templateTransformer.sanitizeName(newName);

        Component newComp = parent.getComponent(newDisplayId);
        assertNotNull(newComp);

        assertEquals(newDisplayId, newComp.getName());
        assertEquals(parent.getVersion(), newComp.getVersion());
        assertEquals(newDeff.getIdentity(), newComp.getDefinitionIdentity());
        assertEquals(replaced.getRoles(), newComp.getRoles());

        //check if newDefinition is correct        
        assertEquals(newDisplayId, newDeff.getName());
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
    public void testConcretizeComponent() throws Exception {
        Set<ComponentDefinition> cmpDefs = doc.getComponentDefinitions();
        int cmpCount = 0;

        for (ComponentDefinition cmpDef : cmpDefs) {
            System.out.println(cmpDef.getDisplayId());
            System.out.println(cmpDef.getIdentity());

            String cmpDefId = cmpDef.getDisplayId();

            if (cmpDefId.equals("sll00199_codA_Km") || cmpDefId.equals("cyano_codA_Km")) {
                // Create new sub-component belonging to one of these parent CDs
                Component subCmp = cmpDef.getComponent("left");
                // String genericComponentId = subCmp.getIdentity().toString();
                String genericComponentId = subCmp.getDisplayId();
                System.out.println(genericComponentId);
                String newName = "test_left_".concat(String.valueOf(cmpCount));
                String newSequence = "GATTACA";

                // Get list of original components in parent before concretize
                List<Component> origCmps = cmpDef.getSortedComponents();

                ComponentDefinition newSubCmpDef = templateTransformer.concretizePart(cmpDef, genericComponentId,
                        newName, newSequence, doc);

                // Get child components and verify they match in new component
                List<Component> newCmps = cmpDef.getSortedComponents();
                int count = 0;

                for (Component child : origCmps) {
                    Component newCmp = newCmps.get(count);

                    System.out.println(child.getDisplayId());
                    System.out.println(newCmp.getDisplayId());
                }

                cmpCount += 1;
                // Get sequence constraints and verify they match in new component

                // Get sequence annos and verify they match in new component
            }
        }
    }

    /**
     * Test of instantiateFromTemplate method, of class TemplateTransformer.
     */
    @Test
    public void testFlattenSequences() throws Exception {
        assertNotNull(doc);
        ComponentDefinition template = doc.getComponentDefinition("sll00199_codA_Km", "1.0.0");
        assertNotNull(template);

        // Assume we are adding a new sequence to the component
        String version = "1.0.0"; // should this be the version of the component definition?

        int cmpCount = 0;

        for (Sequence sequence : template.getSequences()) {
            System.out.println("Initial sequence: " + sequence.getElements());
        }

        String newName = "sll00199_codA_Km!/new_1";
        ComponentDefinition parent = templateTransformer.flattenSequences(template, newName, doc);

        assertNotNull(parent);

        String expFlattenedSeq = buildSll00199CodAKmSequence();
        for (Sequence sequence : parent.getSequences()) {
            System.out.println("Flattened sequence:\n" + sequence.getElements());
            assertEquals(expFlattenedSeq, sequence.getElements());
        }

        for (Component cmp : template.getComponents()) {
            ComponentDefinition cmpDef = cmp.getDefinition();
            String newSequence = "GATTACA";
            Sequence seq = doc.createSequence(cmpDef.getDisplayId() + "_" + cmpCount + "_seq", version,
                    newSequence, Sequence.IUPAC_DNA);
            cmpDef.addSequence(seq);
        }

        newName = "sll00199_codA_Km!/new_2";
        parent = templateTransformer.flattenSequences(template, newName, doc);

        assertNotNull(parent);

        for (Sequence sequence : parent.getSequences()) {
            System.out.println("Flattened sequence:\n" + sequence.getElements());
            assertNotEquals(expFlattenedSeq, sequence.getElements());
        }
    }

    private String buildSll00199CodAKmSequence() {
        assertNotNull(doc);

        // ampr -> left -> insert -> right -> gap
        Sequence amprOrigSeq = doc.getSequence("ampr_origin_seq", "1.0.0");
        Sequence leftSeq = doc.getSequence("sll00199_left_seq", "1.0.0");
        Sequence codAKmSeq = doc.getSequence("codA_Km_seq", "1.0.0");
        Sequence rightSeq = doc.getSequence("sll00199_right_seq", "1.0.0");
        Sequence gapSeq = doc.getSequence("gap_seq", "1.0.0");

        assertNotNull(amprOrigSeq);
        assertNotNull(leftSeq);
        assertNotNull(codAKmSeq);
        assertNotNull(rightSeq);
        assertNotNull(gapSeq);

        String flattenedSequence = amprOrigSeq.getElements();
        System.out.println(flattenedSequence);
        flattenedSequence = flattenedSequence.concat(leftSeq.getElements());
        System.out.println(flattenedSequence);
        flattenedSequence = flattenedSequence.concat(codAKmSeq.getElements());
        flattenedSequence = flattenedSequence.concat(rightSeq.getElements());
        flattenedSequence = flattenedSequence.concat(gapSeq.getElements());

        return flattenedSequence;
    }
}
