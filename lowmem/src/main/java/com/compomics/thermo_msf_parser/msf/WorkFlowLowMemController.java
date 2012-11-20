/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser.msf;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Davy
 */
public class WorkFlowLowMemController {
    
    /**
     * 
     * @param aConnection a connection to the msf file
     * @return a workflowinfo object
     */
    
    public WorkflowInfo getWorkFlowInfo(Connection aConnection){
        WorkflowInfo iWorkFlowInfo = null;
        try {
            Statement stat = aConnection.createStatement();
            ResultSet rs = stat.executeQuery("select * from WorkflowInfo");
            while (rs.next()) {
                iWorkFlowInfo = new WorkflowInfo(rs.getString("WorkflowName"), rs.getString("WorkflowDescription"), rs.getString("User"), rs.getString("WorkflowTemplate"), null);
            }
            //add the messages to the workflow info
            rs = stat.executeQuery("select * from WorkflowMessages");
            while (rs.next()) {
                iWorkFlowInfo.addMessage(new WorkflowMessage(rs.getInt("MessageID"), rs.getInt("ProcessingNodeID"), rs.getInt("ProcessingNodeNumber"), rs.getLong("Time"), rs.getInt("MessageKind"), rs.getString("Message")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(WorkFlowLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
    return iWorkFlowInfo;
    }
    
}
