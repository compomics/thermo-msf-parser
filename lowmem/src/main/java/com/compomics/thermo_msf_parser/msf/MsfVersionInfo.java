package com.compomics.thermo_msf_parser.msf;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 21-Feb-2011
 * Time: 09:18:47
 */
public class MsfVersionInfo {
    // Class specific log4j logger for Thermo_msf_parserGUI instances.
	 private static Logger logger = Logger.getLogger(MsfVersionInfo.class);
    /**
     * The schema version
     */
    private int iSchemaVersion;
    /**
     * The software version
     */
    private String iSoftwareVersion;

    /**
     * The MsfVersionInfo constructor
     * @param iSchemaVersion The schema version
     * @param iSoftwareVersion The software version
     */
    public MsfVersionInfo(int iSchemaVersion, String iSoftwareVersion) {
        this.iSchemaVersion = iSchemaVersion;
        this.iSoftwareVersion = iSoftwareVersion;
    }


    /**
     * Getter for the schema version
     * @return int with the schema version
     */
    public int getSchemaVersion() {
        return iSchemaVersion;
    }

    /**
     * Getter for the software version
     * @return String with software version
     */
    public String getSoftwareVersion() {
        return iSoftwareVersion;
    }
}
