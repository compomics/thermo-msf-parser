package com.compomics.thermo_msf_parser.msf.proteinsorter;

import com.compomics.thermo_msf_parser.msf.ProteinLowMem;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 24-Feb-2011
 * Time: 15:52:07
 */

/**
 * This will sort proteins based on the number of peptides
 */
public class ProteinSorter implements ProteinSorterInterface {

    private Boolean i1to20;
    private Boolean iAtoZ;


    public int compare(ProteinLowMem o1, ProteinLowMem o2) {
        if(i1to20 != null){
            if (i1to20){
                if(o2.getNumberOfPeptides() - o1.getNumberOfPeptides() > 0){
                    return -1;
                }

                if(o2.getNumberOfPeptides() - o1.getNumberOfPeptides() < 0){
                    return 1;
                }
        } else if (!i1to20 && iAtoZ == null) {
                if((o2.getNumberOfPeptides() - o1.getNumberOfPeptides()) > 0){
                    return 1;
                }
                if(o2.getNumberOfPeptides() - o1.getNumberOfPeptides() < 0){
                    return -1;
                }
        }
    } else  {
        if (iAtoZ) {
            return o1.toString().compareTo(o2.toString());
        } else {
            return o1.toString().compareTo(o2.toString()) * -1;
        }
    }
    return 0;
}

    public void compareProteinByAccession(Boolean lAtoZ) {
        this.iAtoZ = lAtoZ;
        this.i1to20 = null;
    }

    public void compareProteinByNumberOfPeptides(Boolean l1to20) {
        this.i1to20 = l1to20;
        this.iAtoZ = null;
    }
}

