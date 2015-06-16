package com.compomics.thermo_msf_parser_API.highmeminstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 11/04/11
 * Time: 09:43
 *
 * @author Davy Maddelein
 * @version $Id: $Id
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
    public List<AminoAcid> iAttachedAminoAcids = new ArrayList<AminoAcid>();

    /**
     * The constructor
     *
     * @param aNeutralLossId a int.
     * @param aName a {@link java.lang.String} object.
     * @param aMonoisotopicMass a double.
     * @param aAverageMass a double.
     */
    public NeutralLoss(int aNeutralLossId, String aName, double aMonoisotopicMass, double aAverageMass) {
        iNeutralLossId = aNeutralLossId;
        iName = aName;
        iMonoisotopicMass = aMonoisotopicMass;
        iAverageMass = aAverageMass;
    }

    /**
     * <p>getNeutralLossId.</p>
     *
     * @return a int.
     */
    public int getNeutralLossId() {
        return iNeutralLossId;
    }

    /**
     * <p>getName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return iName;
    }

    /**
     * <p>getMonoisotopicMass.</p>
     *
     * @return a double.
     */
    public double getMonoisotopicMass() {
        return iMonoisotopicMass;
    }

    /**
     * <p>getAverageMass.</p>
     *
     * @return a double.
     */
    public double getAverageMass() {
        return iAverageMass;
    }

    /**
     * Add an amino acid to this neutral loss
     *
     * @param aAminoAcid a {@link com.compomics.thermo_msf_parser_API.highmeminstance.AminoAcid} object.
     */
    public void addAminoAcid(AminoAcid aAminoAcid){
        iAttachedAminoAcids.add(aAminoAcid);
    }
}
