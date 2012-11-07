package com.compomics.thermo_msf_parser.msf;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 5/2/12
 * Time: 11:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class CustomDataLowMemController {
    private HashMap<Integer,CustomDataField> customDataFieldHashMap = new HashMap<Integer, CustomDataField>();

    /**
     * retriever for the Custom data fields
     * @param iConnection a connection to the msf file
     * @return a hashmap containing the custom fields key: fieldid in the db value: displayname given in the db
     */
    public HashMap<Integer,CustomDataField> getCustomFieldMap(Connection iConnection){
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
            Logger.getLogger(CustomDataLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customDataFieldHashMap;
    }

    /**
     * retriever for the custom peptide data
     * @param iConnection a connection to the msf file
     * @return a hashmap with the custom peptide data key: proteinid value:hashmap with key:the custom field id value: the field value
     */
    public ArrayList<CustomDataField> getCustomPeptideData(HashMap<Integer,CustomDataField> customData,Connection iConnection){
        ArrayList<CustomDataField> iPeptideUsedCustomDataFields = new ArrayList<CustomDataField>();
        try {
            PreparedStatement stat = iConnection.prepareStatement("select FieldID,FieldValue from CustomDataPeptides where group by FieldID");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                iPeptideUsedCustomDataFields.add(customData.get(rs.getInt("FieldID")));
            }
            rs.close();
            stat.close();    
        } catch (SQLException ex) {
            Logger.getLogger(CustomDataLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return iPeptideUsedCustomDataFields;
        }

    /**
     * adds the custom data of a protein to that protein object
     * @param protein the protein (low memory instance) object
     * @param iConnection connection to the msf file
     * @throws SQLException
     */

    public void getCustomProteinsData(ProteinLowMem protein, Connection iConnection){
        try {
            PreparedStatement stat = iConnection.prepareStatement("select FieldValue,FieldID from CustomDataProteins where ProteinID = "+ protein.getProteinID());
            ResultSet rs = stat.executeQuery();
        while (rs.next()) {
            protein.addCustomDataField(rs.getInt(2),rs.getString(1));
        }
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            Logger.getLogger(CustomDataLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * retriever for the custom spectra data stored in the SQLite db
     * @param iConnection connection to the msf file
     * @return an arraylist with the custom spectra data
     * @throws SQLException
     */
    public ArrayList<CustomDataField> getCustomSpectraData(HashMap<Integer, CustomDataField> iCustomDataFieldsMap,Connection iConnection) throws SQLException {


        ArrayList<CustomDataField> iSpectrumUsedCustomDataFields = new ArrayList<CustomDataField>();
        PreparedStatement stat = iConnection.prepareStatement("select fieldid from CustomDataSpectra group by fieldid");
        ResultSet rs = stat.executeQuery();
        while (rs.next()) {
            iSpectrumUsedCustomDataFields.add(iCustomDataFieldsMap.get(rs.getInt("FieldID")));
        }
        rs.close();
        stat.close();
        return iSpectrumUsedCustomDataFields;
    }
}

