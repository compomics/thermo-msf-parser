package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinGroupLowMem;
import java.sql.PreparedStatement;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>ProteinGroupLowMemController class.</p>
 *
 * @author Davy
 * @version $Id: $Id
 */
public class ProteinGroupLowMemController {

    private static final Logger logger = Logger.getLogger(ProteinGroupLowMemController.class);


    /**
     * <p>getProteinGroupIDForProteinID.</p>
     *
     * @param proteinID a int.
     * @param msfFile a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile} object.
     * @return a int.
     */
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


    /**
     * <p>getProteinGroupForProteinID.</p>
     *
     * @param proteinID a int.
     * @param msfFile a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile} object.
     * @return a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinGroupLowMem} object.
     */
    public ProteinGroupLowMem getProteinGroupForProteinID(int proteinID, MsfFile msfFile) {
        return new ProteinGroupLowMem(getProteinGroupIDForProteinID(proteinID, msfFile));

    }
}
