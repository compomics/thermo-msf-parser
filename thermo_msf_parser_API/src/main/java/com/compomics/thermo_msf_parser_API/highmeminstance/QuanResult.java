package com.compomics.thermo_msf_parser_API.highmeminstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 21-Feb-2011
 * Time: 11:45:06
 * To change this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class QuanResult {
    /**
     * The quan result id
     */
    private int iQuanResultId;
    /**
     * A vector with the quan channel ids
     */
    private List<Integer> iQuanChannelIds = new ArrayList<Integer>();
    /**
     * A vector with the masses
     */
    private List<Double> iMasses = new ArrayList<Double>();
    /**
     * A vector with the charges
     */
    private List<Integer> iCharges = new ArrayList<Integer>();
    /**
     * A vector with the intensities
     */
    private List<Double> iIntensities = new ArrayList<Double>();
    /**
     * A vector with the retention times
     */
    private List<Double> iRetentionTimes = new ArrayList<Double>();
    /**
     * A Vector that holds the isotope patterns
     */
    private List<IsotopePattern> iIsotopePatterns = new ArrayList<IsotopePattern>();
    /**
     * The processing node number
     */
    private List<Integer> iProcessingNodeNumbers = new ArrayList<Integer>();
    /**
     * The spectrum id
     */
    private List<Integer> iSpectrumIds = new ArrayList<Integer>();
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


    /**
     * The quan result constructor
     *
     * @param iQuanResultId The quan result id
     */
    public QuanResult(int iQuanResultId) {
        this.iQuanResultId = iQuanResultId;
    }
    


    //getters

    /**
     * <p>getQuanResultId.</p>
     *
     * @return a int.
     */
    public int getQuanResultId() {
        return iQuanResultId;
    }

    /**
     * <p>getQuanChannelIds.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Integer> getQuanChannelIds() {
        return iQuanChannelIds;
    }

    /**
     * <p>getMasses.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Double> getMasses() {
        return iMasses;
    }

    /**
     * <p>getCharges.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Integer> getCharges() {
        return iCharges;
    }

    /**
     * <p>getIntensities.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Double> getIntensities() {
        return iIntensities;
    }

    /**
     * <p>getRetentionTimes.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Double> getRetentionTimes() {
        return iRetentionTimes;
    }

    /**
     * <p>getIsotopePatterns.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<IsotopePattern> getIsotopePatterns() {
        return iIsotopePatterns;
    }

    /**
     * <p>getHeight.</p>
     *
     * @return a double.
     */
    public double getHeight() {
        return iHeight;
    }

    /**
     * <p>getArea.</p>
     *
     * @return a double.
     */
    public double getArea() {
        return iArea;
    }

    /**
     * <p>getStartTime.</p>
     *
     * @return a double.
     */
    public double getStartTime() {
        return iStartTime;
    }

    /**
     * <p>getEndTime.</p>
     *
     * @return a double.
     */
    public double getEndTime() {
        return iEndTime;
    }

    /**
     * <p>getStartPeakTime.</p>
     *
     * @return a double.
     */
    public double getStartPeakTime() {
        return iStartPeakTime;
    }

    /**
     * <p>getStartPeakIntensity.</p>
     *
     * @return a double.
     */
    public double getStartPeakIntensity() {
        return iStartPeakIntensity;
    }

    /**
     * <p>getEndPeakTime.</p>
     *
     * @return a double.
     */
    public double getEndPeakTime() {
        return iEndPeakTime;
    }

    /**
     * <p>getEndPeakIntensity.</p>
     *
     * @return a double.
     */
    public double getEndPeakIntensity() {
        return iEndPeakIntensity;
    }

    /**
     * This method will calculate a ratio (double) based on the given ratio type
     *
     * @param lType The ratio type
     * @return double with the ratio
     */
    public Double getRatioByRatioType(RatioType lType){
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
     *
     * @param lType The ratio type
     * @return double with the numerator
     */
    public Double getNumeratorByRatioType(RatioType lType){
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
        return lNumValue;
    }

    /**
     * This method will give a denominator based on the given ratio type
     *
     * @param lType The ratio type
     * @return double with the denominator
     */
    public Double getDenominatorByRatioType(RatioType lType){
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
        return lDenValue;
    }

    //setters

    /**
     * <p>setHeight.</p>
     *
     * @param iHeight a double.
     */
    public void setHeight(double iHeight) {
        this.iHeight = iHeight;
    }

    /**
     * <p>setArea.</p>
     *
     * @param iArea a double.
     */
    public void setArea(double iArea) {
        this.iArea = iArea;
    }

    /**
     * <p>setStartTime.</p>
     *
     * @param iStartTime a double.
     */
    public void setStartTime(double iStartTime) {
        this.iStartTime = iStartTime;
    }

    /**
     * <p>setEndTime.</p>
     *
     * @param iEndTime a double.
     */
    public void setEndTime(double iEndTime) {
        this.iEndTime = iEndTime;
    }

    /**
     * <p>setStartPeakTime.</p>
     *
     * @param iStartPeakTime a double.
     */
    public void setStartPeakTime(double iStartPeakTime) {
        this.iStartPeakTime = iStartPeakTime;
    }

    /**
     * <p>setStartPeakIntensity.</p>
     *
     * @param iStartPeakIntensity a double.
     */
    public void setStartPeakIntensity(double iStartPeakIntensity) {
        this.iStartPeakIntensity = iStartPeakIntensity;
    }

    /**
     * <p>setEndPeakTime.</p>
     *
     * @param iEndPeakTime a double.
     */
    public void setEndPeakTime(double iEndPeakTime) {
        this.iEndPeakTime = iEndPeakTime;
    }

    /**
     * <p>setEndPeakIntensity.</p>
     *
     * @param iEndPeakIntensity a double.
     */
    public void setEndPeakIntensity(double iEndPeakIntensity) {
        this.iEndPeakIntensity = iEndPeakIntensity;
    }


    /**
     * This method will add an isotope pattern to this quan result
     *
     * @param lIso The isotope pattern to add
     */
    public void addIsotopePattern(IsotopePattern lIso){
        this.iIsotopePatterns.add(lIso);
    }

    /**
     * This method will add some quan values
     *
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

    /**
     * <p>addSpectrumId.</p>
     *
     * @param anInt a int.
     */
    public void addSpectrumId(int anInt) {
        this.iSpectrumIds.add(anInt);
    }

    /**
     * <p>addProcessingNodeNumber.</p>
     *
     * @param anInt a int.
     */
    public void addProcessingNodeNumber(int anInt) {
        this.iProcessingNodeNumbers.add(anInt);
    }

    /**
     * <p>getSpectrumIds.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Integer> getSpectrumIds() {
        return iSpectrumIds;
    }

    /**
     * <p>getProcessingNodeNumbers.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Integer> getProcessingNodeNumbers() {
        return iProcessingNodeNumbers;
    }
}
