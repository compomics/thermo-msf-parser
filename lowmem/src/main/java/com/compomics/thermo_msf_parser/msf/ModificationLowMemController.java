package com.compomics.thermo_msf_parser.msf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/24/12
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModificationLowMemController implements ModificationInterface {


    public String addModificationsToPeptideSequence(Peptide peptide,HashMap modificationMap,Connection iConnection) {
        String modifiedSequence = peptide.getSequence();
        try {
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
                    lengthChanged+=4;
                }
                //do the middle
                rs = stat.executeQuery("select Position,Abbreviation from PeptidesAminoAcidModifications,AminoAcidModifications where PeptidesAminoAcidModifications.AminoAcidModificationID = AminoAcidModificationID and PeptideID ="+peptide.getPeptideId()+"order by ASC Position");
                while(rs.next()){
                    modifiedSequence = modifiedSequence.substring(0,rs.getInt(1)+lengthChanged) + "<" + rs.getString(2) + ">"+modifiedSequence.substring(rs.getInt(1)+lengthChanged+1,modifiedSequence.length());
                    lengthChanged = lengthChanged+ rs.getString(2).length()+2;
                }
                //do the C terminus
                modifiedSequence += "-COOH";
                rs.close();
                stat.close();
        } catch (SQLException ex) {
            Logger.getLogger(ModificationLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return modifiedSequence;
    }


    public HashMap<Integer, String> createModificationMap(Connection iConnection){
        HashMap<Integer, String> modificationsMap = new HashMap<Integer, String>();
        try {
            PreparedStatement stat = iConnection.prepareStatement("select AminoAcidModificationID,Abbreviation from AminoAcidModifications");
            ResultSet rs = stat.executeQuery();
            while(rs.next()){
                modificationsMap.put(rs.getInt(1),rs.getString(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModificationLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return modificationsMap;
    }
}
