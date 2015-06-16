package com.compomics.thermo_msf_parser_API.interfaces;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/23/12
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public interface ChromatogramInterface {
    
    /**
     * returns the file number for the chromatogram stored in the SQLite db
     *
     * @return the chromatogram file number
     */
    public int getChromatogramFileNumber();

    /**
     * sets a new File number for the chromatogram stored in the SQLite db
     *
     * @param newFileNumber the new file number
     */
    public void setChromatogramFileNumber(int newFileNumber);
    
    /**
     * returns the Tracetype id for the chromatogram
     *
     * @return the TraceType id
     */
    public int getTraceTypeID();
    
    /**
     * sets a new TraceTypeID for the chromatogram
     *
     * @param newTraceTypeID the new TraceType id for the chromatogram
     */
    public void setTraceTypeID(int newTraceTypeID);
}
