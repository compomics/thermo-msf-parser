package com.compomics.thermo_msf_parser.msf;

import org.apache.log4j.Logger;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 18-Feb-2011
 * Time: 09:26:18
 * To change this template use File | Settings | File Templates.
 */
public class Modification {
    // Class specific log4j logger for Thermo_msf_parserGUI instances.
	 private static Logger logger = Logger.getLogger(Modification.class);
    /**
     * The modification id
     */
    private int iAminoAcidModificationId;
    /**
     * The modification name
     */
    private String iModificationName;
    /**
     * The delta mass
     */
    private double iDeltaMass;
    /**
     * The chemical formula of the substitution
     */
    private String iSubstitution;
    /**
     * The leaving group
     */
    private String iLeavingGroup;
    /**
     * The abbreviation of the modification
     */
    private String iAbbreviation;
    /**
     * The position type
     */
    private int iPositionType;
    /**
     * Int that indicates if this modification is active
     */
    private int iIsActive;
    /**
     * The average delta mass
     */
    private double iDeltaAverageMass;
    /**
     * The unimod accession
     */
    private int iUnimodAccession;
    /**
     * Int that indicates if this modification is a substitution
     */
    private int iIsSubstitution;
    /**
     * The amino acids linked to this modification
     */
    private Vector<AminoAcid> iAttachedAminoAcids = new Vector<AminoAcid>();
    /**
     * The neutral losses attached to this amino acid
     * Only works for Thermo Proteome Discoverer version 1.3
     */
    private Vector<NeutralLoss> iAttachedNeutralLosses = new Vector<NeutralLoss>();
    /**
     * The classification for the amino acid
     * Only works for Thermo Proteome Discoverer version 1.3
     */
    private Vector<Integer> iAttachedClassifications = new Vector<Integer>();

    /**
     * The Modification constructor
     * @param iAminoAcidModificationId The modificationid
     * @param iModificationName The modification name
     * @param iDeltaMass The delta mass
     * @param iSubstitution The chemical formula of the substitution
     * @param iLeavingGroup The leaving group
     * @param iAbbreviation The abbreviation
     * @param iPositionType The position type
     * @param iIsActive An int that indicates if this modification is active
     * @param iDeltaAverageMass The average delta mass
     * @param iUnimodAccession The unimod accession
     * @param iIsSubstitution An int that indicates if this modification is a substitution
     */
    public Modification(int iAminoAcidModificationId, String iModificationName, double iDeltaMass, String iSubstitution, String iLeavingGroup, String iAbbreviation, int iPositionType, int iIsActive, double iDeltaAverageMass, int iUnimodAccession, int iIsSubstitution) {
        this.iAminoAcidModificationId = iAminoAcidModificationId;
        this.iModificationName = iModificationName;
        this.iDeltaMass = iDeltaMass;
        this.iSubstitution = iSubstitution;
        this.iLeavingGroup = iLeavingGroup;
        this.iAbbreviation = iAbbreviation;
        this.iPositionType = iPositionType;
        this.iIsActive = iIsActive;
        this.iDeltaAverageMass = iDeltaAverageMass;
        this.iUnimodAccession = iUnimodAccession;
        this.iIsSubstitution = iIsSubstitution;
    }


    public void addAminoAcid(AminoAcid lAa){
        this.iAttachedAminoAcids.add(lAa);
    }

    public void addNeutralLoss(NeutralLoss aNeutralLoss){
        this.iAttachedNeutralLosses.add(aNeutralLoss);
    }
    
    //getters

    public int getAminoAcidModificationId() {
        return iAminoAcidModificationId;
    }

    public String getModificationName() {
        return iModificationName;
    }

    public double getDeltaMass() {
        return iDeltaMass;
    }

    public String getSubstitution() {
        return iSubstitution;
    }

    public String getLeavingGroup() {
        return iLeavingGroup;
    }

    public String getAbbreviation() {
        return iAbbreviation;
    }

    public int getPositionType() {
        return iPositionType;
    }

    public int getIsActive() {
        return iIsActive;
    }

    public double getDeltaAverageMass() {
        return iDeltaAverageMass;
    }

    public int getUnimodAccession() {
        return iUnimodAccession;
    }

    public int getIsSubstitution() {
        return iIsSubstitution;
    }

    public Vector<AminoAcid> getAttachedAminoAcids() {
        return iAttachedAminoAcids;
    }

    public void addClassificationForAminoAcid(int aClassification) {
        this.iAttachedClassifications.add(aClassification);
    }

}
