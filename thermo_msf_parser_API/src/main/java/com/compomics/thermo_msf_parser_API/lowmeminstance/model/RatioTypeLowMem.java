package com.compomics.thermo_msf_parser_API.lowmeminstance.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/30/12
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class RatioTypeLowMem{

    private List<String> iComponents = new ArrayList<String>();
    private final String iRatioType;
    private final String iNumerator;
    private final String iDenominator;
    private int iDenominatorChannelId;
    private int iNumeratorChannelId;
    private final List<Integer> lChannelIds;
    
/**
 * <p>Constructor for RatioTypeLowMem.</p>
 *
 * @param lRatioType a {@link java.lang.String} object.
 * @param lNumerator a {@link java.lang.String} object.
 * @param lDenominator a {@link java.lang.String} object.
 * @param lChannelIds a {@link java.util.List} object.
 * @param lComponents a {@link java.util.List} object.
 */
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
    /**
     * <p>getComponents.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<String> getComponents(){
    return iComponents;
    }
    /**
     * <p>getChannelIds.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Integer> getChannelIds(){
    return lChannelIds;
    }
    
}
