package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.version_1_2;

import com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.ModificationLowMemController;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Davy
 */
public class ModificationLowMemControllerTest {

    private static MsfFile msfFile;

    public ModificationLowMemControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException, SQLException {
        msfFile = new MsfFile(new File(ModificationLowMemControllerTest.class.getClassLoader().getResource("test-msf-v-1.2.msf").getPath()));
    }

    /**
     * Test of getModifiedSequenceForPeptide method, of class
     * ModificationLowMemController.
     */
    @Test
    public void testAddModificationsToPeptideSequence() {
        System.out.println("addModificationsToPeptideSequence");
        fail("add an actual peptide to add to");
        PeptideLowMem peptide = null;
        ModificationLowMemController instance = new ModificationLowMemController();
        String expResult = "";
        String result = instance.getModifiedSequenceForPeptide(peptide, msfFile);
        assertEquals(expResult, result);
    }

    /**
     * Test of createModificationMap method, of class
     * ModificationLowMemController.
     */
    @Test
    public void testCreateModificationMap() {
        System.out.println("createModificationMap");
        ModificationLowMemController instance = new ModificationLowMemController();
        HashMap<Integer, String> result = instance.createModificationMap(msfFile);
        assertThat(result.isEmpty(), is(false));
        assertThat(result.containsKey(709), is(true));
        assertThat(result.get(5), is("Amidated"));
    }

    /**
     * Test of getAllModificationNames method, of class
     * ModificationLowMemController.
     */
    @Test
    public void testGetAllModificationNames() {
        System.out.println("getAllModificationNames");
        ModificationLowMemController instance = new ModificationLowMemController();
        List<String> result = instance.getAllModificationNames(msfFile);
        assertThat(result.isEmpty(), is(false));
        assertThat(result.size(), is(709));
        assertThat(result.get(1), is("15dB-biotin"));
        assertThat(result.get(708), allOf(is("ZGB"), is(result.get(707))));
    }

    /**
     * Test of getPeptidesWithModification method, of class
     * ModificationLowMemController.
     */
    @Test
    public void testGetPeptidesWithModification() {
        System.out.println("getPeptidesWithModification");
        String modification = "Oxidation";
        ModificationLowMemController instance = new ModificationLowMemController();
        List<PeptideLowMem> result = instance.getPeptidesWithModification(modification, msfFile,false);
        assertThat(result.isEmpty(), is(false));
        assertThat(result.size(),is(33));
        assertThat(result.get(5).getModifiedPeptideSequence(),is("NH2-KDLMSSK-COOH"));
        assertThat(result.get(5).getSequence(),is("KDLMSSK"));
        List<PeptideLowMem> resultWithModificationsAdded = instance.getPeptidesWithModification(modification, msfFile, true);
        assertThat(resultWithModificationsAdded.get(5).getSequence(),is("KDLMSSK"));
        assertThat(resultWithModificationsAdded.get(5).getModifiedPeptideSequence(),is("NH2-KDLMSSK-COOH"));
        }

    /**
     * Test of getAllModifications method, of class
     * ModificationLowMemController.
     */
    @Test
    public void testGetAllModifications() {
        System.out.println("getAllModifications");
        ModificationLowMemController instance = new ModificationLowMemController();
        List result = instance.getAllModifications(msfFile);
        assertThat(result.size(),is(0));
    }

    /**
     * Test of getListOfFixedModificationNumbers method, of class
     * ModificationLowMemController.
     */
    @Test
    public void testGetListOfFixedModificationNumbers() {
        System.out.println("getListOfFixedModificationNumbers");
        ModificationLowMemController instance = new ModificationLowMemController();
        List result = instance.getListOfFixedModificationNumbers(msfFile);
        assertThat(result.size(),is(0));
    }

    /**
     * Test of getListOfVariableModidifcationNumbers method, of class
     * ModificationLowMemController.
     */
    @Test
    public void testGetListOfVariableModidifcationNumbers() {
        System.out.println("getListOfVariableModidifcationNumbers");
        ModificationLowMemController instance = new ModificationLowMemController();
        List result = instance.getListOfVariableModidifcationNumbers(msfFile);
        assertThat(result.size(),is(0));
    }
}