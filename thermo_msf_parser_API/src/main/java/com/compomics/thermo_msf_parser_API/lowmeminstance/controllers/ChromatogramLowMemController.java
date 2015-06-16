package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.highmeminstance.Chromatogram;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipInputStream;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 10/1/12 Time: 10:21 AM To change
 * this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class ChromatogramLowMemController {

    private static final Logger logger = Logger.getLogger(ChromatogramLowMemController.class);

    /**
     * returns the chromatogram for a given peptide ID in the SQLite db
     *
     * @param peptideID the id of the peptide
     * @param msfFile the proteome discoverer file to retrieve the chromatograms from
     * @return a vector containing the chromatogram files
     */
    //TODO check of this returns one or multiple files
    public List<Chromatogram> getChromatogramFileForPeptideID(int peptideID, MsfFile msfFile) {
        List<Chromatogram> chromatogramFiles = new ArrayList<Chromatogram>();
        try {
            PreparedStatement stat = msfFile.getConnection().prepareStatement("select chro.Chromatogram,chro.TraceType,chro.FileID from Chromatograms as chro,MassPeaks,SpectrumHeaders,Peptides where chro.fileID = MassPeaks.fileID and MassPeaks.MassPeakID = SpectrumHeaders.MassPeakID and SpectrumHeaders.SpectrumID = Peptides.SpectrumID and Peptides.PeptideID =" + peptideID);
            try {
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        chromatogramFiles.add(new Chromatogram(rs.getInt("FileID"), rs.getInt("TraceType"), rs.getBytes("Chromatogram")));
                    }
                } finally {
                    rs.close();
                }

            } finally {
                stat.close();
            }
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return chromatogramFiles;
    }

    /**
     * <p>getUnzippedChromatogramXml.</p>
     *
     * @param zippedChromatogramXML the zipped chromatogram xml file retrieved
     * by getChromatogramFileForPeptideID
     * @return the unzipped chromatogram file in a string
     * @throws java.io.IOException if the chromatogram xml file could not be written to disk
     */
    public String getUnzippedChromatogramXml(byte[] zippedChromatogramXML) throws IOException {
        String iUnzippedChromatogramXml = "";
        try {
            File lZippedFile = File.createTempFile("zip", null);
            FileOutputStream fos = new FileOutputStream(lZippedFile);
            try {
                fos.write(zippedChromatogramXML);
                fos.flush();
            } finally {
                fos.close();
            }
            ByteArrayOutputStream lStream = new ByteArrayOutputStream(50);
            BufferedOutputStream out = new BufferedOutputStream(lStream, 50);
            ZipInputStream in = null;
            try {
                in = new ZipInputStream(new BufferedInputStream(new FileInputStream(lZippedFile)));
                while (in.getNextEntry() != null) {
                    int count;
                    byte data[] = new byte[50];
                    out = new BufferedOutputStream(lStream, 50);
                    while ((count = in.read(data, 0, 50)) != -1) {
                        out.write(data, 0, count);
                    }
                    out.flush();
                }
            } finally {
                if (in != null) {
                    in.close();
                }
                out.close();
            }
            if (!lZippedFile.delete()) {
                throw new IOException("could not remove temp files");
            }
            try {
                iUnzippedChromatogramXml = lStream.toString("UTF-8");
            } finally {
                lStream.close();
            }

        } catch (IOException ex) {
            logger.error(ex);
        }
        return iUnzippedChromatogramXml;
    }

    /**
     * returns a vector containing Point objects extracted from the chromatogram
     * XML file
     *
     * @param chromatogramXML the unzipped chromatogram xml file retrieved from
     * getUnzippedChromatogramXml
     * @return a vector containing all the point objects retrieved from the xml
     * file
     */
    public List<Point> getPoints(String chromatogramXML) {
        String[] lLines = chromatogramXML.split("\n");
        List<Point> lPoints = new ArrayList<Point>();
        for (String lLine : lLines) {
            if (lLine.trim().startsWith("<Pt ")) {
                lPoints.add(new Point(lLine));
            }
        }
        return lPoints;
    }

    /**
     * <p>getChromatogramFilesForMsfFile.</p>
     *
     * @param msfFile the proteome discoverer file to retrieve from.
     * @return a {@link java.util.Collection} containing the retrieved chromatograms.
     */
    public Collection<? extends Chromatogram> getChromatogramFilesForMsfFile(MsfFile msfFile) {
        List<Chromatogram> chromatogramFiles = new ArrayList<Chromatogram>();
        try {
            PreparedStatement stat = null;

            try {
                stat = msfFile.getConnection().prepareStatement("select chro.Chromatogram,chro.TraceType,chro.FileID from Chromatograms as chro");
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        chromatogramFiles.add(new Chromatogram(rs.getInt("FileID"), rs.getInt("TraceType"), rs.getBytes("Chromatogram")));
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
        return chromatogramFiles;


    }

    /**
     * inner class Point
     */
    public class Point {

        /* 
         * from this point on, the inner class methods are public only for semantic reasons. the vm does not enforce visibility rules here so they are still private on compile.
         * this only matters if the inner class should ever be converted to an outer class.
         */
        /**
         * Time
         */
        double iT;
        /**
         * Intensity
         */
        double iY;
        /**
         * Scan number
         */
        int iScan;

        /**
         * The Point constructor
         *
         * @param lLine An xml line that will be parsed
         */
        public Point(String lLine) {
            String[] lElements = lLine.split(" ");
            for (int i = 0; i < lElements.length; i++) {
                String lElement = lElements[i];
                if (lElement.startsWith("T=\"")) {
                    iT = Double.valueOf(lElement.substring(3, lElement.lastIndexOf("\"")));
                }
                if (lElement.startsWith("Y=\"")) {
                    iY = Double.valueOf(lElement.substring(3, lElement.lastIndexOf("\"")));
                }
                if (lElement.startsWith("Scan=\"")) {
                    iScan = Integer.valueOf(lElement.substring(6, lElement.lastIndexOf("\"")));
                }
            }
        }

        public double getT() {
            return iT;
        }

        public double getY() {
            return iY;
        }

        public int getScan() {
            return iScan;
        }
    }
}
