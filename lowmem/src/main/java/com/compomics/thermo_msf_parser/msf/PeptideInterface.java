package com.compomics.thermo_msf_parser.msf;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

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
    @param iAminoAcids a Vector containing the objects returned from the AminoAcid
    @return a Vector containing all the peptides connected to the given Protein, empty if none are found
    */
    
    public Vector getPeptidesForProtein(ProteinLowMem protein,MsfVersion iMsfVersion,Vector<AminoAcid> iAminoAcids) throws SQLException;

   /**
   @param lProteinAccession: a string containing the accession of the protein
   @param aConnection: a connection to the SQLite database
   @param iMsfVersion: enumeration object containing the version number of the current Msf file
   @param iAminoAcids a Vector containing the objects returned from the AminoAcid
   @return a Vector containing all the peptides connected to the given protein accession, empty if none are found
   */

    public Vector getPeptidesForAccession(String lProteinAccession,MsfVersion iMsfVersion,Connection aConnection,Vector<AminoAcid> iAminoAcids);

    /**
    @param peptideID: the peptide ID in the sqlite database
    @param aConnection: a connection to the SQLite database
    @param fullInfo if the returned information should be concise or not
    @return a vector containing the different values to present in the thermo-msf-parser, empty if none are found
    */

    public Vector getInformationForPeptide(int peptideID,Connection aConnection,boolean fullInfo);

    
     /**
     *
     * @param confidenceLevel the confidence level of the peptides wanted
     * @param aConnection connection to the msf file
     * @param iMsfVersion the version with which the msf file is made
     * @param iAminoAcids vector with the amino acids fetched from the amino acid lowmem class
     * @return a vector containing the peptides identified and the specified confidence level, empty if none are found
     */
    public Vector<PeptideLowMem> getPeptidesWithConfidenceLevel(int confidenceLevel, Connection aConnection, MsfVersion iMsfVersion, Vector<AminoAcid> iAminoAcids);
        

    /**
     * @param confidenceLevel the confidence level of the peptides wanted
     * @param aConnection connection to the msf file
     */
    
    public int getNumberOfPeptidesForConfidenceLevel(int confidenceLevel, Connection aConnection);
    
    /**
     * @param proteinLowMemVector a vector containing the protein objects we want to retrieve the peptides for
     * @param aConnection a connection to the msf file
     * @param iAminoAcids a vector of the amino acids retrieved from the msf file
     * @param iMsfVersion the msf file version
     * @param confidenceLevel the confidence level we want to retrieve the peptides at
     */
    public void getPeptidesForProteinVector(Vector<ProteinLowMem> proteinLowMemVector, Connection aConnection, Vector<AminoAcid> iAminoAcids, MsfVersion iMsfVersion,int confidenceLevel);
    
    /**
     * @param proteinLowMemVector a vector containing the protein objects we want to retrieve the peptides for
     * @param aConnection a connection to the msf file
     * @param iAminoAcids a vector of the amino acids retrieved from the msf file
     * @param iMsfVersion the msf file version
     */
    public void getPeptidesForProteinVector(Vector<ProteinLowMem> proteinLowMemVector, Connection aConnection, Vector<AminoAcid> iAminoAcids, MsfVersion iMsfVersion);
    
}
