package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinGroupLowMem;
import java.sql.PreparedStatement;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Davy
 */
public class ProteinGroupLowMemController {

    private static final Logger logger = Logger.getLogger(ProteinGroupLowMemController.class);


    public int getProteinGroupIDForProteinID(int proteinID, MsfFile msfFile) {
        int proteinGroupID = 0;
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select ProteinGroupID from ProteinsProteinGroups where ProteinID = ?");
                stat.setInt(1,proteinID);
                ResultSet rs = stat.executeQuery();
                try {
                    rs.next();
                    proteinGroupID = rs.getInt("ProteinGroupID");
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return proteinGroupID;
    }


    public ProteinGroupLowMem getProteinGroupForProteinID(int proteinID, MsfFile msfFile) {
        return new ProteinGroupLowMem(getProteinGroupIDForProteinID(proteinID, msfFile));

    }
}