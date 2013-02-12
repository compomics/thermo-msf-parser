package com.compomics.thermo_msf_parser.msf;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/23/12
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SpectrumInterface {

    
        /**
     *
     * @param peptideID the id of the peptide we want the spectrum xml for
     * @param aConnection connection to the msf file
     * @return the unzipped XML file in a string object
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    
    public String createSpectrumXMLForPeptide(int peptideID,Connection aConnection) throws SQLException, IOException;

    /**
     *
     * @param lXml the unzipped XML spectrum file to retrieve the peaks from
     * @return Vector with all the MS/MS peaks
     * @throws Exception
     */

    public Vector<Peak> getMSMSPeaks(String lXml);

    /**
     * @param lXml the unzipped XML spectrum file to retrieve the peaks from
     * @return Vector with all the MS peaks
     * @throws Exception
     */

    public Vector<Peak> getMSPeaks(String lXml);

    /**

     @param lXml the unzipped XML spectrum file to retrieve the peaks from
     @return the fragmented MS peak from the XML file created with the createSpectrumXMLForPeptide method
     @throws Exception
     */
    public Peak getFragmentedMsPeak(String lXml);

    /**
     * 
     * @param peptideOfInterestID the id of the peptide of which we want the spectrum from 
     * @param aConnection a connection to the msf file
     * @return a spectrum object
     */
    public SpectrumLowMem getSpectrumForPeptideID(int peptideOfInterestID,Connection aConnection);
    
    
    /**
     *
     * @param rawFileName the raw file name connected to the spectrum
     * @param lspectrum the spectrum object we want the title from
     * @return a processed title
     */
    public String getSpectrumTitle(String rawFileName, SpectrumLowMem lspectrum);
    
     /**
     *
     * @param lSpectrum the spectrum to which we want to add the zipped xml spectrum file
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public void createSpectrumXMLForSpectrum(SpectrumLowMem lSpectrum) throws SQLException, IOException;
    
    /**
     * fetches the zipped xml (if not yet added to the spectrum object) ,unzips it and adds it to the spectrum object
     * @param spectrum the spectrum of which we want the unzipped xml file
     */
    public void unzipXMLforSpectrum(SpectrumLowMem spectrum);
    

}
