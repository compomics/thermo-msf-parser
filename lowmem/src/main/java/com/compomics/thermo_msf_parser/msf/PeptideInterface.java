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
    query the msf file for the peptides of a given Protein object

    @param  protein: a Protein object
    @param  iMsfVersion: enmueration object containing the version number of the current Msf file
    @param iAminoAcids a Vector containing the objects returned from the AminoAcid
    @return a Vector containing all the peptides connected to the given Protein
    @throws SQLException if something went wrong with the retrieving
    */

    public Vector getPeptidesForProtein(ProteinLowMem protein,MsfVersion iMsfVersion,Vector<AminoAcid> iAminoAcids) throws SQLException;

   /**
   query the msf file for the peptides of a given Protein object

   @param lProteinAccession: a string containing the accession of the protein
   @param iConnection: a connection to the SQLite database
   @param iMsfVersion: enumeration object containing the version number of the current Msf file
   @param iAminoAcids a Vector containing the objects returned from the AminoAcid
   @return a Vector containing all the peptides connected to the given protein accession
   @throws SQLException if something went wrong with the retrieving
   */

    public Vector getPeptidesForAccession(String lProteinAccession,MsfVersion iMsfVersion,Connection iConnection,Vector<AminoAcid> iAminoAcids) throws SQLException;

    /**
    get the information relevant to the peptide from the msf file

    @param peptideID: the peptide ID in the sqlite database
    @param iConnection: a connection to the SQLite database
    @param fullInfo if the returned information should be consise or not
    @return a vector containing the different values to present in the thermo-msf-parser
    @throws SQLException if something went wrong with the retrieving
    */


    public Vector getInformationForPeptide(int peptideID,Connection iConnection,boolean fullInfo) throws SQLException;


}
