/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser_API.interfaces;

import com.compomics.thermo_msf_parser_API.highmeminstance.ProcessingNode;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import java.util.List;

/**
 * <p>ProcessingNodeInterface interface.</p>
 *
 * @author Davy
 * @version $Id: $Id
 */
public interface ProcessingNodeInterface {

    /**
     * <p>getAllProcessingNodes.</p>
     *
     * @param msfFile a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile} object.
     * @return a {@link java.util.List} object.
     */
    public List<ProcessingNode> getAllProcessingNodes(MsfFile msfFile);
    
     /**
      * <p>getQuantitationMethod.</p>
      *
      * @param msfFile a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile} object.
      * @return a {@link java.lang.String} object.
      */
     public String getQuantitationMethod(MsfFile msfFile); 
     
     /**
      * <p>getProcessingNodeByNumber.</p>
      *
      * @param processingNodeId a int.
      * @param msfFile a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile} object.
      * @return a {@link com.compomics.thermo_msf_parser_API.highmeminstance.ProcessingNode} object.
      */
     public ProcessingNode getProcessingNodeByNumber(int processingNodeId,MsfFile msfFile);
     
     /**
      * <p>getProcessingNodeByName.</p>
      *
      * @param processingNodeName a {@link java.lang.String} object.
      * @param msfFile a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile} object.
      * @return a {@link com.compomics.thermo_msf_parser_API.highmeminstance.ProcessingNode} object.
      */
     public ProcessingNode getProcessingNodeByName(String processingNodeName,MsfFile msfFile);
}
