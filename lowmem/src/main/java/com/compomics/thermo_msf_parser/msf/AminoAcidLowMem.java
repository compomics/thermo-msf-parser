package com.compomics.thermo_msf_parser.msf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/24/12
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class AminoAcidLowMem implements AminoAcidInterface{

    /**
     *
     * @param iConnection connection to a msf file
     * @return vector containing all the aminoacids from the msf file
     * @throws SQLException
     */
    public Vector getAminoAcidsFromDb(Connection iConnection) throws SQLException {
        Vector<AminoAcid> iAminoAcids = new Vector<AminoAcid>();
        PreparedStatement stat = iConnection.prepareStatement("select * from AminoAcids");
        ResultSet rs = stat.executeQuery();
        while (rs.next()) {
            AminoAcid lAA = new AminoAcid(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getString(7));
            iAminoAcids.add(lAA);
        }
        return iAminoAcids;
    }
}
