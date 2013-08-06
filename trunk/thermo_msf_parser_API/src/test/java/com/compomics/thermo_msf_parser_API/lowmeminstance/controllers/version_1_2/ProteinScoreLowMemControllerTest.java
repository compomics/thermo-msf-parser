package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.version_1_2;

import com.compomics.thermo_msf_parser_API.highmeminstance.ProteinScore;
import com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.ProteinScoreLowMemController;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Davy
 */
public class ProteinScoreLowMemControllerTest {

    static MsfFile msfFile;
    
    public ProteinScoreLowMemControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException, SQLException {
        msfFile = new MsfFile(new File(ProteinScoreLowMemControllerTest.class.getClassLoader().getResource("test-msf-v-1.2.msf").getPath()));
    
    }

    /**
     * Test of getScoresForProteinId method, of class ProteinScoreLowMemController.
     */
    @Test
    public void testGetScoresForProteinId() {
        System.out.println("getScoresForProteinId");
        int proteinID = 0;
        ProteinScoreLowMemController instance = new ProteinScoreLowMemController();
        List<ProteinScore> result = instance.getScoresForProteinId(proteinID, msfFile);
        assertThat(result.isEmpty(),is(true));
        proteinID = 16242;
        result = instance.getScoresForProteinId(proteinID, msfFile);
        assertThat(result.size(),is(1));
        assertThat(result.get(0).getScore(),is(36.70483302561849));
        assertThat(result.get(0).getCoverage(),is(1.0));
        proteinID = 16233;
        result = instance.getScoresForProteinId(proteinID, msfFile);
        assertThat(result.size(),is(2));
        assertThat(result.get(0).getProcessingNodeNumber(),allOf(is(not(result.get(1).getProcessingNodeNumber())),is(3)));
    }
}