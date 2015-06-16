package com.compomics.thermo_msf_parser_API.interfaces;


import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/23/12
 * Time: 2:00 PM
 * To change this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public interface ProteinControllerInterface {

    /**
     * a method to fetch all the proteins stored in the msf file
     *
     * @param msfFile the proteome discoverer file to retrieve from
     * @return an iterator containing all the proteins in Protein objects
     */
    public List getAllProteins(MsfFile msfFile);

    /**
     * get a protein from an accession
     *
     * @param proteinAccession an accession
     * @param msfFile          the proteome discoverer file to retrieve from
     * @return the protein associated with the protein accession from the given proteome discoverer file
     */
    public ProteinLowMem getProteinFromAccession(String proteinAccession, MsfFile msfFile);

    /**
     * get the accession from a protein
     *
     * @param proteinID a protein id in the db
     * @param msfFile   the proteome discoverer file to retrieve from
     * @return the accession for a given Protein object
     */
    public String getAccessionFromProteinID(int proteinID, MsfFile msfFile);

    /**
     * get the sequence stored in the db for a given ProteinID
     *
     * @param proteinID the ID of the protein in the SQLite DB
     * @param msfFile   the proteome discoverer file to retrieve from
     * @return string containing the sequence stored in the DB
     */
    public String getSequenceForProteinID(int proteinID, MsfFile msfFile);

    /**
     * method for retrieving all the proteins connected to a given peptide in the DB
     *
     * @param PeptideID peptideID stored in the SQLite DB
     * @param msfFile   the proteome discoverer file to retrieve from
     * @return a vector containing all Protein objects connected to a given peptide
     */
    public List<ProteinLowMem> getProteinsForPeptide(int PeptideID, MsfFile msfFile);
}
