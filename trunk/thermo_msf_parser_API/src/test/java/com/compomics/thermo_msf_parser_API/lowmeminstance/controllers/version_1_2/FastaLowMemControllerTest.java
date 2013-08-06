package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.version_1_2;

import com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.FastaLowMemController;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import java.io.File;
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
public class FastaLowMemControllerTest {

    private static MsfFile msfFile;

    public FastaLowMemControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException, SQLException {
        msfFile = new MsfFile(new File(FastaLowMemControllerTest.class.getClassLoader().getResource("test-msf-v-1.2.msf").getPath()));
    }

    /**
     * Test of getVirtualFastaFileNames method, of class FastaLowMemController.
     */
    @Test
    public void testGetVirtualFastaFileNames() {
        System.out.println("getFastaFileNames");
        FastaLowMemController instance = new FastaLowMemController();
        List<String> expResult = new ArrayList<String>() {
            {
                this.add("Mascot5_uniprot_Spombe_All entries");
            }
        };
        List<String> result = instance.getVirtualFastaFileNames(msfFile);
        assertEquals(expResult, result);
    }

    /**
     * Test of getNumberOfProteinsInFastaFile method, of class
     * FastaLowMemController.
     */
    @Test
    public void testGetNumberOfProteinsInFastaFile() {
        System.out.println("getNumberOfProteinsInFastaFile");
        String fastaFileName = "";
        FastaLowMemController instance = new FastaLowMemController();
        int expResult = 0;
        int result = instance.getNumberOfProteinsInFastaFile(msfFile, fastaFileName);
        assertEquals(expResult, result);
        fastaFileName = "Partial import of (Mascot5_uniprot_Spombe_All entries)";
        result = instance.getNumberOfProteinsInFastaFile(msfFile, fastaFileName);
        expResult = 68;
        assertThat(expResult,is(result));
    }

    /**
     * Test of getNumberOfAminoAcidsInFastaFile method, of class
     * FastaLowMemController.
     */
    @Test
    public void testGetNumberOfAminoAcidsInFastaFile() {
        System.out.println("getNumberOfAminoAcidsInFastaFile");
        String fastaFileName = "";
        FastaLowMemController instance = new FastaLowMemController();
        int expResult = 0;
        int result = instance.getNumberOfAminoAcidsInFastaFile(msfFile, fastaFileName);
        assertEquals(expResult, result);
        fastaFileName = "Partial import of (Mascot5_uniprot_Spombe_All entries)";
        expResult  = 144159;
        result = instance.getNumberOfAminoAcidsInFastaFile(msfFile, fastaFileName);
        assertThat(result,is(expResult));
        
    }
    
    @Test
    public void testGetFastaFileNames(){
    System.out.println("getFastaFileNames");
        FastaLowMemController instance = new FastaLowMemController();
        List<String> expResult = new ArrayList<String>() {
            {
                this.add("Partial import of (Mascot5_uniprot_Spombe_All entries)");
            }
        };
        List<String> result = instance.getFastaFileNames(msfFile);
        assertEquals(expResult, result);
    }
    
}