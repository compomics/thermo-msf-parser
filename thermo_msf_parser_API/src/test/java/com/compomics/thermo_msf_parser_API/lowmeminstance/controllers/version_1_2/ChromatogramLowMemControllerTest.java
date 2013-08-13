package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.version_1_2;

import com.compomics.thermo_msf_parser_API.highmeminstance.Chromatogram;
import com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.ChromatogramLowMemController;
import com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.ChromatogramLowMemController.Point;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Davy
 */
public class ChromatogramLowMemControllerTest {

    private static MsfFile msfFile;
    private static byte[] zippedChromatogramXML = new byte[38466];
    private static String unzippedChromatogramXml;

    public ChromatogramLowMemControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
        msfFile = new MsfFile(new File(ChromatogramLowMemControllerTest.class.getClassLoader().getResource("test-msf-v-1.2.msf").getPath()));
        RandomAccessFile f = new RandomAccessFile(new File(ChromatogramLowMemControllerTest.class.getClassLoader().getResource("zippedTestChromatorgramByteArray").getPath()), "r");
        f.readFully(zippedChromatogramXML);
        FileInputStream fin = new FileInputStream(new File(ChromatogramLowMemControllerTest.class.getClassLoader().getResource("chromatogram_902_TicTrace.xml").getPath()));
        BufferedReader myInput = new BufferedReader(new InputStreamReader(fin));
        StringBuilder sb = new StringBuilder();
        String thisLine;
        while ((thisLine = myInput.readLine()) != null) {
            sb.append(thisLine).append("\r\n");
        }
        sb.replace(sb.length()-2, sb.length(), "");
        unzippedChromatogramXml = sb.toString();
    }

    /**
     * Test of getChromatogramFileForPeptideID method, of class
     * ChromatogramLowMemController.
     */
    @Test
    public void testGetChromatogramFileForPeptideID() throws FileNotFoundException, IOException {
        System.out.println("getChromatogramFileForPeptideID");
        int peptideID = 0;
        ChromatogramLowMemController instance = new ChromatogramLowMemController();
        List<Chromatogram> result = instance.getChromatogramFileForPeptideID(peptideID, msfFile);
        assertThat(result.size(), is(0));
        peptideID = 63;
        result = instance.getChromatogramFileForPeptideID(peptideID, msfFile);
        assertThat(result.size(), is(2));
        assertThat(result.get(0).getZippedChromatogramXml().length, is(38466));
    }

    /**
     * Test of getUnzippedChromatogramXml method, of class
     * ChromatogramLowMemController.
     */
    @Test
    public void testGetUnzippedChromatogramXml() throws Exception {
        System.out.println("getUnzippedChromatogramXml");
        ChromatogramLowMemController instance = new ChromatogramLowMemController();
        String result = instance.getUnzippedChromatogramXml(zippedChromatogramXML);
        assertThat(unzippedChromatogramXml.compareTo(result), is(0));
    }

    /**
     * Test of getPoints method, of class ChromatogramLowMemController.
     */
    @Test
    public void testGetPoints() {
        System.out.println("getPoints");
        ChromatogramLowMemController instance = new ChromatogramLowMemController();
        List<Point> result = instance.getPoints(unzippedChromatogramXml);
        assertThat(result.size(), allOf(is(3864),is(result.get(3863).getScan())));
        assertThat(result.get(488).getScan(),is(489));
        assertThat(result.get(488).getT(),is(6.35));
        assertThat(result.get(1048).getY(),is(1932012.0));
    }

    /**
     * Test of getChromatogramFilesForMsfFile method, of class
     * ChromatogramLowMemController.
     */
    @Test
    public void testGetChromatogramFilesForMsfFile() {
        System.out.println("getChromatogramFilesForMsfFile");
        ChromatogramLowMemController instance = new ChromatogramLowMemController();
        ArrayList<Chromatogram> result = (ArrayList<Chromatogram>) instance.getChromatogramFilesForMsfFile(msfFile);
        assertThat(result.size(), is(2));
        assertThat(result.get(1).getZippedChromatogramXml().length, is(39031));
    }
}