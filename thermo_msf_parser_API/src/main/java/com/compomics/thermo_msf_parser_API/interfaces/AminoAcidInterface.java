package com.compomics.thermo_msf_parser_API.interfaces;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/24/12
 * Time: 11:06 AM
 * To change this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public interface AminoAcidInterface {

    /**
     * fetches the amino acids from the proteome discoverer database
     *
     * @param msfFile the proteome discoverer file to retrieve the amino acids for
     * @return a List containing all the amino acids stored in the msf file
     * @throws java.sql.SQLException if something went wrong with the retrieving
     */
    public List getAminoAcidsFromDb(MsfFile msfFile) throws SQLException;   
}
