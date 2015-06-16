package com.compomics.thermo_msf_parser_API.highmeminstance;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 25-Feb-2011
 * Time: 11:48:50
 * To change this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class ProcessingNodeParameter {
    /**
     * The processing node number
     */
    private int iProcessingNodeNumber;
    /**
     * The processing node id
     */
    private int iProcessingNodeId;
    /**
     * The parameter name
     */
    private String iParameterName;
    /**
     * The friendly parameter name
     */
    private String iFriendlyName;
    /**
     * The intended purpose
     */
    private int iIntendedPurpose;
    /**
     * The purpose details
     */
    private String iPurposeDetails;
    /**
     * Advanced
     */
    private int iAdvanced;
    /**
     * The category
     */
    private String iCategory;
    /**
     * The position
     */
    private int iPosition;
    /**
     * The parameter value
     */
    private String iParameterValue;
    /**
     * The displayed value
     */
    private String iValueDisplayString;

    /**
     * The processing node parameter
     *
     * @param iProcessingNodeNumber The processing node number
     * @param iProcessingNodeId     The processing node id
     * @param iParameterName        The parameter name
     * @param iFriendlyName         The friendly name
     * @param iIntendedPurpose      The intended purpose
     * @param iPurposeDetails       The purpose details
     * @param iAdvanced             An int that indicates the advanced status
     * @param iCategory             The category
     * @param iPosition             The position
     * @param iParameterValue       The parameter value
     * @param iValueDisplayString   The displayed parameter value
     */
    public ProcessingNodeParameter(int iProcessingNodeNumber, int iProcessingNodeId, String iParameterName,
                                   String iFriendlyName, int iIntendedPurpose, String iPurposeDetails,
                                   int iAdvanced, String iCategory, int iPosition, String iParameterValue, String iValueDisplayString) {
        this.iProcessingNodeNumber = iProcessingNodeNumber;
        this.iProcessingNodeId = iProcessingNodeId;
        this.iParameterName = iParameterName;
        this.iFriendlyName = iFriendlyName;
        this.iIntendedPurpose = iIntendedPurpose;
        this.iPurposeDetails = iPurposeDetails;
        this.iAdvanced = iAdvanced;
        this.iCategory = iCategory;
        this.iPosition = iPosition;
        this.iParameterValue = iParameterValue;
        this.iValueDisplayString = iValueDisplayString;
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
     * <p>getParameterName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getParameterName() {
        return iParameterName;
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
     * <p>getIntendedPurpose.</p>
     *
     * @return a int.
     */
    public int getIntendedPurpose() {
        return iIntendedPurpose;
    }

    /**
     * <p>getPurposeDetails.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPurposeDetails() {
        return iPurposeDetails;
    }

    /**
     * <p>getAdvanced.</p>
     *
     * @return a int.
     */
    public int getAdvanced() {
        return iAdvanced;
    }

    /**
     * <p>getCategory.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCategory() {
        return iCategory;
    }

    /**
     * <p>getPosition.</p>
     *
     * @return a int.
     */
    public int getPosition() {
        return iPosition;
    }

    /**
     * <p>getParameterValue.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getParameterValue() {
        return iParameterValue;
    }

    /**
     * <p>getValueDisplayString.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getValueDisplayString() {
        return iValueDisplayString;
    }
}
