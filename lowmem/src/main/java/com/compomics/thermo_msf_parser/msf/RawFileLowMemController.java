/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser.msf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 *
 * @author Davy
 */
public class RawFileLowMemController implements RawFileInterface {
    private static final Logger logger = Logger.getLogger(RawFileLowMemController.class);
    /**
     *
     * @param fileID the file id stored in the msf file
     * @param aConnection connection to the msf file
     * @return hashmap connecting the file name entry to the fileID
     */
    @Override
    public HashMap<Integer, String> getRawFileForFileID(int fileID, Connection aConnection) {
        HashMap<Integer, String> lResult = new HashMap<Integer, String>();
        try {
            PreparedStatement stat = aConnection.prepareStatement("select FileName from FileInfos where FileId = " + fileID);
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                lResult.put(fileID, rs.getString("Filename"));
            }
            rs.close();
            stat.close();
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return lResult;
    }

    /**
     *
     * @param aConnection connection to the msf file
     * @return a vector containing all the names of the stored raw files
     */
    public Vector<RawFileLowMem> getRawFileNames(Connection aConnection) {
        Vector<RawFileLowMem> rawFiles = new Vector<RawFileLowMem>();
        try {
            PreparedStatement stat = aConnection.prepareStatement("select FileId,FileName from FileInfos");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                rawFiles.add(new RawFileLowMem(rs.getInt("FileId"), rs.getString("FileName")));
            }
            rs.close();
            stat.close();
        } catch (SQLException sqle) {
            logger.error(sqle);
        }

        return rawFiles;

    }

    /**
     *
     * @param fileID the file id stored in the msf file
     * @param aConnection connection to the msf file
     * @return a processed filename (eg sample4_1.raw)
     */
    @Override
    public String getRawFileNameForFileID(int fileID, Connection aConnection) {
        String lResult = "";
        try {
            Statement stat = aConnection.createStatement();
            ResultSet rs = stat.executeQuery("select FileName from FileInfos where FileId = " + fileID);
            rs.next();
            lResult = rs.getString("FileName").substring(rs.getString("FileName").lastIndexOf("\\") + 1);
            rs.close();
            stat.close();
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return lResult;
    }
}
