package com.compomics.thermo_msf_parser_API.lowmeminstance.model;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 4/25/12 Time: 10:39 AM To change
 * this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
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

    /**
     * <p>Constructor for ScoreTypeLowMem.</p>
     *
     * @param aScoreID a int.
     * @param aScoreName a {@link java.lang.String} object.
     * @param aFriendlyName a {@link java.lang.String} object.
     * @param aDescription a {@link java.lang.String} object.
     * @param aScoreCategory a int.
     * @param isMainScore a int.
     */
    public ScoreTypeLowMem(int aScoreID, String aScoreName, String aFriendlyName, String aDescription, int aScoreCategory, int isMainScore) {
        this.iScoreTypeId = aScoreID;
        this.iScoreName = aScoreName;
        this.iFriendlyName = aFriendlyName;
        this.iDescription = aDescription;
        this.iScoreCategory = aScoreCategory;
        this.iIsMainScore = isMainScore;
    }

    /**
     * <p>getScoreTypeId.</p>
     *
     * @return a int.
     */
    public int getScoreTypeId() {
        return iScoreTypeId;
    }

    /**
     * <p>getScoreName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getScoreName() {
        return iScoreName;
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
     * <p>getDescription.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescription() {
        return iDescription;
    }

    /**
     * <p>getScoreCategory.</p>
     *
     * @return a int.
     */
    public int getScoreCategory() {
        return iScoreCategory;
    }

    /**
     * <p>getIsMainScore.</p>
     *
     * @return a int.
     */
    public int getIsMainScore() {
        return iIsMainScore;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return iFriendlyName;
    }
}
