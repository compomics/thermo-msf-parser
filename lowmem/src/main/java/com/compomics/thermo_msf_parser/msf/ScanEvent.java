package com.compomics.thermo_msf_parser.msf;

import com.compomics.thermo_msf_parser.msf.enums.ActivationType;
import com.compomics.thermo_msf_parser.msf.enums.DetectorType;
import com.compomics.thermo_msf_parser.msf.enums.IonizationType;
import com.compomics.thermo_msf_parser.msf.enums.ScanType;
import java.util.EnumSet;
import org.apache.commons.lang3.EnumUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 18-Feb-2011
 * Time: 13:26:06
 * To change this template use File | Settings | File Templates.
 */
public class ScanEvent {
    /**
     * The scan event id
     */
    private int iScanEventId;
    /**
     * The ms level
     */
    private int iMSLevel;
    /**
     * The polarity
     */
    private int iPolarity;
    /**
     * The scan type
     */
    private int iScanType;
    /**
     * The ionization
     */
    private int iIonization;
    /**
     * The mass analyzer
     */
    private int iMassAnalyzer;
    /**
     * The activation type
     */
    private int iActivationType;

    /**
     * The scanevent constructor
     * @param iScanEventId The scanevent id
     * @param iMSLevel The ms level
     * @param iPolarity The polarity
     * @param iScanType The scan type
     * @param iIonization The ionization
     * @param iMassAnalyzer The mass analyzer
     * @param iActivationType The activation type
     */
    public ScanEvent(int iScanEventId, int iMSLevel, int iPolarity, int iScanType, int iIonization, int iMassAnalyzer, int iActivationType) {
        this.iScanEventId = iScanEventId;
        this.iMSLevel = iMSLevel;
        this.iPolarity = iPolarity;
        this.iScanType = iScanType;
        this.iIonization = iIonization;
        this.iMassAnalyzer = iMassAnalyzer;
        this.iActivationType = iActivationType;
    }
    
    
    //getters

    public int getScanEventId() {
        return iScanEventId;
    }

    public int getMSLevel() {
        return iMSLevel;
    }

    public int getPolarity() {
        return iPolarity;
    }

//    public int getScanType() {
//        return iScanType;
//    }
    
    public ScanType getScanType() {
        return ScanType.values()[iScanType];
    }

    public int getIonization() {
        return iIonization;
    }
    
    public IonizationType getIonizationTypes() {
        return IonizationType.values()[iIonization];
    }

    public int getMassAnalyzer() {
        return iMassAnalyzer;
    }
    
    public DetectorType getMassAnalyzerType() {
        return DetectorType.values()[iMassAnalyzer];
    }

    public int getActivationType() {
        return iActivationType;
    }
    
    public EnumSet<ActivationType> getActivationTypeSet() {
        return EnumUtils.processBitVector(ActivationType.class, (long)iActivationType);
    }

    
}
