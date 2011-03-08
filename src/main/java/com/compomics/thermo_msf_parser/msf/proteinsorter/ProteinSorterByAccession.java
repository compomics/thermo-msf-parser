package com.compomics.thermo_msf_parser.msf.proteinsorter;

import com.compomics.thermo_msf_parser.msf.Protein;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 24-Feb-2011
 * Time: 15:50:00
 */

/**
 * This will sort proteins alphabetically 
 */
public class ProteinSorterByAccession implements Comparator<Protein> {
    public int compare(Protein o1, Protein o2) {
        return o1.toString().compareTo(o2.toString());
    }
}

