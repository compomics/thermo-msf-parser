/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser.msf;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author Davy Maddelein
 */
public class ProteinGroupLowMem {
    private int proteinGroupId;
    private Vector<ProteinLowMem> proteins = new Vector<ProteinLowMem>();
    private Set<Integer> peptideIds = new HashSet<Integer>();

    public ProteinGroupLowMem(int proteinGroupId) {
        this.proteinGroupId = proteinGroupId;
    }

    public Set<Integer> getPeptideIds() {
        return peptideIds;
    }


    public int getProteinGroupId() {
        return proteinGroupId;
    }

    public Vector<ProteinLowMem> getProteins() {
        return proteins;
    }
   
    
}
