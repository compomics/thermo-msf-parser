package com.compomics.thermo_msf_parser.msf;

import java.io.*;
import java.sql.*;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 10/1/12
 * Time: 11:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class SpectrumLowMemController implements SpectrumInterface{

    /**
     *
     * @param peptideID the id of the peptide we want the spectrum xml for
     * @param iConnection connection to the msf file
     * @return the unzipped XML file in a string object
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public String createSpectrumXMLForPeptide(int peptideID,Connection iConnection) throws SQLException, IOException {
        byte[] lZippedSpectrumXml = null;
        String lXml;
        ResultSet rs;
        Statement stat = iConnection.createStatement();
        rs = stat.executeQuery("select Spectrum from Spectra where UniqueSpectrumID = (select SpectrumID from Peptides where Peptides.PeptideID = "+peptideID+")");
        while (rs.next()) {
            lZippedSpectrumXml = rs.getBytes(1);
        }

        if (lZippedSpectrumXml == null) {

        }
        File lZippedFile = File.createTempFile("zip",null);
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
     * @return a vector containing all the MSMS peak objects retrieved from the xml
     * @throws Exception
     */
    public Vector<Peak> getMSMSPeaks(String lXml) throws Exception {
        Vector<Peak> lPeaks = new Vector<Peak>();
        try{
            lXml = lXml.substring(lXml.indexOf("<Peak ", lXml.indexOf("<PeakCentr")), lXml.lastIndexOf("</PeakCent"));
            String[] lLines = lXml.split("\n");
            for (int i = 0; i < lLines.length; i++) {
                if (lLines[i].trim().startsWith("<Peak ")) {
                    lPeaks.add(new Peak(lLines[i]));
                }
            }
        } catch(IndexOutOfBoundsException e){
            //no peaks found
        }
        return lPeaks;
    }

    /**
     *
     * @param lXml the spectrum xml we want the MS peak from
     * @return a vector containing all the MS peak objects retrieved from the xml
     * @throws Exception
     */
    public Vector<Peak> getMSPeaks(String lXml) throws Exception {
        lXml = lXml.substring(lXml.indexOf("<Peak ", lXml.indexOf("<IsotopeClusterPeakCentroids")), lXml.lastIndexOf("</IsotopeClusterPeakCentroids"));
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
     *
     * @param lXml the spectrum xml we want the fragmented peak from
     * @return a peak object containing the fragmented MS peak
     * @throws Exception
     */
    public Peak getFragmentedMsPeak(String lXml) throws Exception {
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
     *
     * @param iConnection connection to the msf file
     * @return a vector containing all the spectra (in spectrum objects) stored in the msf file
     * @throws SQLException
     */
    public Vector<SpectrumLowMem> getAllSpectra(Connection iConnection) throws SQLException {
        Vector<SpectrumLowMem> iSpectra = new Vector<SpectrumLowMem>();
        PreparedStatement stat = iConnection.prepareStatement("select s.*, m.FileID from spectrumheaders as s, masspeaks as m where m.masspeakid = s.masspeakid");
        ResultSet rs = stat.executeQuery();
        while (rs.next()) {
            SpectrumLowMem lSpectrum = new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), iConnection);
            lSpectrum.setFileId(rs.getInt("FileID"));
            iSpectra.add(lSpectrum);
        }
        rs.close();
        stat.close();
        return iSpectra;
    }

    public SpectrumLowMem getSpectrumForPeptideID(int peptideOfInterestID,Connection iConnection) throws SQLException {
        Statement stat = iConnection.createStatement();
        ResultSet rs;
        rs = stat.executeQuery("select s.*, m.FileID from spectrumheaders as s, masspeaks as m,(select SpectrumID from Peptides where Peptideid = "+ peptideOfInterestID + ") as specid where m.masspeakid = s.masspeakid and s.SpectrumID = specid.SpectrumID");
        rs.next();
        SpectrumLowMem returnSpectrum = new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), iConnection);
        returnSpectrum.setFileId(rs.getInt("m.FileID"));
        rs = stat.executeQuery("select * from CustomDataSpectra where SpectrumID = "+returnSpectrum.getSpectrumId());
        while(rs.next())
            returnSpectrum.addCustomDataField(rs.getInt("FieldID"),rs.getString("FieldValue"));
        rs.close();
        stat.close();
        return returnSpectrum;
    }

    /**
     *
     * @param rawFileName the raw file name connected to the spectrum
     * @param lspectrum the spectrum object we want the title from
     * @return a processed title
     */
    public String getSpectrumTitle(String rawFileName, SpectrumLowMem lspectrum){
        String spectrumTitle = rawFileName.substring(0, rawFileName.toLowerCase().indexOf(".raw"));
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
        rs = stat.executeQuery("select Spectrum from Spectra where UniqueSpectrumID = "+lSpectrum.getSpectrumId());
        while (rs.next()) {
            lZippedSpectrumXml = rs.getBytes(1);
        }

        if (lZippedSpectrumXml == null) {
        //TODO
        }
        File lZippedFile = File.createTempFile("zip",null);
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
    public void unzipXMLforSpectrum(SpectrumLowMem spectrum){
        File lZippedFile = null;
        String lXml = "";
        try {
            if (spectrum.getZippedSpectrumXml() == null){
                try {
                    createSpectrumXMLForSpectrum(spectrum);
                } catch (SQLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            } else{
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
}
