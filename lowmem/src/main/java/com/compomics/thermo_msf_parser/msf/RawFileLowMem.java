package com.compomics.thermo_msf_parser.msf;


/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 5/30/12
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class RawFileLowMem {
    
    private int fileId;
    private String fileName;
    
    RawFileLowMem(int fileId, String fileName){
        
        this.fileId = fileId;
        this.fileName = fileName;
    
    }
    
    public int getFileId(){
        return fileId;
    }
    
    public String getFileName() {
        return fileName;
    }

}
