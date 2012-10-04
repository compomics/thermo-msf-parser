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


    public ProteinLowMem(String aAccession,Connection aConnection,int aProteinID){
        this.accession = aAccession;
        this.iConnection = aConnection;
        this.lProteinID = aProteinID;
    }

    private String accession;
    private int locationInVector = -1;
    private int lProteinID;
    private String lSequence;
    private Connection iConnection;
    private int proteinCounter = 0;
    private HashMap<Integer, String> iCustomDataFieldValues = new HashMap<Integer, String>();
    private int numberOfPeptides;


    public String getAccession() {
        return accession;
    }

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

    public void addPeptide(PeptideLowMem peptideLowMem) {

    }

    public void addDecoyPeptide(PeptideLowMem peptideLowMem) {


    }
}
