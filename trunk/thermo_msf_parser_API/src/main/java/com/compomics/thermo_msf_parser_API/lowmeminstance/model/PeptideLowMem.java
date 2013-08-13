package com.compomics.thermo_msf_parser_API.lowmeminstance.model;

import com.compomics.thermo_msf_parser_API.highmeminstance.AminoAcid;
import com.compomics.thermo_msf_parser_API.highmeminstance.Modification;
import com.compomics.thermo_msf_parser_API.highmeminstance.ModificationPosition;
import com.compomics.thermo_msf_parser_API.highmeminstance.PeptideFragmentIon;
import com.compomics.thermo_msf_parser_API.highmeminstance.Charge;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 4/24/12 Time: 9:37 AM To change
 * this template use File | Settings | File Templates.
 */
public class PeptideLowMem {

    private int counter = 0;
    // Class specific log4j LOGGER for Thermo_msf_parserGUI instances.
    private static final Logger LOGGER = Logger.getLogger(PeptideLowMem.class);
    /**
     * The peptide id
     */
    private int iPeptideId;
    /**
     * The spectrum id linked to this peptide
     */
    private int iSpectrumId;
    /**
     * The confidence level
     */
    private int iConfidenceLevel;
    /**
     * The peptide sequence
     */
    private String iSequence;
    /**
     * The scores
     */
    private final List<Double> iScores = new ArrayList<Double>();
    /**
     * The scoretypeids
     */
    private final List<Integer> iScoreTypeIds = new ArrayList<Integer>();
    /**
     * The scoretype
     */
    private final List<ScoreTypeLowMem> iScoreTypes = new ArrayList<ScoreTypeLowMem>();
    /**
     * The total ions count
     */
    private int iTotalIonsCount;
    /**
     * The matched ions count
     */
    private int iMatchedIonsCount;
    /**
     * The peptide annotation
     */
    private String iAnnotation;
    /**
     * The proteins linked to this peptide
     */
    private final List<ProteinLowMem> iPeptideProteins = new ArrayList<ProteinLowMem>();
    /**
     * The modifications linked to this peptide
     */
    private final List<Modification> iPeptideModifications = new ArrayList<Modification>();
    /**
     * The modifications positions of the modifications in the
     * iPeptideModifications List
     */
    private final List<ModificationPosition> iPeptideModificationPositions = new ArrayList<ModificationPosition>();
    /**
     * The site probabilities of the Phospho modifications, contents can be {@code null}
     */
    private final List<Float> iPhosphoRSSiteProbabilities = new ArrayList<Float>();
    /**
     * the phosphoRS p value for the current 'phosphorylation isoform'
     */
    private Float phosphoRSScore = null;
    /**
     * the phosphoRS probability of the sequence
     */
    private Float phoshpoRSSequenceProbability = null;
    /**
     * Boolean that indicates if this peptide is N-terminally modified
     */
    private boolean iHasNTermModification = false;
    /**
     * The modified sequence
     */
    private String iModifiedPeptide = null;
    /**
     * The channel
     */
    private int iChannelId = 0;
    /**
     * HashMap with the custom data field values. The key is the id of the
     * custom data field
     */
    private final HashMap<Integer, String> iCustomDataFieldValues = new HashMap<Integer, String>();
    /**
     * The fragment ions
     */
    private List<PeptideFragmentIon> iTheoreticalFragmentIons;
    /**
     * This vector holds the peptide sequence in amino acid objects
     */
    private List<AminoAcid> iAminoAcidSequence = new ArrayList<AminoAcid>();
    /**
     * The spectrum linked to this peptide
     */
    private SpectrumLowMem iParentSpectrum;
    /**
     * The processing node number
     */
    private int iProcessingNodeNumber;
    /**
     * Int that indicates if this peptide has a missed cleavage WORKS ONLY FROM
     * PROTEOME DISCOVERER VERSION 1.3
     */
    private int iMissedCleavage;
    /**
     * The unique peptide sequence id WORKS ONLY FROM PROTEOME DISCOVERER
     * VERSION 1.3
     */
    /**
     * int of the unique peptide id
     */
    private int iUniquePeptideSequenceId;

    /**
     * connection to the msf file
     */
    /**
     * Constructor for a low memory instance peptide.
     *
     * @param iPeptideId SQLite id for peptide
     * @param iSpectrumId SQLite id for spectrum
     * @param iConfidenceLevel the confidence level of the peptide
     * @param iSequence the peptide sequence
     * @param iTotalIonsCount the total ions counted for the peptide
     * @param iMatchedIonsCount the matched ions for the peptide
     * @param iAnnotation the peptide annotation
     * @param iProcessingNodeNumber the procession number
     * @param iAminoAcids List containing the amino acids used in the SQLite
     * database. returned from the AminoAcidLowMem class
     */
    public PeptideLowMem(int iPeptideId, int iSpectrumId, int iConfidenceLevel, String iSequence, int iTotalIonsCount, int iMatchedIonsCount, String iAnnotation, int iProcessingNodeNumber, List<AminoAcid> iAminoAcids) {
        this.iPeptideId = iPeptideId;
        this.iSpectrumId = iSpectrumId;
        this.iConfidenceLevel = iConfidenceLevel;
        this.iSequence = iSequence;
        this.iTotalIonsCount = iTotalIonsCount;
        this.iMatchedIonsCount = iMatchedIonsCount;
        this.iAnnotation = iAnnotation;
        this.iProcessingNodeNumber = iProcessingNodeNumber;
        createAminoAcidSequence(iSequence, iAminoAcids);
    }
    /*
     * constructor for a peptidelowmem instance
     * @param rs
     */

    public PeptideLowMem(ResultSet rs, List<AminoAcid> iAminoAcids) {
        try {
            this.iPeptideId = rs.getInt("PeptideID");
            this.iSpectrumId = rs.getInt("SpectrumID");
            this.iConfidenceLevel = rs.getInt("ConfidenceLevel");
            this.iSequence = rs.getString("Sequence");
            this.iTotalIonsCount = rs.getInt("TotalIonsCount");
            this.iMatchedIonsCount = rs.getInt("MatchedIonsCount");
            this.iAnnotation = rs.getString("Annotation");
            this.iProcessingNodeNumber = rs.getInt("ProcessingNodeNumber");
            iAminoAcidSequence = new ArrayList<AminoAcid>();
            createAminoAcidSequence(iSequence,iAminoAcids);
        } catch (SQLException ex) {
            LOGGER.error(ex);
        }
    }

    /**
     * create the amino acid sequence for the peptide
     * @param sequence
     * @param iAminoAcids 
     */
    private void createAminoAcidSequence(String sequence, List<AminoAcid> iAminoAcids) {
        for (int i = 0; i < sequence.length(); i++) {
            String lAaOneLetterCode = String.valueOf(sequence.charAt(i));
            for (int a = 0; a < iAminoAcids.size(); a++) {
                if (iAminoAcids.get(a).getOneLetterCode() != null) {
                    if (iAminoAcids.get(a).getOneLetterCode().equalsIgnoreCase(lAaOneLetterCode)) {
                        iAminoAcidSequence.add(iAminoAcids.get(a));
                    }
                }
            }
        }

    }

    /**
     * Getter for the peptide id
     *
     * @return int with the peptide id
     */
    public int getPeptideId() {
        return iPeptideId;
    }

    /**
     * Getter for the spectrum id
     *
     * @return int with the spectrum id
     */
    public int getSpectrumId() {
        return iSpectrumId;
    }

    /**
     * Getter for the confidence level
     *
     * @return int with the confidence level
     */
    public int getConfidenceLevel() {
        return iConfidenceLevel;
    }

    /**
     * Getter for the sequence
     *
     * @return String with the sequence
     */
    public String getSequence() {
        return iSequence;
    }

    /**
     * Getter for the scores
     *
     * @return List with the scores (double)
     */
    public List<Double> getScores() {
        return iScores;
    }

    /**
     * Getter for the score type ids. These score type ids represent the score
     * types
     *
     * @return List with the score type ids
     */
    public List<Integer> getScoreTypeIds() {
        return iScoreTypeIds;
    }

    /**
     * Getter for the score type. These score type are linked to the scores
     *
     * @return List with the score type
     */
    public List<ScoreTypeLowMem> getScoreTypes() {
        return iScoreTypes;
    }

    /**
     * Getter for the total ion count
     *
     * @return int with the number of ions counted
     */
    public int getTotalIonsCount() {
        return iTotalIonsCount;
    }

    /**
     * Getter for the number of matched ions
     *
     * @return int with the number of matched ions
     */
    public int getMatchedIonsCount() {
        return iMatchedIonsCount;
    }

    /**
     * Getter for the annotation
     *
     * @return String with the annotation
     */
    public String getAnnotation() {
        return iAnnotation;
    }

    /**
     * Getter of the proteins linked to this peptide
     *
     * @return vector with the linked proteins
     */
    public List<ProteinLowMem> getPeptideProteins() {
        return iPeptideProteins;
    }

    /**
     * Getter for the modifications linked to this peptide
     *
     * @return vector with the linked modifications
     */
    public List<Modification> getPeptideModifications() {
        return iPeptideModifications;
    }

    /**
     * Getter for the modification positions linked to this peptide
     *
     * @return vector with the linked modification positions
     */
    public List<ModificationPosition> getPeptideModificationPositions() {
        return iPeptideModificationPositions;
    }

    /**
     * Getter for a boolean that indicates if this peptide has an N terminal
     * modifcation
     *
     * @return boolean that indicates if this peptide has an N terminal
     * modifcation
     */
    public boolean isHasNTermModification() {
        return iHasNTermModification;
    }

    /**
     * Getter for the custom data fields linked to this peptide. The key is the
     * id of the custom data field and the value is the data field value
     *
     * @return hashmap
     */
    public HashMap<Integer, String> getCustomDataFieldValues() {
        return iCustomDataFieldValues;
    }

    /**
     * Getter for a vector with all theoretical fragment ions
     *
     * @return vector with all theoretical fragment ions
     */
    public List<PeptideFragmentIon> getTheoreticalFragmentIons() {
        return iTheoreticalFragmentIons;
    }

    /**
     * Getter for the vector with the amino acid sequence
     *
     * @return vector with the amino acid sequence
     */
    public List<AminoAcid> getAminoAcidSequence() {
        return iAminoAcidSequence;
    }

    /**
     * Getter for the parent spectrum
     *
     * @return The parent spectrum
     */
    public SpectrumLowMem getParentSpectrum() {
        return iParentSpectrum;
    }

    /**
     * Setter for the parent spectrum
     *
     * @param lSpectrum The parent spectrum to set
     */
    public void setParentSpectrum(SpectrumLowMem lSpectrum) {
        this.iParentSpectrum = lSpectrum;
    }

    /**
     * Getter for a score by score type
     *
     * @param lScoreType The score type of the requested score
     * @return double with the score
     */
    public Double getScoreByScoreType(ScoreTypeLowMem lScoreType) {
        Double lScore = null;

        for (int i = 0; i < iScoreTypes.size(); i++) {
            if (lScoreType.getDescription().equalsIgnoreCase(iScoreTypes.get(i).getDescription())) {
                lScore = iScores.get(i);
            }
        }
        return lScore;
    }

    /**
     * Getter for the main score
     *
     * @return double with the score
     */
    public Double getMainScore() {
        Double lScore = null;
        for (int i = 0; i < iScoreTypes.size(); i++) {
            if (iScoreTypes.get(i).getIsMainScore() == 1) {
                lScore = iScores.get(i);
            }
        }
        return lScore;
    }

    /**
     * This method will add a score (with a specific score type id) to this
     * peptide
     *
     * @param iScore double with the score
     * @param iScoreTypeid the score type id the added score
     * @param lScoreTypes The different score types found in the msf file
     */
    public void setScore(double iScore, int iScoreTypeid, List<ScoreTypeLowMem> lScoreTypes) {
        this.iScores.add(iScore);
        this.iScoreTypeIds.add(iScoreTypeid);
        boolean added = false;
        for (int i = 0; i < lScoreTypes.size(); i++) {
            if (lScoreTypes.get(i).getScoreTypeId() == iScoreTypeid) {
                iScoreTypes.add(lScoreTypes.get(i));
                added = true;
            }
        }
        if (!added) {
            iScoreTypes.add(null);
        }
    }

    /**
     * This method will add a protein to this peptide
     *
     * @param lProtein The protein to add
     */
    public void addProtein(ProteinLowMem lProtein) {
        iPeptideProteins.add(lProtein);
        lProtein.addPeptide(this);
    }

    /**
     * This method will add an amino acid modification and the chance it is a phospho site to this peptide 
     *
     * @param lMod The modification
     * @param lModPos The modification position
     * @param pRSSiteMod chance that the modification is a phospho site
     */
    public void addModification(Modification lMod, ModificationPosition lModPos, Float pRSSiteMod) {
        iPeptideModifications.add(lMod);
        iPeptideModificationPositions.add(lModPos);
        iPhosphoRSSiteProbabilities.add(pRSSiteMod);
        if (lModPos.isNterm()) {
            iHasNTermModification = true;
        }
    }

    /**
     * adds an amino acid modification to the peptide
     * 
     * @param lMod the modification to add
     * @param lModPos the location of the modification on the peptide
     */
    public void addModification(Modification lMod, ModificationPosition lModPos) {
        addModification(lMod, lModPos, null);
    }

    
    /**
     * Getter for the proteins linked to this peptide
     *
     * @return List with the proteins linked to this peptide
     */
    public List<ProteinLowMem> getProteins() {
        return iPeptideProteins;
    }

    /**
     * Getter for the modified peptide sequence
     *
     * @return String with the modified peptide sequence
     */
    public String getModifiedPeptideSequence() {
        if (iModifiedPeptide == null) {
            //do the N terminus
            if (iHasNTermModification) {
                for (int m = 0; m < iPeptideModifications.size(); m++) {
                    if (iPeptideModifications.get(m).getPositionType() == 1) {
                        iModifiedPeptide = String.format("%s-", iPeptideModifications.get(m).getAbbreviation());
                    }
                }
            } else {
                iModifiedPeptide = "NH2-";
            }
            //do the middle
            for (int c = 0; c < iSequence.length(); c++) {
                iModifiedPeptide += iSequence.charAt(c);
                for (int m = 0; m < iPeptideModifications.size(); m++) {
                    if (iPeptideModificationPositions.get(m).getPosition() == c && !iPeptideModificationPositions.get(m).isNterm()) {

                        iModifiedPeptide = String.format("%s<%s", iModifiedPeptide, iPeptideModifications.get(m).getAbbreviation());
                        if (iPhosphoRSSiteProbabilities.get(m) != null) {
                            iModifiedPeptide += new StringBuilder().append(":").append(iPhosphoRSSiteProbabilities.get(m) * 100).append("%").toString();
                        }
                        iModifiedPeptide += ">";
                    }
                }
            }
            //do the C terminus
            iModifiedPeptide += "-COOH";

        }
        return iModifiedPeptide;
    }

    /**
     * Setter for the channel id
     *
     * @param lChannelId int with the channel id to set
     */
    public void setChannelId(int lChannelId) {
        this.iChannelId = lChannelId;
    }

    /**
     * Getter for the channel id
     *
     * @return int with the channel id
     */
    public int getChannelId() {
        return iChannelId;
    }

    /**
     * Getter for the processing node number
     *
     * @return int with the processing node number
     */
    public int getProcessingNodeNumber() {
        return iProcessingNodeNumber;
    }

    /**
     * This method will add a value in the custom data field map by the id off
     * the custom data field
     *
     * @param lId The custom data field id
     * @param lValue The value to add
     */
    public void addCustomDataField(int lId, String lValue) {
        iCustomDataFieldValues.put(lId, lValue);
    }

    /**
     * This code is adapted from the X!tandem parser code
     *
     * @param iSpectrumCharge The maximum charge of the fragement ions
     */
    private void calculateFragmentions(int iSpectrumCharge) {

        iTheoreticalFragmentIons = new ArrayList<PeptideFragmentIon>();
        double lHydrogenMass = 1.007825;
        double lCarbonMass = 12.000000;
        double lNitrogenMass = 14.003070;
        double lOxygenMass = 15.994910;

        for (int charge = 1; charge <= iSpectrumCharge; charge++) {

            for (int i = 0; i < iAminoAcidSequence.size() - 1; i++) {
                double bMass = 0.0;
                double yMass = 0.0;

                // Each peptide mass is added to the b ion mass
                for (int j = 0; j <= i; j++) {
                    bMass += iAminoAcidSequence.get(j).getMonoisotopicMass();
                    //check if it is not modified
                    for (int m = 0; m < iPeptideModificationPositions.size(); m++) {
                        if (iPeptideModificationPositions.get(m).getPosition() == j) {
                            bMass += iPeptideModifications.get(m).getDeltaMass();
                        }
                    }

                }
                // Each peptide mass is added to the y ion mass, taking the reverse direction (from the C terminal end)
                for (int j = 0; j <= i; j++) {
                    yMass += iAminoAcidSequence.get((iAminoAcidSequence.size() - 1) - j).getMonoisotopicMass();
                    for (int m = 0; m < iPeptideModificationPositions.size(); m++) {
                        if (iPeptideModificationPositions.get(m).getPosition() == (iAminoAcidSequence.size() - 1) - j) {
                            yMass += iPeptideModifications.get(m).getDeltaMass();
                        }
                    }
                }
                //add 1 Hydrogen for the Y ions (for the NH2 terminus) and 1 H and 1 O for the OH group
                yMass = yMass + lHydrogenMass + lHydrogenMass + lOxygenMass;

                // Create an instance for each fragment ion

                //B Ion
                PeptideFragmentIon lIon = new PeptideFragmentIon(PeptideFragmentIon.PeptideFragmentIonType.B_ION, i + 1, (bMass + charge * lHydrogenMass) / charge);
                lIon.addUrParam(new Charge(charge));
                iTheoreticalFragmentIons.add(lIon);
                //BNH3 Ion
                lIon = new PeptideFragmentIon(PeptideFragmentIon.PeptideFragmentIonType.BNH3_ION, i + 1, (bMass - lOxygenMass - 2 * -lNitrogenMass - 3 * lHydrogenMass + charge * lHydrogenMass) / charge);
                lIon.addUrParam(new Charge(charge));
                iTheoreticalFragmentIons.add(lIon);
                //BH2O Ion
                lIon = new PeptideFragmentIon(PeptideFragmentIon.PeptideFragmentIonType.BH2O_ION, i + 1, (bMass - lOxygenMass - 2 * lHydrogenMass + charge * lHydrogenMass) / charge);
                lIon.addUrParam(new Charge(charge));
                iTheoreticalFragmentIons.add(lIon);
                //A Ion
                lIon = new PeptideFragmentIon(PeptideFragmentIon.PeptideFragmentIonType.A_ION, i + 1, (bMass - lOxygenMass - lCarbonMass + charge * lHydrogenMass) / charge);
                lIon.addUrParam(new Charge(charge));
                iTheoreticalFragmentIons.add(lIon);
                //ANH3 Ion
                lIon = new PeptideFragmentIon(PeptideFragmentIon.PeptideFragmentIonType.ANH3_ION, i + 1, (bMass - lOxygenMass - lCarbonMass - lNitrogenMass - 3 * lHydrogenMass + charge * lHydrogenMass) / charge);
                lIon.addUrParam(new Charge(charge));
                iTheoreticalFragmentIons.add(lIon);
                //AH2O Ion
                lIon = new PeptideFragmentIon(PeptideFragmentIon.PeptideFragmentIonType.AH2O_ION, i + 1, (bMass - 2 * lOxygenMass - lCarbonMass - 2 * lHydrogenMass + charge * lHydrogenMass) / charge);
                lIon.addUrParam(new Charge(charge));
                iTheoreticalFragmentIons.add(lIon);
                //C Ion
                lIon = new PeptideFragmentIon(PeptideFragmentIon.PeptideFragmentIonType.C_ION, i + 1, (bMass + lNitrogenMass + 3 * lHydrogenMass + charge * lHydrogenMass) / charge);
                lIon.addUrParam(new Charge(charge));
                iTheoreticalFragmentIons.add(lIon);

                // Create an instance of the fragment y ion
                //Y Ion
                lIon = new PeptideFragmentIon(PeptideFragmentIon.PeptideFragmentIonType.Y_ION, i + 1, (yMass + charge * lHydrogenMass) / charge);
                lIon.addUrParam(new Charge(charge));
                iTheoreticalFragmentIons.add(lIon);
                //YNH3 Ion
                lIon = new PeptideFragmentIon(PeptideFragmentIon.PeptideFragmentIonType.YNH3_ION, i + 1, (yMass - lNitrogenMass - 3 * lHydrogenMass + charge * lHydrogenMass) / charge);
                lIon.addUrParam(new Charge(charge));
                iTheoreticalFragmentIons.add(lIon);
                //YH2O Ion
                lIon = new PeptideFragmentIon(PeptideFragmentIon.PeptideFragmentIonType.YH2O_ION, i + 1, (yMass - 2 * lHydrogenMass - lOxygenMass + charge * lHydrogenMass) / charge);
                lIon.addUrParam(new Charge(charge));
                iTheoreticalFragmentIons.add(lIon);
                //X Ion
                lIon = new PeptideFragmentIon(PeptideFragmentIon.PeptideFragmentIonType.X_ION, i + 1, (yMass + lCarbonMass + lOxygenMass - 2 * lHydrogenMass + charge * lHydrogenMass) / charge);
                lIon.addUrParam(new Charge(charge));
                iTheoreticalFragmentIons.add(lIon);
                //Z Ion
                lIon = new PeptideFragmentIon(PeptideFragmentIon.PeptideFragmentIonType.Z_ION, i + 1, (yMass - lNitrogenMass - 2 * lHydrogenMass + charge * lHydrogenMass) / charge);
                lIon.addUrParam(new Charge(charge));
                iTheoreticalFragmentIons.add(lIon);

            }
        }
    }

    /**
     * This method will give the fragment ions for a specific type and charge
     *
     * @param lCharge The charge of the fragment ions wanted
     * @param lTypes The types of fragment ions wanted
     * @return List with the requested fragment ions
     */
    public List<PeptideFragmentIon> getFragmentIonsByTypeAndCharge(int lCharge, List<PeptideFragmentIon.PeptideFragmentIonType> lTypes) {
        if (iTheoreticalFragmentIons == null) {
            this.calculateFragmentions(getParentSpectrum().getCharge());
        }
        List<PeptideFragmentIon> lResult = new ArrayList<PeptideFragmentIon>();

        for (int i = 0; i < iTheoreticalFragmentIons.size(); i++) {
            if (((Charge) iTheoreticalFragmentIons.get(i).getUrParam(new Charge())).getCharge() == lCharge) {
                boolean lPass = false;
                for (int t = 0; t < lTypes.size(); t++) {
                    if (iTheoreticalFragmentIons.get(i).getType() == lTypes.get(t)) {
                        lPass = true;
                    }
                }
                if (lPass) {
                    lResult.add(iTheoreticalFragmentIons.get(i));
                }
            }
        }

        return lResult;
    }

    /**
     * To string method
     *
     * @return String with the peptide sequence
     */
    @Override
    public String toString() {
        return iSequence;
    }

    /**
     *
     * @param aMissedCleavage
     */
    public void setMissedCleavage(int aMissedCleavage) {
        this.iMissedCleavage = aMissedCleavage;
    }

    /**
     *
     * @param aUniquePeptideSequenceId
     */
    public void setUniquePeptideSequenceId(int aUniquePeptideSequenceId) {
        this.iUniquePeptideSequenceId = aUniquePeptideSequenceId;
    }

    /**
     *
     * @return
     */
    public int getMissedCleavage() {
        return iMissedCleavage;
    }

    /**
     *
     * @return
     */
    public int getUniquePeptideSequenceId() {
        return iUniquePeptideSequenceId;
    }

    /**
     *
     * @param aProtein
     */
    public void addDecoyProtein(ProteinLowMem aProtein) {
        iPeptideProteins.add(aProtein);
        //aProtein.addDecoyPeptide(this);
    }

    /**
     *
     * @param aAnnotation
     */
    public void setAnnotation(String aAnnotation) {
        this.iAnnotation = aAnnotation;
    }

    /**
     *
     * @param pRSScore
     */
    public void setPhosphoRSScore(Float pRSScore) {
        this.phosphoRSScore = pRSScore;
    }

    /**
     *
     * @return
     */
    public Float getPhosphoRSScore() {
        return phosphoRSScore;
    }

    /**
     *
     * @return
     */
    public Float getPhoshpoRSSequenceProbability() {
        return phoshpoRSSequenceProbability;
    }

    /**
     *
     * @param phoshpoRSSequenceProbability
     */
    public void setPhoshpoRSSequenceProbability(Float phoshpoRSSequenceProbability) {
        this.phoshpoRSSequenceProbability = phoshpoRSSequenceProbability;
    }

    /**
     *
     * @return
     */
    public List<Float> getPhosphoRSSiteProbabilities() {
        return iPhosphoRSSiteProbabilities;
    }

    /**
     *
     * @param lCharge
     * @return
     */
    public double getPeptideMassForCharge(int lCharge) {
        double lCalculatedMass = 0.0;
        //calculate the peptide mass
        for (int j = 0; j < iAminoAcidSequence.size(); j++) {
            lCalculatedMass += iAminoAcidSequence.get(j).getMonoisotopicMass();
            //check if it is not modified
            for (int m = 0; m < iPeptideModificationPositions.size(); m++) {
                if (iPeptideModificationPositions.get(m).getPosition() == j) {
                    lCalculatedMass += iPeptideModifications.get(m).getDeltaMass();
                }
            }
        }
        lCalculatedMass = lCalculatedMass + 1.007825 + 15.994910 + 1.007825;
        lCalculatedMass = (lCalculatedMass + ((double) lCharge * 1.007825)) / (double) lCharge;
        return lCalculatedMass;
    }

    /**
     * @return the counter
     */
    public int getCounter() {
        return counter;
    }

    /**
     * @param counter the counter to set
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void setSequence(String iSequence) {
        this.iSequence = iSequence;
    }

    @Override
    public boolean equals(Object peptideLowMemToCompare) {
        boolean equals = false;
        if (peptideLowMemToCompare != null && peptideLowMemToCompare instanceof PeptideLowMem) {
            equals = (((PeptideLowMem) peptideLowMemToCompare).getPeptideId() == this.getPeptideId() && ((PeptideLowMem) peptideLowMemToCompare).getSequence().equalsIgnoreCase(this.getSequence()));
        }
        return equals;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.iPeptideId;
        hash = 97 * hash + (this.iSequence != null ? this.iSequence.hashCode() : 0);
        return hash;
    }
}
