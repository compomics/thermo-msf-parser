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
 *
 * @author Davy
 */
public class ScoreTypeLowMemController implements ScoreTypeInterface {

    private static final Logger logger = Logger.getLogger(ScoreTypeLowMemController.class);
    private List<ScoreTypeLowMem> iScoreTypes = new ArrayList<ScoreTypeLowMem>();

    @Override
    public List<ScoreTypeLowMem> getScoreTypesOfMsfFileList(List<MsfFile> MsfFiles) {
        for (MsfFile msfFile : MsfFiles) {
            try {
                PreparedStatement stat = msfFile.getConnection().prepareStatement("select * from ProcessingNodeScores");
                //get the score types
                if (iScoreTypes.isEmpty()) {
                    ResultSet rs = stat.executeQuery();
                    while (rs.next()) {
                        ScoreTypeLowMem lScoreType = new ScoreTypeLowMem(rs.getInt("ScoreID"), rs.getString("ScoreName"), rs.getString("FriendlyName"), rs.getString("Description"), rs.getInt("ScoreCategory"), rs.getInt("IsMainScore"));
                        iScoreTypes.add(lScoreType);
                    }
                    rs.close();
                }
                stat.close();
            } catch (SQLException ex) {
                logger.error(ex);
            }
        }
        return iScoreTypes;
    }

    @Override
    public Map<Integer, Double> getScoresForPeptide(PeptideLowMem peptide, MsfFile msfFile) {
        Map<Integer, Double> peptideScores = new HashMap<Integer, Double>();
        try {
            int scoreID;
            double scoreValue;
            PreparedStatement stat = msfFile.getConnection().prepareStatement("select ScoreID,ScoreValue from PeptideScores where PeptideID = " + peptide.getPeptideId());
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                scoreID = rs.getInt(1);
                scoreValue = rs.getDouble(2);
                peptideScores.put(scoreID, scoreValue);
            }
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return peptideScores;
    }

    @Override
    public void addScoresToPeptide(PeptideLowMem peptide, MsfFile msfFile) {
        try {
            int scoreID;
            double scoreValue;
            List<ScoreTypeLowMem> scoreTypes = getScoreTypes(msfFile);
            PreparedStatement stat = msfFile.getConnection().prepareStatement("select ScoreID,ScoreValue from PeptideScores where PeptideID = " + peptide.getPeptideId());
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                scoreID = rs.getInt(1);
                scoreValue = rs.getDouble(2);
                peptide.setScore(scoreValue, scoreID, scoreTypes);
            }
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
    }

    @Override
    public void getScoresForPeptideList(List<PeptideLowMem> peptideLowMemList, MsfFile msfFile) {
        String listOfPeptideids = "";
        HashMap<Integer, PeptideLowMem> pepidToPeptide = new HashMap<Integer, PeptideLowMem>();
        for (PeptideLowMem aPeptide : peptideLowMemList) {
            listOfPeptideids = "," + aPeptide.getPeptideId() + listOfPeptideids;
            pepidToPeptide.put(aPeptide.getPeptideId(), aPeptide);
        }
        listOfPeptideids = listOfPeptideids.replaceFirst(",", "");
        try {
            iScoreTypes = getScoreTypes(msfFile);
            PreparedStatement stat = msfFile.getConnection().prepareStatement("select PeptideID,ScoreID,ScoreValue from PeptideScores where PeptideID in (" + listOfPeptideids + ")");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                pepidToPeptide.get(rs.getInt("PeptideID")).setScore(rs.getDouble("ScoreValue"), rs.getInt("ScoreID"), iScoreTypes);
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    @Override
    public List<ScoreTypeLowMem> getScoreTypes(MsfFile msfFile) {
        //get the score types
        if (iScoreTypes.isEmpty()) {
            try {
                PreparedStatement stat = msfFile.getConnection().prepareStatement("select * from ProcessingNodeScores");
                ResultSet rs = stat.executeQuery();
                while (rs.next()) {
                    ScoreTypeLowMem lScoreType = new ScoreTypeLowMem(rs.getInt("ScoreID"), rs.getString("ScoreName"), rs.getString("FriendlyName"), rs.getString("Description"), rs.getInt("ScoreCategory"), rs.getInt("IsMainScore"));
                    iScoreTypes.add(lScoreType);
                }
                rs.close();
                stat.close();
            } catch (SQLException ex) {
                logger.error(ex);
            }
        }
        return iScoreTypes;
    }

    public void setScoreTypes(List<ScoreTypeLowMem> scoreTypes) {
        this.iScoreTypes = scoreTypes;
    }

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
