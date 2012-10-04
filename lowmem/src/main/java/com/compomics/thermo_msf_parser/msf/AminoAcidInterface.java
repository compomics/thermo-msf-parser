package com.compomics.thermo_msf_parser.msf;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/24/12
 * Time: 11:06 AM
 * To change this template use File | Settings | File Templates.
 */
public interface AminoAcidInterface {

    /**
     *
     * @param iConnection connection to a msf file
     * @return a Vector containing all the amino acids stored in the msf file
     * @throws java.sql.SQLException if something went wrong with the retrieving
    */

    public Vector getAminoAcidsFromDb(Connection iConnection) throws SQLException;
    
}
