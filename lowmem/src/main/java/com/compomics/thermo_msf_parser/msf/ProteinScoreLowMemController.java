/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser.msf;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author Davy
 */
public class ProteinScoreLowMemController {
private static final Logger logger = Logger.getLogger(ProteinScoreLowMemController.class);
    
    
    public Vector<ProteinScore> getScoresForProteinId (int proteinID, Connection msfFileConnection) {
        Vector<ProteinScore> proteinScores = new Vector<ProteinScore>();
        try{
            Statement stat = msfFileConnection.createStatement();
            ResultSet rs = stat.executeQuery("select ProteinIdentificationGroupID,ProteinScore,Coverage from ProteinScores where ProteinID ="+proteinID);
            while (rs.next()) {
                proteinScores.add(new ProteinScore(rs.getInt("ProteinIdentificationGroupID"),rs.getInt("ProteinScore"),rs.getInt("Coverage")));
            }
            rs.close();
            stat.close();
        } catch(SQLException sqle) {
            logger.error(sqle);
        }
        return proteinScores;  
    }
    
}