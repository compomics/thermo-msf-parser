package com.compomics.thermo_msf_parser_API.highmeminstance;

import java.util.ArrayList;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Niklaas Date: 18-Feb-2011 Time: 09:17:23
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class Peptide {
    // Class specific log4j logger for Thermo_msf_parserGUI instances.

    private static Logger logger = Logger.getLogger(Peptide.class);
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
    private List<Double> iScores = new ArrayList<Double>();
    /**
     * The scoretypeids
     */
    private List<Integer> iScoreTypeIds = new ArrayList<Integer>();
    /**
     * The scoretype
     */
    private List<ScoreType> iScoreTypes = new ArrayList<ScoreType>();
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
    private List<Protein> iPeptideProteins = new ArrayList<Protein>();
    /**
     * The modifications linked to this peptide
     */
    private List<Modification> iPeptideModifications = new ArrayList<Modification>();
    /**
     * The modifications positions of the modifications in the
     * iPeptideModifications List
     */
    private List<ModificationPosition> iPeptideModificationPositions = new ArrayList<ModificationPosition>();
    /**
     * The site probabilities of the Phospho modifications
     */
    private List<Float> iPhosphoRSSiteProbabilities = new ArrayList<Float>();
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
    private HashMap<Integer, String> iCustomDataFieldValues = new HashMap<Integer, String>();
    /**
     * All the amino acids
     */
    private Map<Character, AminoAcid> iAminoAcids;
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
    private Spectrum iParentSpectrum;
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
    private int iUniquePeptideSequenceId;

    /**
     * Constructor for the peptide
     *
     * @param iPeptideId The peptide id
     * @param iSpectrumId The spectrum id
     * @param iConfidenceLevel The confidence level
     * @param iSequence The sequence
     * @param iTotalIonsCount The total ion count
     * @param iMatchedIonsCount The matched ion count
     * @param iAnnotation The annotation
     * @param iProcessingNodeNumber a int.
     * @param iAminoAcids The amino acids found in the msf file
     */
    public Peptide(int iPeptideId, int iSpectrumId, int iConfidenceLevel, String iSequence, int iTotalIonsCount, int iMatchedIonsCount, String iAnnotation, int iProcessingNodeNumber, Map<Character, AminoAcid> iAminoAcids) {
        this.iPeptideId = iPeptideId;
        this.iSpectrumId = iSpectrumId;
        this.iConfidenceLevel = iConfidenceLevel;
        this.iSequence = iSequence;
        this.iTotalIonsCount = iTotalIonsCount;
        this.iMatchedIonsCount = iMatchedIonsCount;
        this.iAnnotation = iAnnotation;
        this.iAminoAcids = iAminoAcids;
        this.iProcessingNodeNumber = iProcessingNodeNumber;
        iAminoAcidSequence = new ArrayList<AminoAcid>();
        for (int i = 0; i < iSequence.length(); i++) {
            Character lAaOneLetterCode = iSequence.toUpperCase().charAt(i);
            iAminoAcidSequence.add(iAminoAcids.get(lAaOneLetterCode));
        }

        if (iAminoAcidSequence.size() != iSequence.length()) {
            //error
            logger.error(iSequence);
        }

    }

    /**
     * This method will calculate the mass based on the charge
     *
     * @param lCharge The charge
     * @return double with the mass
     */
    public double getPeptideMassForCharge(int lCharge) {
        double lCalculatedMass = 0.0;
        //calculate the peptide mass
        for (int j = 0; j < iAminoAcidSequence.size(); j++) {
            lCalculatedMass = lCalculatedMass + iAminoAcidSequence.get(j).getMonoisotopicMass();
            //check if it is not modified
            for (int m = 0; m < iPeptideModificationPositions.size(); m++) {
                if (iPeptideModificationPositions.get(m).getPosition() == j) {
                    lCalculatedMass = lCalculatedMass + iPeptideModifications.get(m).getDeltaMass();
                }
            }
        }
        lCalculatedMass = lCalculatedMass + 1.007825 + 15.994910 + 1.007825;
        lCalculatedMass = (lCalculatedMass + ((double) lCharge * 1.007825)) / (double) lCharge;
        return lCalculatedMass;
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
    public List<ScoreType> getScoreTypes() {
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
    public List<Protein> getPeptideProteins() {
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
     * Getter for the amino acids found in the msf file
     *
     * @return vector with all the amino acids found in the msf file
     */
    public List<AminoAcid> getAminoAcids() {
        return new ArrayList(iAminoAcids.values());
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
    public Spectrum getParentSpectrum() {
        return iParentSpectrum;
    }

    /**
     * Setter for the parent spectrum
     *
     * @param lSpectrum The parent spectrum to set
     */
    public void setParentSpectrum(Spectrum lSpectrum) {
        this.iParentSpectrum = lSpectrum;
    }

    /**
     * Getter for a score by score type
     *
     * @param lScoreType The score type of the requested score
     * @return double with the score
     */
    public Double getScoreByScoreType(ScoreType lScoreType) {
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
     * @param iScoreTypeid the score type id the added score
     * @param lScoreTypes The different score types found in the msf file
     */
    public void setScore(double iScore, int iScoreTypeid, List<ScoreType> lScoreTypes) {
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
    public void addProtein(Protein lProtein) {
        iPeptideProteins.add(lProtein);
        lProtein.addPeptide(this);
    }

    /**
     * This method will add an amino acid modification to this peptide
     *
     * @param lMod The modification
     * @param lModPos The modification position
     * @param lModPos The modification position
     * @param pRSSiteMod a {@link java.lang.Float} object.
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
     * Getter for the proteins linked to this peptide
     *
     * @return List with the proteins linked to this peptide
     */
    public List<Protein> getProteins() {
        return iPeptideProteins;
    }

    /**
     * Getter for the modified peptide
     *
     * @return String with the modified peptide
     */
    public String getModifiedPeptide() {
        if (iModifiedPeptide == null) {
            //do the N terminus
            if (iHasNTermModification) {
                for (int m = 0; m < iPeptideModifications.size(); m++) {
                    if (iPeptideModifications.get(m).getPositionType() == 1) {
                        iModifiedPeptide = iPeptideModifications.get(m).getAbbreviation() + "-";
                    }
                }
            } else {
                iModifiedPeptide = "NH2-";
            }
            //do the middle
            for (int c = 0; c < iSequence.length(); c++) {
                iModifiedPeptide = iModifiedPeptide + iSequence.charAt(c);
                for (int m = 0; m < iPeptideModifications.size(); m++) {
                    if (iPeptideModificationPositions.get(m).getPosition() == c && !iPeptideModificationPositions.get(m).isNterm()) {

                        iModifiedPeptide = iModifiedPeptide + "<" + iPeptideModifications.get(m).getAbbreviation();
                        if (iPhosphoRSSiteProbabilities.get(m) != null) {
                            iModifiedPeptide += ":" + (iPhosphoRSSiteProbabilities.get(m) * 100) + "%";
                        }
                        iModifiedPeptide += ">";
                    }
                }
            }
            //do the C terminus
            iModifiedPeptide = iModifiedPeptide + "-COOH";

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
    public void calculateFragmentions(int iSpectrumCharge) {

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
                    bMass = bMass + iAminoAcidSequence.get(j).getMonoisotopicMass();
                    //check if it is not modified
                    for (int m = 0; m < iPeptideModificationPositions.size(); m++) {
                        if (iPeptideModificationPositions.get(m).getPosition() == j) {
                            bMass = bMass + iPeptideModifications.get(m).getDeltaMass();
                        }
                    }

                }
                // Each peptide mass is added to the y ion mass, taking the reverse direction (from the C terminal end)
                for (int j = 0; j <= i; j++) {
                    yMass = yMass + iAminoAcidSequence.get((iAminoAcidSequence.size() - 1) - j).getMonoisotopicMass();
                    for (int m = 0; m < iPeptideModificationPositions.size(); m++) {
                        if (iPeptideModificationPositions.get(m).getPosition() == (iAminoAcidSequence.size() - 1) - j) {
                            yMass = yMass + iPeptideModifications.get(m).getDeltaMass();
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
     * @param lTypes The typs of fragment ions wanted
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
     * {@inheritDoc}
     *
     * To string method
     */
    @Override
    public String toString() {
        return iSequence;
    }

    /**
     * <p>setMissedCleavage.</p>
     *
     * @param aMissedCleavage a int.
     */
    public void setMissedCleavage(int aMissedCleavage) {
        this.iMissedCleavage = aMissedCleavage;
    }

    /**
     * <p>setUniquePeptideSequenceId.</p>
     *
     * @param aUniquePeptideSequenceId a int.
     */
    public void setUniquePeptideSequenceId(int aUniquePeptideSequenceId) {
        this.iUniquePeptideSequenceId = aUniquePeptideSequenceId;
    }

    /**
     * <p>getMissedCleavage.</p>
     *
     * @return a int.
     */
    public int getMissedCleavage() {
        return iMissedCleavage;
    }

    /**
     * <p>getUniquePeptideSequenceId.</p>
     *
     * @return a int.
     */
    public int getUniquePeptideSequenceId() {
        return iUniquePeptideSequenceId;
    }

    /**
     * <p>addDecoyProtein.</p>
     *
     * @param aProtein a {@link com.compomics.thermo_msf_parser_API.highmeminstance.Protein} object.
     */
    public void addDecoyProtein(Protein aProtein) {
        iPeptideProteins.add(aProtein);
        aProtein.addDecoyPeptide(this);
    }

    /**
     * <p>setAnnotation.</p>
     *
     * @param aAnnotation a {@link java.lang.String} object.
     */
    public void setAnnotation(String aAnnotation) {
        this.iAnnotation = aAnnotation;
    }

    /**
     * <p>Setter for the field <code>phosphoRSScore</code>.</p>
     *
     * @param pRSScore a {@link java.lang.Float} object.
     */
    public void setPhosphoRSScore(Float pRSScore) {
        this.phosphoRSScore = pRSScore;
    }

    /**
     * <p>Getter for the field <code>phosphoRSScore</code>.</p>
     *
     * @return a {@link java.lang.Float} object.
     */
    public Float getPhosphoRSScore() {
        return phosphoRSScore;
    }

    /**
     * <p>Getter for the field <code>phoshpoRSSequenceProbability</code>.</p>
     *
     * @return a {@link java.lang.Float} object.
     */
    public Float getPhoshpoRSSequenceProbability() {
        return phoshpoRSSequenceProbability;
    }

    /**
     * <p>Setter for the field <code>phoshpoRSSequenceProbability</code>.</p>
     *
     * @param phoshpoRSSequenceProbability a {@link java.lang.Float} object.
     */
    public void setPhoshpoRSSequenceProbability(Float phoshpoRSSequenceProbability) {
        this.phoshpoRSSequenceProbability = phoshpoRSSequenceProbability;
    }

    /**
     * <p>getPhosphoRSSiteProbabilities.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Float> getPhosphoRSSiteProbabilities() {
        return iPhosphoRSSiteProbabilities;
    }
}
