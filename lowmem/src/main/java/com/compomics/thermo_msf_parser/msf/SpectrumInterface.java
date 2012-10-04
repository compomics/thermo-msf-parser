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
     * @param peptideID the ID of the peptide the XML file is needed from in the Msf File
     * @param iConnection an SQLite Connection to the Msf File
     * @return a String containing the unzipped spectrum XML
     * @throws SQLException Exception thrown if something went wrong with the retrieving of the zipped XML File
     * @throws IOException Exception thrown if something went wrong with the unzipping of the XML File
     */
    public String createSpectrumXMLForPeptide(int peptideID,Connection iConnection) throws SQLException, IOException;

    /**
     *
     * @param lXml the unzipped XML spectrum file to retrieve the peaks from
     * @return Vector with all the MS/MS peaks
     * @throws Exception
     */

    public Vector<Peak> getMSMSPeaks(String lXml) throws Exception;

    /**
     * @param lXml the unzipped XML spectrum file to retrieve the peaks from
     * @return Vector with all the MS peaks
     * @throws Exception
     */

    public Vector<Peak> getMSPeaks(String lXml) throws Exception;

    /**

     @param lXml the unzipped XML spectrum file to retrieve the peaks from
     @return the fragmented MS peak from the XML file created with the createSpectrumXMLForPeptide method
     @throws Exception
     */
    public Peak getFragmentedMsPeak(String lXml) throws Exception;

    /**
     *get a Spectrum object for a given Peptide object
     @param peptideOfInterestID: a Peptide object
     @param aConnectionToMsfFile SQLite connection to the Msf File where the peptide is stored
     @return a Spectrum object
     @throws SQLException if something went wrong with the retrieving
     */

    //public Spectrum getSpectrumForPeptideID(int peptideOfInterestID,Connection aConnectionToMsfFile) throws SQLException;
}
