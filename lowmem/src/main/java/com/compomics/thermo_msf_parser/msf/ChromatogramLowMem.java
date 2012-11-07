package com.compomics.thermo_msf_parser.msf;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 5/7/12
 * Time: 9:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class ChromatogramLowMem implements ChromatogramInterface{

    private int fileNumber;
    private int iTraceTypeID;

    
    public int getChromatogramFileNumber(){
        return fileNumber;
    }

    public void setChromatogramFileNumber(int newFileNumber){
        this.fileNumber = newFileNumber;
    }

    public int getTraceTypeID(){
        return iTraceTypeID;
    }

    public void setTraceTypeID(int newTraceTypeID){
        this.iTraceTypeID = newTraceTypeID;
    }




}
