package com.compomics.thermo_msf_parser_API.interfaces;


import com.compomics.thermo_msf_parser_API.highmeminstance.CustomDataField;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 5/25/12 Time: 4:45 PM To change
 * this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public interface CustomDataInterface {

    /**
     * <p>getCustomDataFields.</p>
     *
     * @param msfFile the proteome discoverer file to retrieve the custom data from
     * @return a hashmap containing the custom fields key: fieldid in the db
     * value: displayname given in the db
     */
    public Map<Integer, CustomDataField> getCustomDataFields (MsfFile msfFile);
    
    /**
     * <p>getCustomPeptideData.</p>
     *
     * @param customData a {@link java.util.Map} object.
     * @param msfFile a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile} object.
     * @return a {@link java.util.List} object.
     */
    public List<CustomDataField> getCustomPeptideData(Map<Integer, CustomDataField> customData, MsfFile msfFile);

    /**
     * <p>addCustomProteinsData.</p>
     *
     * @param protein a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem} object.
     * @param msfFile a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile} object.
     */
    public void addCustomProteinsData(ProteinLowMem protein, MsfFile msfFile);

    /**
     * <p>getCustomSpectraData.</p>
     *
     * @param msfFile the proteome discoverer file to retrieve the custom spectrum data from
     * @return an List with the custom spectra data
     * @param iCustomDataFieldsMap a {@link java.util.Map} object.
     */
    public List<CustomDataField> getCustomSpectraData(Map<Integer, CustomDataField> iCustomDataFieldsMap, MsfFile msfFile);
}
