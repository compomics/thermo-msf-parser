package com.compomics.thermo_msf_parser.msf;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 21-Feb-2011
 * Time: 10:55:51
 */

/**
 * This class represents a chromatogram
 */
public class Chromatogram {
    // Class specific log4j logger for Thermo_msf_parserGUI instances.
	 private static Logger logger = Logger.getLogger(Chromatogram.class);

    /**
     * The raw file id
     */
    private int iFileId;
    /**
     * The trace type id
     */
    private int iTraceTypeId;
    /**
     * The trace type
     */
    private String iTraceType;
    /**
     * The zipped chromatogram xml
     */
    private byte[] iZippedChromatogramXml;
    /**
     * The unzipped chromatogram xml
     */
    private String iUnzippedChromatogramXml;


    /**
     * The chromatogram constructor
     * @param iFileId The file id
     * @param iTraceTypeid The trace type id
     * @param iZippedChromatogramXml The zipped chromatogram xml
     */
    public Chromatogram(int iFileId, int iTraceTypeid, byte[] iZippedChromatogramXml) {
        this.iFileId = iFileId;
        this.iTraceTypeId = iTraceTypeid;
        if(iTraceTypeId == 1){
            iTraceType = "TicTrace";
        }
        if(iTraceTypeId == 2){
            iTraceType = "BasePeakTrace";
        }
        this.iZippedChromatogramXml = iZippedChromatogramXml;
    }

    /**
     * Getter for the file id
     * @return int with the file id
     */
    public int getFileId() {
        return iFileId;
    }

    /**
     * Getter for the trace type id
     * @return int with the trace type id
     */
    public int getTraceTypeId() {
        return iTraceTypeId;
    }

    /**
     * Getter for the trace type
     * @return String with the trace type (TicTrace or BasePeakTrace)
     */
    public String getTraceType() {
        return iTraceType;
    }

    /**
     * Getter for the zipped chromatogram xml
     * @return byte[] with the zipped chromatogram xml
     */
    public byte[] getZippedChromatogramXml() {
        return iZippedChromatogramXml;
    }

    /**
     * Getter for the unzipped chromatogram xml
     * @return String with the unzipped chromatogram xml
     * @throws IOException An error is thrown when the zipped byte[] can not be unzipped
     */
    public String getUnzippedChromatogramXml() throws IOException {
        if(iUnzippedChromatogramXml == null){

            File lZippedFile = File.createTempFile("zip",null);
            FileOutputStream fos = new FileOutputStream(lZippedFile);
            fos.write(iZippedChromatogramXml);
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
        }
        return iUnzippedChromatogramXml;
    }

    /**
     * Getter for the different points in the chromatgram
     * @return Vector with the different points
     * @throws IOException An error is thrown when the zipped byte[] can not be unzipped
     */
    public Vector<Point> getPoints() throws IOException {
        String lXml = getUnzippedChromatogramXml();
        String[] lLines = lXml.split("\n");
        Vector<Point> lPoints = new Vector<Point>();
        for(int i= 0; i<lLines.length; i ++){
            if(lLines[i].trim().startsWith("<Pt ")){
                lPoints.add(new Point(lLines[i]));
            }
        }
        return lPoints;
    }


    /**
     * This class represent one point in the chromatogram
     */
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



