package com.compomics.thermo_msf_parser_API.highmeminstance;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 18-Feb-2011
 * Time: 13:08:01
 * To change this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
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
     *
     * @param lRatioType The ratio type
     * @param lNumerator The numerator
     * @param lDenominator The denominator
     * @param lChannelIds The channelids
     * @param lComponents The components
     */
    public RatioType(String lRatioType, String lNumerator, String lDenominator, List<Integer> lChannelIds, List<String> lComponents) {
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


    /**
     * <p>Constructor for RatioType.</p>
     */
    public RatioType(){
    
    }
    
    //getters

    /**
     * <p>getRatioType.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getRatioType() {
        return iRatioType;
    }

    /**
     * <p>getNumerator.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNumerator() {
        return iNumerator;
    }

    /**
     * <p>getDenominator.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDenominator() {
        return iDenominator;
    }

    /**
     * <p>getNumeratorChannelId.</p>
     *
     * @return a int.
     */
    public int getNumeratorChannelId() {
        return iNumeratorChannelId;
    }

    /**
     * <p>getDenominatorChannelId.</p>
     *
     * @return a int.
     */
    public int getDenominatorChannelId() {
        return iDenominatorChannelId;
    }
}
