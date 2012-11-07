package com.compomics.thermo_msf_parser.gui;

import com.compomics.thermo_msf_parser.msf.AminoAcid;

import java.io.File;
import java.sql.*;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/25/12
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class MsfFile{

    private File msfFile;
    private static Connection iConnection;
    private Vector<AminoAcid> iAminoAcid = new Vector<AminoAcid>();

    public MsfFile(File aMsfFile) throws ClassNotFoundException, SQLException {
        this.msfFile = aMsfFile;
        Class.forName("org.sqlite.JDBC");
        this.iConnection = DriverManager.getConnection("jdbc:sqlite:" + msfFile.getAbsolutePath());
    }

    public File getMsfFile() {
        return msfFile;
    }

    public void setMsfFile(File msfFile) {
        this.msfFile = msfFile;
    }

    public Connection getConnection() {
        return iConnection;
    }

    public void setConnection(Connection iConnection) {
        this.iConnection = iConnection;
    }

    public Vector<AminoAcid> getAminoAcids() throws SQLException {
    try {
        PreparedStatement stat = iConnection.prepareStatement("select * from AminoAcids");
        ResultSet rs = stat.executeQuery();
        while(rs.next()){
            AminoAcid aminoAcid = new AminoAcid(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getString(7));
            iAminoAcid.add(aminoAcid);
        }
        rs.close();
        stat.close();
    } catch (Exception e){
            e.printStackTrace();
        }
        return iAminoAcid;
    }

    public void setAminoAcids(Vector<AminoAcid> aAminoAcidVector){
        this.iAminoAcid = aAminoAcidVector;
    }
}
