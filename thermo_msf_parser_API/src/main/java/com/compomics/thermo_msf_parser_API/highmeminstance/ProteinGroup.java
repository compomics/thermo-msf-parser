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
 * <p>ProteinGroup class.</p>
 *
 * @author toorn101
 * @version $Id: $Id
 */
public class ProteinGroup {
    private int proteinGroupId;
    private List<Protein> proteins = new ArrayList<Protein>();
    private Set<Integer> peptideIds = new HashSet<Integer>();

    /**
     * <p>Constructor for ProteinGroup.</p>
     *
     * @param proteinGroupId a int.
     */
    public ProteinGroup(int proteinGroupId) {
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
    public List<Protein> getProteins() {
        return proteins;
    }
   
    
}
