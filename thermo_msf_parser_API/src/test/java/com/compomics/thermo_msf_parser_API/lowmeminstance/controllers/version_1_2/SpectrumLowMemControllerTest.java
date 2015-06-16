package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.version_1_2;

import com.compomics.thermo_msf_parser_API.highmeminstance.Peak;
import com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.SpectrumLowMemController;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.SpectrumLowMem;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Davy
 */
public class SpectrumLowMemControllerTest {

    static MsfFile msfFile;
    private static String unzippedSpectrumXml;
    private static SpectrumLowMem lSpectrum;
    private static byte[] zippedSpectrumXML = new byte[7342];

    public SpectrumLowMemControllerTest() throws ClassNotFoundException, SQLException {
    }

    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
        msfFile = new MsfFile(new File(SpectrumLowMemControllerTest.class.getClassLoader().getResource("test-msf-v-1.2.msf").getPath()));
        FileInputStream fin = new FileInputStream(new File(SpectrumLowMemControllerTest.class.getClassLoader().getResource("first-spectrum-test-1.2.xml").getPath()));
        BufferedReader myInput = new BufferedReader(new InputStreamReader(fin));
        StringBuilder sb = new StringBuilder();
        String thisLine;
        while ((thisLine = myInput.readLine()) != null) {
            sb.append(thisLine).append("\r\n");
        }
        sb.replace(sb.length() - 2, sb.length(), "");
        unzippedSpectrumXml = sb.toString();
        RandomAccessFile f = new RandomAccessFile(new File(SpectrumLowMemControllerTest.class.getClassLoader().getResource("first-spectrum-test-1.2.zip").getPath()), "r");
        f.readFully(zippedSpectrumXML);
        lSpectrum = new SpectrumLowMem(1,499,1,5,5,5,3,0.04635833333,2300.15530757984,2); 
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
        fail("add peptide to get spectrum xml for");
        PeptideLowMem peptide = null;
        String result = instance.createSpectrumXMLForPeptide(peptide, msfFile);
        assertEquals(unzippedSpectrumXml, result);
    }

    /**
     * Test of createSpectrumXMLForSpectrum method, of class
     * SpectrumLowMemController.
     */
    @Test
    public void testCreateSpectrumXMLForSpectrum() throws Exception {
        System.out.println("createSpectrumXMLForSpectrum");
        SpectrumLowMemController instance = new SpectrumLowMemController();
        instance.createSpectrumXMLForSpectrum(lSpectrum, msfFile);
        FileUtils.writeStringToFile(new File("/spectrumoutput"), lSpectrum.getSpectrumXML());
        //TODO test this file
    }

    /**
     * Test of getMSMSPeaks method, of class SpectrumLowMemController.
     */
    @Test
    public void testGetMSMSPeaks() {
        System.out.println("getMSMSPeaks");
        SpectrumLowMemController instance = new SpectrumLowMemController();
        List result = instance.getMSMSPeaks(unzippedSpectrumXml);
        assertThat(result.size(),is(not(0)));
    }

    /**
     * Test of getMSPeaks method, of class SpectrumLowMemController.
     */
    @Test
    public void testGetMSPeaks() {
        System.out.println("getMSPeaks");
        SpectrumLowMemController instance = new SpectrumLowMemController();
        List result = instance.getMSPeaks(unzippedSpectrumXml);
        assertThat(result.size(),is(not(0)));
    }

    /**
     * Test of getFragmentedMsPeak method, of class SpectrumLowMemController.
     */
    @Test
    public void testGetFragmentedMsPeak() {
        System.out.println("getFragmentedMsPeak");
        SpectrumLowMemController instance = new SpectrumLowMemController();
        Peak expResult = new Peak(767.38995,925.5309,3,23005,7.1);
        Peak result = instance.getFragmentedMsPeak(unzippedSpectrumXml);
        assertThat(result.getX(),is(expResult.getX()));
        assertThat(result.getY(),is(expResult.getY()));
        assertThat(result.getR(),is(expResult.getR()));
        assertThat(result.getSN(),is(expResult.getSN()));
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
        fail("add a peptide to check against");
        SpectrumLowMemController instance = new SpectrumLowMemController();
        SpectrumLowMem expResult = null;
        SpectrumLowMem result = instance.getSpectrumForPeptide(peptideOfInterest, msfFile);
        assertEquals(expResult, result);
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
        String rawFileName = "test.afterdot";
        SpectrumLowMemController instance = new SpectrumLowMemController();
        String expResult = "test_1_5_3";
        String result = instance.getSpectrumTitle(rawFileName, lSpectrum);
        assertEquals(expResult, result);
    }

    /**
     * Test of unzipXMLforSpectrum method, of class SpectrumLowMemController.
     */
    @Test
    public void testUnzipXMLforSpectrum() {
        System.out.println("unzipXMLforSpectrum");
        SpectrumLowMem spectrum = lSpectrum;
        SpectrumLowMemController instance = new SpectrumLowMemController();
        spectrum.setZippedSpectrumXML(zippedSpectrumXML);
        instance.unzipXMLforSpectrum(spectrum);
        assertThat(spectrum.getSpectrumXML().compareTo(unzippedSpectrumXml),is(0));
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