package com.compomics.thermo_msf_parser.gui;

import com.compomics.util.experiment.personalization.UrParameter;

/**
 * A class needed to be able to add charge to PeptideFragmentIon objects, as 
 * this is not supported in the standard PeptideFragmentIon objects.
 *
 * @author Harald Barsnes
 */
public class Charge implements UrParameter {

    /**
     * The charge as an int.
     */
    private int charge;

    /**
     * Default constructor. Used to retrieve the UrParamater.
     */
    public Charge() {
    }

    /**
     * Create a charge object with the given charge. Assumed to be
     * positive.
     *
     * @param charge the charge as an int
     */
    public Charge(int charge) {
        this.charge = charge;
    }

    /**
     * Returns the charge as an int.
     *
     * @return the charge as an int
     */
    public int getCharge() {
        return charge;
    }

    public String getFamilyName() {
        return "Charge";
    }

    public int getIndex() {
        return 0;
    }
}
