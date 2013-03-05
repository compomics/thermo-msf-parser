package com.compomics.thermo_msf_parser.msf;

import org.apache.log4j.Logger;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 10/1/12 Time: 11:08 AM To change
 * this template use File | Settings | File Templates.
 */
public class SpectrumLowMemController implements SpectrumInterface {

    private static final Logger logger = Logger.getLogger(SpectrumLowMemController.class);

    public String createSpectrumXMLForPeptide(PeptideLowMem peptide, Connection aConnection) throws SQLException, IOException {
        byte[] lZippedSpectrumXml = null;
        String lXml = peptide.getParentSpectrum().getSpectrumXML();
        if (lXml == null) {
            ResultSet rs;
            Statement stat = aConnection.createStatement();
            rs = stat.executeQuery("select Spectrum from Spectra where UniqueSpectrumID = (select SpectrumID from Peptides where Peptides.PeptideID = " + peptide.getPeptideId() + ")");
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
            ByteArrayOutputStream lStream = new ByteArrayOutputStream(50);
            out = new BufferedOutputStream(lStream, 50);
            while ((entry = in.getNextEntry()) != null) {
                int count;
                byte data[] = new byte[50];
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
        } else {
            return lXml;
        }
    }

    /**
     *
     * @param lXml the spectrum xml to return the msms peaks from
     * @return a vector containing all the MSMS peak objects retrieved from the
     * xml
     * @throws Exception
     */
    @Override
    public Vector<Peak> getMSMSPeaks(String lXml) {
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
    @Override
    public Vector<Peak> getMSPeaks(String lXml) {
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
     */
    @Override
    public Peak getFragmentedMsPeak(String lXml) {
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
    
    
        /**
     *
     * @param aConnection connection to the msf file
     * @return a vector containing all the spectra (in spectrum objects) stored
     * in the msf file
     * @throws SQLException
     */
    public Vector<Integer> getAllSpectraIds(Connection aConnection) {
        Vector<Integer> iSpectra = new Vector<Integer>();
        try {
            PreparedStatement stat = aConnection.prepareStatement("select s.SpectrumID from spectrumheaders as s, masspeaks where masspeaks.masspeakid = s.masspeakid");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                iSpectra.add(rs.getInt("SpectrumID"));
            }
            rs.close();
            stat.close();
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return iSpectra;
    }

    public SpectrumLowMem getSpectrumForPeptideID(PeptideLowMem peptideOfInterest, Connection aConnection) {
        SpectrumLowMem returnSpectrum = peptideOfInterest.getParentSpectrum();
        if (returnSpectrum == null) {
            try {
                Statement stat = aConnection.createStatement();
                ResultSet rs;
                rs = stat.executeQuery("select s.*, FileID from spectrumheaders as s, masspeaks,(select SpectrumID from Peptides where Peptideid = " + peptideOfInterest.getPeptideId() + ") as specid where masspeaks.masspeakid = s.masspeakid and s.SpectrumID = specid.SpectrumID");
                rs.next();
                returnSpectrum = new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), aConnection);
                returnSpectrum.setFileId(rs.getInt("FileID"));
                rs = stat.executeQuery("select * from CustomDataSpectra where SpectrumID = " + returnSpectrum.getSpectrumId());
                while (rs.next()) {
                    returnSpectrum.addCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
                }
                peptideOfInterest.setParentSpectrum(returnSpectrum);
                rs.close();
                stat.close();
            } catch (SQLException ex) {
                logger.error(ex);
            }
            return returnSpectrum;
        } else {
            return returnSpectrum;
        }
    }

    public SpectrumLowMem getSpectrumForPeptideID(int peptideOfInterest, Connection aConnection) {
        SpectrumLowMem returnSpectrum = null;
        try {
            Statement stat = aConnection.createStatement();
            ResultSet rs;
            rs = stat.executeQuery("select s.*, FileID from spectrumheaders as s, masspeaks,(select SpectrumID from Peptides where Peptideid = " + peptideOfInterest + ") as specid where masspeaks.masspeakid = s.masspeakid and s.SpectrumID = specid.SpectrumID");
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
    public void createSpectrumXMLForSpectrum(SpectrumLowMem lSpectrum) throws IOException {
        try {
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
            ByteArrayOutputStream lStream = new ByteArrayOutputStream(50);
            out = new BufferedOutputStream(lStream, 50);
            while ((entry = in.getNextEntry()) != null) {
                int count;
                byte data[] = new byte[50];
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
        } catch (SQLException ex) {
            logger.error(ex);
        }
    }

    @Override
    public void unzipXMLforSpectrum(SpectrumLowMem spectrum) {
        File lZippedFile = null;
        String lXml = "";
        if (spectrum.getSpectrumXML() == null) {
            try {
                if (spectrum.getZippedSpectrumXml() == null) {
                    createSpectrumXMLForSpectrum(spectrum);
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
                    ByteArrayOutputStream lStream = new ByteArrayOutputStream(50);
                    out = new BufferedOutputStream(lStream, 50);
                    while ((entry = in.getNextEntry()) != null) {
                        int count;
                        byte data[] = new byte[50];
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
                logger.error(e);
            }
        }
    }

    public int getNumberOfSpectra(Connection msfFileConnection) {
        int numberOfSpectra = 0;
        try {
            Statement stat = msfFileConnection.createStatement();
            ResultSet rs = stat.executeQuery("select count(SpectrumID) from SpectrumHeaders");
            rs.next();
            numberOfSpectra = rs.getInt(1);
            rs.close();
            stat.close();
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
