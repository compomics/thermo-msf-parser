/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser_API.highmeminstance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author toorn101
 */
public class ProteinGroup {
    private int proteinGroupId;
    private List<Protein> proteins = new ArrayList<Protein>();
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

    public List<Protein> getProteins() {
        return proteins;
    }
   
    
}
