package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.version_1_2;

import com.compomics.thermo_msf_parser_API.highmeminstance.Modification;
import com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.ModificationLowMemController;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
     * Test of addModificationsToPeptideSequence method, of class
     * ModificationLowMemController.
     */
    @Test
    public void testAddModificationsToPeptideSequence() {
        System.out.println("addModificationsToPeptideSequence");
        PeptideLowMem peptide = null;
        ModificationLowMemController instance = new ModificationLowMemController();
        String expResult = "";
        String result = instance.addModificationsToPeptideSequence(peptide, msfFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createModificationMap method, of class
     * ModificationLowMemController.
     */
    @Test
    public void testCreateModificationMap() {
        System.out.println("createModificationMap");
        ModificationLowMemController instance = new ModificationLowMemController();
        HashMap expResult = null;
        HashMap result = instance.createModificationMap(msfFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllModificationNames method, of class
     * ModificationLowMemController.
     */
    @Test
    public void testGetAllModificationNames() {
        System.out.println("getAllModificationNames");
        MsfFile msfFile = null;
        ModificationLowMemController instance = new ModificationLowMemController();
        List expResult = null;
        List result = instance.getAllModificationNames(msfFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPeptidesWithModification method, of class
     * ModificationLowMemController.
     */
    @Test
    public void testGetPeptidesWithModification() {
        System.out.println("getPeptidesWithModification");
        String modification = "";
        MsfFile msfFile = null;
        ModificationLowMemController instance = new ModificationLowMemController();
        List expResult = null;
        List result = instance.getPeptidesWithModification(modification, msfFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllModifications method, of class
     * ModificationLowMemController.
     */
    @Test
    public void testGetAllModifications() {
        System.out.println("getAllModifications");
        MsfFile msfFile = null;
        ModificationLowMemController instance = new ModificationLowMemController();
        List expResult = null;
        List result = instance.getAllModifications(msfFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getListOfFixedModificationNumbers method, of class
     * ModificationLowMemController.
     */
    @Test
    public void testGetListOfFixedModificationNumbers() {
        System.out.println("getListOfFixedModificationNumbers");
        MsfFile msfFile = null;
        ModificationLowMemController instance = new ModificationLowMemController();
        List expResult = null;
        List result = instance.getListOfFixedModificationNumbers(msfFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getListOfVariableModidifcationNumbers method, of class
     * ModificationLowMemController.
     */
    @Test
    public void testGetListOfVariableModidifcationNumbers() {
        System.out.println("getListOfVariableModidifcationNumbers");
        ModificationLowMemController instance = new ModificationLowMemController();
        List expResult = null;
        List result = instance.getListOfVariableModidifcationNumbers(msfFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}