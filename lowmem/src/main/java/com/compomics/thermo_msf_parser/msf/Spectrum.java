package com.compomics.thermo_msf_parser.msf;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA. User: Niklaas Date: 18-Feb-2011 Time: 11:33:33
 */
public class Spectrum {
    // Class specific log4j logger for Thermo_msf_parserGUI instances.

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
    private Vector<Peptide> iPeptides = new Vector<Peptide>();
    /**
     * The decoy peptides linked to this spectrum
     */
    private Vector<Peptide> iPeptidesDecoy = new Vector<Peptide>();
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
    private com.compomics.thermo_msf_parser.Parser iParser;

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
    public Spectrum(int iSpectrumId, int iUniqueSpectrumId, int iMassPeakId, int iLastScan, int iFirstScan, int iScan, int iCharge, double iRetentionTime, double iMass, int iScanEventId, Connection iConnection, com.compomics.thermo_msf_parser.Parser iParser) {
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
    public Spectrum clone() {
       return new Spectrum(iSpectrumId, iUniqueSpectrumId, iMassPeakId,
                iLastScan, iFirstScan, iScan,
                iCharge, iRetentionTime, iSinglyChargedMass,
                iScanEventId, iConnection, iParser);
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

    public byte[] getZippedSpectrumXml() throws SQLException {
        if (iZippedSpectrumXml == null) {
            ResultSet rs;
            Statement stat = iConnection.createStatement();
            rs = stat.executeQuery("select * from Spectra where UniqueSpectrumID = " + iUniqueSpectrumId);
            while (rs.next()) {
                iZippedSpectrumXml = rs.getBytes("Spectrum");
            }
            rs.close();
            stat.close();
        }
        return iZippedSpectrumXml;
    }

    public Vector<Peptide> getPeptides() {
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

        String lResult = lStream.toString();
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
    public Vector<Peak> getMSMSPeaks() throws Exception {
        String lXml = getUnzippedSpectrumXml();
        Vector<Peak> lPeaks = new Vector<Peak>();
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
    public Vector<Peak> getMSPeaks() throws Exception {
        String lXml = getUnzippedSpectrumXml();
        lXml = lXml.substring(lXml.indexOf("<Peak ", lXml.indexOf("<IsotopeClusterPeakCentroids")), lXml.lastIndexOf("</"));
        String[] lLines = lXml.split("\n");
        Vector<Peak> lPeaks = new Vector<Peak>();
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
        Vector lEventVector = new Vector<Integer>();
        lEventVector.add(iScanEvent.getScanEventId());
        Vector<Event> lEvents = Event.getEventByIds(lEventVector, iConnection);
        return lEvents.get(0);

    }

    //setters
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

    public com.compomics.thermo_msf_parser.Parser getParser() {
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
    public boolean isHighestScoring(Peptide iSelectedPeptide, Vector<ScoreType> scoreTypes) {
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

    public boolean isLowestScoring(Peptide iSelectedPeptide, Vector<ScoreType> scoreTypes) {
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
