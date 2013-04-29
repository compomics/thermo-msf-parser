package com.compomics.thermo_msf_parser.msf;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 18-Feb-2011
 * Time: 11:27:21
 * To change this template use File | Settings | File Templates.
 */
public class Peak {
    // Class specific log4j logger for Thermo_msf_parserGUI instances.
	 private static Logger logger = Logger.getLogger(Peak.class);
    /**
     * The mass
     */
    private double iX;
    /**
     * The intensity
     */
    private double iY;
    /**
     * The charge
     */
    private double iZ;
    /**
     * The R
     */
    private double iR;
    /**
     * The SN
     */
    private double iSN;

    /**
     * The constructor for the peak
     * @param iX The x value
     * @param iY The y value
     * @param iZ The z value
     * @param iR The r value
     * @param iSN The sn value
     */
    public Peak(double iX, double iY, double iZ, double iR, double iSN) {
        this.iX = iX;
        this.iY = iY;
        this.iZ = iZ;
        this.iR = iR;
        this.iSN = iSN;
    }

    /**
     * Construct a peak object from a peakline found in the spectrum xml
     * @param lPeakLineFromXml The xml line that will be parsed
     */
    public Peak(String lPeakLineFromXml){
        lPeakLineFromXml = lPeakLineFromXml.trim();
        String[] lElements = lPeakLineFromXml.split(" ");
        for(int i = 0; i<lElements.length; i ++){
            String lElement = lElements[i];
            if(lElement.startsWith("X=\"")){
                iX = Double.valueOf(lElement.substring(3, lElement.lastIndexOf("\"")));
            }
            if(lElement.startsWith("Y=\"")){
                iY = Double.valueOf(lElement.substring(3, lElement.lastIndexOf("\"")));
            }
            if(lElement.startsWith("Z=\"")){
                iZ = Double.valueOf(lElement.substring(3, lElement.lastIndexOf("\"")));
            }
            if(lElement.startsWith("R=\"")){
                iR = Double.valueOf(lElement.substring(3, lElement.lastIndexOf("\"")));
            }
            if(lElement.startsWith("SN=\"")){
                iSN = Double.valueOf(lElement.substring(4, lElement.lastIndexOf("\"")));
            }
        }
    }


    /**
     * Getter for X
     * @return double for the X value
     */
    public double getX() {
        return iX;
    }

    /**
     * Getter for Y
     * @return double for the Y value
     */
    public double getY() {
        return iY;
    }

    /**
     * Getter for Z
     * @return double for the Z value
     */
    public double getZ() {
        return iZ;
    }


    /**
     * Getter for R
     * @return double for the R value
     */
    public double getR() {
        return iR;
    }


    /**
     * Getter for SN
     * @return double for the SN value
     */
    public double getSN() {
        return iSN;
    }
}
