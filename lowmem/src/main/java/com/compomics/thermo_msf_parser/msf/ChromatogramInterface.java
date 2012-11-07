package com.compomics.thermo_msf_parser.msf;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/23/12
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ChromatogramInterface {
    
    /**
     * returns the file number for the chromatogram stored in the SQLite db
     * @return the chromatogram file number
     */
    
    public int getChromatogramFileNumber();

    /**
     * sets a new File number for the chromatogram stored in the SQLite db
     * @param newFileNumber the new file number
     */
    
    public void setChromatogramFileNumber(int newFileNumber);
    
    /**
    * returns the iTracetype id for the chromatogram
    * @return the iTraceType id
    */
    
    public int getiTraceTypeID();
    
    /**
     * sets a new iTraceTypeID for the chromatogram
     * @param newTraceTypeID the new TraceType id for the chromatogram
     */
    
    public void setiTraceTypeID(int newTraceTypeID);
}