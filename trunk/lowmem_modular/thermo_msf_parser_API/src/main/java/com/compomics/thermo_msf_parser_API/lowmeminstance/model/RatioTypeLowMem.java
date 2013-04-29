package com.compomics.thermo_msf_parser_API.lowmeminstance.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/30/12
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class RatioTypeLowMem{

    private List<String> iComponents = new ArrayList<String>();
    private final String iRatioType;
    private final String iNumerator;
    private final String iDenominator;
    private int iDenominatorChannelId;
    private int iNumeratorChannelId;
    private final List<Integer> lChannelIds;
    
public RatioTypeLowMem(String lRatioType, String lNumerator, String lDenominator, List<Integer> lChannelIds, List<String> lComponents) {
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
    public List<String> getComponents(){
    return iComponents;
    }
    public List<Integer> getChannelIds(){
    return lChannelIds;
    }
    
}
