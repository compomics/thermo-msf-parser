package com.compomics.thermo_msf_parser.msf;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 18-Feb-2011
 * Time: 13:46:17
 */
public class ScoreType {
    // Class specific log4j logger for Thermo_msf_parserGUI instances.
	 private static Logger logger = Logger.getLogger(ScoreType.class);
    /**
     * The score type id
     */
    private int iScoreTypeId;
    /**
     * The score name
     */
    private String iScoreName;
    /**
     * The friendly score name
     */
    private String iFriendlyName;
    /**
     * The description
     */
    private String iDescription;
    /**
     * The score category
     */
    private int iScoreCategory;
    /**
     * An int that indicates if this score is the main score
     */
    private int iIsMainScore;

    /**
     * The score type constructor
     * @param iScoreTypeId The score type id
     * @param iScoreName The score name
     * @param iFriendlyName The friendly score name
     * @param iDescription The description
     * @param iScoreCategory The score category
     * @param iIsMainScore An int that indicates if this score is the main score
     */
    public ScoreType(int iScoreTypeId, String iScoreName, String iFriendlyName, String iDescription, int iScoreCategory, int iIsMainScore) {
        this.iScoreTypeId = iScoreTypeId;
        this.iScoreName = iScoreName;
        this.iFriendlyName = iFriendlyName;
        this.iDescription = iDescription;
        this.iScoreCategory = iScoreCategory;
        this.iIsMainScore = iIsMainScore;
    }
    
    
    //getters

    public int getScoreTypeId() {
        return iScoreTypeId;
    }

    public String getScoreName() {
        return iScoreName;
    }

    public String getFriendlyName() {
        return iFriendlyName;
    }

    public String getDescription() {
        return iDescription;
    }

    public int getScoreCategory() {
        return iScoreCategory;
    }

    public int getIsMainScore() {
        return iIsMainScore;
    }

    public String toString(){
        return iFriendlyName;
    }
}
