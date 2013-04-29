/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.highmeminstance.ProteinScore;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Davy
 */
public class ProteinScoreLowMemController {
private static final Logger logger = Logger.getLogger(ProteinScoreLowMemController.class);
    
    
    public List<ProteinScore> getScoresForProteinId (int proteinID, MsfFile msfFile) {
        List<ProteinScore> proteinScores = new ArrayList<ProteinScore>();
        try{
            Statement stat = msfFile.getConnection().createStatement();
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
