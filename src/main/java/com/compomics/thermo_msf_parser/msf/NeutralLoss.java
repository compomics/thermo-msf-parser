package com.compomics.thermo_msf_parser.msf;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 11/04/11
 * Time: 09:43
 */
public class NeutralLoss {

    /**
     * The neutral loss id
     */
    public int iNeutralLossId;
    /**
     * The neutral loss name
     */
    public String iName;
    /**
     * The monoisotopic mass
     */
    public double iMonoisotopicMass;
    /**
     * The average mass
     */
    public double iAverageMass;
    /**
     * The amino acids that have this neutral loss
     */
    public Vector<AminoAcid> iAttachedAminoAcids = new Vector<AminoAcid>();

    /**
     * The constructor
     * @param aNeutralLossId
     * @param aName
     * @param aMonoisotopicMass
     * @param aAverageMass
     */
    public NeutralLoss(int aNeutralLossId, String aName, double aMonoisotopicMass, double aAverageMass) {
        iNeutralLossId = aNeutralLossId;
        iName = aName;
        iMonoisotopicMass = aMonoisotopicMass;
        iAverageMass = aAverageMass;
    }

    public int getNeutralLossId() {
        return iNeutralLossId;
    }

    public String getName() {
        return iName;
    }

    public double getMonoisotopicMass() {
        return iMonoisotopicMass;
    }

    public double getAverageMass() {
        return iAverageMass;
    }

    /**
     * Add an amino acid to this neutral loss
     * @param aAminoAcid
     */
    public void addAminoAcid(AminoAcid aAminoAcid){
        iAttachedAminoAcids.add(aAminoAcid);
    }
}
