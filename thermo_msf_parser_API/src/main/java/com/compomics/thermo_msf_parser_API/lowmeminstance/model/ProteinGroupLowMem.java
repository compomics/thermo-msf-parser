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
 * <p>ProteinGroupLowMem class.</p>
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class ProteinGroupLowMem {
    private final int proteinGroupId;
    private final List<ProteinLowMem> proteins = new ArrayList<ProteinLowMem>();
    private final Set<Integer> peptideIds = new HashSet<Integer>();

    /**
     * <p>Constructor for ProteinGroupLowMem.</p>
     *
     * @param proteinGroupId a int.
     */
    public ProteinGroupLowMem(int proteinGroupId) {
        this.proteinGroupId = proteinGroupId;
    }

    /**
     * <p>Getter for the field <code>peptideIds</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<Integer> getPeptideIds() {
        return peptideIds;
    }


    /**
     * <p>Getter for the field <code>proteinGroupId</code>.</p>
     *
     * @return a int.
     */
    public int getProteinGroupId() {
        return proteinGroupId;
    }

    /**
     * <p>Getter for the field <code>proteins</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<ProteinLowMem> getProteins() {
        return proteins;
    }  
}
