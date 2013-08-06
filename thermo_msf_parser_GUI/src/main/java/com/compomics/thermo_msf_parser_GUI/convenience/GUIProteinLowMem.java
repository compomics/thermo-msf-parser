package com.compomics.thermo_msf_parser_GUI.convenience;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem;

/**
 *
 * @author Davy
 */
public class GUIProteinLowMem extends ProteinLowMem {

    /**
     * convenience reference to proteome discoverer file
     */
    private MsfFile msfFileForProtein;

    /**
     * convenience constructor to reference proteome discoverer file from protein
     * @param aAccession the protein accession
     * @param aProteinID the protein id
     * @param aMsfFile the proteome discoverer file to reference
     */
    public GUIProteinLowMem(String aAccession, int aProteinID, MsfFile aMsfFile) {
        super(aAccession, aProteinID);
        msfFileForProtein = aMsfFile;
    }

    /**
     * convenience constructor to reference proteome discoverer file from protein
     * @param aAccession the protein accession
     * @param aProteinID the protein id
     */
    public GUIProteinLowMem(String aAccession, int aProteinID) {
        super(aAccession, aProteinID);
    }

    public void setMsfFile(MsfFile msfFile) {
        msfFileForProtein = msfFile;
    }

    public MsfFile getMsfFile() {
        return msfFileForProtein;
    }
}
