package com.compomics.thermo_msf_parser.msf;

import com.compomics.thermo_msf_parser.gui.MsfFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/25/12
 * Time: 10:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class ScoreTypeLowMem {
    
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


    ScoreTypeLowMem(int aScoreID, String aScoreName, String aFriendlyName, String aDescription, int aScoreCategory, int isMainScore) {
        this.iScoreTypeId = aScoreID;
        this.iScoreName = aScoreName;
        this.iFriendlyName = aFriendlyName;
        this.iDescription = aDescription;
        this.iScoreCategory = aScoreCategory;
        this.iIsMainScore = isMainScore;
    }

    /**
     *
     * @param iConnection a connection to the msf file
     * @return a vector containing all the scoretypes stored in the msf file
     * @throws SQLException
     */


    /**
     *
    * @return a vector containing all the scoretypes stored in the msf file
     * @throws SQLException
     */
    
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
