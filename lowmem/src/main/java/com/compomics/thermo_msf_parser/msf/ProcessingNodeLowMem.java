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
public class ProcessingNodeLowMem {

    public ProcessingNodeLowMem(){}

    public ArrayList<ProcessingNode> getAllProcessingNodes(Connection aConnection, MsfVersion iMsfVersion) throws SQLException {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  new ArrayList<ProcessingNode>(allNodesMap.values());

    }
    public String getQuantitationMethod(Connection aConnection) throws SQLException {
        String iQuantitationMethod = "";
        try {
            PreparedStatement stat = aConnection.prepareStatement("select ParameterValue from ProcessingNodeParameters where ParameterName = 'QuantificationMethod'");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                iQuantitationMethod = rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iQuantitationMethod;
    }
}
