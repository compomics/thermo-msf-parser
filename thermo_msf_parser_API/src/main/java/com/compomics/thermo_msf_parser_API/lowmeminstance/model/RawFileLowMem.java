package com.compomics.thermo_msf_parser_API.lowmeminstance.model;


/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 5/30/12
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class RawFileLowMem {
    
    private final int fileId;
    private final String fileName;
    
   /**
    * <p>Constructor for RawFileLowMem.</p>
    *
    * @param fileId a int.
    * @param fileName a {@link java.lang.String} object.
    */
   public RawFileLowMem(int fileId, String fileName){
        
        this.fileId = fileId;
        this.fileName = fileName;
    
    }
    
    /**
     * <p>Getter for the field <code>fileId</code>.</p>
     *
     * @return a int.
     */
    public int getFileId(){
        return fileId;
    }
    
    /**
     * <p>Getter for the field <code>fileName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFileName() {
        return fileName;
    }

}
