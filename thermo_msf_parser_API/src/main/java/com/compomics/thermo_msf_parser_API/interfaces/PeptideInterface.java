package com.compomics.thermo_msf_parser_API.interfaces;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/23/12
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public interface PeptideInterface {

    /**
    @param  protein: a Protein object
     @param msfFile the proteome discoverer file to retrieve from
    @return a List containing all the peptides connected to the given Protein, empty if none are found
    */

    public List getPeptidesForProtein(ProteinLowMem protein,MsfFile msfFile);

   /**
   @param lProteinAccession: a string containing the accession of the protein
    @param msfFile the proteome discoverer file to retrieve from
   @return a List containing all the peptides connected to the given protein accession, empty if none are found
   */

    public List getPeptidesForAccession(String lProteinAccession,MsfFile msfFile);

    /**
    @param peptideID: the peptide ID in the sqlite database
    @param msfFile the msf file to get the data from
    @param fullInfo if the returned information should be concise or not
    @return a vector containing the different values to present in the thermo-msf-parser, empty if none are found
    */

    public List getInformationForPeptide(int peptideID,MsfFile msfFile,boolean fullInfo);

    
    /**
     * <p>getPeptidesWithConfidenceLevel.</p>
     *
     * @param confidenceLevel the confidence level of the peptides wanted
     * @param msfFile the proteome discoverer file to retrieve from
     * @return a vector containing the peptides identified and the specified confidence level, empty if none are found
     */
    public List<PeptideLowMem> getPeptidesWithConfidenceLevel(int confidenceLevel,MsfFile msfFile);
        

    /**
     * <p>getNumberOfPeptidesForConfidenceLevel.</p>
     *
     * @param confidenceLevel the confidence level of the peptides wanted
     * @param msfFile the proteome discoverer file to retrieve from
     * @return the number of peptides at a given confidence level for a proteome discoverer file.
     */
    public int getNumberOfPeptidesForConfidenceLevel(int confidenceLevel,MsfFile msfFile);
    
    /**
     * <p>getPeptidesForProteinList.</p>
     *
     * @param proteinLowMemList a vector containing the protein objects we want to retrieve the peptides for
     * @param msfFile the proteome discoverer file to retrieve from
     * @param confidenceLevel the confidence level we want to retrieve the peptides at
     */
    public void getPeptidesForProteinList(List<ProteinLowMem> proteinLowMemList,MsfFile msfFile,int confidenceLevel);
    
    /**
     * <p>returnNumberOfPeptides.</p>
     *@param msfFile the proteome discoverer file to retrieve from
     * @return the number of peptides in the proteome discoverer.
     */
    public int returnNumberOfPeptides(MsfFile msfFile);
}
