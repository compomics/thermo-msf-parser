package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.version_1_2;

import com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.ProteinLowMemController;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem;
import com.compomics.thermo_msf_parser_API.util.UtilProtein;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Davy
 */
public class ProteinLowMemControllerTest {

    static MsfFile msfFile;

    public ProteinLowMemControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException, SQLException {
        msfFile = new MsfFile(new File(ProteinLowMemControllerTest.class.getClassLoader().getResource("test-msf-v-1.2.msf").getPath()));
    }

    /**
     * Test of getAllProteins method, of class ProteinLowMemController.
     */
    @Test
    public void testGetAllProteins() {
        System.out.println("getAllProteins");
        ProteinLowMemController instance = new ProteinLowMemController();
        List result = instance.getAllProteins(msfFile);
        assertThat(result.size(), is(20));
    }

    /**
     * Test of getNumberOfProteinsWithAPeptideAtConfidenceLevel method, of class
     * ProteinLowMemController.
     */
    @Test
    public void testGetNumberOfProteinsForConfidenceLevel() {
        System.out.println("getNumberOfProteinsForConfidenceLevel");
        int confidenceLevel = 0;
        ProteinLowMemController instance = new ProteinLowMemController();
        int expResult = 0;
        int result = instance.getNumberOfProteinsWithAPeptideAtConfidenceLevel(confidenceLevel, msfFile);
        assertEquals(expResult, result);
        confidenceLevel = 1;
        expResult = 16;
        result = instance.getNumberOfProteinsWithAPeptideAtConfidenceLevel(confidenceLevel, msfFile);
        assertEquals(expResult, result);
        confidenceLevel = 2;
        expResult = 0;
        result = instance.getNumberOfProteinsWithAPeptideAtConfidenceLevel(confidenceLevel, msfFile);
        assertEquals(expResult, result);
        confidenceLevel = 3;
        expResult = 5;
        result = instance.getNumberOfProteinsWithAPeptideAtConfidenceLevel(confidenceLevel, msfFile);
        assertEquals(expResult, result);
    }

    /**
     * Test of getProteinForProteinId method, of class ProteinLowMemController.
     */
    @Test
    public void testGetProteinForProteinId() {
        System.out.println("getProteinForProteinId");
        int aProteinId = 0;
        ProteinLowMemController instance = new ProteinLowMemController();
        ProteinLowMem expResult = null;
        ProteinLowMem result = instance.getProteinForProteinId(aProteinId,msfFile);
        assertEquals(expResult, result);
        aProteinId = 16242;
        result = instance.getProteinForProteinId(aProteinId,msfFile);
        assertThat(result.getAccession(), is("P22194"));
        assertThat(result.getSequence(), is("MSNVSSEDGIAPETQLIIDDPDVQQIDADEDLLDDVPDDVDCVELIQSRIQSMASLGLERFKNLQSLCLRQNQIKKIESVPETLTELDLYDNLIVRIENLDNVKNLTYLDLSFNNIKTIRNINHLKGLENLFFVQNRIRRIENLEGLDRLTNLELGGNKIRVIENLDTLVNLEKLWVGKNKITKFENFEKLQKLSLLSIQSNRITQFENLACLSHCLRELYVSHNGLTSFSGIEVLENLEILDVSNNMIKHLSYLAGLKNLVELWASNNELSSFQEIEDELSGLKKLETVYFEGNPLQKTNPAVYRNKVRLCLPQLRQIDATIIPKTSKQFP"));
        assertThat(result.getProteinID(), is(16242));
    }

    /**
     * Test of getProteinFromAccession method, of class ProteinLowMemController.
     */
    @Test
    public void testGetProteinFromAccession() {
        System.out.println("getProteinFromAccession");
        ProteinLowMemController instance = new ProteinLowMemController();
        ProteinLowMem expResult = new ProteinLowMem("", 0);
        ProteinLowMem result = instance.getProteinFromAccession("", msfFile);
        assertEquals(expResult, result);
        result = instance.getProteinFromAccession("P22194", msfFile);
        expResult = new ProteinLowMem("P22194", 16242);
        assertThat(expResult, is(result));
    }

    /**
     * Test of getAccessionFromProteinID method, of class
     * ProteinLowMemController.
     */
    @Test
    public void testGetAccessionFromProteinID() {
        System.out.println("getAccessionFromProteinID");
        int proteinID = 0;
        ProteinLowMemController instance = new ProteinLowMemController();
        String result = instance.getAccessionFromProteinID(proteinID, msfFile);
        assertThat(result.isEmpty(), is(true));
        proteinID = 16239;
        result = instance.getAccessionFromProteinID(proteinID, msfFile);
        assertThat(result, is("Q10061"));
    }

    /**
     * Test of getUtilProteinForProteinID method, of class
     * ProteinLowMemController.
     */
    @Test
    public void testGetUtilProteinForProteinID() {
        System.out.println("getUtilProteinForProteinID");
        int proteinID = 0;
        ProteinLowMemController instance = new ProteinLowMemController();
        UtilProtein expResult = null;
        UtilProtein result = instance.getUtilProteinForProteinID(proteinID, msfFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllProteinAccessions method, of class ProteinLowMemController.
     */
    @Test
    public void testGetAllProteinAccessions() {
        System.out.println("getAllProteinAccessions");
        ProteinLowMemController instance = new ProteinLowMemController();
        Map<String, ProteinLowMem> result = instance.getAllProteinAccessions(msfFile);
        assertThat(result.entrySet().size(), is(20));
        ProteinLowMem expResult = new ProteinLowMem("P22194", 16242);
        assertThat(result.get(expResult.getAccession()), is(expResult));
    }

    /**
     * Test of getProteinsForConfidenceLevel method, of class
     * ProteinLowMemController.
     */
    @Test
    public void testGetProteinsForConfidenceLevel_int_MsfFile() {
        System.out.println("getProteinsForConfidenceLevel");
        int confidenceLevel = 0;
        ProteinLowMemController instance = new ProteinLowMemController();
        List result = instance.getProteinsForConfidenceLevel(confidenceLevel, msfFile);
        assertThat(result.isEmpty(), is(true));
        confidenceLevel = 1;
        result = instance.getProteinsForConfidenceLevel(confidenceLevel, msfFile);
        assertThat(result.size(), is(16));
        confidenceLevel = 2;
        result = instance.getProteinsForConfidenceLevel(confidenceLevel, msfFile);
        assertThat(result.size(), is(0));
        confidenceLevel = 3;
        result = instance.getProteinsForConfidenceLevel(confidenceLevel, msfFile);
        assertThat(result.size(), is(5));


    }

    /**
     * Test of getProteinsForConfidenceLevel method, of class
     * ProteinLowMemController.
     */
    @Test
    public void testGetProteinsForConfidenceLevel_3args() {
        System.out.println("getProteinsForConfidenceLevel");
        int confidenceLevel = 0;
        boolean useCounter = true;
        ProteinLowMemController instance = new ProteinLowMemController();
        List<ProteinLowMem> result = instance.getProteinsForConfidenceLevel(confidenceLevel, msfFile, useCounter);
        assertThat(result.isEmpty(), is(true));
        assertThat(instance.getNumberOfProteinsProcessed(), is(0));
        confidenceLevel = 1;
        result = instance.getProteinsForConfidenceLevel(confidenceLevel, msfFile, useCounter);
        assertThat(result.size(), allOf(is(16), is(instance.getNumberOfProteinsProcessed())));
        confidenceLevel = 3;
        result = instance.getProteinsForConfidenceLevel(confidenceLevel, msfFile, useCounter);
        assertThat(result.size(), allOf(is(5), is(instance.getNumberOfProteinsProcessed())));

    }

    /**
     * Test of getSequenceForProteinID method, of class ProteinLowMemController.
     */
    @Test
    public void testGetSequenceForProteinID() {
        System.out.println("getSequenceForProteinID");
        int proteinID = 0;
        ProteinLowMemController instance = new ProteinLowMemController();
        String expResult = "";
        String result = instance.getSequenceForProteinID(proteinID, msfFile);
        assertEquals(expResult, result);
        expResult = "MSNVSSEDGIAPETQLIIDDPDVQQIDADEDLLDDVPDDVDCVELIQSRIQSMASLGLERFKNLQSLCLRQNQIKKIESVPETLTELDLYDNLIVRIENLDNVKNLTYLDLSFNNIKTIRNINHLKGLENLFFVQNRIRRIENLEGLDRLTNLELGGNKIRVIENLDTLVNLEKLWVGKNKITKFENFEKLQKLSLLSIQSNRITQFENLACLSHCLRELYVSHNGLTSFSGIEVLENLEILDVSNNMIKHLSYLAGLKNLVELWASNNELSSFQEIEDELSGLKKLETVYFEGNPLQKTNPAVYRNKVRLCLPQLRQIDATIIPKTSKQFP";
        result = instance.getSequenceForProteinID(16242, msfFile);
        assertThat(result, is(expResult));
    }

    /**
     * Test of getProteinsForPeptide method, of class ProteinLowMemController.
     */
    @Test
    public void testGetProteinsForPeptide() {
        System.out.println("getProteinsForPeptide");
        int peptideID = 0;
        ProteinLowMemController instance = new ProteinLowMemController();
        List result = instance.getProteinsForPeptide(peptideID, msfFile);
        assertThat(result.isEmpty(), is(true));
        peptideID = 9;
        result = instance.getProteinsForPeptide(peptideID, msfFile);
        assertThat(result.size(), is(1));
    }

    /**
     * Test of getProteinsForPeptideList method, of class
     * ProteinLowMemController.
     */
    @Test
    public void testGetProteinsForPeptideList() {
        System.out.println("getProteinsForPeptideList");
        List<PeptideLowMem> peptideList = new ArrayList<PeptideLowMem>();
        ProteinLowMemController instance = new ProteinLowMemController();
        List result = instance.getProteinsForPeptideList(peptideList, msfFile);
        assertThat(result.isEmpty(), is(true));
        fail("add an actual list of peptides to get for");
    }

    /**
     * Test of getAllProteinAccessionsForPeptide method, of class
     * ProteinLowMemController.
     */
    @Test
    public void testGetAllProteinAccessionsForPeptide() {
        System.out.println("getAllProteinAccessionsForPeptide");
        int peptideID = 0;
        ProteinLowMemController instance = new ProteinLowMemController();
        List result = instance.getAllProteinAccessionsForPeptide(peptideID, msfFile);
        assertThat(result.isEmpty(), is(true));
        peptideID = 9;
        result = instance.getAllProteinAccessionsForPeptide(peptideID, msfFile);
        assertThat(result.size(),is(1));
    }

    /**
     * Test of getNumberOfPeptidesForProtein method, of class
     * ProteinLowMemController.
     */
    @Test
    public void testGetNumberOfPeptidesForProtein() {
        System.out.println("getNumberOfPeptidesForProtein");
        int proteinID = 0;
        ProteinLowMemController instance = new ProteinLowMemController();
        Integer result = instance.getNumberOfPeptidesForProtein(proteinID, msfFile);
        assertThat(result, is(0));
        proteinID = 16242;
        result = instance.getNumberOfPeptidesForProtein(proteinID, msfFile);
        assertThat(result, is(8));
        proteinID = 16237;
        result = instance.getNumberOfPeptidesForProtein(proteinID, msfFile);
        assertThat(result, is(1));


    }

    /**
     * Test of getNumberOfProteinsProcessed method, of class
     * ProteinLowMemController.
     */
    @Test
    public void testGetNumberOfProteinsProcessed() {
        System.out.println("getNumberOfProteinsProcessed");
        ProteinLowMemController instance = new ProteinLowMemController();
        int expResult = 0;
        int result = instance.getNumberOfProteinsProcessed();
        assertEquals(expResult, result);
        expResult = 20;
        instance.getAllProteins(msfFile);
        result = instance.getNumberOfProteinsProcessed();
        assertEquals(expResult, result);
        instance.getProteinsForPeptide(9, msfFile);
        result = instance.getNumberOfProteinsProcessed();
        expResult = 1;
        assertEquals(expResult, result);
        instance.getProteinsForConfidenceLevel(3, msfFile, true);
        result = instance.getNumberOfProteinsProcessed();
        expResult = 5;
        assertEquals(expResult, result);
    }

    /**
     * Test of isMasterProtein method, of class ProteinLowMemController.
     */
    @Test
    public void testIsMasterProtein() {
        System.out.println("isMasterProtein");
        int proteinID = 16242;
        ProteinLowMemController instance = new ProteinLowMemController();
        boolean result = instance.isMasterProtein(proteinID, msfFile);
        assertThat(result, is(false));
        proteinID = 16424;
        result = instance.isMasterProtein(proteinID, msfFile);
        assertThat(result, is(false));
    }
}