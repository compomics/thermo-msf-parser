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
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class SpectrumLowMemController implements SpectrumInterface {

    private static final Logger logger = Logger.getLogger(SpectrumLowMemController.class);

    /**
     * <p>createSpectrumXMLForPeptide.</p>
     *
     * @param peptide the {@code PeptideLowMem} peptide to fetch the spectrum
     * XML file
     * @param msfFile the msf file to fetch in
     * @return the XML file in a {@code String}
     */
    public String createSpectrumXMLForPeptide(PeptideLowMem peptide, MsfFile msfFile) {
        byte[] lZippedSpectrumXml = null;
        String lXml = peptide.getParentSpectrum().getSpectrumXML();
        if (lXml == null) {
            try {
                ResultSet rs;
                Statement stat = msfFile.getConnection().createStatement();
                rs = stat.executeQuery(new StringBuilder().append("select Spectrum from Spectra where UniqueSpectrumID = (select UniqueSpectrumID from Peptides,SpectrumHeaders where SpectrumHeaders.SpectrumID = Peptides.SpectrumID and Peptides.PeptideID = ").append(peptide.getPeptideId()).append(")").toString());
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
                lXml = lStream.toString("UTF-8");
                lStream.close();
                rs.close();
                stat.close();
            } catch (SQLException ex) {
                logger.error(ex);
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
        return lXml;

    }

    /** {@inheritDoc} */
    @Override
    public List<Peak> getMSMSPeaks(String lXml) {
        List<Peak> lPeaks = new ArrayList<Peak>();
        String xmlSubstring = lXml.substring(lXml.indexOf("<Peak ", lXml.indexOf("<PeakCentr")), lXml.lastIndexOf("</PeakCentroids>"));
        String[] lLines = xmlSubstring.split("\n");
        for (int i = 0; i < lLines.length; i++) {
            if (lLines[i].trim().startsWith("<Peak ")) {
                lPeaks.add(new Peak(lLines[i]));
            }
        }
        return lPeaks;
    }

    /**
     * {@inheritDoc}
     *
     * gets the MS peaks from a spectrum XML file
     */
    @Override
    public List<Peak> getMSPeaks(String lXml) {
        String xmlSubstring = lXml.substring(lXml.indexOf("<Peak ", lXml.indexOf("<IsotopeClusterPeakCentroids")), lXml.lastIndexOf("</IsotopeClusterPeakCentroids>"));
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
     * {@inheritDoc}
     *
     * gets the fragmented MS peaks from a spectrum XML file
     */
    @Override
    public Peak getFragmentedMsPeak(String lXml) {
        String xmlSubstring = lXml.substring(lXml.indexOf("<MonoisotopicPeakCentroids"), lXml.indexOf("</MonoisotopicPeakCentroids>"));
        String[] lLines = xmlSubstring.split("\n");
        Peak lPeak = null;
        for (int i = 0; i < lLines.length; i++) {
            if (lLines[i].trim().startsWith("<Peak ")) {
                lPeak = new Peak(lLines[i]);
                break;
            }
        }
        return lPeak;
    }

    /**
     * returns a {@code List} containing all of the spectra in a proteome
     * distiller file
     *
     * @param msfFile the msf file to fetch from
     * @return a {@code List} containing all the {@code SpectrumLowMem}
     * representations of the spectra stored in the msf file
     */
    public List<SpectrumLowMem> getAllSpectra(MsfFile msfFile) {
        List<SpectrumLowMem> iSpectra = new ArrayList<SpectrumLowMem>();
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select s.*, FileID from spectrumheaders as s, masspeaks where masspeaks.masspeakid = s.masspeakid");
                ResultSet rs = null;
                try {
                    rs = stat.executeQuery();
                    while (rs.next()) {
                        SpectrumLowMem lSpectrum = new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"));
                        lSpectrum.setFileId(rs.getInt("FileID"));
                        iSpectra.add(lSpectrum);
                    }
                } finally {
                    if (rs != null) {
                        rs.close();
                    }
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return iSpectra;
    }

    /**
     * gets a {@code List} containing all of the spectra ids in the proteome
     * discoverer file
     *
     * @param msfFile the msf file to look in
     * @return a {@code List} containing all the {@code SpectrumLowMem}
     * representations of the spectra stored in the msf file
     */
    public List<Integer> getAllSpectraIds(MsfFile msfFile) {
        List<Integer> iSpectra = new ArrayList<Integer>();
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select s.SpectrumID from spectrumheaders as s, masspeaks where masspeaks.masspeakid = s.masspeakid");
                ResultSet rs = null;
                try {
                    rs = stat.executeQuery();
                    while (rs.next()) {
                        iSpectra.add(rs.getInt("SpectrumID"));
                    }
                } finally {
                    if (rs != null) {
                        rs.close();
                    }
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return iSpectra;
    }

    /**
     * gets the spectrum connected to a peptide from a proteome discoverer file
     *
     * @param peptideOfInterest the peptide to retrieve the spectrum for
     * @param msfFile the proteome discoverer file to look in
     * @return the requested {@code SpectrumLowMem} object
     */
    public SpectrumLowMem getSpectrumForPeptide(PeptideLowMem peptideOfInterest, MsfFile msfFile) {
        SpectrumLowMem returnSpectrum = peptideOfInterest.getParentSpectrum();
        if (returnSpectrum == null) {
            returnSpectrum = getSpectrumForPeptideID(peptideOfInterest.getPeptideId(), msfFile);
        }
        peptideOfInterest.setParentSpectrum(returnSpectrum);
        return returnSpectrum;

    }

    /**
     * gets the spectrum for a peptide ID in a proteome discoverer file
     *
     * @param peptideIdOfInterest the peptide to return the spectrum for
     * @param msfFile the proteome discoverer file to look in
     * @return the requested spectrum
     */
    public SpectrumLowMem getSpectrumForPeptideID(int peptideIdOfInterest, MsfFile msfFile) {
        SpectrumLowMem returnSpectrum = null;
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select s.*, FileID from spectrumheaders as s, masspeaks, Peptides where Peptides.Peptideid = ? and masspeaks.masspeakid = s.masspeakid and s.SpectrumID = Peptides.SpectrumID");
                stat.setInt(1, peptideIdOfInterest);
                ResultSet rs = null;
                try {
                    rs = stat.executeQuery();
                    rs.next();
                    returnSpectrum = new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"));
                    returnSpectrum.setFileId(rs.getInt("FileID"));
                    rs = stat.executeQuery(new StringBuilder().append("select * from CustomDataSpectra where SpectrumID = ").append(returnSpectrum.getSpectrumId()).toString());
                    while (rs.next()) {
                        returnSpectrum.addCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
                    }
                } finally {
                    if (rs != null) {
                        rs.close();
                    }
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
        }

        return returnSpectrum;
    }

    /**
     * gets the spectrum connected to the spectrumid from a proteome discoverer
     * file
     *
     * @param spectrumOfInterestID the spectrumid to retrieve the spectrum for
     * @param msfFile the proteome discoverer file to look in
     * @return the requested spectrum
     */
    public SpectrumLowMem getSpectrumForSpectrumID(int spectrumOfInterestID, MsfFile msfFile) {
        SpectrumLowMem returnSpectrum = null;
        try {
            Statement stat = null;
            try {
                stat = msfFile.getConnection().createStatement();
                ResultSet rs;
                rs = stat.executeQuery(new StringBuilder().append("select s.*, FileID from spectrumheaders as s, masspeaks where masspeaks.masspeakid = s.masspeakid and s.SpectrumID = ").append(spectrumOfInterestID).toString());
                try {
                    rs.next();
                    returnSpectrum = new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"));
                    returnSpectrum.setFileId(rs.getInt("FileID"));
                    rs = stat.executeQuery(new StringBuilder().append("select * from CustomDataSpectra where SpectrumID = ").append(returnSpectrum.getSpectrumId()).toString());
                    while (rs.next()) {
                        returnSpectrum.addCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
                    }
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return returnSpectrum;
    }

    /** {@inheritDoc} */
    @Override
    public String getSpectrumTitle(String rawFileName, SpectrumLowMem lspectrum) {
        String spectrumTitle = rawFileName.substring(0, rawFileName.toLowerCase().lastIndexOf("."));
        return spectrumTitle + "_" + lspectrum.getSpectrumId() + "_" + lspectrum.getFirstScan() + "_" + lspectrum.getCharge();
    }

    //TODO redo this part properly

    /** {@inheritDoc} */
    @Override
    public void createSpectrumXMLForSpectrum(SpectrumLowMem lSpectrum, MsfFile msfFile) throws IOException {
        //TODO make own spectrumfile object to return to avoid constantly going through this
        try {
            byte[] lZippedSpectrumXml = null;
            String lXml;
            ResultSet rs = null;
            Statement stat = null;
            try {
                stat = msfFile.getConnection().createStatement();

                try {
                    rs = stat.executeQuery(new StringBuilder().append("select Spectrum from Spectra where UniqueSpectrumID = ").append(lSpectrum.getUniqueSpectrumId()).toString());
                    while (rs.next()) {
                        lZippedSpectrumXml = rs.getBytes(1);
                    }
                } finally {
                    if (rs != null) {
                        rs.close();
                    }
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
            File lZippedFile = File.createTempFile("zip", null);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(lZippedFile);
                fos.write(lZippedSpectrumXml);
            } finally {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            }
            BufferedOutputStream out;
            ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(lZippedFile)));
            ByteArrayOutputStream lStream = new ByteArrayOutputStream(50);
            out = new BufferedOutputStream(lStream, 50);
            while (in.getNextEntry() != null) {
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
            lXml = lStream.toString("UTF-8");
            lStream.close();
            lSpectrum.addSpectrumXML(lXml);
        } catch (SQLException ex) {
            logger.error(ex);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean unzipXMLforSpectrum(SpectrumLowMem spectrum) {
        boolean returnvalue = false;
        File lZippedFile;
        String lXml;
        if (spectrum.getSpectrumXML() == null) {
            try {
                byte[] lZippedSpectrumXml;
                if ((lZippedSpectrumXml = spectrum.getZippedSpectrumXml()) != null) {
                    lZippedFile = File.createTempFile("zip", null);
                    FileOutputStream fos = new FileOutputStream(lZippedFile);
                    fos.write(lZippedSpectrumXml);
                    fos.flush();
                    fos.close();
                    BufferedOutputStream out;
                    ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(lZippedFile)));
                    ByteArrayOutputStream lStream = new ByteArrayOutputStream(50);
                    out = new BufferedOutputStream(lStream, 50);
                    while (in.getNextEntry() != null) {
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
                    lXml = lStream.toString("UTF-8");
                    lStream.close();
                    spectrum.addSpectrumXML(lXml);
                    returnvalue = true;
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }
        return returnvalue;
    }

    /**
     * get the number of spectra in a proteome discoverer file
     *
     * @param msfFile the file to count the spectra in
     * @return total spectra in the file
     */
    public int getNumberOfSpectra(MsfFile msfFile) {
        int numberOfSpectra = 0;
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select count(SpectrumID) from SpectrumHeaders");
                ResultSet rs = stat.executeQuery();
                try {
                    rs.next();
                    numberOfSpectra = rs.getInt(1);
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return numberOfSpectra;
    }

    /**
     * returns a map containing all the spectra linked to their ids
     *
     * @param msfFile the proteome discoverer file to look in
     * @return a hashmap with key spectrumid and value spectrum
     */
    public HashMap<Integer, SpectrumLowMem> getSpectraMap(MsfFile msfFile) {
        HashMap<Integer, SpectrumLowMem> spectraMap = new HashMap<Integer, SpectrumLowMem>();
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select s.*, FileID from spectrumheaders as s, masspeaks where masspeaks.masspeakid = s.masspeakid");
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        SpectrumLowMem lSpectrum = new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"));
                        lSpectrum.setFileId(rs.getInt("FileID"));
                        spectraMap.put(rs.getInt("SpectrumID"), lSpectrum);
                    }
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return spectraMap;
    }
}
