package com.compomics.thermo_msf_parser_API.interfaces;


import com.compomics.thermo_msf_parser_API.highmeminstance.CustomDataField;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 5/25/12 Time: 4:45 PM To change
 * this template use File | Settings | File Templates.
 */
public interface CustomDataInterface {

     /**
     *
     * @param msfFile the proteome discoverer file to retrieve the custom data from
     * @return a hashmap containing the custom fields key: fieldid in the db
     * value: displayname given in the db
     */
    public Map<Integer, CustomDataField> getCustomDataFields (MsfFile msfFile);
    
    public List<CustomDataField> getCustomPeptideData(Map<Integer, CustomDataField> customData, MsfFile msfFile);

    public void addCustomProteinsData(ProteinLowMem protein, MsfFile msfFile);

     /**
     *
     * @param msfFile the proteome discoverer file to retrieve the custom spectrum data from
     * @return an List with the custom spectra data
     */
    
    public List<CustomDataField> getCustomSpectraData(Map<Integer, CustomDataField> iCustomDataFieldsMap, MsfFile msfFile);
}
