package com.compomics.thermo_msf_parser.gui;

import com.compomics.thermo_msf_parser.msf.AminoAcid;
import com.compomics.thermo_msf_parser.msf.MsfVersion;
import org.apache.log4j.Logger;

import java.io.File;
import java.sql.*;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 4/25/12 Time: 3:36 PM To change
 * this template use File | Settings | File Templates.
 */
public class MsfFile {
    
    private static final Logger logger = Logger.getLogger(MsfFile.class);

    private File msfFile;
    private Connection iConnection;
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


    public Vector<AminoAcid> getAminoAcids(){
        try {
            PreparedStatement stat = iConnection.prepareStatement("select * from AminoAcids order by AminoAcidID");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                AminoAcid aminoAcid = new AminoAcid(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getString(7));
                iAminoAcid.add(aminoAcid);
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            logger.error(e);
        }
        return iAminoAcid;
    }


    public MsfVersion getVersion() {
        MsfVersion iMsfVersion = MsfVersion.VERSION1_2;
        try {
            Statement stat = getConnection().createStatement();
            ResultSet rs = stat.executeQuery("select * from SchemaInfo");
            while (rs.next()) {
                String lVersion = rs.getString("SoftwareVersion");
                if (lVersion.startsWith("1.2")) {
                    iMsfVersion = MsfVersion.VERSION1_2;
                } else if (lVersion.startsWith("1.3")) {
                    iMsfVersion = MsfVersion.VERSION1_3;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return iMsfVersion;
    }
}
