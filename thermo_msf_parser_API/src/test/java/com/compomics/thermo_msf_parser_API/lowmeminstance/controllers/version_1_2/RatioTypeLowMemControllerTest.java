package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.version_1_2;
import com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.RatioTypeLowMemController;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import java.io.File;
import java.sql.SQLException;
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
public class RatioTypeLowMemControllerTest {
    private static MsfFile msfFile;
    
    public RatioTypeLowMemControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException, SQLException {
                msfFile = new MsfFile(new File(RatioTypeLowMemControllerTest.class.getClassLoader().getResource("test-msf-v-1.2.msf").getPath()));
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
     * Test of parseRatioTypes method, of class RatioTypeLowMemController.
     */
    @Test
    public void testParseRatioTypes() {
        System.out.println("parseRatioTypes");
        RatioTypeLowMemController instance = new RatioTypeLowMemController();
        List result = instance.parseRatioTypes(msfFile);
        assertThat(result.isEmpty(),is(true));
    }
}