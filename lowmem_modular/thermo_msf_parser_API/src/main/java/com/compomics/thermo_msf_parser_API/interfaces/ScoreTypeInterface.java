package com.compomics.thermo_msf_parser_API.interfaces;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ScoreTypeLowMem;
import java.sql.SQLException;
import java.util.Map;
import java.util.List;

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

    @param aConnection a connection to the msf file
    @return a Hashmap containing the score types
     * @throws java.sql.SQLException if something went wrong with the retrieving
    */

    public List<ScoreTypeLowMem> getScoreTypes(MsfFile msfFile);
 
    /**
     * @param peptideLowMemList a vector containing the peptides we want to retrieve the scores for
     * @param aConnection a connection to the msf file
     */
    
    public void getScoresForPeptideList(List<PeptideLowMem>peptideLowMemList,MsfFile msfFile);
    
    /**
     * 
     * @param peptide a peptide to which we want to add the scores
     * @param aConnection a connection to the msf file
     */
    
    public void addScoresToPeptide (PeptideLowMem peptide, MsfFile msfFile);
    
     /**
     *
     * @param peptide a peptide to return the different score types for
     * @param aConnection a connection to the msf file
     * @return a hashMap with key scoreID value: scoreValue
     * @throws SQLException
     */
    public Map<Integer, Double> getScoresForPeptide(PeptideLowMem peptide, MsfFile msfFile);
    
    /**
     * get all the score types used in multiple msf files
     * @param MsfFiles a vector containing all the msf files we want the score types from
     * @return a vector containing the score type objects
     */
    public List<ScoreTypeLowMem> getScoreTypesOfMsfFileList(List<MsfFile> MsfFiles);
        
}
