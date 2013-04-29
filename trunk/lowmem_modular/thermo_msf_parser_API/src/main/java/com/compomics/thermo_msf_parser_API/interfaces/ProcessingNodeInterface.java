/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser_API.interfaces;

import com.compomics.thermo_msf_parser_API.highmeminstance.ProcessingNode;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import java.util.List;

/**
 *
 * @author Davy
 */
public interface ProcessingNodeInterface {

    public List<ProcessingNode> getAllProcessingNodes(MsfFile msfFile);
    
     public String getQuantitationMethod(MsfFile msfFile); 
     
     public ProcessingNode getProcessingNodeByNumber(int processingNodeId,MsfFile msfFile);
     
     public ProcessingNode getProcessingNodeByName(String processingNodeName,MsfFile msfFile);
}
