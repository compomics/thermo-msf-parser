package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.highmeminstance.MsfVersionInfo;
import com.compomics.thermo_msf_parser_API.highmeminstance.WorkflowInfo;
import com.compomics.thermo_msf_parser_API.highmeminstance.WorkflowMessage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 * <p>WorkFlowLowMemController class.</p>
 *
 * @author Davy
 * @version $Id: $Id
 */
public class WorkFlowLowMemController {

    private static final Logger logger = Logger.getLogger(WorkFlowLowMemController.class);

    /**
     * get the work flow info in a proteome discoverer file
     *
     * @param msfFile the proteome discoverer file to get the workflow for
     * @return the requested {@code WorkflowInfo}
     */
    public WorkflowInfo getWorkFlowInfo(MsfFile msfFile) {
        WorkflowInfo iWorkFlowInfo = null;
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select * from WorkflowInfo");
                ResultSet rs = stat.executeQuery();
                try {
                    rs.next();
                    iWorkFlowInfo = new WorkflowInfo(rs.getString("WorkflowName"), rs.getString("WorkflowDescription"), rs.getString("User"), rs.getString("WorkflowTemplate"), rs.getString("MachineName"));
                    stat = msfFile.getConnection().prepareStatement("select * from SchemaInfo");
                    rs = stat.executeQuery();
                    rs.next();
                    iWorkFlowInfo.setMsfVersionInfo(new MsfVersionInfo(rs.getInt("Version"), rs.getString("SoftwareVersion")));
                    //add the messages to the workflow info
                    stat = msfFile.getConnection().prepareStatement("select * from WorkflowMessages");
                    rs = stat.executeQuery();
                    while (rs.next()) {
                        iWorkFlowInfo.addMessage(new WorkflowMessage(rs.getInt("MessageID"), rs.getInt("ProcessingNodeID"), rs.getInt("ProcessingNodeNumber"), rs.getLong("Time"), rs.getInt("MessageKind"), rs.getString("Message")));
                    }
                } finally {
                    if (rs != null) {
                        rs.close();
                    }
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }

            }
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return iWorkFlowInfo;
    }
}
