package com.compomics.thermo_msf_parser_API.highmeminstance;

import java.util.ArrayList;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 21-Feb-2011
 * Time: 09:32:57
 */
public class Filter {

    /**
     * The filter name
     */
    private String iFilterName;
    /**
     * The filter description
     */
    private String iFilterDescription;
    /**
     * The filter kind
     */
    private String iKind;
    /**
     * The applyable nodes
     */
    private String iApplyableNodes;
    /**
     * Boolean that indicates if this filter is active
     */
    private boolean iIsActive;
    /**
     * Boolean that indicates if this filter is applied
     */
    private boolean iIsApplied;
    /**
     * Boolean that indicates if this filter is a confidence filter
     */
    private boolean iIsConfidenceFilter;
    /**
     * The confidence level
     */
    private String iConfidenceLevel;
    /**
     * The processing node number
     */
    private int iProcessingNodeNumber;
    /**
     * The version
     */
    private int iVersion;
    /**
     * List with the parameter names
     */
    private List<String> iParameterName = new ArrayList<String>();
    /**
     * List with the parameter values
     */
    private List<String> iParameterValue = new ArrayList<String>();
    /**
     * The filter set name
     */
    private String iFilterSetName;

    /**
     * The filter constructor
     * @param iFilterSetName The filter set name
     * @param iFilterXmlLines The filter xml
     */
    public Filter(String iFilterSetName, List<String> iFilterXmlLines) {
        this.iFilterSetName = iFilterSetName;
        //parse the xml
        String iFilterLine = iFilterXmlLines.get(0);
        this.iFilterName = iFilterLine.substring(iFilterLine.indexOf("\"", iFilterLine.indexOf("FilterName")) + 1,iFilterLine.indexOf("\"", iFilterLine.indexOf("\"", iFilterLine.indexOf("FilterName")) + 1));
        this.iFilterDescription = iFilterLine.substring(iFilterLine.indexOf("\"", iFilterLine.indexOf("Description")) + 1,iFilterLine.indexOf("\"", iFilterLine.indexOf("\"", iFilterLine.indexOf("Description")) + 1));
        this.iKind = iFilterLine.substring(iFilterLine.indexOf("\"", iFilterLine.indexOf("FilterKind")) + 1,iFilterLine.indexOf("\"", iFilterLine.indexOf("\"", iFilterLine.indexOf("FilterKind")) + 1));
        this.iIsActive = Boolean.valueOf(iFilterLine.substring(iFilterLine.indexOf("\"", iFilterLine.indexOf("IsActive")) + 1,iFilterLine.indexOf("\"", iFilterLine.indexOf("\"", iFilterLine.indexOf("IsActive")) + 1)));
        this.iIsActive = Boolean.valueOf(iFilterLine.substring(iFilterLine.indexOf("\"", iFilterLine.indexOf("IsApplied")) + 1,iFilterLine.indexOf("\"", iFilterLine.indexOf("\"", iFilterLine.indexOf("IsApplied")) + 1)));
        this.iIsActive = Boolean.valueOf(iFilterLine.substring(iFilterLine.indexOf("\"", iFilterLine.indexOf("IsConfidenceFilter")) + 1,iFilterLine.indexOf("\"", iFilterLine.indexOf("\"", iFilterLine.indexOf("IsConfidenceFilter")) + 1)));
        this.iConfidenceLevel = iFilterLine.substring(iFilterLine.indexOf("\"", iFilterLine.indexOf("ConfidenceLevel")) + 1,iFilterLine.indexOf("\"", iFilterLine.indexOf("\"", iFilterLine.indexOf("ConfidenceLevel")) + 1));
        this.iProcessingNodeNumber = Integer.valueOf(iFilterLine.substring(iFilterLine.indexOf("\"", iFilterLine.indexOf("ProcessingNodeNumber")) + 1,iFilterLine.indexOf("\"", iFilterLine.indexOf("\"", iFilterLine.indexOf("ProcessingNodeNumber")) + 1)));
        this.iVersion = Integer.valueOf(iFilterLine.substring(iFilterLine.indexOf("\"", iFilterLine.indexOf("Version")) + 1,iFilterLine.indexOf("\"", iFilterLine.indexOf("\"", iFilterLine.indexOf("Version")) + 1)));

        //parse the parameter lines
        for(int i = 1; i<iFilterXmlLines.size(); i ++){
            String iParameterLine = iFilterXmlLines.get(i);
            iParameterName.add(iParameterLine.substring(iParameterLine.indexOf("\"", iParameterLine.indexOf("ParameterDisplayName")) + 1,iParameterLine.indexOf("\"", iParameterLine.indexOf("\"", iParameterLine.indexOf("ParameterDisplayName")) + 1)));
            iParameterValue.add(iParameterLine.substring(iParameterLine.indexOf("\"", iParameterLine.indexOf("ParameterValue")) + 1,iParameterLine.indexOf("\"", iParameterLine.indexOf("\"", iParameterLine.indexOf("ParameterValue")) + 1)));
        }
    }


    /**
     * Getter for the filter name
     * @return String with the filter name
     */
    public String getFilterName() {
        return iFilterName;
    }

    /**
     * Getter with the filter description
     * @return String with the filter description
     */
    public String getFilterDescription() {
        return iFilterDescription;
    }

    /**
     * Getter for the kind
     * @return String with the kind
     */
    public String getKind() {
        return iKind;
    }

    /**
     * Getter for the applyable nodes
     * @return String with the applyable nodes
     */
    public String getApplyableNodes() {
        return iApplyableNodes;
    }

    /**
     * Getter for a boolean that indicates that the filter is active
     * @return boolean that indicates that the filter is active
     */
    public boolean isActive() {
        return iIsActive;
    }

    /**
     * Getter for a boolean that indicates that the filter is applied
     * @return boolean that indicates that the filter is applied
     */
    public boolean isApplied() {
        return iIsApplied;
    }

    /**
     * Getter for a boolean that indicates that the filter is a confidence filter
     * @return boolean that indicates that the filter is confidence filter
     */
    public boolean isConfidenceFilter() {
        return iIsConfidenceFilter;
    }

    /**
     * Getter for the confidence level
     * @return String with the confidence level
     */
    public String getConfidenceLevel() {
        return iConfidenceLevel;
    }

    /**
     * Getter for the processing node number
     * @return int with the processing node number
     */
    public int getProcessingNodeNumber() {
        return iProcessingNodeNumber;
    }

    /**
     * Getter for the version
     * @return int with the version
     */
    public int getVersion() {
        return iVersion;
    }

    /**
     * Getter for a vector with the different parameter names
     * @return vector with the different parameter names
     */
    public List<String> getParameterName() {
        return iParameterName;
    }

    /**
     * Getter for a vector with the different parameter values
     * @return vector with the different parameter values
     */
    public List<String> getParameterValue() {
        return iParameterValue;
    }

    /**
     * Getter for the filter name
     * @return String with the filter name
     */
    public String getFilterSetName() {
        return iFilterSetName;
    }
}
