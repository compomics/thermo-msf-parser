package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Davy
 */
public class FastaLowMemController {

    private static final Logger logger = Logger.getLogger(FastaLowMemController.class);

    /**
     * get the file names for the fasta files from the proteome discoverer file
     *
     * @param msfFile the proteome discoverer file to get the names for
     * @return a {@code List} containing the file names
     */
    public List<String> getFastaFileNames(MsfFile msfFile) {
        List<String> iFastaFiles = new ArrayList<String>();
        try {
            PreparedStatement stat = msfFile.getConnection().prepareStatement("select FileName as file from fastafiles");
            try {
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        iFastaFiles.add(rs.getString("file"));
                    }
                } finally {
                    rs.close();
                }
            } finally {
                stat.close();
            }
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return iFastaFiles;
    }

    /**
     * gets the virtual file names from the proteome discoverer file
     *
     * @param msfFile the proteome discoverer file to fetch from
     * @return a {@code List} containing the virtual names of the fasta file
     */
    public List<String> getVirtualFastaFileNames(MsfFile msfFile) {
        List<String> iVirtualFastaFiles = new ArrayList<String>();
        try {
            PreparedStatement stat = msfFile.getConnection().prepareStatement("select VirtualFileName as file from fastafiles");
            try {
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        iVirtualFastaFiles.add(rs.getString("file"));
                    }
                } finally {
                    rs.close();
                }
            } finally {
                stat.close();
            }
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return iVirtualFastaFiles;
    }

    /**
     * gets the number of proteins recorded in the fasta file that is stored in
     * the proteome discoverer file
     *
     * @param msfFile the proteome discoverer file to fetch from
     * @param fastaFileName the file name to get the number of proteins for
     * @return the number of proteins for the fasta file name as stored in the proteome discoverer file
     * filename
     */
    public int getNumberOfProteinsInFastaFile(MsfFile msfFile, String fastaFileName) {
        int numberOfProteins = 0;
        try {
            PreparedStatement stat = msfFile.getConnection().prepareStatement("select NumberOfProteins from fastafiles where FileName = ?");
            try {
                stat.setString(1, fastaFileName);
                ResultSet rs = stat.executeQuery();
                try {
                    rs.next();
                    numberOfProteins = rs.getInt("NumberOfProteins");
                } finally {
                    rs.close();
                }
            } finally {
                stat.close();
            }
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return numberOfProteins;
    }

    /**
     * returns the number of amino acids recorded in the fasta file in the proteome discoverer file
     * @param msfFile the proteome discoverer file to fetch from
     * @param fastaFileName the file name to fetch for
     * @return the number of amino acids in the fasta file as stored in the proteome discoverer file
     */
    public int getNumberOfAminoAcidsInFastaFile(MsfFile msfFile, String fastaFileName) {
        int numberOfAminoAcids = 0;
        try {
            PreparedStatement stat = msfFile.getConnection().prepareStatement("select NumberOfAminoAcids from fastafiles where FileName = ?");
            try {
                stat.setString(1, fastaFileName);
                ResultSet rs = stat.executeQuery();
                try {
                    rs.next();
                    numberOfAminoAcids = rs.getInt("NumberOfAminoAcids");
                } finally {
                    rs.close();
                }
            } finally {
                stat.close();
            }
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return numberOfAminoAcids;
    }
}
