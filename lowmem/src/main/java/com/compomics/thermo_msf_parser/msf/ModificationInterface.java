package com.compomics.thermo_msf_parser.msf;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/23/12
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ModificationInterface {

    /**
     a method to return a peptide sequence with its modifications

     @param peptide a Peptide object
     @param modificationMap a hashmap returned from the modification method createModificationMap
     @param iConnection connection to the SQLite db
     @return a string of the modified peptide
     @throws SQLException if something went wrong with the retrieving
     */

    public String addModificationsToPeptideSequence(Peptide peptide,HashMap modificationMap,Connection iConnection) throws SQLException;

    /**
    a method to create a hashmap containing the modifications stored in the msf file

    @param iConnection: a connection to the sqlite database
    @return hashmap containing the modifications
    @throws SQLException if something went wrong with the retrieving
    */

    public HashMap createModificationMap(Connection iConnection) throws SQLException;
}
