/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser_API.lowmeminstance.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

/**
 *
 * @author Davy Maddelein
 */
public class ProteinGroupLowMem {
    private final int proteinGroupId;
    private final List<ProteinLowMem> proteins = new ArrayList<ProteinLowMem>();
    private final Set<Integer> peptideIds = new HashSet<Integer>();

    public ProteinGroupLowMem(int proteinGroupId) {
        this.proteinGroupId = proteinGroupId;
    }

    public Set<Integer> getPeptideIds() {
        return peptideIds;
    }


    public int getProteinGroupId() {
        return proteinGroupId;
    }

    public List<ProteinLowMem> getProteins() {
        return proteins;
    }  
}
