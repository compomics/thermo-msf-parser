package com.compomics.thermo_msf_parser_API.highmeminstance;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 18-Feb-2011
 * Time: 13:46:17
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class ScoreType {
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
     *
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
    public String toString(){
        return iFriendlyName;
    }
}
