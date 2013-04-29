package com.compomics.thermo_msf_parser_API.highmeminstance;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 21-Feb-2011
 * Time: 13:07:51
 */

/**
 * This class represents the raw file 
 */
public class RawFile {


    /**
     * The file id
     */
    private int iFileId;
    /**
     * The file name
     */
    private String iFileName;

    /**
     * The constructor for the raw file
     * @param aFileId The file id
     * @param aFileName The file name
     */
    public RawFile(int aFileId, String aFileName) {
        this.iFileId = aFileId; 
        this.iFileName = aFileName;
    }


    /**
     * Getter for the file id
     * @return int with the file id
     */
    public int getFileId() {
        return iFileId;
    }

    /**
     * Getter for the file name
     * @return String with the file name
     */
    public String getFileName() {
        return iFileName;
    }
}
