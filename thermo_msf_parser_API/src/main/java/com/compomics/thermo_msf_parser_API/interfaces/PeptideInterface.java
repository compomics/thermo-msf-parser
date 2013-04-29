package com.compomics.thermo_msf_parser_API.interfaces;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem;
import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/23/12
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PeptideInterface {

    /**
    @param  protein: a Protein object
    @param  iMsfVersion: enumeration object containing the version number of the current Msf file
    @param iAminoAcids a List containing the objects returned from the AminoAcid
    @return a List containing all the peptides connected to the given Protein, empty if none are found
    */
    
    public List getPeptidesForProtein(ProteinLowMem protein,MsfFile msfFile);

   /**
   @param lProteinAccession: a string containing the accession of the protein
   @param msfFileConnection: a connection to the SQLite database
   @param iMsfVersion: enumeration object containing the version number of the current Msf file
   @param iAminoAcids a List containing the objects returned from the AminoAcid
   @return a List containing all the peptides connected to the given protein accession, empty if none are found
   */

    public List getPeptidesForAccession(String lProteinAccession,MsfFile msfFile);

    /**
    @param peptideID: the peptide ID in the sqlite database
    @param msfFileConnection: a connection to the SQLite database
    @param fullInfo if the returned information should be concise or not
    @return a vector containing the different values to present in the thermo-msf-parser, empty if none are found
    */

    public List getInformationForPeptide(int peptideID,Connection msfFileConnection,boolean fullInfo);

    
     /**
     *
     * @param confidenceLevel the confidence level of the peptides wanted
     * @param msfFileConnection connection to the msf file
     * @param iMsfVersion the version with which the msf file is made
     * @param iAminoAcids vector with the amino acids fetched from the amino acid lowmem class
     * @return a vector containing the peptides identified and the specified confidence level, empty if none are found
     */
    public List<PeptideLowMem> getPeptidesWithConfidenceLevel(int confidenceLevel,MsfFile msfFile);
        

    /**
     * @param confidenceLevel the confidence level of the peptides wanted
     * @param aConnection connection to the msf file
     */
    
    public int getNumberOfPeptidesForConfidenceLevel(int confidenceLevel,MsfFile msfFile);
    
    /**
     * @param proteinLowMemList a vector containing the protein objects we want to retrieve the peptides for
     * @param msfFileConnection a connection to the msf file
     * @param iAminoAcids a vector of the amino acids retrieved from the msf file
     * @param iMsfVersion the msf file version
     * @param confidenceLevel the confidence level we want to retrieve the peptides at
     */
    public void getPeptidesForProteinList(List<ProteinLowMem> proteinLowMemList,MsfFile msfFile,int confidenceLevel);
    
    /**
     * @param proteinLowMemList a vector containing the protein objects we want to retrieve the peptides for
     * @param msfFileConnection a connection to the msf file
     * @param iAminoAcids a vector of the amino acids retrieved from the msf file
     * @param iMsfVersion the msf file version
     */
    public void getPeptidesForProteinList(List<ProteinLowMem> proteinLowMemList,MsfFile msfFile);
    
    /**
     * 
     * @param msfFileConnection connection to the msf file
     */
    
    public int returnNumberOfPeptides(MsfFile msfFile);
}
