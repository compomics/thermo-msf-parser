package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinGroupLowMem;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Davy
 */
public class ProteinGroupLowMemController {

    private static final Logger logger = Logger.getLogger(ProteinGroupLowMemController.class);

    /**
     *
     * @param proteinID
     * @param msfFile
     * @return
     */
    public int getProteinGroupIDForProteinID(int proteinID, MsfFile msfFile) {
        int proteinGroupID = 0;
        try {
            Statement stat = msfFile.getConnection().createStatement();
            ResultSet rs = stat.executeQuery("select ProteinGroupID from ProteinsProteinGroups where ProteinID = " + proteinID);
            try {
                rs.next();
                proteinGroupID = rs.getInt("ProteinGroupID");
            } finally {
                rs.close();
            }
            stat.close();
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return proteinGroupID;
    }

    /**
     *
     * @param proteinID
     * @param msfFile
     * @return
     */
    public ProteinGroupLowMem getProteinGroupForProteinID(int proteinID, MsfFile msfFile) {
        return new ProteinGroupLowMem(getProteinGroupIDForProteinID(proteinID, msfFile));

    }
}