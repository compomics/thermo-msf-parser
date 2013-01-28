package com.compomics.thermo_msf_parser.msf;

import java.sql.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/26/12
 * Time: 9:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessingNodeLowMemController {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(QuanResult.class);
    /**
    * @param aConnection a connection to the msf file
    * @param iMsfVersion the version of the msf file
    * @return an arrayList containing all the processing nodes
    */
    public ArrayList<ProcessingNode> getAllProcessingNodes(Connection aConnection, MsfVersion iMsfVersion){
        HashMap<Integer, ProcessingNode> allNodesMap = new HashMap<Integer, ProcessingNode>();
        try {
            Statement stat = aConnection.createStatement();
            ResultSet rs = stat.executeQuery("select * from ProcessingNodes");
            while (rs.next()) {
                allNodesMap.put(rs.getInt("ProcessingNodeNumber"),new ProcessingNode(rs.getInt("ProcessingNodeNumber"), rs.getInt("ProcessingNodeID"), rs.getString("ProcessingNodeParentNumber"), rs.getString("NodeName"), rs.getString("FriendlyName"), rs.getInt("MajorVersion"), rs.getInt("MinorVersion"), rs.getString("NodeComment")));
            }
            if (iMsfVersion== MsfVersion.VERSION1_3) {
                rs = stat.executeQuery("select * from CustomDataProcessingNodes");
                while (rs.next()){
                    if(allNodesMap.get(rs.getInt("ProcessingNodeNumber")) != null){
                        allNodesMap.get(rs.getInt("ProcessingNodeNumber")).addCustomDataField(rs.getInt("FieldID"),rs.getString("FieldValue"));
                    }
                } 
            }
            rs = stat.executeQuery("select * from ProcessingNodeParameters");
            while (rs.next()) {
                ProcessingNodeParameter lNodeParameter = new ProcessingNodeParameter(rs.getInt("ProcessingNodeNumber"), rs.getInt("ProcessingNodeId"), rs.getString("ParameterName"), rs.getString("FriendlyName"), rs.getInt("IntendedPurpose"), rs.getString("PurposeDetails"), rs.getInt("Advanced"), rs.getString("Category"), rs.getInt("Position"), rs.getString("ParameterValue"), rs.getString("ValueDisplayString"));

                if(allNodesMap.get(lNodeParameter.getProcessingNodeNumber()) != null){
                    allNodesMap.get(lNodeParameter.getProcessingNodeNumber()).addProcessingNodeParameter(lNodeParameter);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return new ArrayList<ProcessingNode>(allNodesMap.values());
    }
    
    /**
     * @param aConnection a connection to the msf file
     */
    
    public String getQuantitationMethod(Connection aConnection){
        String iQuantitationMethod = "";
        try {
            PreparedStatement stat = aConnection.prepareStatement("select ParameterValue from ProcessingNodeParameters where ParameterName = 'QuantificationMethod'");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                iQuantitationMethod = rs.getString(1);
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return iQuantitationMethod;
    }
    
    /**
     * @param aConnection a connection to the msf file
     * @param processingNodeId the id stored in the database for which we want to retrieve the processing node
     * @param iMsfVersion the version of the msf file
     * @return a processing node object
     */
    
    public ProcessingNode getProcessingNodeByNumber(Connection aConnection,int processingNodeId,MsfVersion iMsfVersion) {
        
            ProcessingNode processingNodeToReturn = null;
            Statement stat;
        try {
            stat = aConnection.createStatement();
            ResultSet rs = stat.executeQuery("select * from ProcessingNodes where ProcessingNodeNumber = "+processingNodeId);
            while(rs.next()){
               processingNodeToReturn = new ProcessingNode(rs.getInt("ProcessingNodeNumber"), rs.getInt("ProcessingNodeID"), rs.getString("ProcessingNodeParentNumber"), rs.getString("NodeName"), rs.getString("FriendlyName"), rs.getInt("MajorVersion"), rs.getInt("MinorVersion"), rs.getString("NodeComment"));
            }
            if (iMsfVersion== MsfVersion.VERSION1_3) {
                rs = stat.executeQuery("select * from CustomDataProcessingNodes where ProcessingNodeNumber = "+processingNodeId);
                while (rs.next()){
                        processingNodeToReturn.addCustomDataField(rs.getInt("FieldID"),rs.getString("FieldValue"));
                    }
                }
            rs = stat.executeQuery("select * from ProcessingNodeParameters where processingNodeNumber = "+processingNodeId);
            while(rs.next()) {
                ProcessingNodeParameter lNodeParameter = new ProcessingNodeParameter(rs.getInt("ProcessingNodeNumber"), rs.getInt("ProcessingNodeId"), rs.getString("ParameterName"), rs.getString("FriendlyName"), rs.getInt("IntendedPurpose"), rs.getString("PurposeDetails"), rs.getInt("Advanced"), rs.getString("Category"), rs.getInt("Position"), rs.getString("ParameterValue"), rs.getString("ValueDisplayString"));
                processingNodeToReturn.addProcessingNodeParameter(lNodeParameter);
            }    
        } catch (SQLException ex) {
            logger.error(ex);
        }    
        return processingNodeToReturn;
    
    }
}
