package com.compomics.thermo_msf_parser.msf.proteinsorter;

import com.compomics.thermo_msf_parser.msf.Protein;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 24-Feb-2011
 * Time: 15:52:07
 */

/**
 * This will sort proteins based on the number of peptides
 */
public class ProteinSorterByNumberOfPeptides implements Comparator<Protein> {
    public int compare(Protein o1, Protein o2) {
        if(o2.getPeptides().size() - o1.getPeptides().size() > 0){
            return 1;
        }
        if(o2.getPeptides().size() - o1.getPeptides().size() < 0){
            return -1;
        }
        return 0;
    }
}

