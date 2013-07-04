package com.compomics.thermo_msf_parser_API.highmeminstance;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA. User: Niklaas Date: 18-Feb-2011 Time: 11:33:33
 */
public class Spectrum implements Cloneable {

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
    private List<Double> iSpectrumScores = new ArrayList<Double>();
    /**
     * A vector with the processing node numbers for the spectrum scores
     */
    private List<Integer> iSpectrumScoresProcessingNodeNumber = new ArrayList<Integer>();
    /**
     * The peptides added to this spectrum
     */
    private List<Peptide> iSpectrumPeptides = new ArrayList<Peptide>();
    /**
     * The scan event
     */
    private ScanEvent iScanEvent;
    /**
     * HashMap with the custom data field values. The key is the id of the
     * custom data field
     */
    private HashMap<Integer, String> iCustomDataFieldValues = new HashMap<Integer, String>();
    /**
     * The zipped spectrum xml byte array
     */
    private byte[] iZippedSpectrumXml;
    /**
     * The peptides linked to this spectrum
     */
    private List<Peptide> iPeptides = new ArrayList<Peptide>();
    /**
     * The decoy peptides linked to this spectrum
     */
    private List<Peptide> iPeptidesDecoy = new ArrayList<Peptide>();
    /**
     * The quan result linked to this spectrum
     */
    private QuanResult iQuanResult;
    /**
     * The connection to the msf file
     */
    private Connection iConnection;
    /**
     * The parser
     */
    private Parser iParser;

    /**
     * Constructor for the spectrum
     *
     * @param iSpectrumId The spectrum id
     * @param iUniqueSpectrumId The unique spectrum id
     * @param iMassPeakId The mass peak id
     * @param iLastScan The last scan
     * @param iFirstScan The first scan
     * @param iScan The scan
     * @param iCharge The charge
     * @param iRetentionTime The retention time
     * @param iMass The mass
     * @param iScanEventId The scan event id
     * @param iConnection The connection to the msf file
     * @param iParser The parser
     */
    public Spectrum(int iSpectrumId, int iUniqueSpectrumId, int iMassPeakId, int iLastScan, int iFirstScan, int iScan, int iCharge, double iRetentionTime, double iMass, int iScanEventId, Connection iConnection, Parser iParser) {
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
        this.iParser = iParser;
    }

    @Override
    public Spectrum clone() throws CloneNotSupportedException {
        //todo check if this works
        return (Spectrum) super.clone();
    }

    //getters
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

    public List<Double> getSpectrumScores() {
        return iSpectrumScores;
    }

    public List<Integer> getSpectrumScoresProcessingNodeNumber() {
        return iSpectrumScoresProcessingNodeNumber;
    }

    public List<Peptide> getSpectrumPeptides() {
        return iSpectrumPeptides;
    }

    public ScanEvent getScanEvent() {
        return iScanEvent;
    }

    public HashMap<Integer, String> getCustomDataFieldValues() {

        return iCustomDataFieldValues;
    }

    public byte[] getZippedSpectrumXml() throws SQLException {
        byte[] zippedSpectrumXML;
        if (iZippedSpectrumXml == null) {
            ResultSet rs;
            Statement stat = iConnection.createStatement();
            try {
                rs = stat.executeQuery(new StringBuilder().append("select * from Spectra where UniqueSpectrumID = ").append(iUniqueSpectrumId).toString());
                while (rs.next()) {
                    iZippedSpectrumXml = rs.getBytes("Spectrum");
                }
                rs.close();
            } finally {
                stat.close();
            }
        }
        zippedSpectrumXML = iZippedSpectrumXml;
        return zippedSpectrumXML;
    }

    public List<Peptide> getPeptides() {
        return iPeptides;
    }

    public QuanResult getQuanResult() {
        return iQuanResult;
    }

    /**
     * This method will unzip the zipped xml byte array
     *
     * @return A string with the spectrum xml
     * @throws Exception An exception is thrown when there is a problem with the
     * connection to the msf file
     */
    public String getUnzippedSpectrumXml() throws Exception {
        byte[] lZippedSpectrumXml = getZippedSpectrumXml();

        ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(lZippedSpectrumXml));

        ByteArrayOutputStream lStream = new ByteArrayOutputStream(50);
        while (in.getNextEntry() != null) {
            int count;
            byte[] data = new byte[50];
            while ((count = in.read(data, 0, 50)) != -1) {
                lStream.write(data, 0, count);
            }
        }

        String lResult = lStream.toString("UTF-8");
        lStream.close();
        return lResult;
    }

    /**
     * This method will give a vector with the MS/MS peaks found in the spectrum
     * xml
     *
     * @return vector with the MS/MS peaks found in the spectrum xml
     * @throws Exception An exception is thrown when there is a problem with the
     * connection to the msf file
     */
    public List<Peak> getMSMSPeaks() throws Exception {
        String lXml = getUnzippedSpectrumXml();
        List<Peak> lPeaks = new ArrayList<Peak>();
        try {
            lXml = lXml.substring(lXml.indexOf("<Peak ", lXml.indexOf("<PeakCentr")), lXml.lastIndexOf("</PeakCent"));
            String[] lLines = lXml.split("\n");
            for (int i = 0; i < lLines.length; i++) {
                if (lLines[i].trim().startsWith("<Peak ")) {
                    lPeaks.add(new Peak(lLines[i]));
                }
            }
        } catch (IndexOutOfBoundsException e) {
            //no peaks found
        }
        return lPeaks;
    }

    /**
     * This method will give a vector with the MS peaks found in the spectrum
     * xml
     *
     * @return vector with the MS peaks found in the spectrum xml
     * @throws Exception An exception is thrown when there is a problem with the
     * connection to the msf file
     */
    public List<Peak> getMSPeaks() throws Exception {
        String lXml = getUnzippedSpectrumXml();
        lXml = lXml.substring(lXml.indexOf("<Peak ", lXml.indexOf("<IsotopeClusterPeakCentroids")), lXml.lastIndexOf("</"));
        String[] lLines = lXml.split("\n");
        List<Peak> lPeaks = new ArrayList<Peak>();
        for (int i = 0; i < lLines.length; i++) {
            if (lLines[i].trim().startsWith("<Peak ")) {
                lPeaks.add(new Peak(lLines[i]));
            }
        }
        return lPeaks;
    }

    /**
     * This method will give the MS peak that is used for the fragmentation
     *
     * @return peak (MS) that is used for the fragmentation
     * @throws Exception An exception is thrown when there is a problem with the
     * connection to the msf file
     */
    public Peak getFragmentedMsPeak() throws Exception {
        String lXml = getUnzippedSpectrumXml();
        lXml = lXml.substring(lXml.indexOf("<MonoisotopicPeakCentroids"), lXml.lastIndexOf("</MonoisotopicPeakCentroids"));
        String[] lLines = lXml.split("\n");
        Peak lPeak = null;
        for (int i = 0; i < lLines.length; i++) {
            if (lLines[i].trim().startsWith("<Peak ")) {
                lPeak = new Peak(lLines[i]);
            }
        }
        return lPeak;
    }

    /**
     * This method will give the event linked to this
     *
     * @return Event
     * @throws SQLException An exception is thrown when there is a problem with
     * the connection to the msf file
     */
    public Event getEvent() throws SQLException {
        List lEventList = new ArrayList<Integer>();
        lEventList.add(iScanEvent.getScanEventId());
        List<Event> lEvents = Event.getEventByIds(lEventList, iConnection);
        return lEvents.get(0);

    }

    //setters
    public void setFileId(int iFileId) {
        this.iFileId = iFileId;
    }

    public void setZippedBytes(byte[] lZipped) {
        this.iZippedSpectrumXml = lZipped.clone();
    }

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
     * @param lPeptide
     */
    public void addPeptide(Peptide lPeptide) {
        this.iPeptides.add(lPeptide);
        lPeptide.setParentSpectrum(this);
    }

    /**
     * This will add a peptide to this spectrum
     *
     * @param lPeptide
     */
    public void addDecoyPeptide(Peptide lPeptide) {
        this.iPeptidesDecoy.add(lPeptide);
        lPeptide.setParentSpectrum(this);
    }

    public void setQuanResult(QuanResult lQuanResult) {
        if (iQuanResult != null) {
            System.out.println("Double quanresult added to the spectrum");
        }
        this.iQuanResult = lQuanResult;
    }

    public Connection getConnection() {
        return iConnection;
    }

    public Parser getParser() {
        return iParser;
    }

    public String getSpectrumTitle() {
        String lRawFile = iParser.getRawfileNameByFileId(iFileId);
        if (lRawFile == null) {
            lRawFile = "";
        } else {
            lRawFile = lRawFile.substring(0, lRawFile.toLowerCase().lastIndexOf("."));
        }

        return lRawFile + "_" + iSpectrumId + "_" + iFirstScan + "_" + iCharge;
    }

    public String getUniqueSpectrumTitle() {
        String lRawFile = iParser.getRawfileNameByFileId(iFileId);
        if (lRawFile == null) {
            lRawFile = "";
        } else {
            lRawFile = lRawFile.substring(0, lRawFile.lastIndexOf("."));
        }

        return lRawFile + "_" + iUniqueSpectrumId + "_" + iFirstScan + "_" + iCharge;

    }

    //TODO this looks wrong
    public boolean isHighestScoring(Peptide iSelectedPeptide, List<ScoreType> scoreTypes) {
        Boolean lIsHighestScore = null;
        for (int l = 0; l < scoreTypes.size(); l++) {
            if (iSelectedPeptide.getScoreByScoreType(scoreTypes.get(l)) != null) {
                Double lScore = iSelectedPeptide.getScoreByScoreType(scoreTypes.get(l));
                if (lIsHighestScore == null) {
                    lIsHighestScore = true;
                }
                if (lScore != null) {
                    for (int i = 0; i < iPeptides.size(); i++) {
                        if (!iSelectedPeptide.getModifiedPeptide().equalsIgnoreCase(iPeptides.get(i).getModifiedPeptide())) {
                            Double lCompareScore = iPeptides.get(i).getScoreByScoreType(scoreTypes.get(l));
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

    public boolean isLowestScoring(Peptide iSelectedPeptide, List<ScoreType> scoreTypes) {
        Boolean lIsLowestScore = null;
        for (int i = 0; i < scoreTypes.size(); i++) {
            if (iSelectedPeptide.getScoreByScoreType(scoreTypes.get(i)) != null) {
                Double lScore = iSelectedPeptide.getScoreByScoreType(scoreTypes.get(i));
                if (lIsLowestScore == null) {
                    lIsLowestScore = true;
                }
                if (lScore != null) {
                    for (int j = 0; j < iPeptides.size(); j++) {
                        if (!iSelectedPeptide.getModifiedPeptide().equalsIgnoreCase(iPeptides.get(j).getModifiedPeptide())) {
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
}
