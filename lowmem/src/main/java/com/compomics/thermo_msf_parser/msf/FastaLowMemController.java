/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser.msf;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Davy
 */
public class FastaLowMemController {

    public ArrayList<String> getFastaFileNames(Connection msfFileConnection) {
        ArrayList<String> iFastaFiles = new ArrayList<String>();
        try {
            Statement stat = msfFileConnection.createStatement();
            ResultSet rs = stat.executeQuery("select VirtualFileName as file from fastafiles");
            while (rs.next()) {
                iFastaFiles.add(rs.getString("file"));
            }
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            Logger.getLogger(FastaLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return iFastaFiles;
    }

    public int getNumberOfProteinsInFastaFile(Connection msfFileConnection,String fastaFileName) {
        int numberOfProteins = 0;
        try {
            Statement stat = msfFileConnection.createStatement();
            ResultSet rs = stat.executeQuery("select NumberOfProteins from fastafiles where VirtualFileName = "+fastaFileName);
            rs.next();
            numberOfProteins =rs.getInt("NumberOfProteins");
        } catch (SQLException ex) {
            Logger.getLogger(FastaLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numberOfProteins;
    }
    
    public int getNumberOfAminoAcidsInFastaFile(Connection msfFileConnection, String fastaFileName) {
            int numberOfAminoAcids = 0;
        try {
            Statement stat = msfFileConnection.createStatement();
            ResultSet rs = stat.executeQuery("select NumberOfAminoAcids from fastafiles where VirtualFileName = "+fastaFileName);
            rs.next();
            numberOfAminoAcids =rs.getInt("NumberOfAminoAcids");
        } catch (SQLException ex) {
            Logger.getLogger(FastaLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numberOfAminoAcids;
    }
}
