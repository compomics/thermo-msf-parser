package com.compomics.thermo_msf_parser_GUI.convenience;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem;

/**
 * <p>GUIProteinLowMem class.</p>
 *
 * @author Davy
 * @version $Id: $Id
 */
public class GUIProteinLowMem extends ProteinLowMem {

    /**
     * convenience reference to proteome discoverer file
     */
    private MsfFile msfFileForProtein;

    /**
     * convenience constructor to reference proteome discoverer file from
     * protein
     *
     * @param aAccession the protein accession
     * @param aProteinID the protein id
     * @param aMsfFile the proteome discoverer file to reference
     */
    public GUIProteinLowMem(String aAccession, int aProteinID, MsfFile aMsfFile) {
        super(aAccession, aProteinID);
        msfFileForProtein = aMsfFile;
    }

    /**
     * convenience constructor to reference proteome discoverer file from
     * protein
     *
     * @param aAccession the protein accession
     * @param aProteinID the protein id
     */
    public GUIProteinLowMem(String aAccession, int aProteinID) {
        super(aAccession, aProteinID);
    }

    /**
     * <p>setMsfFile.</p>
     *
     * @param msfFile a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile} object.
     */
    public void setMsfFile(MsfFile msfFile) {
        msfFileForProtein = msfFile;
    }

    /**
     * <p>getMsfFile.</p>
     *
     * @return a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile} object.
     */
    public MsfFile getMsfFile() {
        return msfFileForProtein;
    }
}
