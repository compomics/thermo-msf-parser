package com.compomics.thermo_msf_parser_API.lowmeminstance.model;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 4/25/12 Time: 10:39 AM To change
 * this template use File | Settings | File Templates.
 */
public class ScoreTypeLowMem {

    private final int iScoreTypeId;
    /**
     * The score name
     */
    private final String iScoreName;
    /**
     * The friendly score name
     */
    private final String iFriendlyName;
    /**
     * The description
     */
    private final String iDescription;
    /**
     * The score category
     */
    private final int iScoreCategory;
    /**
     * An int that indicates if this score is the main score
     */
    private final int iIsMainScore;

    public ScoreTypeLowMem(int aScoreID, String aScoreName, String aFriendlyName, String aDescription, int aScoreCategory, int isMainScore) {
        this.iScoreTypeId = aScoreID;
        this.iScoreName = aScoreName;
        this.iFriendlyName = aFriendlyName;
        this.iDescription = aDescription;
        this.iScoreCategory = aScoreCategory;
        this.iIsMainScore = isMainScore;
    }

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

    @Override
    public String toString() {
        return iFriendlyName;
    }
}
