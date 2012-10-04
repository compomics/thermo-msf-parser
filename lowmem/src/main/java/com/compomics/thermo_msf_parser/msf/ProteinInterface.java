package com.compomics.thermo_msf_parser.msf;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/23/12
 * Time: 2:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProteinInterface {

    /**
    a method to fetch all the proteins stored in the msf file

    @param iConnection connection to the msf file
    @return an iterator containing all the proteins in Protein objects
    @throws SQLException if something went wrong with the retrieving
     */

    public Iterator getAllProteins(Connection iConnection) throws SQLException;

    /**
    get a protein from an accession

     @return a Protein object
    @throws SQLException if something went wrong with the retrieving

     * @param proteinAccession an accession
     * @param iConnection a connection to the msf file
     */

    public ProteinLowMem getProteinFromAccession(String proteinAccession, Connection iConnection) throws SQLException;

    /**
    get the accession from a protein

    @param proteinID a protein id in the db
    @param iConnection a connection to the msf file
    @return the accession for a given Protein object
    @throws SQLException if something went wrong with the retrieving
    */

    public String getAccessionFromProteinID(int proteinID,Connection iConnection) throws SQLException;

    /**
     * get the sequence stored in the db for a given ProteinID
     *
     * @param proteinID the ID of the protein in the SQLite DB
     * @param iConnection connection to the SQLite DB
     * @return string containing the sequence stored in the DB
     * @throws SQLException if something went wrong with the retrieving
     */

    public String getSequenceForProteinID(int proteinID,Connection iConnection) throws SQLException;

    /**
     * method for retrieving all the proteins connected to a given peptide in the DB
     *
     * @param PeptideID peptideID stored in the SQLite DB
     * @param iConnection connection to the SQLite DB
     * @return a vector containing all Protein objects connected to a given peptide
     * @throws SQLException if something went wrong with the retrieving
     */
    public Vector<ProteinLowMem> getProteinsForPeptide(int PeptideID,Connection iConnection) throws SQLException;
}
