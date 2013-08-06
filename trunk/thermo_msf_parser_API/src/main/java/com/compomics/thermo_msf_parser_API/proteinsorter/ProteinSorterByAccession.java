package com.compomics.thermo_msf_parser_API.proteinsorter;


import com.compomics.thermo_msf_parser_API.interfaces.models.ProteinModel;
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
public class ProteinSorterByAccession implements Comparator<ProteinModel>, Serializable {
    private boolean iAtoZ;

    public ProteinSorterByAccession(boolean lAtoZ){
        this.iAtoZ = lAtoZ;
    }
    @Override
    public int compare(ProteinModel o1, ProteinModel o2) {
        int comparator;
        if(iAtoZ){
            comparator = o1.getAccession().compareTo(o2.getAccession());
        } else {
            comparator = o1.getAccession().compareTo(o2.getAccession()) * -1;
        }
        return comparator;
    }
}

