package com.compomics.thermo_msf_parser_API.lowmeminstance.model;

import com.compomics.thermo_msf_parser_API.highmeminstance.IsotopePattern;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 5/7/12
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class QuanResultLowMem {

    /**
     * The quan result constructor
     * @param iQuanResultId The quan result id
     */
    public QuanResultLowMem(int iQuanResultId) {
        this.iQuanResultId = iQuanResultId;
    }

    /**
     * The quan result id
     */
    private final int iQuanResultId;
    /**
     * A vector with the quan channel ids
     */
    private final List<Integer> iQuanChannelIds = new ArrayList<Integer>();
    /**
     * A vector with the masses
     */
    private final List<Double> iMasses = new ArrayList<Double>();
    /**
     * A vector with the charges
     */
    private final List<Integer> iCharges = new ArrayList<Integer>();
    /**
     * A vector with the intensities
     */
    private final List<Double> iIntensities = new ArrayList<Double>();
    /**
     * A vector with the retention times
     */
    private final List<Double> iRetentionTimes = new ArrayList<Double>();
    /**
     * A List that holds the isotope patterns
     */
    private final List<IsotopePattern> iIsotopePatterns = new ArrayList<IsotopePattern>();
    /**
     * The processing node number
     */
    private final List<Integer> iProcessingNodeNumbers = new ArrayList<Integer>();
    /**
     * The spectrum id
     */
    private final List<Integer> iSpectrumIds = new ArrayList<Integer>();
    /**
     * The height
     */
    private double iHeight;
    /**
     * The area
     */
    private double iArea;
    /**
     * The start time
     */
    private double iStartTime;
    /**
     * The end time
     */
    private double iEndTime;
    /**
     * The start peak time
     */
    private double iStartPeakTime;
    /**
     * The start peak intensity
     */
    private double iStartPeakIntensity;
    /**
     * The end peak time
     */
    private double iEndPeakTime;
    /**
     * The end peak intensity
     */
    private double iEndPeakIntensity;



    //getters

    public int getQuanResultId() {
        return iQuanResultId;
    }

    public List<Integer> getQuanChannelIds() {
        return iQuanChannelIds;
    }

    public List<Double> getMasses() {
        return iMasses;
    }

    public List<Integer> getCharges() {
        return iCharges;
    }

    public List<Double> getIntensities() {
        return iIntensities;
    }

    public List<Double> getRetentionTimes() {
        return iRetentionTimes;
    }

    public List<IsotopePattern> getIsotopePatterns() {
        return iIsotopePatterns;
    }

    public double getHeight() {
        return iHeight;
    }

    public double getArea() {
        return iArea;
    }

    public double getStartTime() {
        return iStartTime;
    }

    public double getEndTime() {
        return iEndTime;
    }

    public double getStartPeakTime() {
        return iStartPeakTime;
    }

    public double getStartPeakIntensity() {
        return iStartPeakIntensity;
    }

    public double getEndPeakTime() {
        return iEndPeakTime;
    }

    public double getEndPeakIntensity() {
        return iEndPeakIntensity;
    }

    /**
     * This method will calculate a ratio (double) based on the given ratio type
     * @param lType The ratio type
     * @return double with the ratio
     */
    public Double getRatioByRatioType(RatioTypeLowMem lType){
        Double lNumValue = null;
        Double lDenValue = null;
        for(int i = 0; i<iQuanChannelIds.size(); i ++){
            if(lType.getNumeratorChannelId() ==  iQuanChannelIds.get(i)){
                lNumValue = iIntensities.get(i);
            }
            if(lType.getDenominatorChannelId() ==  iQuanChannelIds.get(i)){
                lDenValue = iIntensities.get(i);
            }
        }
        if(lNumValue != null && lDenValue != null){
            return lNumValue / lDenValue;
        } else {
            return null;
        }
    }

    /**
     * This method will give a numerator based on the given ratio type
     * @param lType The ratio type
     * @return double with the numerator
     */
    public Double getNumeratorByRatioType(RatioTypeLowMem lType){
        Double lNumValue = null;
        for(int i = 0; i<iQuanChannelIds.size(); i ++){
            if(lType.getNumeratorChannelId() ==  iQuanChannelIds.get(i)){
                lNumValue = iIntensities.get(i);
            }
        }
        return lNumValue;
    }

    /**
     * This method will give a denominator based on the given ratio type
     * @param lType The ratio type
     * @return double with the denominator
     */
    public Double getDenominatorByRatioType(RatioTypeLowMem lType){
        Double lDenValue = null;
        for(int i = 0; i<iQuanChannelIds.size(); i ++){
            if(lType.getDenominatorChannelId() ==  iQuanChannelIds.get(i)){
                lDenValue = iIntensities.get(i);
            }
        }
        return lDenValue;
    }

    //setters

    public void setHeight(double iHeight) {
        this.iHeight = iHeight;
    }

    public void setArea(double iArea) {
        this.iArea = iArea;
    }

    public void setStartTime(double iStartTime) {
        this.iStartTime = iStartTime;
    }

    public void setEndTime(double iEndTime) {
        this.iEndTime = iEndTime;
    }

    public void setStartPeakTime(double iStartPeakTime) {
        this.iStartPeakTime = iStartPeakTime;
    }

    public void setStartPeakIntensity(double iStartPeakIntensity) {
        this.iStartPeakIntensity = iStartPeakIntensity;
    }

    public void setEndPeakTime(double iEndPeakTime) {
        this.iEndPeakTime = iEndPeakTime;
    }

    public void setEndPeakIntensity(double iEndPeakIntensity) {
        this.iEndPeakIntensity = iEndPeakIntensity;
    }


    /**
     * This method will add an isotope pattern to this quan result
     * @param lIso The isotope pattern to add
     */
    public void addIsotopePattern(IsotopePattern lIso){
        this.iIsotopePatterns.add(lIso);
    }

    /**
     * This method will add some quan values
     * @param iQuanChannelId The quan channel id
     * @param iMass The mass
     * @param iCharge The charge
     * @param iIntensity The intensity
     * @param iRetentionTime The retention time
     */
    public void addQuanValues(int iQuanChannelId, double iMass, int iCharge, double iIntensity, double iRetentionTime) {
        this.iQuanChannelIds.add(iQuanChannelId);
        this.iMasses.add(iMass);
        this.iCharges.add(iCharge);
        this.iIntensities.add(iIntensity);
        this.iRetentionTimes.add(iRetentionTime);
    }

    public void addSpectrumId(int anInt) {
        this.iSpectrumIds.add(anInt);
    }

    public void addProcessingNodeNumber(int anInt) {
        this.iProcessingNodeNumbers.add(anInt);
    }

    public List<Integer> getSpectrumIds() {
        return iSpectrumIds;
    }

    public List<Integer> getProcessingNodeNumbers() {
        return iProcessingNodeNumbers;
    }
}
