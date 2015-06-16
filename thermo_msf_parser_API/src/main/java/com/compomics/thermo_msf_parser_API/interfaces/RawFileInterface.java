package com.compomics.thermo_msf_parser_API.interfaces;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.RawFileLowMem;
import java.util.Map;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 4/23/12 Time: 3:08 PM To change
 * this template use File | Settings | File Templates.
 */
public interface RawFileInterface {

    /**
     *
     * @param fileID the file id stored in the msf file
     @param msfFile the proteome discoverer file to retrieve from
     * @return hashmap connecting the file name entry to the fileID
     */
    public Map<Integer, String> getRawFileForFileID(int fileID, MsfFile msfFile);

    /**
     *
     @param msfFile the proteome discoverer file to retrieve from
     * @return a vector containing all the names of the stored raw files
     */
    public List<RawFileLowMem> getRawFileNames(MsfFile msfFile);

    /**
     *
     * @param fileID the file id stored in the msf file
     @param msfFile the proteome discoverer file to retrieve from
     * @return a processed filename (eg sample4_1.raw)
     */
    public String getRawFileNameForFileID(int fileID, MsfFile msfFile);
}
