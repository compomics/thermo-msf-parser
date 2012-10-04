package com.compomics.thermo_msf_parser.msf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/24/12
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModificationLowMemController implements ModificationInterface {

    //TODO add createmodificationmap to addModificationsToPeptideSequence?

    /**
     *
     * @param peptide a Peptide object
     * @param modificationMap a hashmap returned from the modification method createModificationMap
     * @param iConnection connection to the SQLite db
     * @return a string containing the modified peptide sequence
     * @throws SQLException
     */

    public String addModificationsToPeptideSequence(Peptide peptide,HashMap modificationMap,Connection iConnection) throws SQLException {
        String modifiedSequence = peptide.getSequence();
        int lengthChanged = 0;
        PreparedStatement stat = iConnection.prepareStatement("select TerminalModificationID from PeptidesTerminalModifications where PeptideID ="+peptide.getPeptideId());
        ResultSet rs = stat.executeQuery();
            //do the N terminus
            if (rs.next()) {
                rs = stat.executeQuery("select Abbreviation from AminoAcidModifications where AminoAcidModificationID ="+ rs.getInt(1));
                rs.next();
                modifiedSequence =  rs.getString(1)+"-";
                lengthChanged = lengthChanged+rs.getString(1).length()+1;

            } else {
                modifiedSequence = "NH2-"+modifiedSequence;
                lengthChanged=lengthChanged+4;
            }
            //do the middle
            rs = stat.executeQuery("select Position,Abbreviation from PeptidesAminoAcidModifications,AminoAcidModifications where PeptidesAminoAcidModifications.AminoAcidModificationID = AminoAcidModificationID and PeptideID ="+peptide.getPeptideId()+"order by ASC Position");
            while(rs.next()){
                modifiedSequence = modifiedSequence.substring(0,rs.getInt(1)+lengthChanged) + "<" + rs.getString(2) + ">"+modifiedSequence.substring(rs.getInt(1)+lengthChanged+1,modifiedSequence.length());
                lengthChanged = lengthChanged+ rs.getString(2).length()+2;
            }
            //do the C terminus
            modifiedSequence = modifiedSequence + "-COOH";
        return modifiedSequence;
    }

    /**
     *
     * @param iConnection: a connection to the msf database
     * @return hashmap containing the modifications from the msf file key: modificationid value: modificationstring
     * @throws SQLException
     */
    public HashMap createModificationMap(Connection iConnection) throws SQLException {
        HashMap<Integer, String> modificationsMap = new HashMap<Integer, String>();
        PreparedStatement stat = iConnection.prepareStatement("select AminoAcidModificationID,Abbreviation from AminoAcidModifications");
        ResultSet rs = stat.executeQuery();
        while(rs.next()){
            modificationsMap.put(rs.getInt(1),rs.getString(2));
        }
        return modificationsMap;
    }
}
