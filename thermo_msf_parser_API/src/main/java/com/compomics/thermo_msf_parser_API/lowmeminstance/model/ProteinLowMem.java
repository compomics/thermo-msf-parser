package com.compomics.thermo_msf_parser_API.lowmeminstance.model;

import com.compomics.thermo_msf_parser_API.interfaces.models.ProteinModel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/24/12
 * Time: 9:48 AM
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class ProteinLowMem implements ProteinModel {


    private String accession;
    private int locationInList = -1;
    private int proteinID;
    private final HashMap<Integer, String> iCustomDataFieldValues = new HashMap<Integer, String>();
    private int numberOfPeptides;
    private final Set<PeptideLowMem> peptidesOfProtein = new HashSet<PeptideLowMem>();
    private boolean isMasterProtein;
    private String sequence;
    
    /**
     * constructor for the ProteinLowMem object
     *
     * @param aAccession the protein accession
     * @param aProteinID the protein id in the SQLite database
     */
    public ProteinLowMem(String aAccession,int aProteinID){
        this.accession = aAccession;
        this.proteinID = aProteinID;
    }
    
    /**
     * <p>Constructor for ProteinLowMem.</p>
     *
     * @param aAccession a {@link java.lang.String} object.
     * @param aProteinID a int.
     * @param aSequence a {@link java.lang.String} object.
     */
    public ProteinLowMem(String aAccession,int aProteinID,String aSequence){
        this.accession =aAccession;
        this.proteinID = aProteinID;
        this.sequence = aSequence;
    }

    /**
     * {@inheritDoc}
     *
     * getter for the protein accession
     */
    @Override
    public String getAccession() {
        return accession;
    }

    /**
     * {@inheritDoc}
     *
     * setter for the protein accession
     */
    @Override
    public void setAccession(String accession) {
        this.accession = accession;
    }

    /** {@inheritDoc} */
    @Override
    public int getLocationInList() {
        return locationInList;
    }

    /** {@inheritDoc} */
    @Override
    public void setLocationInList(int locationInList) {
        this.locationInList = locationInList;
    }
    /**
     * {@inheritDoc}
     *
     * getter for the protein id
     */
    @Override
    public int getProteinID(){
        return proteinID;
    }
    
    /** {@inheritDoc} */
    @Override
    public void setProteinID(int aProteinID){
        this.proteinID = aProteinID;
    }

    /** {@inheritDoc} */
    @Override
    public String toString(){
    return accession;
    }


    /** {@inheritDoc} */
    @Override
    public void addCustomDataField(int lId, String lValue){
        iCustomDataFieldValues.put(lId, lValue);
    }

    /** {@inheritDoc} */
    @Override
    public HashMap<Integer, String> getCustomDataFieldValues() {
        return iCustomDataFieldValues;
    }

    /** {@inheritDoc} */
    @Override
    public int getNumberOfPeptides(){
        return numberOfPeptides;
    }

    /** {@inheritDoc} */
    @Override
    public void setNumberOfPeptides(int newNumberOfPeptides) {
        numberOfPeptides = newNumberOfPeptides;
    }

    /** {@inheritDoc} */
    @Override
    public void addPeptide(PeptideLowMem peptideLowMemToAdd) {
        peptidesOfProtein.add(peptideLowMemToAdd);
    }

    /** {@inheritDoc} */
    @Override
    public Set<PeptideLowMem> getPeptidesForProtein(){
        return peptidesOfProtein;
    }
    
    
    //public void addDecoyPeptide(PeptideLowMem peptideLowMem) {
        //not yet implemented
    //}
    
    /** {@inheritDoc} */
    @Override
    public void setMasterProtein(boolean isMasterProtein){
        this.isMasterProtein = isMasterProtein;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isMasterProtein(){
        return isMasterProtein;
    }

    /** {@inheritDoc} */
    @Override
    public String getSequence() {
        return sequence;
    }    
    
    /** {@inheritDoc} */
    @Override
    public boolean equals(Object aLowMemProtein){
        boolean equal = false;
        if(aLowMemProtein != null && aLowMemProtein instanceof ProteinLowMem){
            equal = (((ProteinLowMem)aLowMemProtein).getProteinID() == this.getProteinID() && ((ProteinLowMem)aLowMemProtein).getAccession().equalsIgnoreCase(this.getAccession()));
        }
        return equal;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + this.proteinID;
        hash = 13 * hash + (this.sequence != null ? this.sequence.hashCode() : 0);
        return hash;
    }
}
