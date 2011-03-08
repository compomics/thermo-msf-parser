package com.compomics.thermo_msf_parser.msf;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 21-Feb-2011
 * Time: 14:53:30
 */
public class FragmentIon {
    /**
     * The fragment ion mass
     */
    private double iMass;
    /**
     * The charge
     */
    private int iCharge;
    /**
     * The fragment ion type
     */
    private FragmentIonType iFragmentIonType;
    /**
     * The fragmentation number
     */
    private int iNumber;
    /**
     * The original b mass
     */
    private double iOriginalBMass;
    /**
     * The original y mass
     */
    private double iOriginalYMass;

    /**
     * Constructor
     * @param lMass double with the mass
     * @param lType FragmentIonType
     * @param lNumber The number
     * @param lCharge The charge
     * @param lOriginalBMass The original b mass
     * @param lOriginalYMass The original y mass
     */
    public FragmentIon(double lMass, FragmentIonType lType, int lNumber, int lCharge, double lOriginalBMass, double lOriginalYMass){
        this.iMass = lMass;
        this.iFragmentIonType = lType;
        this.iNumber = lNumber;
        this.iCharge = lCharge;
        this.iOriginalBMass = lOriginalBMass;
        this.iOriginalYMass = lOriginalYMass;
    }

    /**
     * Getter for the mass
     * @return double with the mass
     */
    public double getMass() {
        return iMass;
    }

    /**
     * Setter for the mass
     * @param iMass double with the mass to set
     */
    public void setMass(double iMass) {
        this.iMass = iMass;
    }

    /**
     * Getter for the charge
     * @return int with the charge
     */
    public int getCharge() {
        return iCharge;
    }

    /**
     * Setter for the charge
     * @param iCharge int with the charge to set
     */
    public void setCharge(int iCharge) {
        this.iCharge = iCharge;
    }

    /**
     * Getter for the fragmentiontype
     * @return Fragmentiontype
     */
    public FragmentIonType getFragmentIonType() {
        return iFragmentIonType;
    }

    /**
     * To string method
     *  Charge = 1 -> Fragmentiontype + ion number
     *  Charge > 1 -> Fragmentiontype + ion number , charge
     * @return String for this fragmention
     */
    public String toString(){
        String lResult = "";
        lResult = iFragmentIonType + "" + iNumber;
        if(iCharge>1){
            lResult = lResult + ", " + iCharge + "+";
        }

        return lResult;
    }

    /**
     * Setter for the fragmentiontype
     * @param iType The fragmentiontype to set
     */
    public void setFragmentIonType(FragmentIonType iType) {
        this.iFragmentIonType = iType;
    }

    /**
     * Getter for the number
     * @return int with the number
     */
    public int getNumber() {
        return iNumber;
    }

    /**
     * Setter for the number
     * @param iNumber int with the number
     */
    public void setNumber(int iNumber) {
        this.iNumber = iNumber;
    }


    /**
     * Getter for the original b mass
     * @return double with the original b mass
     */
    public double getOriginalBMass() {
        return iOriginalBMass;
    }

    /**
     * Getter for the original y mass
     * @return double with the original y mass
     */
    public double getOriginalYMass() {
        return iOriginalYMass;
    }


}
