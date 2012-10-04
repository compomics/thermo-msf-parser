package com.compomics.thermo_msf_parser.msf;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 10/1/12
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class ChromatogramLowMemController {

    public Vector<Chromatogram> getChromatogramFileForPeptideID(int peptideID,Connection iConnection) throws SQLException, IOException {
        Vector<Chromatogram> chromatogramFiles = new Vector<Chromatogram>();
        PreparedStatement stat = iConnection.prepareStatement("select chro.Chromatogram,chro.TraceType,chro.FileID from Chromatograms as chro,MassPeaks,SpectrumHeaders,Peptides where chro.fileID = MassPeaks.fileID and MassPeaks.MassPeakID = SpectrumHeaders.MassPeakID and SpectrumHeaders.SpectrumID = Peptides.SpectrumID and Peptides.PeptideID ="+peptideID);
        ResultSet rs = stat.executeQuery();
        while (rs.next()) {
            chromatogramFiles.add(new Chromatogram(rs.getInt("FileID"), rs.getInt("TraceType"), rs.getBytes("Chromatogram")));
        }
        rs.close();
        stat.close();
        return chromatogramFiles;
    }

    /**
     *
     * @param zippedChromatogramXML the zipped chromatogram xml file retrieved by getChromatogramFileForPeptideID
     * @return the unzipped chromatogram file in a string
     * @throws IOException
     */
    public String getUnzippedChromatogramXml(byte[] zippedChromatogramXML) throws IOException {
        String iUnzippedChromatogramXml;
        File lZippedFile = File.createTempFile("zip",null);
        FileOutputStream fos = new FileOutputStream(lZippedFile);
        fos.write(zippedChromatogramXML);
        fos.flush();
        fos.close();
        BufferedOutputStream out = null;
        ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(lZippedFile)));
        ZipEntry entry;
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
        iUnzippedChromatogramXml = lStream.toString();
        lStream.close();
        return iUnzippedChromatogramXml;
    }

    /**
     *
     * @param chromatogramXML the unzipped chromatogram xml file retrieved from getUnzippedChromatogramXml
     * @return a vector containing all the point objects retrieved from the xml file
     */
    public Vector<Point> getPoints(String chromatogramXML){
        String[] lLines = chromatogramXML.split("\n");
        Vector<Point> lPoints = new Vector<Point>();
        for(int i= 0; i<lLines.length; i ++){
            if(lLines[i].trim().startsWith("<Pt ")){
                lPoints.add(new Point(lLines[i]));
            }
        }
        return lPoints;
    }
    public class Point{
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
         * @param lLine An xml line that will be parsed
         */
        public Point(String lLine) {
            String[] lElements = lLine.split(" ");
            for(int i = 0; i<lElements.length; i ++){
                String lElement = lElements[i];
                if(lElement.startsWith("T=\"")){
                    iT = Double.valueOf(lElement.substring(3, lElement.lastIndexOf("\"")));
                }
                if(lElement.startsWith("Y=\"")){
                    iY = Double.valueOf(lElement.substring(3, lElement.lastIndexOf("\"")));
                }
                if(lElement.startsWith("Scan=\"")){
                    iScan = Integer.valueOf(lElement.substring(6, lElement.lastIndexOf("\"")));
                }
            }
        }

        /**
         * Getter for the t (time) value
         * @return double with the time
         */
        public double getT() {
            return iT;
        }

        /**
         * Getter for the y (intensity) value
         * @return double with the intensity
         */
        public double getY() {
            return iY;
        }

        /**
         * Getter for the scan
         * @return int with the scan
         */
        public int getScan() {
            return iScan;
        }
    }
}
