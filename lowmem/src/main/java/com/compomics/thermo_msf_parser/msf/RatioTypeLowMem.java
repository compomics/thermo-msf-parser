package com.compomics.thermo_msf_parser.msf;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/30/12
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class RatioTypeLowMem{

    private Vector<String> iComponents = new Vector<String>();
    private final String iRatioType;
    private final String iNumerator;
    private final String iDenominator;
    private int iDenominatorChannelId;
    private int iNumeratorChannelId;
    private final Vector<Integer> lChannelIds;
    
public RatioTypeLowMem(String lRatioType, String lNumerator, String lDenominator, Vector<Integer> lChannelIds, Vector<String> lComponents) {
        this.iRatioType = lRatioType;
        this.iNumerator = lNumerator;
        this.iDenominator = lDenominator;
        this.iComponents = lComponents;
        this. lChannelIds = lChannelIds;
        
        for (int i = 0; i < lComponents.size(); i++) {
            if (lComponents.get(i).equalsIgnoreCase(iNumerator)) {
                iNumeratorChannelId = lChannelIds.get(i);
            }
            if (lComponents.get(i).equalsIgnoreCase(iDenominator)) {
                iDenominatorChannelId = lChannelIds.get(i);
            }
        }
    }
    
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
    public Vector<String> getComponents(){
    return iComponents;
    }
    public Vector<Integer> getChannelIds(){
    return lChannelIds;
    }
    
}
