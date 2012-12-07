package com.compomics.thermo_msf_parser.msf;

import java.sql.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/24/12
 * Time: 9:48 AM
 */
public class ProteinLowMem {


    private String accession;
    private int locationInVector = -1;
    private int lProteinID;
    private Connection iConnection;
    private HashMap<Integer, String> iCustomDataFieldValues = new HashMap<Integer, String>();
    private int numberOfPeptides;
    private Vector<PeptideLowMem> peptidesOfProtein = new Vector<PeptideLowMem>();
    
    /**
     * constructor for the ProteinLowMem object
     * 
     * @param aAccession the protein accession
     * @param aConnection a connection to the SQLite database
     * @param aProteinID the protein id in the SQLite database
     */
    
    public ProteinLowMem(String aAccession,Connection aConnection,int aProteinID){
        this.accession = aAccession;
        this.iConnection = aConnection;
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
    
    public int getLocationInVector() {
        return locationInVector;
    }

    public void setLocationInVector(int locationInVector) {
        this.locationInVector = locationInVector;
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
    
    public Connection getConnection(){
        return iConnection;
    }
    
    public void setConnection(Connection aConnection){
        this.iConnection = aConnection;

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

    public Vector<PeptideLowMem> getPeptidesForProtein(){
        return peptidesOfProtein;
    }
    
    
    public void addDecoyPeptide(PeptideLowMem peptideLowMem) {
        //not yet implemented
    }
}
