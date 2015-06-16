package com.compomics.thermo_msf_parser_API.highmeminstance;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 18-Feb-2011
 * Time: 09:21:21
 * To change this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
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
    private Map<Integer, String> iCustomDataFieldValues = new HashMap<Integer,String>();
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
    private List<Peptide> iPeptides = new ArrayList<Peptide>();
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
    private List<PtmAnnotation> iPtmAnnotation = new ArrayList<PtmAnnotation>();
    /**
     * A vector with the decoy peptides
     */
    private List<Peptide> iDecoyPeptides = new ArrayList<Peptide>();
    
    /**
     * vector with score objects
     */
    private List<ProteinScore> scores = new ArrayList<ProteinScore>();
    private List<ProteinScore> decoyScores = new ArrayList<ProteinScore>();

    /**
     * <p>Constructor for Protein.</p>
     *
     * @param iProteinId a int.
     * @param iSequence a {@link java.lang.String} object.
     */
    public Protein(int iProteinId, String iSequence) {
        this.iProteinId = iProteinId;
        this.iSequence = iSequence;
    }

    /**
     * <p>Constructor for Protein.</p>
     *
     * @param anInt a int.
     * @param lParser a {@link com.compomics.thermo_msf_parser_API.highmeminstance.Parser} object.
     */
    public Protein(int anInt, Parser lParser) {
        this.iProteinId = anInt;
        this.iParser = lParser;
    }

    /**
     * <p>getProteinId.</p>
     *
     * @return a int.
     */
    public int getProteinId() {
        return iProteinId;
    }

    /**
     * <p>getSequence.</p>
     *
     * @return a {@link java.lang.String} object.
     * @throws java.sql.SQLException if any.
     */
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

    /**
     * <p>getDescription.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescription() {
        return iDescription;
    }

    /**
     * <p>setDescription.</p>
     *
     * @param iDescription a {@link java.lang.String} object.
     */
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
     *
     * @param lId The custom data field id
     * @param lValue The value to add
     */
    public void addCustomDataField(int lId, String lValue){
        iCustomDataFieldValues.put(lId, lValue);
    }

    /**
     * This method will add a value in the decoy custom data field map by the id off the custom data field
     *
     * @param lId The custom data field id
     * @param lValue The value to add
     */
    public void addDecoyCustomDataField(int lId, String lValue){
        iDecoyCustomDataFieldValues.put(lId, lValue);
    }

    /**
     * <p>addPeptide.</p>
     *
     * @param lPeptide a {@link com.compomics.thermo_msf_parser_API.highmeminstance.Peptide} object.
     */
    public void addPeptide(Peptide lPeptide) {
        this.iPeptides.add(lPeptide);
    }


    /**
     * <p>getCustomDataFieldValues.</p>
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<Integer, String> getCustomDataFieldValues() {
        return iCustomDataFieldValues;
    }


    /**
     * <p>getDecoyCustomDataFieldValues.</p>
     *
     * @return a {@link java.util.HashMap} object.
     */
    public HashMap<Integer, String> getDecoyCustomDataFieldValues() {
        return iDecoyCustomDataFieldValues;
    }

    /**
     * <p>getUtilProtein.</p>
     *
     * @return a {@link com.compomics.util.protein.Protein} object.
     */
    public com.compomics.util.protein.Protein getUtilProtein() {
        return iUtilProtein;
    }

    /**
     * <p>getUtilAccession.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUtilAccession() {
        return iUtilAccession;
    }

    /**
     * <p>getUtilDescription.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUtilDescription() {
        return iUtilDescription;
    }

    /**
     * <p>getPeptides.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Peptide> getPeptides() {
        return iPeptides;
    }


    /** {@inheritDoc} */
    @Override
    public String toString(){
        return iUtilAccession;
    }

    /**
     * <p>getParser.</p>
     *
     * @return a {@link com.compomics.thermo_msf_parser_API.highmeminstance.Parser} object.
     */
    public Parser getParser() {
        return iParser;
    }

    /**
     * <p>setMasterProtein.</p>
     *
     * @param aMasterProtein a int.
     */
    public void setMasterProtein(int aMasterProtein) {
        this.iMasterProtein = aMasterProtein;
    }

    /**
     * <p>getMasterProtein.</p>
     *
     * @return a int.
     */
    public int getMasterProtein() {
        return iMasterProtein;
    }

    /**
     * <p>setProteinGroupId.</p>
     *
     * @param aProteinGroupId a int.
     */
    public void setProteinGroupId(int aProteinGroupId){
        this.iProteinGroupId = aProteinGroupId;
    }

    /**
     * <p>getProteinGroupId.</p>
     *
     * @return a int.
     */
    public int getProteinGroupId() {
        return iProteinGroupId;
    }

    /**
     * <p>addPtmAnnotation.</p>
     *
     * @param aPtmAnnotation a {@link com.compomics.thermo_msf_parser_API.highmeminstance.Protein.PtmAnnotation} object.
     */
    public void addPtmAnnotation(PtmAnnotation aPtmAnnotation) {
        this.iPtmAnnotation.add(aPtmAnnotation);
    }

    /**
     * <p>addDecoyPeptide.</p>
     *
     * @param aPeptide a {@link com.compomics.thermo_msf_parser_API.highmeminstance.Peptide} object.
     */
    public void addDecoyPeptide(Peptide aPeptide) {
        this.iDecoyPeptides.add(aPeptide);
    }

    /**
     * <p>addScore.</p>
     *
     * @param aProteinScore a double.
     * @param aProcessingNodeNumber a int.
     * @param aCoverage a double.
     */
    public void addScore(double aProteinScore, int aProcessingNodeNumber, double aCoverage) {
        this.scores.add(new ProteinScore(aProteinScore,aProcessingNodeNumber,aCoverage));
    }

    /**
     * <p>Getter for the field <code>scores</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<ProteinScore> getScores() {
        return scores;
    }

    /**
     * <p>addDecoyScore.</p>
     *
     * @param aProteinScore a double.
     * @param aProcessingNodeNumber a int.
     * @param aCoverage a double.
     */
    public void addDecoyScore(double aProteinScore, int aProcessingNodeNumber, double aCoverage) {
        this.decoyScores.add(new ProteinScore(aProteinScore,aProcessingNodeNumber,aCoverage));
    }
    
    /**
     * <p>Getter for the field <code>decoyScores</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<ProteinScore> getDecoyScores() {
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

