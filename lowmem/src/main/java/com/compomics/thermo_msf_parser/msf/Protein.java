package com.compomics.thermo_msf_parser.msf;

import com.compomics.thermo_msf_parser.Parser;
import org.apache.log4j.Logger;

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
    // Class specific log4j logger for Thermo_msf_parserGUI instances.
	 private static Logger logger = Logger.getLogger(Protein.class);
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
     * HashMap with the custom data field values. The key is the id of the custom data field
     */
    private HashMap<Integer, String> iDecoyCustomDataFieldValues = new HashMap<Integer,String>();
    /**
     * The compomics.util protein
     */
    private com.compomics.util.protein.Protein iUtilProtein;
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
    /**
     * Int that indicates if this protein is the master protein
     * Only works for Thermo Proteome Discoverer version 1.3
     */
    private int iMasterProtein;
    /**
     * The protein groupid
     * Only works for Thermo Proteome Discoverer version 1.3
     */
    private int iProteinGroupId;
    /**
     * The ptm annotations
     * Only works for Thermo Proteome Discoverer version 1.3
     */
    private Vector<PtmAnnotation> iPtmAnnotation = new Vector<PtmAnnotation>();
    /**
     * A vector with the decoy peptides
     */
    private Vector<Peptide> iDecoyPeptides = new Vector<Peptide>();
    
    /**
     * vector with score objects
     */
    private Vector<ProteinScore> scores = new Vector<ProteinScore>();
    private Vector<ProteinScore> decoyScores = new Vector<ProteinScore>();

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
            iSequence = lSequence; //TODO: HENK: this sort of defeats the point of lazy loading, when obtaining large numbers of sequences?
            return lSequence;
        }
        return iSequence;
    }

    public String getDescription() {
        return iDescription;
    }

    public void setDescription(String iDescription) {
        this.iDescription = iDescription;
        if ( iDescription.contains(">GENSCAN")){
            String lFasta = iDescription + "\n"  + iSequence;
            iUtilProtein = new com.compomics.util.protein.Protein(lFasta);
            iUtilAccession =iUtilProtein.getHeader().toString().substring(iDescription.indexOf("GEN"),iDescription.indexOf("pep") -1);
            iUtilDescription = iDescription;
        }
        else if (iDescription.contains(">ENS")) {
            String lFasta = iDescription + "\n"  + iSequence;
            iUtilProtein = new com.compomics.util.protein.Protein(lFasta);
            iUtilAccession =iUtilProtein.getHeader().toString().substring(iDescription.indexOf("ENS"),iDescription.indexOf("pep") -1);
            iUtilDescription = iDescription;
        } else {
            String lFasta = iDescription + "\n"  + iSequence;
            iUtilProtein = new com.compomics.util.protein.Protein(lFasta);
            iUtilAccession = iUtilProtein.getHeader().getAccession();
            iUtilDescription = iUtilProtein.getHeader().getDescription();
        }
    }

    /**
     * This method will add a value in the custom data field map by the id off the custom data field
     * @param lId The custom data field id
     * @param lValue The value to add
     */
    public void addCustomDataField(int lId, String lValue){
        iCustomDataFieldValues.put(lId, lValue);
    }

    /**
     * This method will add a value in the decoy custom data field map by the id off the custom data field
     * @param lId The custom data field id
     * @param lValue The value to add
     */
    public void addDecoyCustomDataField(int lId, String lValue){
        iDecoyCustomDataFieldValues.put(lId, lValue);
    }

    public void addPeptide(Peptide lPeptide) {
        this.iPeptides.add(lPeptide);
    }


    public HashMap<Integer, String> getCustomDataFieldValues() {
        return iCustomDataFieldValues;
    }


    public HashMap<Integer, String> getDecoyCustomDataFieldValues() {
        return iDecoyCustomDataFieldValues;
    }

    public com.compomics.util.protein.Protein getUtilProtein() {
        return iUtilProtein;
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

    public Parser getParser() {
        return iParser;
    }

    public void setMasterProtein(int aMasterProtein) {
        this.iMasterProtein = aMasterProtein;
    }

    public int getMasterProtein() {
        return iMasterProtein;
    }

    public void setProteinGroupId(int aProteinGroupId){
        this.iProteinGroupId = aProteinGroupId;
    }

    public int getProteinGroupId() {
        return iProteinGroupId;
    }

    public void addPtmAnnotation(PtmAnnotation aPtmAnnotation) {
        this.iPtmAnnotation.add(aPtmAnnotation);
    }

    public void addDecoyPeptide(Peptide aPeptide) {
        this.iDecoyPeptides.add(aPeptide);
    }

    public void addScore(double aProteinScore, int aProcessingNodeNumber, double aCoverage) {
        this.scores.add(new ProteinScore(aProteinScore,aProcessingNodeNumber,aCoverage));
    }

    public Vector<ProteinScore> getScores() {
        return scores;
    }

    public void addDecoyScore(double aProteinScore, int aProcessingNodeNumber, double aCoverage) {
        this.decoyScores.add(new ProteinScore(aProteinScore,aProcessingNodeNumber,aCoverage));
    }
    
    public Vector<ProteinScore> getDecoyScores() {
        return decoyScores;
    }

    public static class PtmAnnotation{
        public int iPtmUnimodId;
        public int iPosition;

        public PtmAnnotation(int aPtmUnimodId, int aPosition) {
            iPtmUnimodId = aPtmUnimodId;
            iPosition = aPosition;
        }

        public int getPtmUnimodId() {
            return iPtmUnimodId;
        }

        public int getPosition() {
            return iPosition;
        }
    }
}

