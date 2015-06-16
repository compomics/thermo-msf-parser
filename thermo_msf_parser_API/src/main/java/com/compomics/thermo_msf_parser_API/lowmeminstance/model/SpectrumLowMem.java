package com.compomics.thermo_msf_parser_API.lowmeminstance.model;

import com.compomics.thermo_msf_parser_API.highmeminstance.ScanEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 4/23/12 Time: 3:53 PM To change
 * this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class SpectrumLowMem {

    /**
     * The spectrum id
     */
    private final int iSpectrumId;
    /**
     * The unique spectrum id
     */
    private final int iUniqueSpectrumId;
    /**
     * The mass peak id
     */
    private final int iMassPeakId;
    /**
     * The last scan
     */
    private final int iLastScan;
    /**
     * The first scan
     */
    private final int iFirstScan;
    /**
     * The scan
     */
    private final int iScan;
    /**
     * The charge
     */
    private final int iCharge;
    /**
     * The retention time
     */
    private final double iRetentionTime;
    /**
     * The singly charged mass
     */
    private final double iSinglyChargedMass;
    /**
     * The scan event id
     */
    private final int iScanEventId;
    /**
     * The file id
     */
    private int iFileId;
    /**
     * A vector with spectrum scores
     */
    private final List<Double> iSpectrumScores = new ArrayList<Double>();
    /**
     * A vector with the processing node numbers for the spectrum scores
     */
    private final List<Integer> iSpectrumScoresProcessingNodeNumber = new ArrayList<Integer>();
    /**
     * The peptides added to this spectrum
     */
    private final List<PeptideLowMem> iSpectrumPeptides = new ArrayList<PeptideLowMem>();
    /**
     * The scan event
     */
    private ScanEvent iScanEvent;
    /**
     * HashMap with the custom data field values. The key is the id of the
     * custom data field
     */
    private final HashMap<Integer, String> iCustomDataFieldValues = new HashMap<Integer, String>();
    /**
     * The zipped spectrum xml byte array
     */
    private byte[] iZippedSpectrumXml;
    /**
     * The peptides linked to this spectrum
     */
    private final List<PeptideLowMem> iPeptides = new ArrayList<PeptideLowMem>();
    /**
     * The decoy peptides linked to this spectrum
     */
    private final List<PeptideLowMem> iPeptidesDecoy = new ArrayList<PeptideLowMem>();
    /**
     * The quan result linked to this spectrum
     */
    private QuanResultLowMem iQuanResult;
    private String spectrumXML;

    //TODo add check if file is unzipped already
    /**
     * <p>Constructor for SpectrumLowMem.</p>
     *
     * @param iSpectrumId a int.
     * @param iUniqueSpectrumId a int.
     * @param iMassPeakId a int.
     * @param iLastScan a int.
     * @param iFirstScan a int.
     * @param iScan a int.
     * @param iCharge a int.
     * @param iRetentionTime a double.
     * @param iMass a double.
     * @param iScanEventId a int.
     */
    public SpectrumLowMem(int iSpectrumId, int iUniqueSpectrumId, int iMassPeakId, int iLastScan, int iFirstScan, int iScan, int iCharge, double iRetentionTime, double iMass, int iScanEventId) {
        this.iSpectrumId = iSpectrumId;
        this.iUniqueSpectrumId = iUniqueSpectrumId;
        this.iMassPeakId = iMassPeakId;
        this.iLastScan = iLastScan;
        this.iFirstScan = iFirstScan;
        this.iScan = iScan;
        this.iCharge = iCharge;
        this.iRetentionTime = iRetentionTime;
        this.iSinglyChargedMass = iMass;
        this.iScanEventId = iScanEventId;
    }

    /**
     * <p>getSpectrumId.</p>
     *
     * @return a int.
     */
    public int getSpectrumId() {
        return iSpectrumId;
    }

    /**
     * <p>getUniqueSpectrumId.</p>
     *
     * @return a int.
     */
    public int getUniqueSpectrumId() {
        return iUniqueSpectrumId;
    }

    /**
     * <p>getMassPeakId.</p>
     *
     * @return a int.
     */
    public int getMassPeakId() {
        return iMassPeakId;
    }

    /**
     * <p>getLastScan.</p>
     *
     * @return a int.
     */
    public int getLastScan() {
        return iLastScan;
    }

    /**
     * <p>getFirstScan.</p>
     *
     * @return a int.
     */
    public int getFirstScan() {
        return iFirstScan;
    }

    /**
     * <p>getScan.</p>
     *
     * @return a int.
     */
    public int getScan() {
        return iScan;
    }

    /**
     * <p>getCharge.</p>
     *
     * @return a int.
     */
    public int getCharge() {
        return iCharge;
    }

    /**
     * <p>getRetentionTime.</p>
     *
     * @return a double.
     */
    public double getRetentionTime() {
        return iRetentionTime;
    }

    /**
     * <p>getSinglyChargedMass.</p>
     *
     * @return a double.
     */
    public double getSinglyChargedMass() {
        return iSinglyChargedMass;
    }

    /**
     * <p>getMz.</p>
     *
     * @return a double.
     */
    public double getMz() {
        return (iSinglyChargedMass + ((double) (iCharge - 1)) * 1.007825) / (double) iCharge;
    }

    /**
     * <p>getScanEventId.</p>
     *
     * @return a int.
     */
    public int getScanEventId() {
        return iScanEventId;
    }

    /**
     * <p>getFileId.</p>
     *
     * @return a int.
     */
    public int getFileId() {
        return iFileId;
    }

    /**
     * <p>getSpectrumScores.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Double> getSpectrumScores() {
        return iSpectrumScores;
    }

    /**
     * <p>getSpectrumScoresProcessingNodeNumber.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Integer> getSpectrumScoresProcessingNodeNumber() {
        return iSpectrumScoresProcessingNodeNumber;
    }

    /**
     * <p>getSpectrumPeptides.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<PeptideLowMem> getSpectrumPeptides() {
        return iSpectrumPeptides;
    }

    /**
     * <p>getScanEvent.</p>
     *
     * @return a {@link com.compomics.thermo_msf_parser_API.highmeminstance.ScanEvent} object.
     */
    public ScanEvent getScanEvent() {
        return iScanEvent;
    }

    /**
     * <p>getCustomDataFieldValues.</p>
     *
     * @return a {@link java.util.HashMap} object.
     */
    public HashMap<Integer, String> getCustomDataFieldValues() {

        return iCustomDataFieldValues;
    }

    /**
     * <p>getPeptides.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<PeptideLowMem> getPeptides() {
        return iPeptides;
    }

    /**
     * <p>getQuanResult.</p>
     *
     * @return a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.QuanResultLowMem} object.
     */
    public QuanResultLowMem getQuanResult() {
        return iQuanResult;
    }

    /**
     * <p>getZippedSpectrumXml.</p>
     *
     * @return an array of byte.
     */
    public byte[] getZippedSpectrumXml() {
        byte[] zippedXMLToReturn = null;
        if (iZippedSpectrumXml != null) {
            zippedXMLToReturn = iZippedSpectrumXml.clone();
        }
        return zippedXMLToReturn;
    }

    /**
     * <p>setFileId.</p>
     *
     * @param iFileId a int.
     */
    public void setFileId(int iFileId) {
        this.iFileId = iFileId;
    }

    /**
     * <p>setZippedSpectrumXML.</p>
     *
     * @param lZipped an array of byte.
     */
    public void setZippedSpectrumXML(byte[] lZipped) {
        this.iZippedSpectrumXml = lZipped;
    }

    /**
     * <p>setScanEvent.</p>
     *
     * @param iScanEvent a {@link com.compomics.thermo_msf_parser_API.highmeminstance.ScanEvent} object.
     */
    public void setScanEvent(ScanEvent iScanEvent) {
        this.iScanEvent = iScanEvent;
    }

    /**
     * This will add score and a processing node number
     *
     * @param lScore The score
     * @param lProcessingNodeNumber The processing node number
     */
    public void addSpectrumScore(double lScore, int lProcessingNodeNumber) {
        this.iSpectrumScores.add(lScore);
        this.iSpectrumScoresProcessingNodeNumber.add(lProcessingNodeNumber);
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
     * This will add a peptide to this spectrum
     *
     * @param lPeptide a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem} object.
     */
    public void addPeptide(PeptideLowMem lPeptide) {
        this.iPeptides.add(lPeptide);
        lPeptide.setParentSpectrum(this);
    }

    /**
     * This will add a peptide to this spectrum
     *
     * @param lPeptide a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem} object.
     */
    public void addDecoyPeptide(PeptideLowMem lPeptide) {
        this.iPeptidesDecoy.add(lPeptide);
        lPeptide.setParentSpectrum(this);
    }

    /**
     * <p>setQuanResult.</p>
     *
     * @param lQuanResult a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.QuanResultLowMem} object.
     */
    public void setQuanResult(QuanResultLowMem lQuanResult) {
        if (iQuanResult != null) {
            System.out.println("Double quanresult added to the spectrum");
        }
        this.iQuanResult = lQuanResult;
    }

    //TODO this looks wrong
    /**
     * <p>isHighestScoring.</p>
     *
     * @param iSelectedPeptide a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem} object.
     * @param scoreTypes a {@link java.util.List} object.
     * @return a boolean.
     */
    public boolean isHighestScoring(PeptideLowMem iSelectedPeptide, List<ScoreTypeLowMem> scoreTypes) {
        Boolean lIsHighestScore = null;
        for (ScoreTypeLowMem scoreType : scoreTypes) {
            if (iSelectedPeptide.getScoreByScoreType(scoreType) != null) {
                Double lScore = iSelectedPeptide.getScoreByScoreType(scoreType);
                if (lIsHighestScore == null) {
                    lIsHighestScore = true;
                }
                if (lScore != null) {
                    for (PeptideLowMem iPeptide : iPeptides) {
                        if (!iSelectedPeptide.getModifiedPeptideSequence().equalsIgnoreCase(iPeptide.getModifiedPeptideSequence())) {
                            Double lCompareScore = iPeptide.getScoreByScoreType(scoreType);
                            if (lCompareScore != null) {
                                if (lScore < lCompareScore) {
                                    lIsHighestScore = false;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (lIsHighestScore == null) {
            lIsHighestScore = false;
        }
        return lIsHighestScore;
    }

    /**
     * <p>isLowestScoring.</p>
     *
     * @param iSelectedPeptide a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem} object.
     * @param scoreTypes a {@link java.util.List} object.
     * @return a boolean.
     */
    public boolean isLowestScoring(PeptideLowMem iSelectedPeptide, List<ScoreTypeLowMem> scoreTypes) {
        Boolean lIsLowestScore = null;
        for (ScoreTypeLowMem scoreType : scoreTypes) {
            if (iSelectedPeptide.getScoreByScoreType(scoreType) != null) {
                Double lScore = iSelectedPeptide.getScoreByScoreType(scoreType);
                if (lIsLowestScore == null) {
                    lIsLowestScore = true;
                }
                if (lScore != null) {
                    for (int j = 0; j < iPeptides.size(); j++) {
                        if (!iSelectedPeptide.getModifiedPeptideSequence().equalsIgnoreCase(iPeptides.get(j).getModifiedPeptideSequence())) {
                            Double lCompareScore = iPeptides.get(j).getScoreByScoreType(scoreTypes.get(j));
                            if (lCompareScore != null) {
                                if (lScore > lCompareScore) {
                                    lIsLowestScore = false;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (lIsLowestScore == null) {
            lIsLowestScore = false;
        }
        return lIsLowestScore;
    }

    /**
     * <p>Getter for the field <code>spectrumXML</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSpectrumXML() {
        return spectrumXML;
    }

    /**
     * <p>addSpectrumXML.</p>
     *
     * @param aSpectrumXML a {@link java.lang.String} object.
     */
    public void addSpectrumXML(String aSpectrumXML) {
        spectrumXML = aSpectrumXML;
    }

    /**
     * <p>Getter for the field <code>iPeptidesDecoy</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<PeptideLowMem> getiPeptidesDecoy() {
        return Collections.unmodifiableList(iPeptidesDecoy);
    }
}
