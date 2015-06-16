/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.interfaces.ScoreTypeInterface;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ScoreTypeLowMem;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * <p>ScoreTypeLowMemController class.</p>
 *
 * @author Davy
 * @version $Id: $Id
 */
public class ScoreTypeLowMemController implements ScoreTypeInterface {

    private static final Logger logger = Logger.getLogger(ScoreTypeLowMemController.class);
    private List<ScoreTypeLowMem> iScoreTypes = new ArrayList<ScoreTypeLowMem>();

    /** {@inheritDoc} */
    @Override
    public List<ScoreTypeLowMem> getScoreTypesOfMsfFileList(List<MsfFile> MsfFiles) {
        for (MsfFile msfFile : MsfFiles) {
            try {
                PreparedStatement stat = null;
                try {
                    stat = msfFile.getConnection().prepareStatement("select * from ProcessingNodeScores");
                    //get the score types
                    if (iScoreTypes.isEmpty()) {
                        ResultSet rs = stat.executeQuery();
                        try {
                            while (rs.next()) {
                                ScoreTypeLowMem lScoreType = new ScoreTypeLowMem(rs.getInt("ScoreID"), rs.getString("ScoreName"), rs.getString("FriendlyName"), rs.getString("Description"), rs.getInt("ScoreCategory"), rs.getInt("IsMainScore"));
                                iScoreTypes.add(lScoreType);
                            }
                        } finally {
                            rs.close();
                        }
                    }
                } finally {
                    if (stat != null) {
                        stat.close();
                    }
                }
            } catch (SQLException ex) {
                logger.error(ex);
            }
        }
        return iScoreTypes;
    }

    /** {@inheritDoc} */
    @Override
    public Map<Integer, Double> getScoresForPeptide(PeptideLowMem peptide, MsfFile msfFile) {
        Map<Integer, Double> peptideScores = new HashMap<Integer, Double>();
        try {
            int scoreID;
            double scoreValue;
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select ScoreID,ScoreValue from PeptideScores where PeptideID = ?");
                stat.setInt(1, peptide.getPeptideId());
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        scoreID = rs.getInt(1);
                        scoreValue = rs.getDouble(2);
                        peptideScores.put(scoreID, scoreValue);
                    }
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return peptideScores;
    }

    /** {@inheritDoc} */
    @Override
    public void addScoresToPeptide(PeptideLowMem peptide, MsfFile msfFile) {
        try {
            int scoreID;
            double scoreValue;
            List<ScoreTypeLowMem> scoreTypes = getScoreTypes(msfFile);
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select ScoreID,ScoreValue from PeptideScores where PeptideID = ?");
                stat.setInt(1, peptide.getPeptideId());
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        scoreID = rs.getInt(1);
                        scoreValue = rs.getDouble(2);
                        peptide.setScore(scoreValue, scoreID, scoreTypes);
                    }
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void getScoresForPeptideList(List<PeptideLowMem> peptideLowMemList, MsfFile msfFile) {
        String listOfPeptideids = "";
        HashMap<Integer, PeptideLowMem> pepidToPeptide = new HashMap<Integer, PeptideLowMem>();
        for (PeptideLowMem aPeptide : peptideLowMemList) {
            listOfPeptideids = String.format(",%s%s", aPeptide.getPeptideId(), listOfPeptideids);
            pepidToPeptide.put(aPeptide.getPeptideId(), aPeptide);
        }
        listOfPeptideids = listOfPeptideids.replaceFirst(",", "");
        try {
            iScoreTypes = getScoreTypes(msfFile);
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select PeptideID,ScoreID,ScoreValue from PeptideScores where PeptideID in (?)");
                stat.setString(1, listOfPeptideids);
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        pepidToPeptide.get(rs.getInt("PeptideID")).setScore(rs.getDouble("ScoreValue"), rs.getInt("ScoreID"), iScoreTypes);
                    }
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<ScoreTypeLowMem> getScoreTypes(MsfFile msfFile) {
        //get the score types
        if (iScoreTypes.isEmpty()) {
            try {
                PreparedStatement stat = null;
                try {
                    stat = msfFile.getConnection().prepareStatement("select * from ProcessingNodeScores");
                    ResultSet rs = stat.executeQuery();
                    try {
                        while (rs.next()) {
                            ScoreTypeLowMem lScoreType = new ScoreTypeLowMem(rs.getInt("ScoreID"), rs.getString("ScoreName"), rs.getString("FriendlyName"), rs.getString("Description"), rs.getInt("ScoreCategory"), rs.getInt("IsMainScore"));
                            iScoreTypes.add(lScoreType);
                        }
                    } finally {
                        rs.close();
                    }
                } finally {
                    if (stat != null) {
                        stat.close();
                    }
                }
            } catch (SQLException ex) {
                logger.error(ex);
            }
        }
        return iScoreTypes;
    }

    /**
     * <p>setScoreTypes.</p>
     *
     * @param scoreTypes a {@link java.util.List} object.
     */
    public void setScoreTypes(List<ScoreTypeLowMem> scoreTypes) {
        this.iScoreTypes = scoreTypes;
    }

    /**
     * <p>getMajorScoreTypes.</p>
     *
     * @param msfFile a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile} object.
     * @return a {@link java.util.List} object.
     */
    public List<ScoreTypeLowMem> getMajorScoreTypes(MsfFile msfFile) {
        List<ScoreTypeLowMem> scoreTypes = getScoreTypes(msfFile);
        List<ScoreTypeLowMem> majorScoreTypes = new ArrayList<ScoreTypeLowMem>();
        for (ScoreTypeLowMem scoreType : scoreTypes) {
            if (scoreType.getIsMainScore() == 1) {
                majorScoreTypes.add(scoreType);
            }
        }
        return majorScoreTypes;
    }
}
