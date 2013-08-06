package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.version_1_2;

import com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.PeptideLowMemController;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem;
import java.io.File;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Davy
 */
public class PeptideLowMemControllerTest {

    static MsfFile msfFile;
    ProteinLowMem proteinWithOnlyOnePeptideConfidenceLevel1;
    ProteinLowMem proteinWithOnlyPeptidesConfidenceLevel1;
    ProteinLowMem proteinWithOnlyPeptidesConfidenceLevel3;
    ProteinLowMem proteinWithPeptidesMultipleConfidenceLevels;

    public PeptideLowMemControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException, SQLException, URISyntaxException {
        msfFile = new MsfFile(new File(PeptideLowMemControllerTest.class.getClassLoader().getResource("test-msf-v-1.2.msf").getPath()));
    }

    @Before
    public void setUp() {
        proteinWithOnlyOnePeptideConfidenceLevel1 = new ProteinLowMem("P50528", 16237);
        proteinWithOnlyPeptidesConfidenceLevel1 = new ProteinLowMem("P87313", 16240);
        proteinWithOnlyPeptidesConfidenceLevel3 = new ProteinLowMem("Q09811", 16233);
        proteinWithPeptidesMultipleConfidenceLevels = new ProteinLowMem("O14108", 16234);
    }

    /**
     * Test of getPeptidesForProtein method, of class PeptideLowMemController.
     */
    @Test
    public void testGetPeptidesForProtein() {
        System.out.println("getPeptidesForProtein");
        PeptideLowMemController instance = new PeptideLowMemController();
        List<PeptideLowMem> result = instance.getPeptidesForProtein(proteinWithOnlyOnePeptideConfidenceLevel1, msfFile);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getSequence(), is("IIVLDK"));
        assertThat(result.get(0).getPeptideId(), is(9));
        result = instance.getPeptidesForProtein(proteinWithOnlyPeptidesConfidenceLevel1, msfFile);
        assertThat(result.size(), is(15));
        assertThat(result.get(0).getSequence(), is("VDRGAIK"));
        assertThat(result.get(0).getPeptideId(), is(23));
        assertThat(result.get(4).getSequence(), is("VDRGAIK"));
        assertThat(result.get(4).getPeptideId(), is(41));
        result = instance.getPeptidesForProtein(proteinWithOnlyPeptidesConfidenceLevel3, msfFile);
        assertThat(result.size(), is(2));
        assertThat(result.get(0).getSequence(), is("IILDVK"));
        assertThat(result.get(0).getPeptideId(), is(5));
        assertThat(result.get(1).getSequence(), is("IILDVK"));
        assertThat(result.get(1).getPeptideId(), is(74));
        result = instance.getPeptidesForProtein(proteinWithPeptidesMultipleConfidenceLevels, msfFile);
        assertThat(result.size(), is(3));
        assertThat(result.get(0).getSequence(), is("LLDLVK"));
        assertThat(result.get(0).getPeptideId(), is(6));
        assertThat(result.get(2).getSequence(), is("VESTNSFNAK"));
        assertThat(result.get(2).getPeptideId(), is(242));
    }

    /**
     * Test of getPeptidesForAccession method, of class PeptideLowMemController.
     */
    @Test
    public void testGetPeptidesForAccession() {
        System.out.println("getPeptidesForAccession");
        PeptideLowMemController instance = new PeptideLowMemController();
        List<PeptideLowMem> result = instance.getPeptidesForAccession(proteinWithOnlyOnePeptideConfidenceLevel1.getAccession(), msfFile);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getSequence(), is("IIVLDK"));
        assertThat(result.get(0).getPeptideId(), is(9));
        result = instance.getPeptidesForAccession(proteinWithOnlyPeptidesConfidenceLevel1.getAccession(), msfFile);
        assertThat(result.size(), is(15));
        assertThat(result.get(0).getSequence(), is("VDRGAIK"));
        assertThat(result.get(0).getPeptideId(), is(23));
        assertThat(result.get(4).getSequence(), is("VDRGAIK"));
        assertThat(result.get(4).getPeptideId(), is(41));
        result = instance.getPeptidesForAccession(proteinWithOnlyPeptidesConfidenceLevel3.getAccession(), msfFile);
        assertThat(result.size(), is(2));
        assertThat(result.get(0).getSequence(), is("IILDVK"));
        assertThat(result.get(0).getPeptideId(), is(5));
        assertThat(result.get(1).getSequence(), is("IILDVK"));
        assertThat(result.get(1).getPeptideId(), is(74));
        result = instance.getPeptidesForAccession(proteinWithPeptidesMultipleConfidenceLevels.getAccession(), msfFile);
        assertThat(result.size(), is(3));
        assertThat(result.get(0).getSequence(), is("LLDLVK"));
        assertThat(result.get(0).getPeptideId(), is(6));
        assertThat(result.get(2).getSequence(), is("VESTNSFNAK"));
        assertThat(result.get(2).getPeptideId(), is(242));

    }

    /**
     * Test of getInformationForPeptide method, of class
     * PeptideLowMemController.
     */
    @Test
    public void testGetInformationForPeptide() {
        System.out.println("getInformationForPeptide");
        int peptideID = 0;
        boolean fullInfo = false;
        PeptideLowMemController instance = new PeptideLowMemController();
        List expResult = new ArrayList();
        List result = instance.getInformationForPeptide(peptideID, msfFile, fullInfo);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPeptidesWithConfidenceLevel method, of class
     * PeptideLowMemController.
     */
    @Test
    public void testGetPeptidesWithConfidenceLevel() {
        System.out.println("getPeptidesWithConfidenceLevel");
        int confidenceLevel = 1;
        PeptideLowMemController instance = new PeptideLowMemController();
        List<PeptideLowMem> result = instance.getPeptidesWithConfidenceLevel(confidenceLevel, msfFile);
        assertThat(result.size(), is(271));
        assertThat(result.get(0).getSequence(), is("IIVLDK"));
        assertThat(result.get(55).getSequence(), is("LEINNDKK"));

        confidenceLevel = 2;
        result = instance.getPeptidesWithConfidenceLevel(confidenceLevel, msfFile);
        assertThat(result.size(), is(0));

        confidenceLevel = 3;
        result = instance.getPeptidesWithConfidenceLevel(confidenceLevel, msfFile);
        assertThat(result.size(), is(9));
        assertThat(result.get(0).getSequence(), is("IILDVK"));
        assertThat(result.get(8).getSequence(), is("SCQIQLLIDSATK"));
    }

    /**
     * Test of getNumberOfPeptidesForConfidenceLevel method, of class
     * PeptideLowMemController.
     */
    @Test
    public void testGetNumberOfPeptidesForConfidenceLevel() {
        System.out.println("getNumberOfPeptidesForConfidenceLevel");
        int confidenceLevel = 1;
        PeptideLowMemController instance = new PeptideLowMemController();
        int expResult = 271;
        int result = instance.getNumberOfPeptidesForConfidenceLevel(confidenceLevel, msfFile);
        assertEquals(expResult, result);

        expResult = 0;
        confidenceLevel = 2;
        result = instance.getNumberOfPeptidesForConfidenceLevel(confidenceLevel, msfFile);
        assertEquals(expResult, result);

        expResult = 9;
        confidenceLevel = 3;
        result = instance.getNumberOfPeptidesForConfidenceLevel(confidenceLevel, msfFile);
        assertEquals(expResult, result);

    }

    /**
     * Test of getNumberOfPeptidesProcessed method, of class
     * PeptideLowMemController.
     */
    @Test
    public void testGetNumberOfPeptidesProcessed() {
        System.out.println("getNumberOfPeptidesProcessed");
        PeptideLowMemController instance = new PeptideLowMemController();
        Integer expResult = 0;
        Integer result = instance.getNumberOfPeptidesProcessed();
        assertEquals(expResult, result);
        expResult = 9;
        assertThat(expResult, allOf(is(instance.getNumberOfPeptidesForConfidenceLevel(3, msfFile)), is(instance.getNumberOfPeptidesProcessed())));
        assertThat(instance.getNumberOfPeptidesForConfidenceLevel(1, msfFile), allOf(is(271), is(instance.getNumberOfPeptidesProcessed())));
        assertThat(expResult, is(not(instance.getNumberOfPeptidesProcessed())));
    }

    /**
     * Test of addPeptidesToProteinsInList method, of class
     * PeptideLowMemController.
     */
    @Test
    public void testGetPeptidesForProteinList_3args() {
        System.out.println("getPeptidesForProteinListAndConfidenceLevel");
        List<ProteinLowMem> proteinLowMemList = new ArrayList<ProteinLowMem>() {
            {
                this.add(proteinWithOnlyOnePeptideConfidenceLevel1);
                this.add(proteinWithOnlyPeptidesConfidenceLevel1);
                this.add(proteinWithOnlyPeptidesConfidenceLevel3);
                this.add(proteinWithPeptidesMultipleConfidenceLevels);
            }
        };
        int confidenceLevel = 0;
        PeptideLowMemController instance = new PeptideLowMemController();
        instance.getPeptidesForProteinList(proteinLowMemList, msfFile, confidenceLevel);
        assertThat(0, allOf(is(proteinLowMemList.get(0).getPeptidesForProtein().size()), is(proteinLowMemList.get(1).getPeptidesForProtein().size()), is(proteinLowMemList.get(2).getPeptidesForProtein().size()), is(proteinLowMemList.get(3).getPeptidesForProtein().size())));
        confidenceLevel = 1;
        instance.getPeptidesForProteinList(proteinLowMemList, msfFile, confidenceLevel);
        assertThat(proteinLowMemList.get(0).getPeptidesForProtein().size(), is(1));
        assertThat(proteinLowMemList.get(1).getPeptidesForProtein().size(), is(15));
        assertThat(proteinLowMemList.get(2).getPeptidesForProtein().size(), is(0));
        assertThat(proteinLowMemList.get(3).getPeptidesForProtein().size(), is(1));
        confidenceLevel = 2;
        instance.getPeptidesForProteinList(proteinLowMemList, msfFile, confidenceLevel);
        assertThat(proteinLowMemList.get(0).getPeptidesForProtein().size(), is(1));
        assertThat(proteinLowMemList.get(1).getPeptidesForProtein().size(), is(15));
        assertThat(proteinLowMemList.get(2).getPeptidesForProtein().size(), is(0));
        assertThat(proteinLowMemList.get(3).getPeptidesForProtein().size(), is(1));
        confidenceLevel = 3;
        instance.getPeptidesForProteinList(proteinLowMemList, msfFile, confidenceLevel);
        assertThat(proteinLowMemList.get(0).getPeptidesForProtein().size(), is(1));
        assertThat(proteinLowMemList.get(1).getPeptidesForProtein().size(), is(15));
        assertThat(proteinLowMemList.get(2).getPeptidesForProtein().size(), is(2));
        assertThat(proteinLowMemList.get(3).getPeptidesForProtein().size(), is(3));
    }

    /**
     * Test of addPeptidesToProteinsInList method, of class
     * PeptideLowMemController.
     */
    @Test
    public void testGetPeptidesForProteinList_List_MsfFile() {
        System.out.println("getPeptidesForProteinList");
        List<ProteinLowMem> proteinLowMemList = new ArrayList<ProteinLowMem>() {
            {
                this.add(proteinWithOnlyOnePeptideConfidenceLevel1);
            }
        };
        PeptideLowMemController instance = new PeptideLowMemController();
        instance.getPeptidesForProteinList(proteinLowMemList, msfFile,1);
        assertThat(proteinLowMemList.get(0).getPeptidesForProtein().size(), is(1));
        instance.getPeptidesForProteinList(proteinLowMemList, msfFile,3);
        assertThat(proteinLowMemList.get(0).getPeptidesForProtein().size(),is(1));
        proteinLowMemList.add(proteinWithOnlyPeptidesConfidenceLevel1);
        instance.getPeptidesForProteinList(proteinLowMemList, msfFile,1);
        assertThat(proteinLowMemList.get(0).getPeptidesForProtein().size(), is(1));
        assertThat(proteinLowMemList.get(1).getPeptidesForProtein().size(), is(15));

        proteinLowMemList.add(proteinWithOnlyPeptidesConfidenceLevel3);
        proteinLowMemList.add(proteinWithPeptidesMultipleConfidenceLevels);
        instance.getPeptidesForProteinList(proteinLowMemList, msfFile,3);
        assertThat(proteinLowMemList.get(0).getPeptidesForProtein().size(), is(1));
        assertThat(proteinLowMemList.get(1).getPeptidesForProtein().size(), is(15));
        assertThat(proteinLowMemList.get(2).getPeptidesForProtein().size(), is(2));
        assertThat(proteinLowMemList.get(3).getPeptidesForProtein().size(), is(2));
        instance.getPeptidesForProteinList(proteinLowMemList, msfFile,1);
        assertThat(proteinLowMemList.get(3).getPeptidesForProtein().size(), is(3));
        instance.getPeptidesForProteinList(proteinLowMemList, msfFile,1);
        assertThat(proteinLowMemList.get(3).getPeptidesForProtein().size(), is(3));
    }

    /**
     * Test of returnNumberOfPeptides method, of class PeptideLowMemController.
     */
    @Test
    public void testReturnNumberOfPeptides() {
        System.out.println("returnNumberOfPeptides");
        PeptideLowMemController instance = new PeptideLowMemController();
        int expResult = 280;
        int result = instance.returnNumberOfPeptides(msfFile);
        assertEquals(expResult, result);
    }
}