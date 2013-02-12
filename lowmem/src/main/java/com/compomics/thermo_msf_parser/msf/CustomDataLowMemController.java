package com.compomics.thermo_msf_parser.msf;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 5/2/12 Time: 11:46 AM To change
 * this template use File | Settings | File Templates.
 */
public class CustomDataLowMemController {

    private static final Logger logger = Logger.getLogger(CustomDataLowMemController.class);
    private final HashMap<Integer, CustomDataField> customDataFieldHashMap = new HashMap<Integer, CustomDataField>();

    /**
     *
     * @param iConnection a connection to the msf file
     * @return a hashmap containing the custom fields key: fieldid in the db
     * value: displayname given in the db
     */
    public HashMap<Integer, CustomDataField> getCustomFieldMap(Connection iConnection) {

        try {
            PreparedStatement stat = iConnection.prepareStatement("select * from CustomDataFields");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                CustomDataField lField = new CustomDataField(rs.getInt("FieldID"), rs.getString("DisplayName"));
                customDataFieldHashMap.put(rs.getInt("FieldID"), lField);
            }
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return customDataFieldHashMap;
    }

    /**
     *
     * @param iConnection a connection to the msf file
     * @return a hashmap with the custom peptide data key: proteinid
     * value:hashmap with key:the custom field id value: the field value
     */
    public ArrayList<CustomDataField> getCustomPeptideData(HashMap<Integer, CustomDataField> customData, Connection iConnection) {
        ArrayList<CustomDataField> iPeptideUsedCustomDataFields = new ArrayList<CustomDataField>();
        try {
            PreparedStatement stat = iConnection.prepareStatement("select FieldID,FieldValue from CustomDataPeptides group by FieldID");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                iPeptideUsedCustomDataFields.add(customData.get(rs.getInt("FieldID")));
            }
            rs.close();
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
    public void addCustomProteinsData(ProteinLowMem protein, Connection iConnection) {
        try {
            PreparedStatement stat = iConnection.prepareStatement("select FieldValue,FieldID from CustomDataProteins where ProteinID = ?");
            stat.setInt(1, protein.getProteinID());
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                protein.addCustomDataField(rs.getInt(2), rs.getString(1));
            }
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
    }

    /**
     *
     * @param iConnection connection to the msf file
     * @return an arraylist with the custom spectra data
     * @throws SQLException
     */
    public ArrayList<CustomDataField> getCustomSpectraData(HashMap<Integer, CustomDataField> iCustomDataFieldsMap, Connection iConnection) {
        ArrayList<CustomDataField> iSpectrumUsedCustomDataFields = new ArrayList<CustomDataField>();
        try {
            PreparedStatement stat = iConnection.prepareStatement("select fieldid from CustomDataSpectra group by fieldid");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                iSpectrumUsedCustomDataFields.add(iCustomDataFieldsMap.get(rs.getInt("FieldID")));
            }
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return iSpectrumUsedCustomDataFields;
    }
}
