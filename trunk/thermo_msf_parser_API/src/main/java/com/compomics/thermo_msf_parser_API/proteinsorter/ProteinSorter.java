package com.compomics.thermo_msf_parser_API.proteinsorter;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem;


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


    @Override
    public int compare(ProteinLowMem protein1, ProteinLowMem protein2) {
        int comparison = 0;
        if(i1to20 != null){
            if (i1to20){
                if(protein2.getNumberOfPeptides() - protein1.getNumberOfPeptides() > 0){
                    comparison = -1;
                }

                if(protein2.getNumberOfPeptides() - protein1.getNumberOfPeptides() < 0){
                    comparison = 1;
                }
        } else if (!i1to20 && iAtoZ == null) {
                if((protein2.getNumberOfPeptides() - protein1.getNumberOfPeptides()) > 0){
                    comparison = 1;
                }
                if(protein2.getNumberOfPeptides() - protein1.getNumberOfPeptides() < 0){
                    comparison = -1;
                }
        }
    } else  {
        if (iAtoZ) {
            comparison = protein1.getAccession().compareTo(protein2.getAccession());
        } else {
            comparison = protein1.getAccession().compareTo(protein2.getAccession()) * -1;
        }
    }
    return comparison;
}

    @Override
    public void compareProteinByAccession(Boolean lAtoZ) {
        this.iAtoZ = lAtoZ;
        this.i1to20 = null;
    }

    @Override
    public void compareProteinByNumberOfPeptides(Boolean l1to20) {
        this.i1to20 = l1to20;
        this.iAtoZ = null;
    } 
}

