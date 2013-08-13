package com.compomics.thermo_msf_parser_API.interfaces;

import com.compomics.thermo_msf_parser_API.highmeminstance.Peak;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.SpectrumLowMem;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
     * @param lXml the unzipped XML spectrum file to retrieve the peaks from
     * @return List with all the MS/MS peaks
     * @throws IndexOutOfBoundsException if the xml file could not be properly read
     */

    public List<Peak> getMSMSPeaks(String lXml) throws IndexOutOfBoundsException;

    /**
     * @param lXml the unzipped XML spectrum file to retrieve the peaks from
     * @return List with all the MS peaks
     * @throws IndexOutOfBoundsException if the xml file could not be properly read
     */

    public List<Peak> getMSPeaks(String lXml) throws IndexOutOfBoundsException;

    /**

     @param lXml the unzipped XML spectrum file to retrieve the peaks from
     @return the fragmented MS peak from the XML file created with the createSpectrumXMLForPeptide method
     * @throws IndexOutOfBoundsException if the xml file could not be properly read
     */
    public Peak getFragmentedMsPeak(String lXml) throws IndexOutOfBoundsException;

   
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
    public void createSpectrumXMLForSpectrum(SpectrumLowMem lSpectrum, MsfFile msfFile) throws SQLException, IOException;
    
    /**
     * fetches the zipped xml (if not yet added to the spectrum object), unzips it and adds it to the spectrum object
     * @param spectrum the spectrum of which we want the unzipped xml file
     */
    public boolean unzipXMLforSpectrum(SpectrumLowMem spectrum);
    

}
