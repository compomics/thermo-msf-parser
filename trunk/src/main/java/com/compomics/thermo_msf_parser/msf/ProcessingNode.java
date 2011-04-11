package com.compomics.thermo_msf_parser.msf;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 25-Feb-2011
 * Time: 11:44:50
 * To change this template use File | Settings | File Templates.
 */
public class ProcessingNode {


    /**
     * The processing node number
     */
    private int iProcessingNodeNumber;
    /**
     * The processing node id
     */
    private int iProcessingNodeId;
    /**
     * The processing node parent number
     */
    private String iProcessingNodeParentNumber;
    /**
     * The node name
     */
    private String iNodeName;
    /**
     * The friendly name
     */
    private String iFriendlyName;
    /**
     * The major version
     */
    private int iMajorVersion;
    /**
     * The minor version
     */
    private int iMinorVersion;
    /**
     * The node comment
     */
    private String iNodeComment;
    /**
     * The processing node parameters added to this processingnode
     */
    private Vector<ProcessingNodeParameter> iProcessingNodeParameters = new Vector<ProcessingNodeParameter>();
        /**
     * HashMap with the custom data field values. The key is the id of the custom data field
     */
    private HashMap<Integer, String> iCustomDataFieldValues = new HashMap<Integer,String>();

    /**
     * The processingnode constructor
     * @param iProcessingNodeNumber  The processing node number
     * @param iProcessingNodeId The processing node id
     * @param iProcessingNodeParentNumber The processing node parent number(s)
     * @param iNodeName The node name
     * @param iFriendlyName The friendly node name
     * @param iMajorVersion The major version
     * @param iMinorVersion The minor version
     * @param iNodeComment The node comment
     */
    public ProcessingNode(int iProcessingNodeNumber, int iProcessingNodeId, String iProcessingNodeParentNumber, String iNodeName, String iFriendlyName, int iMajorVersion, int iMinorVersion, String iNodeComment) {
        this.iProcessingNodeNumber = iProcessingNodeNumber;
        this.iProcessingNodeId = iProcessingNodeId;
        this.iProcessingNodeParentNumber = iProcessingNodeParentNumber;
        this.iNodeName = iNodeName;
        this.iFriendlyName = iFriendlyName;
        this.iMajorVersion = iMajorVersion;
        this.iMinorVersion = iMinorVersion;
        this.iNodeComment = iNodeComment;
    }


    public int getProcessingNodeNumber() {
        return iProcessingNodeNumber;
    }

    public int getProcessingNodeId() {
        return iProcessingNodeId;
    }

    public String getProcessingNodeParentNumber() {
        return iProcessingNodeParentNumber;
    }

    public String getNodeName() {
        return iNodeName;
    }

    public String getFriendlyName() {
        return iFriendlyName;
    }

    public int getMajorVersion() {
        return iMajorVersion;
    }

    public int getMinorVersion() {
        return iMinorVersion;
    }

    public String getNodeComment() {
        return iNodeComment;
    }

    public Vector<ProcessingNodeParameter> getProcessingNodeParameters() {
        return iProcessingNodeParameters;
    }

    /**
     * This method will add a processing node parameter to this processing node
     * @param lNodeParameter The processing node parameter to addd
     */
    public void addProcessingNodeParameter(ProcessingNodeParameter lNodeParameter) {
        iProcessingNodeParameters.add(lNodeParameter);
    }

    /**
     * To string methods gives the processing node id
     * @return The processing node id as a string
     */
    public String toString(){
        return String.valueOf(iProcessingNodeId);
    }

    /**
     * Getter for the custom data fields linked to this peptide. The key is
     * the id of the custom data field and the value is the data field value
     * @return hashmap
     */
    public HashMap<Integer, String> getCustomDataFieldValues() {
        return iCustomDataFieldValues;
    }

    /**
     * This method will add a value in the custom data field map by the id off the custom data field
     * @param lId The custom data field id
     * @param lValue The value to add
     */
    public void addCustomDataField(int lId, String lValue){
        iCustomDataFieldValues.put(lId, lValue);
    }
}
