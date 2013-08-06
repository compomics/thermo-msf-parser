package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.version_1_2;

import com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.ChromatogramLowMemController;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import java.io.File;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
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
    
    public ChromatogramLowMemControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException, SQLException {
        msfFile = new MsfFile(new File(ChromatogramLowMemControllerTest.class.getClassLoader().getResource("test-msf-v-1.2.msf").getPath()));
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getChromatogramFileForPeptideID method, of class ChromatogramLowMemController.
     */
    @Test
    public void testGetChromatogramFileForPeptideID() {
        System.out.println("getChromatogramFileForPeptideID");
        int peptideID = 0;
        ChromatogramLowMemController instance = new ChromatogramLowMemController();
        List result = instance.getChromatogramFileForPeptideID(peptideID, msfFile);
        assertThat(result.size(),is(0));
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUnzippedChromatogramXml method, of class ChromatogramLowMemController.
     */
    @Test
    public void testGetUnzippedChromatogramXml() throws Exception {
        System.out.println("getUnzippedChromatogramXml");
        byte[] zippedChromatogramXML = null;
        ChromatogramLowMemController instance = new ChromatogramLowMemController();
        String expResult = "";
        String result = instance.getUnzippedChromatogramXml(zippedChromatogramXML);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPoints method, of class ChromatogramLowMemController.
     */
    @Test
    public void testGetPoints() {
        System.out.println("getPoints");
        String chromatogramXML = "";
        ChromatogramLowMemController instance = new ChromatogramLowMemController();
        List result = instance.getPoints(chromatogramXML);
        assertThat(result.size(),is(0));
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getChromatogramFilesForMsfFile method, of class ChromatogramLowMemController.
     */
    @Test
    public void testGetChromatogramFilesForMsfFile() {
        System.out.println("getChromatogramFilesForMsfFile");
        ChromatogramLowMemController instance = new ChromatogramLowMemController();
        Collection expResult = null;
        Collection result = instance.getChromatogramFilesForMsfFile(msfFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}