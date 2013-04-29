/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser_GUI.convenience;

import com.compomics.thermo_msf_parser_API.highmeminstance.AminoAcid;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem;
import java.util.List;

/**
 *
 * @author Davy
 */
public class GUIPeptideLowMem extends PeptideLowMem {

    private MsfFile msfFile;
    
    public GUIPeptideLowMem(PeptideLowMem peptide,List<AminoAcid> aminoAcids,MsfFile msfFile) {
        super(peptide.getPeptideId(), peptide.getSpectrumId(), peptide.getConfidenceLevel(), peptide.getSequence(), peptide.getTotalIonsCount(), peptide.getMatchedIonsCount(), peptide.getAnnotation(), peptide.getProcessingNodeNumber(),aminoAcids);
        this.msfFile = msfFile;
    }
    
    public MsfFile getMsfFile(){
        return msfFile;
    }
}
