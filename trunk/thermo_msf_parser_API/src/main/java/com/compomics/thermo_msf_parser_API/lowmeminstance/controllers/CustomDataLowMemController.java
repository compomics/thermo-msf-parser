package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.highmeminstance.CustomDataField;
import com.compomics.thermo_msf_parser_API.interfaces.CustomDataInterface;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 5/2/12 Time: 11:46 AM To change
 * this template use File | Settings | File Templates.
 */
public class CustomDataLowMemController implements CustomDataInterface {

    private static final Logger logger = Logger.getLogger(CustomDataLowMemController.class);
    private final HashMap<Integer, CustomDataField> customDataFieldHashMap = new HashMap<Integer, CustomDataField>();

    @Override
    public Map<Integer, CustomDataField> getCustomDataFields(MsfFile msfFile) {
        if (customDataFieldHashMap.isEmpty()) {
            try {
                PreparedStatement stat = msfFile.getConnection().prepareStatement("select * from CustomDataFields");
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        CustomDataField lField = new CustomDataField(rs.getInt("FieldID"), rs.getString("DisplayName"));
                        customDataFieldHashMap.put(rs.getInt("FieldID"), lField);
                    }
                } finally {
                    rs.close();
                }
                stat.close();
            } catch (SQLException ex) {
                logger.error(ex);
            }
        }
        return customDataFieldHashMap;
    }

    @Override
    public List<CustomDataField> getCustomPeptideData(Map<Integer, CustomDataField> customData, MsfFile msfFile) {
        List<CustomDataField> iPeptideUsedCustomDataFields = new ArrayList<CustomDataField>();
        try {
            PreparedStatement stat = msfFile.getConnection().prepareStatement("select FieldID,FieldValue from CustomDataPeptides group by FieldID");
            ResultSet rs = stat.executeQuery();
            try {
                while (rs.next()) {
                    iPeptideUsedCustomDataFields.add(customData.get(rs.getInt("FieldID")));
                }
            } finally {
                rs.close();
            }
            stat.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return iPeptideUsedCustomDataFields;
    }

    /**
     * adds the custom data of a protein to that protein object
     *
     * @param protein the protein (low memory instance) object
     * @param iConnection connection to the msf file
     * @throws SQLException
     */
    @Override
    public void addCustomProteinsData(ProteinLowMem protein, MsfFile msfFile) {
        try {
            PreparedStatement stat = msfFile.getConnection().prepareStatement("select FieldValue,FieldID from CustomDataProteins where ProteinID = ?");
            stat.setInt(1, protein.getProteinID());
            ResultSet rs = stat.executeQuery();
            try {
                while (rs.next()) {
                    protein.addCustomDataField(rs.getInt(2), rs.getString(1));
                }
            } finally {
                rs.close();
            }
            stat.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
    }

    @Override
    public List<CustomDataField> getCustomSpectraData(Map<Integer, CustomDataField> iCustomDataFieldsMap, MsfFile msfFile) {
        List<CustomDataField> iSpectrumUsedCustomDataFields = new ArrayList<CustomDataField>();
        try {
            PreparedStatement stat = msfFile.getConnection().prepareStatement("select fieldid from CustomDataSpectra group by fieldid");
            ResultSet rs = stat.executeQuery();
            try {
                while (rs.next()) {
                    iSpectrumUsedCustomDataFields.add(iCustomDataFieldsMap.get(rs.getInt("FieldID")));
                }
            } finally {
                rs.close();
            }
            stat.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return iSpectrumUsedCustomDataFields;
    }
}
