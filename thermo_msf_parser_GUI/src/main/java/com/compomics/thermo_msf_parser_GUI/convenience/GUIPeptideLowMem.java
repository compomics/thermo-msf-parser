package com.compomics.thermo_msf_parser_GUI.convenience;

import com.compomics.thermo_msf_parser_API.enums.MsfVersion;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem;

/**
 *
 * @author Davy
 */
public class GUIPeptideLowMem extends PeptideLowMem {

    /**
     * convenience reference to the containing proteome discoverer file
     */
    private static MsfFile msfFile;
/**
 * convenience constructor to reference the proteome discoverer of the peptide
 * @param peptide peptide to convert to convenience peptide
 * @param aMsfFile the proteome discoverer file to reference to
 */
    public GUIPeptideLowMem(final PeptideLowMem peptide, final MsfFile aMsfFile) {
        super(peptide.getPeptideId(), peptide.getSpectrumId(), peptide.getConfidenceLevel(), peptide.getSequence(), peptide.getTotalIonsCount(), peptide.getMatchedIonsCount(), peptide.getAnnotation(), peptide.getProcessingNodeNumber(), aMsfFile.getAminoAcids());
        if (aMsfFile.getVersion() == MsfVersion.VERSION1_3) {
            this.setMissedCleavage(peptide.getMissedCleavage());
            this.setUniquePeptideSequenceId(peptide.getUniquePeptideSequenceId());
        }
        this.setParentSpectrum(peptide.getParentSpectrum());
        msfFile = aMsfFile;
    }
/**
 * get referenced proteome discoverer file
 * @return proteome discoverer
 */
    public MsfFile getMsfFile() {
        return msfFile;
    }
}
