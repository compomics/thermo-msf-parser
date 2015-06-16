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
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class AminoAcidLowMemController implements AminoAcidInterface {

    /** {@inheritDoc} */
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
