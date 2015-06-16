package com.compomics.thermo_msf_parser_API.interfaces;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.RatioTypeLowMem;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/23/12
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RatioTypeInterface {

    /**
     *
     @param msfFile the proteome discoverer file to retrieve from
     * @return vector containing the ratio types
     */

    public List<RatioTypeLowMem> parseRatioTypes(MsfFile msfFile);

}
