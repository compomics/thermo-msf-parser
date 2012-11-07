package com.compomics.thermo_msf_parser.msf;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/23/12
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpectrumLowMem {

    private static Logger logger = Logger.getLogger(Spectrum.class);
    /**
     * The spectrum id
     */
    private int iSpectrumId;
    /**
     * The unique spectrum id
     */
    private int iUniqueSpectrumId;
    /**
     * The mass peak id
     */
    private int iMassPeakId;
    /**
     * The last scan
     */
    private int iLastScan;
    /**
     * The first scan
     */
    private int iFirstScan;
    /**
     * The scan
     */
    private int iScan;
    /**
     * The charge
     */
    private int iCharge;
    /**
     * The retention time
     */
    private double iRetentionTime;
    /**
     * The singly charged mass
     */
    private double iSinglyChargedMass;
    /**
     * The scan event id
     */
    private int iScanEventId;
    /**
     * The file id
     */
    private int iFileId;
    /**
     * A vector with spectrum scores
     */
    private Vector<Double> iSpectrumScores = new Vector<Double>();
    /**
     * A vector with the processing node numbers for the spectrum scores
     */
    private Vector<Integer> iSpectrumScoresProcessingNodeNumber = new Vector<Integer>();
    /**
     * The peptides added to this spectrum
     */
    private Vector<Peptide> iSpectrumPeptides = new Vector<Peptide>();
    /**
     * The scan event
     */
    private ScanEvent iScanEvent;
    /**
     * HashMap with the custom data field values. The key is the id of the custom data field
     */
    private HashMap<Integer, String> iCustomDataFieldValues = new HashMap<Integer, String>();
    /**
     * The zipped spectrum xml byte array
     */
    private byte[] iZippedSpectrumXml;
    /**
     * The peptides linked to this spectrum
     */
    private Vector<PeptideLowMem> iPeptides = new Vector<PeptideLowMem>();
    /**
     * The decoy peptides linked to this spectrum
     */
    private Vector<PeptideLowMem> iPeptidesDecoy = new Vector<PeptideLowMem>();
    /**
     * The quan result linked to this spectrum
     */
    private QuanResultLowMem iQuanResult;
    /**
     * The connection to the msf file
     */
    private Connection iConnection;
    
    private String spectrumXML;

    //TODo add check if file is unzipped already

    public SpectrumLowMem(int iSpectrumId, int iUniqueSpectrumId, int iMassPeakId, int iLastScan, int iFirstScan, int iScan, int iCharge, double iRetentionTime, double iMass, int iScanEventId, Connection iConnection) {
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
        this.iConnection = iConnection;
    }

    public int getSpectrumId() {
        return iSpectrumId;
    }

    public int getUniqueSpectrumId() {
        return iUniqueSpectrumId;
    }

    public int getMassPeakId() {
        return iMassPeakId;
    }

    public int getLastScan() {
        return iLastScan;
    }

    public int getFirstScan() {
        return iFirstScan;
    }

    public int getScan() {
        return iScan;
    }

    public int getCharge() {
        return iCharge;
    }

    public double getRetentionTime() {
        return iRetentionTime;
    }

    public double getSinglyChargedMass() {
        return iSinglyChargedMass;
    }

    public double getMz() {
        return (iSinglyChargedMass + ((double) (iCharge - 1)) * 1.007825) / (double) iCharge;
    }


    public int getScanEventId() {
        return iScanEventId;
    }

    public int getFileId() {
        return iFileId;
    }

    public Vector<Double> getSpectrumScores() {
        return iSpectrumScores;
    }

    public Vector<Integer> getSpectrumScoresProcessingNodeNumber() {
        return iSpectrumScoresProcessingNodeNumber;
    }

    public Vector<Peptide> getSpectrumPeptides() {
        return iSpectrumPeptides;
    }

    public ScanEvent getScanEvent() {
        return iScanEvent;
    }

    public HashMap<Integer, String> getCustomDataFieldValues() {

        return iCustomDataFieldValues;
    }

    public Vector<PeptideLowMem> getPeptides() {
        return iPeptides;
    }

    public QuanResultLowMem getQuanResult() {
        return iQuanResult;
    }

    public byte[] getZippedSpectrumXml(){
        return iZippedSpectrumXml;
    }

    public void setFileId(int iFileId) {
        this.iFileId = iFileId;
    }

    public void setZippedBytes(byte[] lZipped) {
        this.iZippedSpectrumXml = lZipped;
    }

    public void setScanEvent(ScanEvent iScanEvent) {
        this.iScanEvent = iScanEvent;
    }

    /**
     * This will add score and a processing node number
     *
     * @param lScore                The score
     * @param lProcessingNodeNumber The processing node number
     */
    public void addSpectrumScore(double lScore, int lProcessingNodeNumber) {
        this.iSpectrumScores.add(lScore);
        this.iSpectrumScoresProcessingNodeNumber.add(lProcessingNodeNumber);
    }

    /**
     * This method will add a value in the custom data field map by the id off the custom data field
     *
     * @param lId    The custom data field id
     * @param lValue The value to add
     */
    public void addCustomDataField(int lId, String lValue) {
        iCustomDataFieldValues.put(lId, lValue);
    }

    /**
     * This will add a peptide to this spectrum
     *
     * @param lPeptide
     */
    public void addPeptide(PeptideLowMem lPeptide) {
        this.iPeptides.add(lPeptide);
        lPeptide.setParentSpectrum(this);
    }

    /**
     * This will add a peptide to this spectrum
     *
     * @param lPeptide
     */
    public void addDecoyPeptide(PeptideLowMem lPeptide) {
        this.iPeptidesDecoy.add(lPeptide);
        lPeptide.setParentSpectrum(this);
    }

    public void setQuanResult(QuanResultLowMem lQuanResult) {
        if (iQuanResult != null) {
            System.out.println("Double quanresult added to the spectrum");
        }
        this.iQuanResult = lQuanResult;
    }

    public Connection getConnection() {
        return iConnection;
    }

    public boolean isHighestScoring(PeptideLowMem iSelectedPeptide, Vector<ScoreType> scoreTypes) {
        Boolean lIsHighestScore = null;
        for(int l = 0; l<scoreTypes.size(); l++){
            if (iSelectedPeptide.getScoreByScoreType(scoreTypes.get(l)) != null) {
                Double lScore = iSelectedPeptide.getScoreByScoreType(scoreTypes.get(l));
                if(lIsHighestScore == null){
                    lIsHighestScore = true;
                }
                if(lScore != null){
                    for (int i = 0; i < iPeptides.size(); i++) {
                        if (!iSelectedPeptide.getModifiedPeptide().equalsIgnoreCase(iPeptides.get(i).getModifiedPeptide())) {
                            Double lCompareScore = iPeptides.get(i).getScoreByScoreType(scoreTypes.get(l));
                            if(lCompareScore != null){
                                if (lScore < lCompareScore) {
                                    lIsHighestScore = false;
                                }
                            }
                        }
                    }
                }
            }
        }

        if(lIsHighestScore == null){
            lIsHighestScore = false;
        }
        return lIsHighestScore;
    }

    public boolean isLowestScoring(PeptideLowMem iSelectedPeptide, Vector<ScoreType> scoreTypes) {
        Boolean lIsLowestScore = null;
        for(int i = 0; i<scoreTypes.size(); i ++){
            if (iSelectedPeptide.getScoreByScoreType(scoreTypes.get(i)) != null) {
                Double lScore = iSelectedPeptide.getScoreByScoreType(scoreTypes.get(i));
                if(lIsLowestScore == null){
                    lIsLowestScore = true;
                }
                if(lScore != null){
                    for (int j = 0; j < iPeptides.size(); j++) {
                        if (!iSelectedPeptide.getModifiedPeptide().equalsIgnoreCase(iPeptides.get(j).getModifiedPeptide())) {
                            Double lCompareScore = iPeptides.get(j).getScoreByScoreType(scoreTypes.get(j));
                            if(lCompareScore != null){
                                if (lScore > lCompareScore) {
                                    lIsLowestScore = false;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(lIsLowestScore == null){
            lIsLowestScore = false;
        }
        return lIsLowestScore;
    }

    public String getSpectrumXML(){
        return spectrumXML;
    }
    
    public void addSpectrumXML(String aSpectrumXML) {
        spectrumXML = aSpectrumXML;
    }
}
