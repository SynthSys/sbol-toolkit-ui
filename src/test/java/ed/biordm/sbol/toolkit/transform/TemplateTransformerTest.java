/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.toolkit.transform;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.SBOLDocument;

/**
 *
 * @author jhay
 */
public class TemplateTransformerTest {

    TemplateTransformer templateTransformer = new TemplateTransformer();

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
}
