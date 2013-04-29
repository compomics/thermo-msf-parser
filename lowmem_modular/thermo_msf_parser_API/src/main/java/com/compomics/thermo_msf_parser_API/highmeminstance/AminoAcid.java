package com.compomics.thermo_msf_parser_API.highmeminstance;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 18-Feb-2011
 * Time: 09:49:50
 */

/**
 * This class represents an amino acid
 */
public class AminoAcid {

    /**
     * The amino acid id
     */
    private int iAminoAcidId;
    /**
     * The amino acid name
     */
    private String iAminoAcidName;
    /**
     * The one letter code
     */
    private String iOneLetterCode;
    /**
     * The three letter code
     */
    private String iThreeLetterCode;
    /**
     * The monoisotopic mass
     */
    private double iMonoisotopicMass;
    /**
     * The average mass
     */
    private double iAverageMass;
    /**
     * The chemical formula of the amino acid 
     */
    private String iSumFormula;

    /**
     * The constructor for the aminoacid
     * @param iAminoAcidId The amino acid id
     * @param iAminoAcidName The amino acid name
     * @param iOneLetterCode The one letter code
     * @param iThreeLetterCode The three letter code
     * @param iMonoisotopicMass The monoisotopic mass
     * @param iAverageMass The average mass
     * @param iSumFormula The chemical formula of the amino acid
     */
    public AminoAcid(int iAminoAcidId, String iAminoAcidName, String iOneLetterCode, String iThreeLetterCode, double iMonoisotopicMass, double iAverageMass, String iSumFormula) {
        this.iAminoAcidId = iAminoAcidId;
        this.iAminoAcidName = iAminoAcidName;
        this.iOneLetterCode = iOneLetterCode;
        this.iThreeLetterCode = iThreeLetterCode;
        this.iMonoisotopicMass = iMonoisotopicMass;
        this.iAverageMass = iAverageMass;
        this.iSumFormula = iSumFormula;
    }


    /**
     * Getter for the amino acid id
     * @return int with the amino acid id
     */
    public int getAminoAcidId() {
        return iAminoAcidId;
    }

    /**
     * Getter for the amino acid name
     * @return String with the amino acid name
     */
    public String getAminoAcidName() {
        return iAminoAcidName;
    }

    /**
     * Getter for the one letter code
     * @return String with the one letter code
     */
    public String getOneLetterCode() {
        return iOneLetterCode;
    }

    /**
     * Getter for the three letter code
     * @return String with the three letter code
     */
    public String getThreeLetterCode() {
        return iThreeLetterCode;
    }

    /**
     * Getter for the monoisotopic mass
     * @return double with the monoisotopic mass
     */
    public double getMonoisotopicMass() {
        return iMonoisotopicMass;
    }

    /**
     * Getter for the average mass
     * @return double with the average mass
     */
    public double getAverageMass() {
        return iAverageMass;
    }

    /**
     * Getter for the sum formula
     * @return String with the chemical formula of the amino acid
     */
    public String getSumFormula() {
        return iSumFormula;
    }
}
