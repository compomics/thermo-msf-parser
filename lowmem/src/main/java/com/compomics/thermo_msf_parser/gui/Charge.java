package com.compomics.thermo_msf_parser.gui;

import com.compomics.thermo_msf_parser.msf.util.UrParameter;
import org.apache.log4j.Logger;

/**
 * A class needed to be able to add charge to PeptideFragmentIon objects, as 
 * this is not supported in the standard PeptideFragmentIon objects.
 *
 * @author Harald Barsnes
 */
public class Charge implements UrParameter {
	// Class specific log4j logger for Charge instances.
	 private static Logger logger = Logger.getLogger(Charge.class);
    static final long serialVersionUID = 6808590175195298797L;

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
