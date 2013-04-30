package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.highmeminstance.Peak;
import com.compomics.thermo_msf_parser_API.interfaces.SpectrumInterface;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.SpectrumLowMem;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            File lZippedFile = File.createTempFile("zip", null);
            FileOutputStream fos = new FileOutputStream(lZippedFile);
            fos.write(lZippedSpectrumXml);
            fos.flush();
            fos.close();
            BufferedOutputStream out;
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
        }
            return lXml;
        
    }

    /**
     *
     * @param lXml the spectrum xml to return the msms peaks from
     * @return a vector containing all the MSMS peak objects retrieved from the
     * xml
     * @throws Exception
     */
    @Override
    public List<Peak> getMSMSPeaks(String lXml) throws IndexOutOfBoundsException {
        List<Peak> lPeaks = new ArrayList<Peak>();
            String xmlSubstring = lXml.substring(lXml.indexOf("<Peak ", lXml.indexOf("<PeakCentr")), lXml.lastIndexOf("</"));
            String[] lLines = xmlSubstring.split("\n");
            for (int i = 0; i < lLines.length; i++) {
                if (lLines[i].trim().startsWith("<Peak ")) {
                    lPeaks.add(new Peak(lLines[i]));
                }
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
    public List<Peak> getMSPeaks(String lXml) throws IndexOutOfBoundsException {
        String xmlSubstring = lXml.substring(lXml.indexOf("<Peak ", lXml.indexOf("<IsotopeClusterPeakCentroids")), lXml.lastIndexOf("</"));
        String[] lLines = xmlSubstring.split("\n");
        List<Peak> lPeaks = new ArrayList<Peak>();
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
    public Peak getFragmentedMsPeak(String lXml) throws IndexOutOfBoundsException {
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
    public List<SpectrumLowMem> getAllSpectra(MsfFile msfFile) {
        List<SpectrumLowMem> iSpectra = new ArrayList<SpectrumLowMem>();
        try {
            PreparedStatement stat = msfFile.getConnection().prepareStatement("select s.*, FileID from spectrumheaders as s, masspeaks where masspeaks.masspeakid = s.masspeakid");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                SpectrumLowMem lSpectrum = new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"));
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
    public List<Integer> getAllSpectraIds(Connection aConnection) {
        List<Integer> iSpectra = new ArrayList<Integer>();
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

    public SpectrumLowMem getSpectrumForPeptideID(PeptideLowMem peptideOfInterest, MsfFile msfFile) {
        SpectrumLowMem returnSpectrum = peptideOfInterest.getParentSpectrum();
        if (returnSpectrum == null) {
            try {
                Statement stat = msfFile.getConnection().createStatement();
                ResultSet rs;
                rs = stat.executeQuery("select s.*, FileID from spectrumheaders as s, masspeaks,(select SpectrumID from Peptides where Peptideid = " + peptideOfInterest.getPeptideId() + ") as specid where masspeaks.masspeakid = s.masspeakid and s.SpectrumID = specid.SpectrumID");
                rs.next();
                returnSpectrum = new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"));
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

        }
        return returnSpectrum;

    }

    public SpectrumLowMem getSpectrumForPeptideID(int peptideOfInterest, MsfFile msfFile) {
        SpectrumLowMem returnSpectrum = null;
        try {
            Statement stat = msfFile.getConnection().createStatement();
            ResultSet rs;
            rs = stat.executeQuery("select s.*, FileID from spectrumheaders as s, masspeaks,(select SpectrumID from Peptides where Peptideid = " + peptideOfInterest + ") as specid where masspeaks.masspeakid = s.masspeakid and s.SpectrumID = specid.SpectrumID");
            rs.next();
            returnSpectrum = new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"));
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

    public SpectrumLowMem getSpectrumForSpectrumID(int spectrumOfInterestID, MsfFile msfFile) {
        SpectrumLowMem returnSpectrum = null;
        try {
            Statement stat = msfFile.getConnection().createStatement();
            ResultSet rs;
            rs = stat.executeQuery("select s.*, FileID from spectrumheaders as s, masspeaks where masspeaks.masspeakid = s.masspeakid and s.SpectrumID = " + spectrumOfInterestID);
            rs.next();
            returnSpectrum = new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"));
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
    @Override
    public String getSpectrumTitle(String rawFileName, SpectrumLowMem lspectrum) {
        String spectrumTitle = rawFileName.substring(0, rawFileName.toLowerCase().lastIndexOf("."));
        return spectrumTitle + "_" + lspectrum.getSpectrumId() + "_" + lspectrum.getFirstScan() + "_" + lspectrum.getCharge();
    }

    //TODO redo this part properly
    
    /**
     *
     * @param lSpectrum
     * @return the unzipped XML file in a string object
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    @Override
    public void createSpectrumXMLForSpectrum(SpectrumLowMem lSpectrum, MsfFile msfFile) throws IOException {
        //TODO make own spectrumfile object to return to avoid constantly going through this
        try {
            byte[] lZippedSpectrumXml = null;
            String lXml;
            ResultSet rs;
            Statement stat = msfFile.getConnection().createStatement();
            rs = stat.executeQuery("select Spectrum from Spectra where UniqueSpectrumID = " + lSpectrum.getUniqueSpectrumId());
            while (rs.next()) {
                lZippedSpectrumXml = rs.getBytes(1);
            }
            File lZippedFile = File.createTempFile("zip", null);
            FileOutputStream fos = new FileOutputStream(lZippedFile);
            fos.write(lZippedSpectrumXml);
            fos.flush();
            fos.close();
            BufferedOutputStream out;
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
        File lZippedFile;
        String lXml;
        if (spectrum.getSpectrumXML() == null) {
            try {
                if (spectrum.getZippedSpectrumXml() == null) {
                    
                } else {
                    byte[] lZippedSpectrumXml = spectrum.getZippedSpectrumXml();
                    lZippedFile = File.createTempFile("zip", null);
                    FileOutputStream fos = new FileOutputStream(lZippedFile);
                    fos.write(lZippedSpectrumXml);
                    fos.flush();
                    fos.close();
                    BufferedOutputStream out;
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

    public int getNumberOfSpectra(MsfFile msfFile) {
        int numberOfSpectra = 0;
        try {
            Statement stat = msfFile.getConnection().createStatement();
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

    public HashMap<Integer, SpectrumLowMem> getSpectraMap(MsfFile msfFile) {
        HashMap<Integer, SpectrumLowMem> spectraMap = new HashMap<Integer, SpectrumLowMem>();
        try {
            PreparedStatement stat = msfFile.getConnection().prepareStatement("select s.*, FileID from spectrumheaders as s, masspeaks where masspeaks.masspeakid = s.masspeakid");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                SpectrumLowMem lSpectrum = new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"));
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
