package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.highmeminstance.AminoAcid;
import com.compomics.thermo_msf_parser_API.interfaces.AminoAcidInterface;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 4/24/12 Time: 11:09 AM To change
 * this template use File | Settings | File Templates.
 */
public class AminoAcidLowMemController implements AminoAcidInterface {

    /**
     *
     * @param msfFile the proteome discoverer file to retrieve the amino acids from
     * @return a vector containing all the amino acids in the database
     * @throws SQLException
     */
    @Override
    public List<AminoAcid> getAminoAcidsFromDb(MsfFile msfFile) throws SQLException {
        List<AminoAcid> iAminoAcids = new ArrayList<AminoAcid>();
        PreparedStatement stat = null;
        try {
            stat = msfFile.getConnection().prepareStatement("select * from AminoAcids");
            ResultSet rs = stat.executeQuery();
            try {
                while (rs.next()) {
                    AminoAcid lAA = new AminoAcid(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getString(7));
                    iAminoAcids.add(lAA);
                }
            } finally {
                rs.close();
            }
        } finally {
            if (stat != null) {
                stat.close();
            }
        }
        return iAminoAcids;
    }
}
