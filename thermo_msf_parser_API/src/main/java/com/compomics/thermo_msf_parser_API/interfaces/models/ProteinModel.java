package com.compomics.thermo_msf_parser_API.interfaces.models;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem;
import java.util.HashMap;
import java.util.Set;

/**
 * <p>ProteinModel interface.</p>
 *
 * @author Davy
 * @version $Id: $Id
 */
public interface ProteinModel {

    /**
     * <p>addCustomDataField.</p>
     *
     * @param lId a int.
     * @param lValue a {@link java.lang.String} object.
     */
    void addCustomDataField(int lId, String lValue);
    /**
     * add a peptide reference to the protein
     *
     * @param peptideLowMemToAdd a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem} object.
     */
    void addPeptide(PeptideLowMem peptideLowMemToAdd);

    /**
     * {@inheritDoc}
     *
     * equals method for comparing two proteins
     */
    @Override
    boolean equals(Object proteinLowMem);

    /**
     * getter for the protein accession
     *
     * @return the protein accession string
     */
    String getAccession();

    /**
     * <p>getCustomDataFieldValues.</p>
     *
     * @return a {@link java.util.HashMap} object.
     */
    HashMap<Integer, String> getCustomDataFieldValues();

    /**
     * <p>getLocationInList.</p>
     *
     * @return returns the location of the msf file in the msf file vector otherwise returns -1
     */
    int getLocationInList();
    /**
     * <p>getNumberOfPeptides.</p>
     *
     * @return the number of peptides associated with the protein
     */
    int getNumberOfPeptides();
    /**
     * <p>getPeptidesForProtein.</p>
     *
     * @return the peptides assocciated with this protein
     */
    Set<PeptideLowMem> getPeptidesForProtein();

    /**
     * getter for the protein id
     *
     * @return the protein id
     */
    int getProteinID();
    /**
     * <p>getSequence.</p>
     *
     * @return the protein sequence
     */
    String getSequence();

    /**
     * {@inheritDoc}
     *
     * the hash code for the protein
     */
    @Override
    int hashCode();

    /**
     * <p>isMasterProtein.</p>
     *
     * @return a boolean.
     */
    boolean isMasterProtein();

    /**
     * setter for the protein accession
     *
     * @param accession an accession string
     */
    void setAccession(String accession);
    /**
     * <p>setLocationInList.</p>
     *
     * @param locationInList a int.
     */
    void setLocationInList(int locationInList);

    //public void addDecoyPeptide(PeptideLowMem peptideLowMem) {
    //not yet implemented
    //}
    /**
     * <p>setMasterProtein.</p>
     *
     * @param isMasterProtein a boolean.
     */
    void setMasterProtein(boolean isMasterProtein);
    /**
     * the number of peptides this protein has
     *
     * @param newNumberOfPeptides a int.
     */
    void setNumberOfPeptides(int newNumberOfPeptides);

    /**
     * set the proteinid
     *
     * @param aProteinID the proteinid for the protein
     */
    void setProteinID(int aProteinID);

    /**
     * {@inheritDoc}
     *
     * to string method for proteins
     */
    @Override
    String toString();
    
}
