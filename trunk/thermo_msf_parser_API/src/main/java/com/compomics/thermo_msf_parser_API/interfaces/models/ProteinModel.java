package com.compomics.thermo_msf_parser_API.interfaces.models;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Davy
 */
public interface ProteinModel {

    void addCustomDataField(int lId, String lValue);
    /**
     * add a peptide reference to the protein
     * @param peptideLowMemToAdd
     */
    void addPeptide(PeptideLowMem peptideLowMemToAdd);

    /**
     * equals method for comparing two proteins
     * @param proteinLowMem the protein to compare to
     * @return true if equal, false otherwise
     */
    @Override
    boolean equals(Object proteinLowMem);

    /**
     * getter for the protein accession
     * @return the protein accession string
     */
    String getAccession();

    /**
     * 
     * @return 
     */
    HashMap<Integer, String> getCustomDataFieldValues();

    /**
     *
     * @return returns the location of the msf file in the msf file vector otherwise returns -1
     */
    int getLocationInList();
/**
 * 
 * @return the number of peptides associated with the protein
 */
    int getNumberOfPeptides();
/**
 * 
 * @return the peptides assocciated with this protein
 */
    Set<PeptideLowMem> getPeptidesForProtein();

    /**
     * getter for the protein id
     * @return the protein id
     */
    int getProteinID();
/**
 * 
 * @return the protein sequence 
 */
    String getSequence();

    /**
     * the hash code for the protein
     * @return the hashcode for the protein
     */
    @Override
    int hashCode();

    boolean isMasterProtein();

    /**
     * setter for the protein accession
     * @param accession an accession string
     */
    void setAccession(String accession);
/**
 * 
 * @param locationInList 
 */
    void setLocationInList(int locationInList);

    //public void addDecoyPeptide(PeptideLowMem peptideLowMem) {
    //not yet implemented
    //}
    void setMasterProtein(boolean isMasterProtein);
/**
 * the number of peptides this protein has
 * @param newNumberOfPeptides 
 */
    void setNumberOfPeptides(int newNumberOfPeptides);

    /**
     * set the proteinid
     * @param aProteinID the proteinid for the protein 
     */
    void setProteinID(int aProteinID);

    /**
     * to string method for proteins
     * @return the protein accession
     */
    @Override
    String toString();
    
}
