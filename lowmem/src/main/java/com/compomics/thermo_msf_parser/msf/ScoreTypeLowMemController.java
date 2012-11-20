/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser.msf;

import com.compomics.thermo_msf_parser.gui.MsfFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Davy
 */
public class ScoreTypeLowMemController implements ScoreTypeInterface {
    
        private Vector<ScoreTypeLowMem> iScoreTypes = new Vector<ScoreTypeLowMem>();
    
        public Vector<ScoreTypeLowMem> getScoreTypesOfMsfFileVector(Vector<MsfFile> MsfFiles){
        Connection aConnection;
        for (MsfFile msfFile : MsfFiles) {
                try {
                    aConnection = msfFile.getConnection();
                    PreparedStatement stat =aConnection.prepareStatement("select * from ProcessingNodeScores");
                //get the score types
                if (iScoreTypes.isEmpty()){
                    ResultSet rs = stat.executeQuery();
                    while (rs.next()) {
                        ScoreTypeLowMem lScoreType = new ScoreTypeLowMem(rs.getInt("ScoreID"), rs.getString("ScoreName"), rs.getString("FriendlyName"), rs.getString("Description"), rs.getInt("ScoreCategory"), rs.getInt("IsMainScore"));
                        iScoreTypes.add(lScoreType);
                    }
                }
                } catch (SQLException ex) {
                    Logger.getLogger(ScoreTypeLowMemController.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        return iScoreTypes;
    }

    public HashMap<Integer, Double> getScoresForPeptide(Peptide peptide, Connection aConnection){
        HashMap<Integer, Double> peptideScores = new HashMap<Integer, Double>();
        try {
            int scoreID;
            double scoreValue;
            PreparedStatement stat = aConnection.prepareStatement("select ScoreID,ScoreValue from PeptideScores where PeptideID = "+peptide.getPeptideId());
            ResultSet rs = stat.executeQuery();
            while(rs.next()){
                scoreID = rs.getInt(1);
                scoreValue = rs.getDouble(2);
                peptideScores.put(scoreID,scoreValue);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ScoreTypeLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return  peptideScores;
    }

    public void addScoresToPeptide (PeptideLowMem peptide, Connection aConnection) {
        try {
            int scoreID;
            double scoreValue;
            Vector<ScoreTypeLowMem> scoreTypes = getScoreTypes(aConnection);
            PreparedStatement stat = aConnection.prepareStatement("select ScoreID,ScoreValue from PeptideScores where PeptideID = "+peptide.getPeptideId());
            ResultSet rs = stat.executeQuery();
            while(rs.next()){
                scoreID = rs.getInt(1);
                scoreValue = rs.getDouble(2);
                peptide.setScore(scoreValue,scoreID,scoreTypes);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ScoreTypeLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getScoresForPeptideVector(Vector<PeptideLowMem>peptideLowMemVector,Connection aConnection){
        String listOfPeptideids = "";
        HashMap<Integer,PeptideLowMem> pepidToPeptide = new HashMap<Integer,PeptideLowMem>();
            for (PeptideLowMem aPeptide : peptideLowMemVector){
                listOfPeptideids = ","+aPeptide.getPeptideId()+listOfPeptideids;
                pepidToPeptide.put(aPeptide.getPeptideId(),aPeptide);
            }
        listOfPeptideids = listOfPeptideids.replaceFirst(",","");
        try {
            Vector<ScoreTypeLowMem> iScoreTypes = getScoreTypes(aConnection);
            PreparedStatement stat = aConnection.prepareStatement("select PeptideID,ScoreID,ScoreValue from PeptideScores where PeptideID in ("+listOfPeptideids+")");
            ResultSet rs = stat.executeQuery();
            while (rs.next()){
                pepidToPeptide.get(rs.getInt("PeptideID")).setScore(rs.getDouble("ScoreValue"),rs.getInt("ScoreID"),iScoreTypes);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

      public Vector<ScoreTypeLowMem> getScoreTypes(Connection aConnection) {
            //get the score types
            if (iScoreTypes.isEmpty()){
                try {
                PreparedStatement stat =aConnection.prepareStatement("select * from ProcessingNodeScores");
                ResultSet rs = stat.executeQuery();
                while (rs.next()) {
                    ScoreTypeLowMem lScoreType = new ScoreTypeLowMem(rs.getInt("ScoreID"), rs.getString("ScoreName"), rs.getString("FriendlyName"), rs.getString("Description"), rs.getInt("ScoreCategory"), rs.getInt("IsMainScore"));
                    iScoreTypes.add(lScoreType);
                }
                return iScoreTypes;
            } catch (SQLException ex) {
            Logger.getLogger(ScoreTypeLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
            } else {
                return iScoreTypes;
            }
            //not good
            return null;
    }
    
          public void setScoreTypes(Vector<ScoreTypeLowMem> scoreTypes) {
        this.iScoreTypes = scoreTypes;
    }
    
}
