package com.compomics.thermo_msf_parser.msf;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

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
     * @param iConnection connection to the SQLite db
     * @return vector containing the ratio types
     * @throws java.sql.SQLException if something went wrong with the retrieving
     */

    public Vector<RatioTypeLowMem> parseRatioTypes(Connection iConnection) throws SQLException;

}
