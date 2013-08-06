package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.highmeminstance.ProteinScore;
import java.sql.PreparedStatement;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
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
     *
     * @param proteinID the protein id to get the scores for
     * @param msfFile the proteome discoverer file to look in
     * @return a {@code list} containing the protein scores requested
     */
    public List<ProteinScore> getScoresForProteinId(int proteinID, MsfFile msfFile) {
        List<ProteinScore> proteinScores = new ArrayList<ProteinScore>();
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select ProteinIdentificationGroupID,ProteinScore,Coverage,ProcessingNodeNumber from ProteinScores where ProteinID =?");
                stat.setInt(1, proteinID);
                ResultSet rs = null;
                try {
                    rs = stat.executeQuery();
                    while (rs.next()) {
                        proteinScores.add(new ProteinScore(rs.getDouble("ProteinScore"), rs.getInt("ProcessingNodeNumber"), rs.getDouble("Coverage")));
                    }
                } finally {
                    if (rs != null) {
                        rs.close();
                    }
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return proteinScores;
    }
}
