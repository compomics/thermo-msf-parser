/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser.msf.enums;

/**
 *
 * @author Davy
 */
    
    public enum PeptideFragmentIonType {

        /**
         * This int is the identifier for an a ion.
         */
        A_ION,
        /**
         * This int is the identifier for an a ion with NH3 loss.
         */
        ANH3_ION,
        /**
         * This int is the identifier for an a ion with H2O loss.
         */
        AH2O_ION,
        /**
         * This int is the identifier for a b ion.
         */
        B_ION,
        /**
         * This int is the identifier for a b ion with NH3 loss.
         */
        BNH3_ION,
        /**
         * This int is the identifier for a b ion with H2O loss.
         */
        BH2O_ION,
        /**
         * This int is the identifier for a c ion.
         */
        C_ION,
        /**
         * This int is the identifier for a x ion.
         */
        X_ION,
        /**
         * This int is the identifier for a y ion.
         */
        Y_ION, /**
         * This int is the identifier for a y ion with NH3 loss.
         */
        YNH3_ION,
        /**
         * This int is the identifier for a y ion with H2O loss.
         */
        YH2O_ION,
        /**
         * This int is the identifier for a z ion.
         */
        Z_ION,
        /**
         * This int is the identifier for an MH ion. The number of H is not represented here.
         */
        MH_ION,
        /**
         * This int is the identifier for an MH-NH3 ion.
         */
        MHNH3_ION,
        /**
         * This int is the identifier for an MH-H2O ion.
         */
        MHH2O_ION,
        /**
         * This int is the identifier for an immonium ion. The nature of the immonium ion is not coded yet.
         */
        IMMONIUM,
        /**
         * This int is the identifier for a precursor ion loss. The nature of the loss is not coded yet.
         */
        PRECURSOR_LOSS;
    }