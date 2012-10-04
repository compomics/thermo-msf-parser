package com.compomics.thermo_msf_parser.msf;

import java.sql.*;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 5/30/12
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class RawFileLowMem implements RawFileInterface{
    /**
     *
     * @param fileID the file id stored in the msf file
     * @param iConn connection to the msf file
     * @return hashmap connecting the file name entry to the fileID
     */
    public HashMap<Integer,String> getRawFileForFileID(int fileID,Connection iConn) {
       HashMap<Integer,String> lResult = new HashMap<Integer,String>();
        try{
            PreparedStatement stat = iConn.prepareStatement("select FileName from FileInfos where FileId = " +fileID);
            ResultSet rs = stat.executeQuery();
            while (rs.next()){
                lResult.put(fileID,rs.getString("Filename"));
            }
        } catch (SQLException sqle){sqle.printStackTrace();}
    return lResult;
    }

    /**
     *
     * @param fileID the file id stored in the msf file
     * @param iConn  connection to the msf file
     * @return a processed filename (eg sample4_1.raw)
     */
    public String getRawFileNameForFileID(int fileID, Connection iConn) {
        String lResult = "";
        try{
            Statement stat = iConn.createStatement();
            ResultSet rs = stat.executeQuery("select FileName from FileInfos where FileId = "+fileID);
            rs.next();
            lResult = rs.getString("FileName").substring(rs.getString("FileName").lastIndexOf("\\") + 1);
            rs.close();
            stat.close();
        }
        catch (SQLException sqle){
            sqle.printStackTrace();
        }
        return lResult;           
    } 
}
