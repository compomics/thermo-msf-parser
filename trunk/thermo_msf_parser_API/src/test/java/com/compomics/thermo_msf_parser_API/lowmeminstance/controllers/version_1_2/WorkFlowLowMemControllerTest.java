package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.version_1_2;

import com.compomics.thermo_msf_parser_API.highmeminstance.WorkflowInfo;
import com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.WorkFlowLowMemController;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import java.io.File;
import java.sql.SQLException;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Davy
 */
public class WorkFlowLowMemControllerTest {
    
    static MsfFile msfFile;
    
    public WorkFlowLowMemControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException, SQLException {
        msfFile = new MsfFile(new File(WorkFlowLowMemControllerTest.class.getClassLoader().getResource("test-msf-v-1.2.msf").getPath()));

    }

    /**
     * Test of getWorkFlowInfo method, of class WorkFlowLowMemController.
     */
    @Test
    public void testGetWorkFlowInfo() {
        System.out.println("getWorkFlowInfo");
        WorkFlowLowMemController instance = new WorkFlowLowMemController();
        WorkflowInfo result = instance.getWorkFlowInfo(msfFile);
        assertThat(result,is(not(nullValue())));
        assertThat(result.getWorkflowDescription(),is(""));
        assertThat(result.getWorkflowMachineName(),is("Thermo.Magellan.Server"));
        assertThat(result.getWorkflowMessages().size(),is(66));
        assertThat(result.getMsfVersionInfo(),is(not(nullValue())));
    }
}