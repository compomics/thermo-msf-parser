package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.enums.MsfVersion;
import com.compomics.thermo_msf_parser_API.highmeminstance.AminoAcid;
import com.compomics.thermo_msf_parser_API.highmeminstance.Modification;
import com.compomics.thermo_msf_parser_API.highmeminstance.NeutralLoss;
import com.compomics.thermo_msf_parser_API.highmeminstance.ProcessingNode;
import com.compomics.thermo_msf_parser_API.highmeminstance.ProcessingNodeParameter;
import com.compomics.thermo_msf_parser_API.interfaces.ModificationInterface;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 4/24/12 Time: 2:16 PM To change
 * this template use File | Settings | File Templates.
 */
public class ModificationLowMemController implements ModificationInterface {

    private static final Logger logger = Logger.getLogger(ModificationLowMemController.class);
    private final ProcessingNodeLowMemController processingNodes = new ProcessingNodeLowMemController();

    @Override
    public String addModificationsToPeptideSequence(PeptideLowMem peptide, MsfFile msfFile) {
        String modifiedSequence = peptide.getSequence();
        try {
            int lengthChanged = 0;
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select TerminalModificationID from PeptidesTerminalModifications where PeptideID = ?");
                stat.setInt(1, peptide.getPeptideId());
                ResultSet rs = stat.executeQuery();
                //do the N terminus
                try {
                    if (rs.next()) {
                        stat = msfFile.getConnection().prepareStatement("select Abbreviation from AminoAcidModifications where AminoAcidModificationID = ?");
                        stat.setInt(1, rs.getInt(1));
                        rs = stat.executeQuery();
                        rs.next();
                        modifiedSequence = String.format("%s-", rs.getString(1));
                        lengthChanged = lengthChanged + rs.getString(1).length() + 1;

                    } else {
                        modifiedSequence = String.format("NH2-%s", modifiedSequence);
                        lengthChanged += 4;
                    }
                } finally {
                    rs.close();
                }
                //do the middle
                stat = msfFile.getConnection().prepareStatement("select Position,Abbreviation from PeptidesAminoAcidModifications,AminoAcidModifications where PeptidesAminoAcidModifications.AminoAcidModificationID = AminoAcidModificationID and PeptideID = ? order by ASC Position");
                stat.setInt(1, peptide.getPeptideId());
                rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        modifiedSequence = new StringBuilder().append(modifiedSequence.substring(0, rs.getInt(1) + lengthChanged)).append("<").append(rs.getString(2)).append(">").append(modifiedSequence.substring(rs.getInt(1) + lengthChanged + 1, modifiedSequence.length())).toString();
                        lengthChanged = lengthChanged + rs.getString(2).length() + 2;
                    }
                    //do the C terminus
                    modifiedSequence += "-COOH";
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return modifiedSequence;
    }

    @Override
    public HashMap<Integer, String> createModificationMap(MsfFile msfFile) {
        HashMap<Integer, String> modificationsMap = new HashMap<Integer, String>();
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select AminoAcidModificationID,Abbreviation from AminoAcidModifications");
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        modificationsMap.put(rs.getInt(1), rs.getString(2));
                    }
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return modificationsMap;
    }

    @Override
    public List<String> getAllModificationNames(MsfFile msfFile) {
        List<String> allModificationNames = new ArrayList<String>();
        try {
            PreparedStatement stat = null;

            try {
                stat = msfFile.getConnection().prepareStatement("select Abbreviation from AminoAcidModifications");
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        allModificationNames.add(rs.getString("Abbreviation"));
                    }
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return allModificationNames;
    }

    public List<PeptideLowMem> getPeptidesWithModification(String modification, MsfFile msfFile) {
        List<PeptideLowMem> peptidesWithModList = new ArrayList<PeptideLowMem>();
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select p.PeptideID,p.SpectrumID,p.ConfidenceLevel,p.Sequence,p.TotalIonsCount,p.MatchedIonsCount,p.Annotation,p.ProcessingNodeNumber from PeptidesAminoAcidModifications as pepamods,Peptides as p,AminoAcidModifications as amods where amods.AminoAcidModificationID = pepamods.AminoAcidModificationId and pepamods.Peptideid = p.PeptideID and amods.Abbreviation = ?");
                stat.setString(1, modification);
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        peptidesWithModList.add(new PeptideLowMem(rs, msfFile.getAminoAcids()));
                    }
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return peptidesWithModList;
    }

    public List<Modification> getAllModifications(MsfFile msfFile) {
        return getModList(msfFile, true, true);
    }

    /**
     *
     * @param msfFileConnection
     * @param msfVersion
     * @param returnFixed
     * @return
     */
    public List<Modification> getListOfFixedModificationNumbers(MsfFile msfFile) {
        return getModList(msfFile, true, false);
    }

    /**
     *
     * @param msfFileConnection
     * @param msfVersion
     * @return
     */
    public List<Modification> getListOfVariableModidifcationNumbers(MsfFile msfFile) {
        return getModList(msfFile, false, true);
    }

    /**
     *
     * @param msfFileConnection
     * @param msfVersion
     * @param getFixedModifications
     * @param getVariableModifications
     * @return
     */
    private List<Modification> getModList(MsfFile msfFile, boolean getFixedModifications, boolean getVariableModifications) {
        StringBuilder modNumbersToFetch = new StringBuilder();
        List<Modification> modsToReturn = new ArrayList<Modification>();
        List<ProcessingNode> allNodes = processingNodes.getAllProcessingNodes(msfFile);
        List<Integer> fixedMods = new ArrayList<Integer>();
        Map<Integer, List<String>> modsToAminoAcidNumbers = new HashMap<Integer, List<String>>();
        for (ProcessingNode node : allNodes) {
            for (ProcessingNodeParameter parameter : node.getProcessingNodeParameters()) {
                // TODO: add modNames for Z-Core, if they're not covered yet
                String parName = parameter.getParameterName();
                if (getFixedModifications) {
                    if (parName.matches("(?:Static_\\d+|StatMod_\\d+)") && parameter.getParameterValue().contains("#")) {
                        String[] numbers = parameter.getParameterValue().split("#");
                        int modnumber = Integer.parseInt(numbers[numbers.length - 1]);
                        // Add the found amino acid references to add to the modifications later
                        fixedMods.add(modnumber);
                        modNumbersToFetch.append(modnumber);
                        modsToAminoAcidNumbers.put(modnumber, Arrays.asList(numbers).subList(0, numbers.length - 1));
                        modNumbersToFetch.append(",");
                    }
                }
                if (getVariableModifications) {
                    if (parName.matches("(?:DynModification_\\d+|DynMod_\\d+)") && parameter.getParameterValue().contains("#")) {
                        String[] numbers = parameter.getParameterValue().split("#");
                        int modnumber = Integer.parseInt(numbers[numbers.length - 1]);
                        // Add the found amino acid references to add to the modifications later
                        modNumbersToFetch.append(modnumber);
                        modsToAminoAcidNumbers.put(modnumber, Arrays.asList(numbers).subList(0, numbers.length - 1));
                        modNumbersToFetch.append(",");
                    }
                }
            }
        }
        try {
            modNumbersToFetch.deleteCharAt(modNumbersToFetch.length() - 1);
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select * from AminoAcidModifications where AminoAcidModificationID in (?)");
                stat.setString(1, modNumbersToFetch.toString());
                ResultSet rs = null;
                try {
                rs = stat.executeQuery();
                    while (rs.next()) {
                        Modification modToAdd = new Modification(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getDouble(9), rs.getInt(10), rs.getInt(11), fixedMods.contains(rs.getInt(1)));
                        for (String aminoAcidNumber : modsToAminoAcidNumbers.get(rs.getInt(1))) {
                            modToAdd.getSelectedAminoAcids().add(msfFile.getAminoAcids().get(Integer.parseInt(aminoAcidNumber) - 1));
                        }
                        addAminoAcidsToModification(modToAdd, msfFile.getAminoAcids(), msfFile);
                        modsToReturn.add(modToAdd);
                    }
                } finally {
                    if(rs != null){
                    rs.close();}
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return modsToReturn;
    }

    /**
     *
     * @param modification
     * @param aminoAcids
     * @param msfFile
     */
    private void addAminoAcidsToModification(Modification modification, List<AminoAcid> aminoAcids, MsfFile msfFile) {
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select * from AminoAcidModificationsAminoAcids where AminoAcidModificationID = ?");
                stat.setInt(1, modification.getAminoAcidModificationId());
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        int lAaId = rs.getInt("AminoAcidID");
                        modification.addAminoAcid(aminoAcids.get(lAaId - 1));
                        if (msfFile.getVersion() != MsfVersion.VERSION1_2) {
                            modification.addClassificationForAminoAcid(rs.getInt("Classification"));
                        }
                    }
                } finally {
                    rs.close();
                }
                if (msfFile.getVersion() != MsfVersion.VERSION1_2) {
                    NeutralLoss lLoss = null;
                    //get the neutral losses
                    stat = msfFile.getConnection().prepareStatement("select * from AminoAcidModificationsNeutralLosses");
                    rs = stat.executeQuery();
                    try {
                        while (rs.next()) {
                            lLoss = new NeutralLoss(rs.getInt("NeutralLossID"), rs.getString("Name"), rs.getDouble("MonoisotopicMass"), rs.getDouble("AverageMass"));
                        }
                    } finally {
                        rs.close();
                    }
                    //add the amino acid to the neutral losses
                    stat = msfFile.getConnection().prepareStatement("select * from AminoAcidModificationsAminoAcidsNL");
                    rs = stat.executeQuery();
                    try {
                        while (rs.next()) {
                            int lAaId = rs.getInt("AminoAcidID");
                            if (lLoss != null) {
                                lLoss.addAminoAcid(aminoAcids.get(lAaId - 1));
                            }
                        }
                    } finally {
                        rs.close();
                    }
                    //now add the neutral losses to the modification
                    stat = msfFile.getConnection().prepareStatement("select * from AminoAcidModificationsAminoAcidsNL");
                    rs = stat.executeQuery();
                    try {
                        while (rs.next()) {
                            if (lLoss != null) {
                                modification.addNeutralLoss(lLoss);
                            }
                        }
                    } finally {
                        rs.close();
                    }
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
        }
    }
}
