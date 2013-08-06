package com.compomics.thermo_msf_parser_API.interfaces;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem;
import java.sql.SQLException;
import java.util.Map;
import java.util.List;

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
     @param msfFile the proteome discoverer file to get the modifications from
     @return a string of the modified peptide
     @throws SQLException if something went wrong with the retrieving
     */

    public String addModificationsToPeptideSequence(PeptideLowMem peptide,MsfFile msfFile);

    /**
    creates a hashmap containing the modifications stored in the msf file

    @param iConnection: a connection to the SQLite database
    @return hashmap containing the modifications
    @throws SQLException if something went wrong with the retrieving
    */

    public Map createModificationMap(MsfFile msfFile);
    
    /**
     * fetch all the abbreviated names of the modifications used in the msf file
     * 
     * @param aConnection a connection to the SQLite database
     * @return a List containing all the names of the modifications used in the msf file
     */ 
    
    public List<String> getAllModificationNames(MsfFile msfFile);
    
}
