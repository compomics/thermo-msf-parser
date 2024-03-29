package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.enums.MsfVersion;
import com.compomics.thermo_msf_parser_API.highmeminstance.ProcessingNode;
import com.compomics.thermo_msf_parser_API.highmeminstance.ProcessingNodeParameter;
import com.compomics.thermo_msf_parser_API.interfaces.ProcessingNodeInterface;
import java.sql.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 4/26/12 Time: 9:53 AM To change
 * this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class ProcessingNodeLowMemController implements ProcessingNodeInterface {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ProcessingNodeLowMemController.class);

    /** {@inheritDoc} */
    @Override
    public List<ProcessingNode> getAllProcessingNodes(MsfFile msfFile) {
        HashMap<Integer, ProcessingNode> allNodesMap = new HashMap<Integer, ProcessingNode>();
        try {
            Statement stat = null;
            try {
                stat = msfFile.getConnection().createStatement();
                ResultSet rs = stat.executeQuery("select * from ProcessingNodes");
                try {
                    while (rs.next()) {
                        allNodesMap.put(rs.getInt("ProcessingNodeNumber"), new ProcessingNode(rs.getInt("ProcessingNodeNumber"), rs.getInt("ProcessingNodeID"), rs.getString("ProcessingNodeParentNumber"), rs.getString("NodeName"), rs.getString("FriendlyName"), rs.getInt("MajorVersion"), rs.getInt("MinorVersion"), rs.getString("NodeComment")));
                    }
                    rs.close();
                    if (msfFile.getVersion() != MsfVersion.VERSION1_2) {
                        rs = stat.executeQuery("select * from CustomDataProcessingNodes");
                        while (rs.next()) {
                            if (allNodesMap.get(rs.getInt("ProcessingNodeNumber")) != null) {
                                allNodesMap.get(rs.getInt("ProcessingNodeNumber")).addCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
                            }
                        }
                    }
                    rs = stat.executeQuery("select * from ProcessingNodeParameters");
                    while (rs.next()) {
                        ProcessingNodeParameter lNodeParameter = new ProcessingNodeParameter(rs.getInt("ProcessingNodeNumber"), rs.getInt("ProcessingNodeId"), rs.getString("ParameterName"), rs.getString("FriendlyName"), rs.getInt("IntendedPurpose"), rs.getString("PurposeDetails"), rs.getInt("Advanced"), rs.getString("Category"), rs.getInt("Position"), rs.getString("ParameterValue"), rs.getString("ValueDisplayString"));

                        if (allNodesMap.get(lNodeParameter.getProcessingNodeNumber()) != null) {
                            allNodesMap.get(lNodeParameter.getProcessingNodeNumber()).addProcessingNodeParameter(lNodeParameter);
                        }
                    }
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return new ArrayList<ProcessingNode>(allNodesMap.values());
    }

    /** {@inheritDoc} */
    @Override
    public String getQuantitationMethod(MsfFile msfFile) {
        String iQuantitationMethod = "";
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select ParameterValue from ProcessingNodeParameters where ParameterName = 'QuantificationMethod'");
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        iQuantitationMethod = rs.getString(1);
                    }
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return iQuantitationMethod;
    }

    /** {@inheritDoc} */
    @Override
    public ProcessingNode getProcessingNodeByNumber(int processingNodeId, MsfFile msfFile) {

        ProcessingNode processingNodeToReturn = null;
        try {
            Statement stat = null;
            try {
                stat = msfFile.getConnection().createStatement();
                ResultSet rs = stat.executeQuery("select * from ProcessingNodes where ProcessingNodeNumber = " + processingNodeId);
                try {
                    while (rs.next()) {
                        processingNodeToReturn = new ProcessingNode(rs.getInt("ProcessingNodeNumber"), rs.getInt("ProcessingNodeID"), rs.getString("ProcessingNodeParentNumber"), rs.getString("NodeName"), rs.getString("FriendlyName"), rs.getInt("MajorVersion"), rs.getInt("MinorVersion"), rs.getString("NodeComment"));
                    }
                } finally {
                    rs.close();
                }
                if (msfFile.getVersion() != MsfVersion.VERSION1_2) {
                    rs = stat.executeQuery("select * from CustomDataProcessingNodes where ProcessingNodeNumber = " + processingNodeId);
                    while (rs.next()) {
                        processingNodeToReturn.addCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
                    }
                }
                rs = stat.executeQuery("select * from ProcessingNodeParameters where processingNodeNumber = " + processingNodeId);
                try {
                    while (rs.next()) {
                        ProcessingNodeParameter lNodeParameter = new ProcessingNodeParameter(rs.getInt("ProcessingNodeNumber"), rs.getInt("ProcessingNodeId"), rs.getString("ParameterName"), rs.getString("FriendlyName"), rs.getInt("IntendedPurpose"), rs.getString("PurposeDetails"), rs.getInt("Advanced"), rs.getString("Category"), rs.getInt("Position"), rs.getString("ParameterValue"), rs.getString("ValueDisplayString"));
                        processingNodeToReturn.addProcessingNodeParameter(lNodeParameter);
                    }
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return processingNodeToReturn;
    }

    /** {@inheritDoc} */
    @Override
    public ProcessingNode getProcessingNodeByName(String processingNodeName, MsfFile msfFile) {
        PreparedStatement stat = null;
        ProcessingNode processingNodeToReturn = null;
        try {
            stat = msfFile.getConnection().prepareStatement("select * from ProcessingNodes where NodeName = ?");
            ResultSet rs = null;
            try {
                stat.setString(1, processingNodeName);
                rs = stat.executeQuery();
                while (rs.next()) {
                    processingNodeToReturn = new ProcessingNode(rs.getInt("ProcessingNodeNumber"), rs.getInt("ProcessingNodeID"), rs.getString("ProcessingNodeParentNumber"), rs.getString("NodeName"), rs.getString("FriendlyName"), rs.getInt("MajorVersion"), rs.getInt("MinorVersion"), rs.getString("NodeComment"));
                }
                rs.close();
                if (msfFile.getVersion() == MsfVersion.VERSION1_3) {
                    rs = stat.executeQuery("select * from CustomDataProcessingNodes where ProcessingNodeNumber = " + processingNodeToReturn.getProcessingNodeId());
                    while (rs.next()) {
                        processingNodeToReturn.addCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
                    }
                }
                rs = stat.executeQuery("select * from ProcessingNodeParameters where processingNodeNumber = " + processingNodeToReturn.getProcessingNodeId());
                while (rs.next()) {
                    ProcessingNodeParameter lNodeParameter = new ProcessingNodeParameter(rs.getInt("ProcessingNodeNumber"), rs.getInt("ProcessingNodeId"), rs.getString("ParameterName"), rs.getString("FriendlyName"), rs.getInt("IntendedPurpose"), rs.getString("PurposeDetails"), rs.getInt("Advanced"), rs.getString("Category"), rs.getInt("Position"), rs.getString("ParameterValue"), rs.getString("ValueDisplayString"));
                    processingNodeToReturn.addProcessingNodeParameter(lNodeParameter);
                }
            } catch (SQLException ex) {
                logger.error(ex);
            } catch (NullPointerException npe){
                logger.error(npe);
            } finally {
                if (rs != null) {
                    rs.close();
                }
            }
        } catch (SQLException sqle) {
            logger.error(sqle);

        } finally {
            try {
                if (stat != null) {
                    stat.close();
                }
            } catch (SQLException sqle) {
                logger.error(sqle);
            }
        }
        return processingNodeToReturn;
    }
}
