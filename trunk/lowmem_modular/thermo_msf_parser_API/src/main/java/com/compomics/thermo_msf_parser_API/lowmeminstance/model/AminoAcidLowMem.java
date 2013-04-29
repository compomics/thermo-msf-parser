package com.compomics.thermo_msf_parser_API.lowmeminstance.model;

import com.compomics.thermo_msf_parser_API.highmeminstance.AminoAcid;
import com.compomics.thermo_msf_parser_API.interfaces.AminoAcidInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
     * @param aConnection a connection to the msf file
     * @return a vector containing all the amino acids in the database
     * @throws SQLException 
     */
    @Override
    public List<AminoAcid> getAminoAcidsFromDb(Connection aConnection) throws SQLException {
        List<AminoAcid> iAminoAcids = new ArrayList<AminoAcid>();
        PreparedStatement stat = aConnection.prepareStatement("select * from AminoAcids");
        ResultSet rs = stat.executeQuery();
        while (rs.next()) {
            AminoAcid lAA = new AminoAcid(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getString(7));
            iAminoAcids.add(lAA);
        }
        rs.close();
        stat.close();
        return iAminoAcids;
    }
}
