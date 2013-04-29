package com.compomics.thermo_msf_parser_API.lowmeminstance.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/24/12
 * Time: 9:48 AM
 */
public class ProteinLowMem {


    private String accession;
    private int locationInList = -1;
    private int lProteinID;
    private final HashMap<Integer, String> iCustomDataFieldValues = new HashMap<Integer, String>();
    private int numberOfPeptides;
    private final List<PeptideLowMem> peptidesOfProtein = new ArrayList<PeptideLowMem>();
    private boolean isMasterProtein;
    
    /**
     * constructor for the ProteinLowMem object
     * 
     * @param aAccession the protein accession
     * @param aConnection a connection to the SQLite database
     * @param aProteinID the protein id in the SQLite database
     */
    
    public ProteinLowMem(String aAccession,int aProteinID){
        this.accession = aAccession;
        this.lProteinID = aProteinID;
    }

    /**
     * getter for the protein accession
     * @return the protein accession string
     */

    public String getAccession() {
        return accession;
    }

    /**
     * setter for the protein accession
     * @param accession an accession string
     */
    
    public void setAccession(String accession) {
        this.accession = accession;
    }

    /**
     *
     * @return returns the location of the msf file in the msf file vector otherwise returns -1
     */
    
    public int getLocationInList() {
        return locationInList;
    }

    public void setLocationInList(int locationInList) {
        this.locationInList = locationInList;
    }
    /**
     * getter for the protein id 
     * @return the protein id
     */
    public int getProteinID(){
        return lProteinID;
    }
    
    public void setProteinID(int aProteinID){
        this.lProteinID = aProteinID;
    }

    @Override
    public String toString(){
    return accession;
    }


    public void addCustomDataField(int lId, String lValue){
        iCustomDataFieldValues.put(lId, lValue);
    }

    public HashMap<Integer, String> getCustomDataFieldValues() {
        return iCustomDataFieldValues;
    }

    public int getNumberOfPeptides(){
        return numberOfPeptides;
    }

    public void setNumberOfPeptides(int newNumberOfPeptides) {
        numberOfPeptides = newNumberOfPeptides;
    }

    public void addPeptide(PeptideLowMem peptideLowMemToAdd) {
        peptidesOfProtein.add(peptideLowMemToAdd);
    }

    public List<PeptideLowMem> getPeptidesForProtein(){
        return peptidesOfProtein;
    }
    
    
    //public void addDecoyPeptide(PeptideLowMem peptideLowMem) {
        //not yet implemented
    //}
    
    public void setMasterProtein(boolean isMasterProtein){
        this.isMasterProtein = isMasterProtein;
    }
    
    public boolean isMasterProtein(){
        return isMasterProtein;
    }
}
