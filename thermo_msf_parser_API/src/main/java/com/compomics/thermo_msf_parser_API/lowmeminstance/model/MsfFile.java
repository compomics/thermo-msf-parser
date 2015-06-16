package com.compomics.thermo_msf_parser_API.lowmeminstance.model;

import com.compomics.thermo_msf_parser_API.enums.MsfVersion;
import com.compomics.thermo_msf_parser_API.highmeminstance.AminoAcid;
import org.apache.log4j.Logger;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 4/25/12 Time: 3:36 PM To change
 * this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class MsfFile {

    private static final Logger logger = Logger.getLogger(MsfFile.class);
    private final File msfFile;
    private Connection iConnection;
    private List<AminoAcid> iAminoAcid = new ArrayList<AminoAcid>();
    private MsfVersion iMsfVersion;

    /**
     * <p>Constructor for MsfFile.</p>
     *
     * @param aMsfFile a {@link java.io.File} object.
     * @throws java.lang.ClassNotFoundException if any.
     * @throws java.sql.SQLException if any.
     */
    public MsfFile(File aMsfFile) throws ClassNotFoundException, SQLException {
        this.msfFile = aMsfFile;
        Class.forName("org.sqlite.JDBC");
        this.iConnection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", msfFile.getAbsolutePath()));
    }

    /**
     * <p>Getter for the field <code>msfFile</code>.</p>
     *
     * @return a {@link java.io.File} object.
     */
    public File getMsfFile() {
        return msfFile;
    }

    /**
     * <p>getConnection.</p>
     *
     * @return a {@link java.sql.Connection} object.
     */
    public Connection getConnection() {
        return iConnection;
    }

    /**
     * <p>getAminoAcids.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<AminoAcid> getAminoAcids() {
        if (iAminoAcid.isEmpty()) {
            try {
                PreparedStatement stat = null;
                try {
                    stat = this.iConnection.prepareStatement("select * from AminoAcids order by AminoAcidID");
                    ResultSet rs = stat.executeQuery();
                    try {
                        while (rs.next()) {
                            AminoAcid aminoAcid = new AminoAcid(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getString(7));
                            iAminoAcid.add(aminoAcid);
                        }
                    } finally {
                        rs.close();
                    }
                } finally {
                    if (stat != null) {
                        stat.close();
                    }
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return iAminoAcid;
    }

    /**
     * <p>getVersion.</p>
     *
     * @return a {@link com.compomics.thermo_msf_parser_API.enums.MsfVersion} object.
     */
    public MsfVersion getVersion() {
        if (iMsfVersion == null) {
            try {
                PreparedStatement stat = null;
                try {
                    stat = getConnection().prepareStatement("select * from SchemaInfo");
                    ResultSet rs = stat.executeQuery();
                    try {
                        while (rs.next()) {
                            String lVersion = rs.getString("SoftwareVersion");
                            for (MsfVersion version : MsfVersion.values()) {
                                if (lVersion.startsWith(version.getVersion())) {
                                    iMsfVersion = version;
                                    break;
                                }
                            }
                        }
                    } finally {
                        rs.close();
                    }
                } finally {
                    if (stat != null) {
                        stat.close();
                    }
                }
            } catch (SQLException ex) {
                logger.error(ex);
            }
        }
        return iMsfVersion;
    }
}
