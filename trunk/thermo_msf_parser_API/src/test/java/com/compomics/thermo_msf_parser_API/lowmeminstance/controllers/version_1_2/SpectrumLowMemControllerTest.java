package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.version_1_2;

import com.compomics.thermo_msf_parser_API.highmeminstance.Peak;
import com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.SpectrumLowMemController;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.SpectrumLowMem;
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
public class SpectrumLowMemControllerTest {

    static MsfFile msfFile;

    public SpectrumLowMemControllerTest() throws ClassNotFoundException, SQLException {
    }

    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException, SQLException {
        msfFile = new MsfFile(new File(SpectrumLowMemControllerTest.class.getClassLoader().getResource("test-msf-v-1.2.msf").getPath()));

    }

    /**
     * Test of createSpectrumXMLForPeptide method, of class
     * SpectrumLowMemController.
     */
    @Test
    public void testCreateSpectrumXMLForPeptide() throws Exception {
        System.out.println("createSpectrumXMLForPeptide");
        SpectrumLowMemController instance = new SpectrumLowMemController();
        String expResult = "";
        PeptideLowMem peptide = null;
        String result = instance.createSpectrumXMLForPeptide(peptide, msfFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createSpectrumXMLForSpectrum method, of class
     * SpectrumLowMemController.
     */
    @Test
    public void testCreateSpectrumXMLForSpectrum() throws Exception {
        System.out.println("createSpectrumXMLForSpectrum");
        SpectrumLowMem lSpectrum = null;
        SpectrumLowMemController instance = new SpectrumLowMemController();
        instance.createSpectrumXMLForSpectrum(lSpectrum, msfFile);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMSMSPeaks method, of class SpectrumLowMemController.
     */
    @Test
    public void testGetMSMSPeaks() {
        System.out.println("getMSMSPeaks");
        String lXml = "";
        SpectrumLowMemController instance = new SpectrumLowMemController();
        List expResult = null;
        List result = instance.getMSMSPeaks(lXml);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMSPeaks method, of class SpectrumLowMemController.
     */
    @Test
    public void testGetMSPeaks() {
        System.out.println("getMSPeaks");
        String lXml = "";
        SpectrumLowMemController instance = new SpectrumLowMemController();
        List expResult = null;
        List result = instance.getMSPeaks(lXml);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFragmentedMsPeak method, of class SpectrumLowMemController.
     */
    @Test
    public void testGetFragmentedMsPeak() {
        System.out.println("getFragmentedMsPeak");
        String lXml = "";
        SpectrumLowMemController instance = new SpectrumLowMemController();
        Peak expResult = null;
        Peak result = instance.getFragmentedMsPeak(lXml);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllSpectra method, of class SpectrumLowMemController.
     */
    @Test
    public void testGetAllSpectra() {
        System.out.println("getAllSpectra");
        SpectrumLowMemController instance = new SpectrumLowMemController();
        List result = instance.getAllSpectra(msfFile);
        assertThat(result.size(), allOf(is(2238), is(instance.getAllSpectraIds(msfFile).size())));

    }

    /**
     * Test of getAllSpectraIds method, of class SpectrumLowMemController.
     */
    @Test
    public void testGetAllSpectraIds() {
        System.out.println("getAllSpectraIds");
        SpectrumLowMemController instance = new SpectrumLowMemController();
        List result = instance.getAllSpectraIds(msfFile);
        assertThat(result.size(), allOf(is(2238), is(instance.getAllSpectra(msfFile).size())));
    }

    /**
     * Test of getSpectrumForPeptide method, of class SpectrumLowMemController.
     */
    @Test
    public void testGetSpectrumForPeptideID_PeptideLowMem_MsfFile() {
        System.out.println("getSpectrumForPeptideID");
        PeptideLowMem peptideOfInterest = null;
        SpectrumLowMemController instance = new SpectrumLowMemController();
        SpectrumLowMem expResult = null;
        SpectrumLowMem result = instance.getSpectrumForPeptide(peptideOfInterest, msfFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSpectrumForPeptide method, of class SpectrumLowMemController.
     */
    @Test
    public void testGetSpectrumForPeptideID_int_MsfFile() {
        System.out.println("getSpectrumForPeptideID");
        int peptideOfInterest = 0;
        SpectrumLowMemController instance = new SpectrumLowMemController();
        SpectrumLowMem result = instance.getSpectrumForPeptideID(peptideOfInterest, msfFile);
        assertThat(result, (is(nullValue())));
        peptideOfInterest = 7;
        result = instance.getSpectrumForPeptideID(peptideOfInterest, msfFile);
        assertThat(result.getFileId(), is(902));
        assertThat(result.getSpectrumId(), is(1420));
        peptideOfInterest = 15;
        result = instance.getSpectrumForPeptideID(peptideOfInterest, msfFile);
        assertThat(result.getFileId(), is(902));
        assertThat(result.getSpectrumId(), is(643));
    }

    /**
     * Test of getSpectrumForSpectrumID method, of class
     * SpectrumLowMemController.
     */
    @Test
    public void testGetSpectrumForSpectrumID() {
        System.out.println("getSpectrumForSpectrumID");
        int spectrumOfInterestID = 0;
        SpectrumLowMemController instance = new SpectrumLowMemController();
        SpectrumLowMem result = instance.getSpectrumForSpectrumID(spectrumOfInterestID, msfFile);
        assertThat(result, is(nullValue()));
        spectrumOfInterestID = 463;
        result = instance.getSpectrumForSpectrumID(spectrumOfInterestID, msfFile);
        assertThat(result.getScan(), is(1778));
        assertThat(result.getCharge(), is(2));
    }

    /**
     * Test of getSpectrumTitle method, of class SpectrumLowMemController.
     */
    @Test
    public void testGetSpectrumTitle() {
        System.out.println("getSpectrumTitle");
        String rawFileName = "";
        SpectrumLowMem lspectrum = null;
        SpectrumLowMemController instance = new SpectrumLowMemController();
        String expResult = "";
        String result = instance.getSpectrumTitle(rawFileName, lspectrum);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of unzipXMLforSpectrum method, of class SpectrumLowMemController.
     */
    @Test
    public void testUnzipXMLforSpectrum() {
        System.out.println("unzipXMLforSpectrum");
        SpectrumLowMem spectrum = null;
        SpectrumLowMemController instance = new SpectrumLowMemController();
        instance.unzipXMLforSpectrum(spectrum);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNumberOfSpectra method, of class SpectrumLowMemController.
     */
    @Test
    public void testGetNumberOfSpectra() {
        System.out.println("getNumberOfSpectra");
        SpectrumLowMemController instance = new SpectrumLowMemController();
        int expResult = 2238;
        int result = instance.getNumberOfSpectra(msfFile);
        assertEquals(expResult, result);
    }

    /**
     * Test of getSpectraMap method, of class SpectrumLowMemController.
     */
    @Test
    public void testGetSpectraMap() {
        System.out.println("getSpectraMap");
        SpectrumLowMemController instance = new SpectrumLowMemController();
        HashMap<Integer, SpectrumLowMem> result = instance.getSpectraMap(msfFile);
        assertThat(result.entrySet().size(), is(2238));
        assertThat(result.get(634).getScan(), is(1966));
    }
}