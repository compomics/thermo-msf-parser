package com.compomics.thermo_msf_parser.msf;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 18-Feb-2011
 * Time: 13:08:01
 * To change this template use File | Settings | File Templates.
 */
public class RatioType {
    /**
     * The ratio type
     */
    public String iRatioType;
    /**
     * The numerator
     */
    public String iNumerator;
    /**
     * The denominator
     */
    public String iDenominator;
    /**
     * The numerator channelid
     */
    public int iNumeratorChannelId;
    /**
     * The denominator channel id
     */
    public int iDenominatorChannelId;

    /**
     * The ratiotype constructor
     * @param lRatioType The ratio type
     * @param lNumerator The numerator
     * @param lDenominator The denominator
     * @param lChannelIds The channelids
     * @param lComponents The components
     */
    public RatioType(String lRatioType, String lNumerator, String lDenominator, Vector<Integer> lChannelIds, Vector<String> lComponents) {
        this.iRatioType = lRatioType;
        this.iNumerator = lNumerator;
        this.iDenominator = lDenominator;

        for (int i = 0; i < lComponents.size(); i++) {
            if (lComponents.get(i).equalsIgnoreCase(iNumerator)) {
                iNumeratorChannelId = lChannelIds.get(i);
            }
            if (lComponents.get(i).equalsIgnoreCase(iDenominator)) {
                iDenominatorChannelId = lChannelIds.get(i);
            }
        }
    }


    //getters

    public String getRatioType() {
        return iRatioType;
    }

    public String getNumerator() {
        return iNumerator;
    }

    public String getDenominator() {
        return iDenominator;
    }

    public int getNumeratorChannelId() {
        return iNumeratorChannelId;
    }

    public int getDenominatorChannelId() {
        return iDenominatorChannelId;
    }
}
