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
    private boolean iAtoZ;

    public ProteinSorterByAccession(boolean lAtoZ){
        this.iAtoZ = lAtoZ;
    }
    public int compare(Protein o1, Protein o2) {
        if(iAtoZ){
            return o1.toString().compareTo(o2.toString());
        } else {
            return o1.toString().compareTo(o2.toString()) * -1;
        }
    }
}

