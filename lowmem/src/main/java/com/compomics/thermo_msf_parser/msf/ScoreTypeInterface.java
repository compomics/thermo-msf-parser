package com.compomics.thermo_msf_parser.msf;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/23/12
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ScoreTypeInterface {



    /**
    method to fetch the score types stored in the msf file

    @param iConnection a connection to the msf file
    @return a Hashmap containing the score types
     * @throws java.sql.SQLException if something went wrong with the retrieving
    */

    public Vector<ScoreTypeLowMem> getScoreTypes(Connection iConnection) throws SQLException;

}
