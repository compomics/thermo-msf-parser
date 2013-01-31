package com.compomics.thermo_msf_parser.msf;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 10/1/12 Time: 11:08 AM To change
 * this template use File | Settings | File Templates.
 */
public class SpectrumLowMemController implements SpectrumInterface {

    private static Logger logger = Logger.getLogger(PeptideLowMemController.class);

    public String createSpectrumXMLForPeptide(int peptideID, Connection aConnection) throws SQLException, IOException {
        byte[] lZippedSpectrumXml = null;
        String lXml;
        ResultSet rs;
        Statement stat = aConnection.createStatement();
        rs = stat.executeQuery("select Spectrum from Spectra where UniqueSpectrumID = (select SpectrumID from Peptides where Peptides.PeptideID = " + peptideID + ")");
        while (rs.next()) {
            lZippedSpectrumXml = rs.getBytes(1);
        }

        if (lZippedSpectrumXml == null) {
        }
        File lZippedFile = File.createTempFile("zip", null);
        FileOutputStream fos = new FileOutputStream(lZippedFile);
        fos.write(lZippedSpectrumXml);
        fos.flush();
        fos.close();
        BufferedOutputStream out = null;
        ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(lZippedFile)));
        ZipEntry entry;
        byte[] ltest = new byte[0];
        ByteArrayOutputStream lStream = new ByteArrayOutputStream(50);
        while ((entry = in.getNextEntry()) != null) {
            int count;
            byte data[] = new byte[50];
            out = new BufferedOutputStream(lStream, 50);
            while ((count = in.read(data, 0, 50)) != -1) {
                out.write(data, 0, count);
            }
        }
        in.close();
        out.flush();
        out.close();
        lZippedFile.delete();
        lXml = lStream.toString();
        lStream.close();
        rs.close();
        stat.close();
        return lXml;
    }

    /**
     *
     * @param lXml the spectrum xml to return the msms peaks from
     * @return a vector containing all the MSMS peak objects retrieved from the
     * xml
     * @throws Exception
     */
    public Vector<Peak> getMSMSPeaks(String lXml) throws Exception {
        Vector<Peak> lPeaks = new Vector<Peak>();
        try {
            String xmlSubstring = lXml.substring(lXml.indexOf("<Peak ", lXml.indexOf("<PeakCentr")), lXml.lastIndexOf("</"));
            String[] lLines = xmlSubstring.split("\n");
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
     *
     * @param lXml the spectrum xml we want the MS peak from
     * @return a vector containing all the MS peak objects retrieved from the
     * xml
     * @throws Exception
     */
    public Vector<Peak> getMSPeaks(String lXml) throws Exception {
        String xmlSubstring = lXml.substring(lXml.indexOf("<Peak ", lXml.indexOf("<IsotopeClusterPeakCentroids")), lXml.lastIndexOf("</"));
        String[] lLines = xmlSubstring.split("\n");
        Vector<Peak> lPeaks = new Vector<Peak>();
        for (int i = 0; i < lLines.length; i++) {
            if (lLines[i].trim().startsWith("<Peak ")) {
                lPeaks.add(new Peak(lLines[i]));
            }
        }
        return lPeaks;
    }

    /**
     *
     * @param lXml the spectrum xml we want the fragmented peak from
     * @return a peak object containing the fragmented MS peak
     * @throws Exception
     */
    public Peak getFragmentedMsPeak(String lXml) throws Exception {
        String xmlSubstring = lXml.substring(lXml.indexOf("<MonoisotopicPeakCentroids"), lXml.lastIndexOf("</"));
        String[] lLines = xmlSubstring.split("\n");
        Peak lPeak = null;
        for (int i = 0; i < lLines.length; i++) {
            if (lLines[i].trim().startsWith("<Peak ")) {
                lPeak = new Peak(lLines[i]);
            }
        }
        return lPeak;
    }

    /**
     *
     * @param aConnection connection to the msf file
     * @return a vector containing all the spectra (in spectrum objects) stored
     * in the msf file
     * @throws SQLException
     */
    public Vector<SpectrumLowMem> getAllSpectra(Connection aConnection) {
        Vector<SpectrumLowMem> iSpectra = new Vector<SpectrumLowMem>();
        try {
            PreparedStatement stat = aConnection.prepareStatement("select s.*, FileID from spectrumheaders as s, masspeaks where masspeaks.masspeakid = s.masspeakid");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                SpectrumLowMem lSpectrum = new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), aConnection);
                lSpectrum.setFileId(rs.getInt("FileID"));
                iSpectra.add(lSpectrum);
            }
            rs.close();
            stat.close();
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return iSpectra;
    }

    public SpectrumLowMem getSpectrumForPeptideID(int peptideOfInterestID, Connection aConnection) {
        SpectrumLowMem returnSpectrum = null;
        try {
            Statement stat = aConnection.createStatement();
            ResultSet rs;
            rs = stat.executeQuery("select s.*, FileID from spectrumheaders as s, masspeaks,(select SpectrumID from Peptides where Peptideid = " + peptideOfInterestID + ") as specid where masspeaks.masspeakid = s.masspeakid and s.SpectrumID = specid.SpectrumID");
            rs.next();
            returnSpectrum = new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), aConnection);
            returnSpectrum.setFileId(rs.getInt("FileID"));
            rs = stat.executeQuery("select * from CustomDataSpectra where SpectrumID = " + returnSpectrum.getSpectrumId());
            while (rs.next()) {
                returnSpectrum.addCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
            }
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return returnSpectrum;
    }

    public SpectrumLowMem getSpectrumForSpectrumID(int spectrumOfInterestID, Connection aConnection) {
        SpectrumLowMem returnSpectrum = null;
        try {
            Statement stat = aConnection.createStatement();
            ResultSet rs;
            rs = stat.executeQuery("select s.*, FileID from spectrumheaders as s, masspeaks where masspeaks.masspeakid = s.masspeakid and s.SpectrumID = " + spectrumOfInterestID);
            rs.next();
            returnSpectrum = new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), aConnection);
            returnSpectrum.setFileId(rs.getInt("FileID"));
            rs = stat.executeQuery("select * from CustomDataSpectra where SpectrumID = " + returnSpectrum.getSpectrumId());
            while (rs.next()) {
                returnSpectrum.addCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
            }
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return returnSpectrum;
    }

    /**
     *
     * @param rawFileName the raw file name connected to the spectrum
     * @param lspectrum the spectrum object we want the title from
     * @return a processed title
     */
    public String getSpectrumTitle(String rawFileName, SpectrumLowMem lspectrum) {
        String spectrumTitle = rawFileName.substring(0, rawFileName.toLowerCase().lastIndexOf("."));
        return spectrumTitle + "_" + lspectrum.getSpectrumId() + "_" + lspectrum.getFirstScan() + "_" + lspectrum.getCharge();
    }

    /**
     *
     * @param lSpectrum
     * @return the unzipped XML file in a string object
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public void createSpectrumXMLForSpectrum(SpectrumLowMem lSpectrum) throws SQLException, IOException {
        byte[] lZippedSpectrumXml = null;
        String lXml;
        ResultSet rs;
        Statement stat = lSpectrum.getConnection().createStatement();
        rs = stat.executeQuery("select Spectrum from Spectra where UniqueSpectrumID = " + lSpectrum.getUniqueSpectrumId());
        while (rs.next()) {
            lZippedSpectrumXml = rs.getBytes(1);
        }
        File lZippedFile = File.createTempFile("zip", null);
        FileOutputStream fos = new FileOutputStream(lZippedFile);
        fos.write(lZippedSpectrumXml);
        fos.flush();
        fos.close();
        BufferedOutputStream out = null;
        ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(lZippedFile)));
        ZipEntry entry;
        byte[] ltest = new byte[0];
        ByteArrayOutputStream lStream = new ByteArrayOutputStream(50);
        while ((entry = in.getNextEntry()) != null) {
            int count;
            byte data[] = new byte[50];
            out = new BufferedOutputStream(lStream, 50);
            while ((count = in.read(data, 0, 50)) != -1) {
                out.write(data, 0, count);
            }
        }
        in.close();
        out.flush();
        out.close();
        lZippedFile.delete();
        lXml = lStream.toString();
        lStream.close();
        rs.close();
        stat.close();
        lSpectrum.addSpectrumXML(lXml);
    }

    public void unzipXMLforSpectrum(SpectrumLowMem spectrum) {
        File lZippedFile = null;
        String lXml = "";
        try {
            if (spectrum.getZippedSpectrumXml() == null) {
                try {
                    createSpectrumXMLForSpectrum(spectrum);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                byte[] lZippedSpectrumXml = spectrum.getZippedSpectrumXml();
                lZippedFile = File.createTempFile("zip", null);
                FileOutputStream fos = new FileOutputStream(lZippedFile);
                fos.write(lZippedSpectrumXml);
                fos.flush();
                fos.close();
                BufferedOutputStream out = null;
                ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(lZippedFile)));
                ZipEntry entry;
                byte[] ltest = new byte[0];
                ByteArrayOutputStream lStream = new ByteArrayOutputStream(50);
                while ((entry = in.getNextEntry()) != null) {
                    int count;
                    byte data[] = new byte[50];
                    out = new BufferedOutputStream(lStream, 50);
                    while ((count = in.read(data, 0, 50)) != -1) {
                        out.write(data, 0, count);
                    }
                }
                in.close();
                out.flush();
                out.close();
                lZippedFile.delete();
                lXml = lStream.toString();
                lStream.close();
                spectrum.addSpectrumXML(lXml);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public int getNumberOfSpectra(Connection msfFileConnection) {
        int numberOfSpectra = 0;
        try {
            Statement stat = msfFileConnection.createStatement();
            ResultSet rs = stat.executeQuery("select count(SpectrumID) from SpectrumHeaders");
            rs.next();
            numberOfSpectra = rs.getInt(1);
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return numberOfSpectra;
    }

    public HashMap<Integer, SpectrumLowMem> getSpectraMap(Connection msfFileConnection) {
        HashMap<Integer, SpectrumLowMem> spectraMap = new HashMap<Integer, SpectrumLowMem>();
        try {
            PreparedStatement stat = msfFileConnection.prepareStatement("select s.*, FileID from spectrumheaders as s, masspeaks where masspeaks.masspeakid = s.masspeakid");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                SpectrumLowMem lSpectrum = new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), msfFileConnection);
                lSpectrum.setFileId(rs.getInt("FileID"));
                spectraMap.put(rs.getInt("SpectrumID"), lSpectrum);
            }
            rs.close();
            stat.close();
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return spectraMap;
    }
}
