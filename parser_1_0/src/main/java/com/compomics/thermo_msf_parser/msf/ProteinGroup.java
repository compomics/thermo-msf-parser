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
 * @author toorn101
 */
public class ProteinGroup {
    private int proteinGroupId;
    private Vector<Protein> proteins = new Vector<Protein>();
    private Set<Integer> peptideIds = new HashSet<Integer>();

    public ProteinGroup(int proteinGroupId) {
        this.proteinGroupId = proteinGroupId;
    }

    public Set<Integer> getPeptideIds() {
        return peptideIds;
    }


    public int getProteinGroupId() {
        return proteinGroupId;
    }

    public Vector<Protein> getProteins() {
        return proteins;
    }
   
    
}
