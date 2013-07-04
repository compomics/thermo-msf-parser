package com.compomics.thermo_msf_parser_API.proteinsorter;


import java.io.Serializable;
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
public class ProteinSorterByAccession implements Comparator<String>, Serializable {
    private boolean iAtoZ;

    public ProteinSorterByAccession(boolean lAtoZ){
        this.iAtoZ = lAtoZ;
    }
    @Override
    public int compare(String o1, String o2) {
        int comparator;
        if(iAtoZ){
            comparator = o1.compareTo(o2);
        } else {
            comparator = o1.compareTo(o2) * -1;
        }
        return comparator;
    }
}

