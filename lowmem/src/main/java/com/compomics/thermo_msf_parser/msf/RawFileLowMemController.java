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

/**
 *
 * @author Davy
 */
public class RawFileLowMemController implements RawFileInterface{
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
    
    public Vector<RawFileLowMem> getRawFileNames(Connection iConn){
        Vector<RawFileLowMem> rawFiles = new Vector<RawFileLowMem>();
        try{
        PreparedStatement stat = iConn.prepareStatement("select FileId,FileName from FileInfos");
        ResultSet rs = stat.executeQuery();
            while(rs.next()){
                rawFiles.add(new RawFileLowMem(rs.getInt("FileId"),rs.getString("FileName")));
            }
        }catch(SQLException sqle) {
        sqle.printStackTrace();
        }
        
        return rawFiles;
        
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
