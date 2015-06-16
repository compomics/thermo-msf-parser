package com.compomics.thermo_msf_parser_API.highmeminstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 18-Feb-2011
 * Time: 09:26:18
 * To change this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class Modification {

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
    private List<AminoAcid> iAttachedAminoAcids = new ArrayList<AminoAcid>();
    /**
     * The amino acids selected to search on
     */
    private List<AminoAcid> iSelectedAminoAcids = new ArrayList<AminoAcid>();
    /**
     * The neutral losses attached to this amino acid
     * Only works for Thermo Proteome Discoverer version 1.3
     */
    private List<NeutralLoss> iAttachedNeutralLosses = new ArrayList<NeutralLoss>();
    /**
     * The classification for the amino acid
     * Only works for Thermo Proteome Discoverer version 1.3
     */
    private List<Integer> iAttachedClassifications = new ArrayList<Integer>();
    /**
     * Indication if modification is used as a fixed or variable modification
     */
    private boolean fixedModification = false;

    /**
     * The Modification constructor
     *
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
     * @param isFixedModification boolean to indicate whether the modification is used as fixed or variable
     */
    public Modification(int iAminoAcidModificationId, String iModificationName, double iDeltaMass, String iSubstitution, String iLeavingGroup, String iAbbreviation, int iPositionType, int iIsActive, double iDeltaAverageMass, int iUnimodAccession, int iIsSubstitution, boolean isFixedModification) {
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
        this.fixedModification = isFixedModification;
    }


    /**
     * <p>addAminoAcid.</p>
     *
     * @param lAa a {@link com.compomics.thermo_msf_parser_API.highmeminstance.AminoAcid} object.
     */
    public void addAminoAcid(AminoAcid lAa){
        this.iAttachedAminoAcids.add(lAa);
    }

    /**
     * <p>addNeutralLoss.</p>
     *
     * @param aNeutralLoss a {@link com.compomics.thermo_msf_parser_API.highmeminstance.NeutralLoss} object.
     */
    public void addNeutralLoss(NeutralLoss aNeutralLoss){
        this.iAttachedNeutralLosses.add(aNeutralLoss);
    }
    
    //getters

    /**
     * <p>getAminoAcidModificationId.</p>
     *
     * @return a int.
     */
    public int getAminoAcidModificationId() {
        return iAminoAcidModificationId;
    }

    /**
     * <p>getModificationName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getModificationName() {
        return iModificationName;
    }

    /**
     * <p>getDeltaMass.</p>
     *
     * @return a double.
     */
    public double getDeltaMass() {
        return iDeltaMass;
    }

    /**
     * <p>getSubstitution.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSubstitution() {
        return iSubstitution;
    }

    /**
     * <p>getLeavingGroup.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLeavingGroup() {
        return iLeavingGroup;
    }

    /**
     * <p>getAbbreviation.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAbbreviation() {
        return iAbbreviation;
    }

    /**
     * <p>getPositionType.</p>
     *
     * @return a int.
     */
    public int getPositionType() {
        return iPositionType;
    }

    /**
     * <p>getIsActive.</p>
     *
     * @return a int.
     */
    public int getIsActive() {
        return iIsActive;
    }

    /**
     * <p>getDeltaAverageMass.</p>
     *
     * @return a double.
     */
    public double getDeltaAverageMass() {
        return iDeltaAverageMass;
    }

    /**
     * <p>getUnimodAccession.</p>
     *
     * @return a int.
     */
    public int getUnimodAccession() {
        return iUnimodAccession;
    }

    /**
     * <p>getIsSubstitution.</p>
     *
     * @return a int.
     */
    public int getIsSubstitution() {
        return iIsSubstitution;
    }

    /**
     * <p>getAttachedAminoAcids.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<AminoAcid> getAttachedAminoAcids() {
        return iAttachedAminoAcids;
    }

    /**
     * <p>getSelectedAminoAcids.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<AminoAcid> getSelectedAminoAcids() {
        return iSelectedAminoAcids;
    }
    
    

    /**
     * <p>addClassificationForAminoAcid.</p>
     *
     * @param aClassification a int.
     */
    public void addClassificationForAminoAcid(int aClassification) {
        this.iAttachedClassifications.add(aClassification);
    }

    /**
     * <p>isFixedModification.</p>
     *
     * @return a boolean.
     */
    public boolean isFixedModification() {
        return fixedModification;
    }

}
