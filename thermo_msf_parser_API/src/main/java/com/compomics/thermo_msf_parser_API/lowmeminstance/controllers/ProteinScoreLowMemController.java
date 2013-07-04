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
    
    /**
     * get the protein scores for a protein in proteome discoverer file
     * @param proteinID the protein id to get the scores for
     * @param msfFile the proteome discoverer file to look in
     * @return a list containing the protein scores requested
     */
    public List<ProteinScore> getScoresForProteinId (int proteinID, MsfFile msfFile) {
        List<ProteinScore> proteinScores = new ArrayList<ProteinScore>();
        try{
            Statement stat = msfFile.getConnection().createStatement();
            ResultSet rs = stat.executeQuery(new StringBuilder().append("select ProteinIdentificationGroupID,ProteinScore,Coverage from ProteinScores where ProteinID =").append(proteinID).toString());
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
