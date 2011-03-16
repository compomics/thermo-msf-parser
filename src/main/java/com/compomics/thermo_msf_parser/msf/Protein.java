package com.compomics.thermo_msf_parser.msf;

import com.compomics.thermo_msf_parser.Parser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 18-Feb-2011
 * Time: 09:21:21
 * To change this template use File | Settings | File Templates.
 */
public class Protein {

    /**
     * The protein id
     */
    private int iProteinId;
    /**
     * The protein sequence
     */
    private String iSequence;
    /**
     * The protein description
     */
    private String iDescription;
    /**
     * HashMap with the custom data field values. The key is the id of the custom data field
     */
    private HashMap<Integer, String> iCustomDataFieldValues = new HashMap<Integer,String>();
    /**
     * The compomics.util protein
     */
    private com.compomics.util.protein.Protein iUtilProtein;
    /**
     * The protein score
     */
    private double iScore;
    /**
     * The accession found by the compomics.util protein
     */
    private String iUtilAccession;
    /**
     * The description found by the compomics.util protein
     */
    private String iUtilDescription;
    /**
     * The peptides linked to this protein
     */
    private Vector<Peptide> iPeptides = new Vector<Peptide>();
    /**
     * The msf file parser
     */
    private Parser iParser;


    public Protein(int iProteinId, String iSequence) {
        this.iProteinId = iProteinId;
        this.iSequence = iSequence;
    }

    public Protein(int anInt, Parser lParser) {
        this.iProteinId = anInt;
        this.iParser = lParser;
    }

    public int getProteinId() {
        return iProteinId;
    }

    public String getSequence() throws SQLException {
        if(iSequence ==  null){
            ResultSet rs;
            Statement stat = iParser.getConnection().createStatement();
            rs = stat.executeQuery("select * from Proteins where ProteinID = " + iProteinId);
            String lSequence = null;
            while (rs.next()) {
                    lSequence = rs.getString("Sequence");
                }
                rs.close();
                stat.close();
            return lSequence;
        }
        return iSequence;
    }

    public String getDescription() {
        return iDescription;
    }

    public void setDescription(String iDescription) {
        this.iDescription = iDescription;
        String lFasta = iDescription + "\n"  + iSequence;
        iUtilProtein = new com.compomics.util.protein.Protein(lFasta);
        iUtilAccession = iUtilProtein.getHeader().getAccession();
        iUtilDescription = iUtilProtein.getHeader().getDescription();
    }

    /**
     * This method will add a value in the custom data field map by the id off the custom data field
     * @param lId The custom data field id
     * @param lValue The value to add
     */
    public void addCustomDataField(int lId, String lValue){
        iCustomDataFieldValues.put(lId, lValue);
    }

    public void setScore(double score) {
        this.iScore = score;
    }

    public void addPeptide(Peptide lPeptide) {
        this.iPeptides.add(lPeptide);
    }


    public HashMap<Integer, String> getCustomDataFieldValues() {
        return iCustomDataFieldValues;
    }

    public com.compomics.util.protein.Protein getUtilProtein() {
        return iUtilProtein;
    }

    public double getScore() {
        return iScore;
    }

    public String getUtilAccession() {
        return iUtilAccession;
    }

    public String getUtilDescription() {
        return iUtilDescription;
    }

    public Vector<Peptide> getPeptides() {
        return iPeptides;
    }


    public String toString(){
        return iUtilAccession;
    }
}

