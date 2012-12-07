package com.compomics.thermo_msf_parser.msf;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    /**
     * returns the chromatogram for a given peptide ID in the SQLite db
     * @param peptideID the id of the peptide
     * @param iConnection connection to the msf SQLite file
     * @return a vector containing the chromatogram files
     */
    //TODO check of this returns one or multiple files
    
    public Vector<Chromatogram> getChromatogramFileForPeptideID(int peptideID,Connection aConnection) {
        Vector<Chromatogram> chromatogramFiles = new Vector<Chromatogram>();   
        try {
            PreparedStatement stat = aConnection.prepareStatement("select chro.Chromatogram,chro.TraceType,chro.FileID from Chromatograms as chro,MassPeaks,SpectrumHeaders,Peptides where chro.fileID = MassPeaks.fileID and MassPeaks.MassPeakID = SpectrumHeaders.MassPeakID and SpectrumHeaders.SpectrumID = Peptides.SpectrumID and Peptides.PeptideID ="+peptideID);
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                chromatogramFiles.add(new Chromatogram(rs.getInt("FileID"), rs.getInt("TraceType"), rs.getBytes("Chromatogram")));
            }
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            Logger.getLogger(ChromatogramLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return chromatogramFiles;
    }

    /**
     *
     * @param zippedChromatogramXML the zipped chromatogram xml file retrieved by getChromatogramFileForPeptideID
     * @return the unzipped chromatogram file in a string
     */
    
    public String getUnzippedChromatogramXml(byte[] zippedChromatogramXML) {
        String iUnzippedChromatogramXml = "";
        try {
            File lZippedFile = File.createTempFile("zip",null);
            FileOutputStream fos = new FileOutputStream(lZippedFile);
            fos.write(zippedChromatogramXML);
            fos.flush();
            fos.close();
            ByteArrayOutputStream lStream = new ByteArrayOutputStream(50);
            BufferedOutputStream out = new BufferedOutputStream(lStream, 50);
            ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(lZippedFile)));
            ZipEntry entry;
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
            
        } catch (IOException ex) {
            Logger.getLogger(ChromatogramLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return iUnzippedChromatogramXml;
    }

    /**
     * returns a vector containing Point objects extracted from the chromatogram XML file
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
    
    /**
     * inner class Point
     */
    private class Point{
        
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
