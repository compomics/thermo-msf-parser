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
public class ScoreTypeLowMem implements ScoreTypeInterface {

    public ScoreTypeLowMem(){}

    private Vector<ScoreType> iScoreTypes = new Vector<ScoreType>();

    /**
     *
     * @param iConnection a connection to the msf file
     * @return a vector containing all the scoretypes stored in the msf file
     * @throws SQLException
     */
    public Vector<ScoreType> getScoreTypes(Connection iConnection) throws SQLException {

        PreparedStatement stat =iConnection.prepareStatement("select * from ProcessingNodeScores");
        //get the score types
        if (iScoreTypes.isEmpty()){
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                ScoreType lScoreType = new ScoreType(rs.getInt("ScoreID"), rs.getString("ScoreName"), rs.getString("FriendlyName"), rs.getString("Description"), rs.getInt("ScoreCategory"), rs.getInt("IsMainScore"));
                iScoreTypes.add(lScoreType);
            }
            return iScoreTypes;
        } else {
            return iScoreTypes;
        }
    }

    public void setScoreTypes(Vector<ScoreType> scoreTypes) {
        this.iScoreTypes = scoreTypes;
    }

    /**
     *
    * @return a vector containing all the scoretypes stored in the msf file
     * @throws SQLException
     */
    public Vector<ScoreType> getScoreTypesOfMsfFileVector(Vector<MsfFile> MsfFiles) throws SQLException {
        Connection iConnection;
        for (MsfFile msfFile : MsfFiles) {
            iConnection = msfFile.getConnection();
            PreparedStatement stat =iConnection.prepareStatement("select * from ProcessingNodeScores");
        //get the score types
        if (iScoreTypes.isEmpty()){
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                ScoreType lScoreType = new ScoreType(rs.getInt("ScoreID"), rs.getString("ScoreName"), rs.getString("FriendlyName"), rs.getString("Description"), rs.getInt("ScoreCategory"), rs.getInt("IsMainScore"));
                iScoreTypes.add(lScoreType);
            }
        }
        }
        return iScoreTypes;
    }

    /**
     *
     * @param peptide a peptide to return the different scoretypes for
     * @param iConnection a connection to the msf file
     * @return a hashmap with key scoreid(ScoreID in the msf file) value scorevalue (ScoreValue in the msf file)
     * @throws SQLException
     */
    public HashMap<Integer, Double> getScoresForPeptide(Peptide peptide, Connection iConnection) throws SQLException {
        HashMap<Integer, Double> peptideScores = new HashMap<Integer, Double>();
        int scoreID;
        double scoreValue;
        PreparedStatement stat = iConnection.prepareStatement("select ScoreID,ScoreValue from PeptideScores where PeptideID = "+peptide.getPeptideId());
        ResultSet rs = stat.executeQuery();
        while(rs.next()){
            scoreID = rs.getInt(1);
            scoreValue = rs.getDouble(2);
            peptideScores.put(scoreID,scoreValue);
        }
        return  peptideScores;
    }

    public void addScoresToPeptide (PeptideLowMem peptide, Connection iConnection) throws SQLException {
        int scoreID;
        double scoreValue;
        Vector<ScoreType> scoreTypes = getScoreTypes(iConnection);
        PreparedStatement stat = iConnection.prepareStatement("select ScoreID,ScoreValue from PeptideScores where PeptideID = "+peptide.getPeptideId());
        ResultSet rs = stat.executeQuery();
        while(rs.next()){
            scoreID = rs.getInt(1);
            scoreValue = rs.getDouble(2);
            peptide.setScore(scoreValue,scoreID,scoreTypes);
        }
    }

    public void getScoresForPeptideVector(Vector<PeptideLowMem>peptideLowMemVector,Connection iConnection){
        String listOfPeptideids = "";
        HashMap<Integer,PeptideLowMem> pepidToPeptide = new HashMap<Integer,PeptideLowMem>();
            for (PeptideLowMem aPeptide : peptideLowMemVector){
                listOfPeptideids = ","+aPeptide.getPeptideId()+listOfPeptideids;
                pepidToPeptide.put(aPeptide.getPeptideId(),aPeptide);
            }
        listOfPeptideids = listOfPeptideids.replaceFirst(",","");
        try {
            Vector<ScoreType> iScoreTypes = getScoreTypes(iConnection);
            PreparedStatement stat = iConnection.prepareStatement("select PeptideID,ScoreID,ScoreValue from PeptideScores where PeptideID in ("+listOfPeptideids+")");
            ResultSet rs = stat.executeQuery();
            while (rs.next()){
                pepidToPeptide.get(rs.getInt("PeptideID")).setScore(rs.getDouble("ScoreValue"),rs.getInt("ScoreID"),iScoreTypes);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
