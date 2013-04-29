package com.compomics.thermo_msf_parser_API.interfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 5/25/12
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EnzymeInterface {
    /**
     * creates a hashmap containing the enzymes
     * @param iConnection
     * @return
     * @throws SQLException 
     */    
    public Map createEnzymeMap (Connection iConnection);

}
