package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.highmeminstance.MsfVersionInfo;
import com.compomics.thermo_msf_parser_API.highmeminstance.WorkflowInfo;
import com.compomics.thermo_msf_parser_API.highmeminstance.WorkflowMessage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;

/**
 *
 * @author Davy
 */
public class WorkFlowLowMemController {

    private static final Logger logger = Logger.getLogger(WorkFlowLowMemController.class);

   /**
    * get the work flow info in a proteome discoverer file
    * @param msfFile the proteome discoverer file to get the workflow for
    * @return the requested workflow info
    */
    public WorkflowInfo getWorkFlowInfo(MsfFile msfFile) {
        WorkflowInfo iWorkFlowInfo = null;
        try {
            Statement stat = msfFile.getConnection().createStatement();
            ResultSet rs = stat.executeQuery("select * from WorkflowInfo");
            try {
                rs.next();
                iWorkFlowInfo = new WorkflowInfo(rs.getString("WorkflowName"), rs.getString("WorkflowDescription"), rs.getString("User"), rs.getString("WorkflowTemplate"), rs.getString("MachineName"));
            } finally {
                rs.close();
            }
            rs = stat.executeQuery("select * from SchemaInfo");
            try {
                rs.next();
                iWorkFlowInfo.setMsfVersionInfo(new MsfVersionInfo(rs.getInt("Version"), rs.getString("SoftwareVersion")));
                //add the messages to the workflow info
            } finally {
                rs.close();
            }
            rs = stat.executeQuery("select * from WorkflowMessages");
            try {
                while (rs.next()) {
                    iWorkFlowInfo.addMessage(new WorkflowMessage(rs.getInt("MessageID"), rs.getInt("ProcessingNodeID"), rs.getInt("ProcessingNodeNumber"), rs.getLong("Time"), rs.getInt("MessageKind"), rs.getString("Message")));
                }
            } finally {
                rs.close();
            }
            stat.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return iWorkFlowInfo;
    }
}
