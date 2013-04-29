package com.compomics.thermo_msf_parser_API.lowmeminstance.model;

import com.compomics.thermo_msf_parser_API.interfaces.ChromatogramInterface;


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

    
    @Override
    public int getChromatogramFileNumber(){
        return fileNumber;
    }

    @Override
    public void setChromatogramFileNumber(int newFileNumber){
        this.fileNumber = newFileNumber;
    }

    @Override
    public int getTraceTypeID(){
        return iTraceTypeID;
    }

    @Override
    public void setTraceTypeID(int newTraceTypeID){
        this.iTraceTypeID = newTraceTypeID;
    }




}
