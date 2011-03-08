package com.compomics.thermo_msf_parser.msf;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 18-Feb-2011
 * Time: 10:11:01
 */

/**
 * This class represent the enzyme
 */
public class Enzyme {

    /**
     * The enzyme id
     */
    private int iEnzymeId;
    /**
     * The enzyme name
     */
    private String iName;
    /**
     * The abbreviation
     */
    private String iAbbreviation;
    /**
     * The seperator
     */
    private String iSeperator;
    /**
     * The non seperator
     */
    private String iNonSeperator;
    /**
     * The offset
     */
    private int iOffset;
    /**
     * The specificity
     */
    private int iSpecificity;

    /**
     * The constructor for the enzyme
     * @param iEnzymeId The enzyme id
     * @param iName The name
     * @param iAbbreviation The abbreviation
     * @param iSeperator The seperator
     * @param iNonSeperator The non seperator
     * @param iOffset The offset
     */
    public Enzyme(int iEnzymeId, String iName, String iAbbreviation, String iSeperator, String iNonSeperator, int iOffset) {
        this.iEnzymeId = iEnzymeId;
        this.iName = iName;
        this.iAbbreviation = iAbbreviation;
        this.iSeperator = iSeperator;
        this.iNonSeperator = iNonSeperator;
        this.iOffset = iOffset;
    }

    /**
     * Getter for the enzyme id
     * @return int with the enzyme id
     */
    public int getEnzymeId() {
        return iEnzymeId;
    }

    /**
     * Getter for the name
     * @return String with the name
     */
    public String getName() {
        return iName;
    }

    /**
     * Getter for the abbreviation
     * @return String with the abbreviation
     */
    public String getAbbreviation() {
        return iAbbreviation;
    }

    /**
     * Getter for the seperator
     * @return String with the seperator
     */
    public String getSeperator() {
        return iSeperator;
    }

    /**
     * Getter for the non seperator
     * @return String with the non seperator
     */
    public String getNonSeperator() {
        return iNonSeperator;
    }

    /**
     * Getter for the offset
     * @return int with the offset
     */
    public int getOffset() {
        return iOffset;
    }

    /**
     * Getter for the specificity
     * @return int with the specificity
     */
    public int getSpecificity() {
        return iSpecificity;
    }

    /**
     * Setter for the specificity
     * @param iSpecificity int with the specificity
     */
    public void setSpecificity(int iSpecificity) {
        this.iSpecificity = iSpecificity;
    }
}
