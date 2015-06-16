package com.compomics.thermo_msf_parser_API.highmeminstance;

import com.compomics.thermo_msf_parser_API.enums.GUID;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 25-Feb-2011
 * Time: 11:44:50
 * To change this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
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
     * The node GUID
     */
    private String iNodeGUID;
    /**
     * The processing node parameters added to this processingnode
     */
    private List<ProcessingNodeParameter> iProcessingNodeParameters = new ArrayList<ProcessingNodeParameter>();
        /**
     * HashMap with the custom data field values. The key is the id of the custom data field
     */
    private HashMap<Integer, String> iCustomDataFieldValues = new HashMap<Integer,String>();

    /**
     * <p>Constructor for ProcessingNode.</p>
     *
     * @param iProcessingNodeNumber a int.
     * @param iProcessingNodeId a int.
     * @param iProcessingNodeParentNumber a {@link java.lang.String} object.
     * @param iNodeName a {@link java.lang.String} object.
     * @param iFriendlyName a {@link java.lang.String} object.
     * @param iMajorVersion a int.
     * @param iMinorVersion a int.
     * @param iNodeComment a {@link java.lang.String} object.
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

    /**
     * The processingnode constructor
     *
     * @param iProcessingNodeNumber  The processing node number
     * @param iProcessingNodeId The processing node id
     * @param iProcessingNodeParentNumber The processing node parent number(s)
     * @param iNodeName The node name
     * @param iFriendlyName The friendly node name
     * @param iMajorVersion The major version
     * @param iMinorVersion The minor version
     * @param iNodeComment The node comment
     * @param iNodeGuID a {@link java.lang.String} object.
     */
    public ProcessingNode(int iProcessingNodeNumber, int iProcessingNodeId, String iProcessingNodeParentNumber, String iNodeName, String iFriendlyName, int iMajorVersion, int iMinorVersion, String iNodeComment, String iNodeGuID) {
        this.iProcessingNodeNumber = iProcessingNodeNumber;
        this.iProcessingNodeId = iProcessingNodeId;
        this.iProcessingNodeParentNumber = iProcessingNodeParentNumber;
        this.iNodeName = iNodeName;
        this.iFriendlyName = iFriendlyName;
        this.iMajorVersion = iMajorVersion;
        this.iMinorVersion = iMinorVersion;
        this.iNodeComment = iNodeComment;
        this.iNodeGUID = iNodeGuID;
    }


    /**
     * <p>getProcessingNodeNumber.</p>
     *
     * @return a int.
     */
    public int getProcessingNodeNumber() {
        return iProcessingNodeNumber;
    }

    /**
     * <p>getProcessingNodeId.</p>
     *
     * @return a int.
     */
    public int getProcessingNodeId() {
        return iProcessingNodeId;
    }

    /**
     * <p>getProcessingNodeParentNumber.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getProcessingNodeParentNumber() {
        return iProcessingNodeParentNumber;
    }

    /**
     * <p>getNodeName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNodeName() {
        return iNodeName;
    }

    /**
     * <p>getFriendlyName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFriendlyName() {
        return iFriendlyName;
    }

    /**
     * <p>getMajorVersion.</p>
     *
     * @return a int.
     */
    public int getMajorVersion() {
        return iMajorVersion;
    }

    /**
     * <p>getMinorVersion.</p>
     *
     * @return a int.
     */
    public int getMinorVersion() {
        return iMinorVersion;
    }

    /**
     * <p>getNodeComment.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNodeComment() {
        return iNodeComment;
    }

    /**
     * <p>getNodeGUIDString.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNodeGUIDString() {
        return iNodeGUID;
    }
    
    /**
     * <p>getNodeGUID.</p>
     *
     * @return a {@link com.compomics.thermo_msf_parser_API.enums.GUID} object.
     */
    public GUID getNodeGUID() {
        return GUID.fromGUIDString(iNodeGUID);
    }

    /**
     * <p>getProcessingNodeParameters.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<ProcessingNodeParameter> getProcessingNodeParameters() {
        return iProcessingNodeParameters;
    }

    /**
     * This method will add a processing node parameter to this processing node
     *
     * @param lNodeParameter The processing node parameter to add
     */
    public void addProcessingNodeParameter(ProcessingNodeParameter lNodeParameter) {
        iProcessingNodeParameters.add(lNodeParameter);
    }

    /**
     * {@inheritDoc}
     *
     * To string methods gives the processing node id
     */
    @Override
    public String toString(){
        return String.valueOf(iProcessingNodeId);
    }

    /**
     * Getter for the custom data fields linked to this peptide. The key is
     * the id of the custom data field and the value is the data field value
     *
     * @return hashmap
     */
    public HashMap<Integer, String> getCustomDataFieldValues() {
        return iCustomDataFieldValues;
    }

    /**
     * This method will add a value in the custom data field map by the id off the custom data field
     *
     * @param lId The custom data field id
     * @param lValue The value to add
     */
    public void addCustomDataField(int lId, String lValue){
        iCustomDataFieldValues.put(lId, lValue);
    }
}
