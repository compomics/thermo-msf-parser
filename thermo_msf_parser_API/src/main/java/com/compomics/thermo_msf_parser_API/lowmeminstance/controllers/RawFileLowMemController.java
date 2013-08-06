/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.interfaces.RawFileInterface;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.RawFileLowMem;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Davy
 */
public class RawFileLowMemController implements RawFileInterface {

    private static final Logger logger = Logger.getLogger(RawFileLowMemController.class);

    @Override
    public HashMap<Integer, String> getRawFileForFileID(int fileID, MsfFile msfFile) {
        HashMap<Integer, String> lResult = new HashMap<Integer, String>();
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select FileName from FileInfos where FileId = ?");
                stat.setInt(1, fileID);
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        lResult.put(fileID, rs.getString("Filename"));
                    }
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return lResult;
    }

    @Override
    public List<RawFileLowMem> getRawFileNames(MsfFile msfFile) {
        List<RawFileLowMem> rawFiles = new ArrayList<RawFileLowMem>();
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select FileId,FileName from FileInfos");
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        rawFiles.add(new RawFileLowMem(rs.getInt("FileId"), rs.getString("FileName")));
                    }
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }

        return rawFiles;

    }

    @Override
    public String getRawFileNameForFileID(int fileID, MsfFile msfFile) {
        String lResult = "";
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select FileName from FileInfos where FileId = ?");
                stat.setInt(1, fileID);
                ResultSet rs = stat.executeQuery();
                try {
                    rs.next();
                    lResult = rs.getString("FileName").substring(rs.getString("FileName").lastIndexOf("\\") + 1);
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return lResult;
    }
}
