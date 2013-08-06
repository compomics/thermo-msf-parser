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
     * @param aConnection a connection to the SQLite database
     * @param aProteinID the protein id in the SQLite database
     */
    
    public ProteinLowMem(String aAccession,int aProteinID){
        this.accession = aAccession;
        this.proteinID = aProteinID;
    }
    
    public ProteinLowMem(String aAccession,int aProteinID,String aSequence){
        this.accession =aAccession;
        this.proteinID = aProteinID;
        this.sequence = aSequence;
    }

    /**
     * getter for the protein accession
     * @return the protein accession string
     */

    @Override
    public String getAccession() {
        return accession;
    }

    /**
     * setter for the protein accession
     * @param accession an accession string
     */
    
    @Override
    public void setAccession(String accession) {
        this.accession = accession;
    }

    /**
     *
     * @return returns the location of the msf file in the msf file vector otherwise returns -1
     */
    
    @Override
    public int getLocationInList() {
        return locationInList;
    }

    @Override
    public void setLocationInList(int locationInList) {
        this.locationInList = locationInList;
    }
    /**
     * getter for the protein id 
     * @return the protein id
     */
    @Override
    public int getProteinID(){
        return proteinID;
    }
    
    @Override
    public void setProteinID(int aProteinID){
        this.proteinID = aProteinID;
    }

    @Override
    public String toString(){
    return accession;
    }


    @Override
    public void addCustomDataField(int lId, String lValue){
        iCustomDataFieldValues.put(lId, lValue);
    }

    @Override
    public HashMap<Integer, String> getCustomDataFieldValues() {
        return iCustomDataFieldValues;
    }

    @Override
    public int getNumberOfPeptides(){
        return numberOfPeptides;
    }

    @Override
    public void setNumberOfPeptides(int newNumberOfPeptides) {
        numberOfPeptides = newNumberOfPeptides;
    }

    @Override
    public void addPeptide(PeptideLowMem peptideLowMemToAdd) {
        peptidesOfProtein.add(peptideLowMemToAdd);
    }

    @Override
    public Set<PeptideLowMem> getPeptidesForProtein(){
        return peptidesOfProtein;
    }
    
    
    //public void addDecoyPeptide(PeptideLowMem peptideLowMem) {
        //not yet implemented
    //}
    
    @Override
    public void setMasterProtein(boolean isMasterProtein){
        this.isMasterProtein = isMasterProtein;
    }
    
    @Override
    public boolean isMasterProtein(){
        return isMasterProtein;
    }

    @Override
    public String getSequence() {
        return sequence;
    }    
    
    @Override
    public boolean equals(Object proteinLowMem){
        boolean equal = false;
        if(proteinLowMem != null && proteinLowMem instanceof ProteinLowMem){
            equal = (((ProteinLowMem)proteinLowMem).getProteinID() == this.getProteinID() && ((ProteinLowMem)proteinLowMem).getAccession().equalsIgnoreCase(this.getAccession()));
        }
        return equal;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + this.proteinID;
        hash = 13 * hash + (this.sequence != null ? this.sequence.hashCode() : 0);
        return hash;
    }
}
