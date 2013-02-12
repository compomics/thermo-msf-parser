package com.compomics.thermo_msf_parser.msf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 4/24/12 Time: 2:16 PM To change
 * this template use File | Settings | File Templates.
 */
public class ModificationLowMemController implements ModificationInterface {

    private static final Logger logger = Logger.getLogger(ModificationLowMemController.class);
    private final ProcessingNodeLowMemController processingNodes = new ProcessingNodeLowMemController();

    public String addModificationsToPeptideSequence(Peptide peptide, HashMap modificationMap, Connection iConnection) {
        String modifiedSequence = peptide.getSequence();
        try {
            int lengthChanged = 0;
            PreparedStatement stat = iConnection.prepareStatement("select TerminalModificationID from PeptidesTerminalModifications where PeptideID =" + peptide.getPeptideId());
            ResultSet rs = stat.executeQuery();
            //do the N terminus
            if (rs.next()) {
                rs = stat.executeQuery("select Abbreviation from AminoAcidModifications where AminoAcidModificationID =" + rs.getInt(1));
                rs.next();
                modifiedSequence = rs.getString(1) + "-";
                lengthChanged = lengthChanged + rs.getString(1).length() + 1;

            } else {
                modifiedSequence = "NH2-" + modifiedSequence;
                lengthChanged += 4;
            }
            //do the middle
            rs = stat.executeQuery("select Position,Abbreviation from PeptidesAminoAcidModifications,AminoAcidModifications where PeptidesAminoAcidModifications.AminoAcidModificationID = AminoAcidModificationID and PeptideID =" + peptide.getPeptideId() + " order by ASC Position");
            while (rs.next()) {
                modifiedSequence = modifiedSequence.substring(0, rs.getInt(1) + lengthChanged) + "<" + rs.getString(2) + ">" + modifiedSequence.substring(rs.getInt(1) + lengthChanged + 1, modifiedSequence.length());
                lengthChanged = lengthChanged + rs.getString(2).length() + 2;
            }
            //do the C terminus
            modifiedSequence += "-COOH";
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return modifiedSequence;
    }

    public HashMap<Integer, String> createModificationMap(Connection aConnection) {
        HashMap<Integer, String> modificationsMap = new HashMap<Integer, String>();
        try {
            PreparedStatement stat = aConnection.prepareStatement("select AminoAcidModificationID,Abbreviation from AminoAcidModifications");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                modificationsMap.put(rs.getInt(1), rs.getString(2));
            }
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return modificationsMap;
    }

    public Vector<String> getAllModificationNames(Connection aConnection) {
        Vector<String> allModificationNames = new Vector<String>();
        try {
            PreparedStatement stat = aConnection.prepareStatement("select Abbreviation from AminoAcidModifications");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                allModificationNames.add(rs.getString("Abbreviation"));
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return allModificationNames;
    }

    public Vector<PeptideLowMem> getPeptidesWithModification(String modification, Vector<AminoAcid> aminoAcids, Connection aConnection) {
        Vector<PeptideLowMem> peptidesWithModVector = new Vector<PeptideLowMem>();
        try {
            PreparedStatement stat = aConnection.prepareStatement("select p.PeptideID,p.SpectrumID,p.ConfidenceLevel,p.Sequence,p.TotalIonsCount,p.MatchedIonsCount,p.Annotation,p.ProcessingNodeNumber from PeptidesAminoAcidModifications as pepamods,Peptides as p,AminoAcidModifications as amods where amods.AminoAcidModificationID = pepamods.AminoAcidModificationId and pepamods.Peptideid = p.PeptideID and amods.Abbreviation = ?");
            stat.setString(1, modification);
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                peptidesWithModVector.add(new PeptideLowMem(rs, aminoAcids, aConnection));
            }
            rs.close();
            stat.close();
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return peptidesWithModVector;
    }

    public ArrayList<Modification> getAllModifications(Connection msfFileConnection, MsfVersion msfVersion, Vector<AminoAcid> aminoAcids) {
        return getModList(msfFileConnection, msfVersion,aminoAcids, true, true);
    }

    /**
     *
     * @param msfFileConnection
     * @param msfVersion
     * @param returnFixed
     * @return
     */
    public ArrayList<Modification> getListOfFixedModificationNumbers(Connection msfFileConnection, MsfVersion msfVersion,Vector<AminoAcid> aminoAcids) {
        return getModList(msfFileConnection, msfVersion,aminoAcids, true, false);
    }

    /**
     *
     * @param msfFileConnection
     * @param msfVersion
     * @return
     */
    public ArrayList<Modification> getListOfVariableModidifcationNumbers(Connection msfFileConnection, MsfVersion msfVersion,Vector<AminoAcid> aminoAcids) {
        return getModList(msfFileConnection, msfVersion,aminoAcids, false, true);
    }

    /**
     *
     * @param msfFileConnection
     * @param msfVersion
     * @param getFixedModifications
     * @param getVariableModifications
     * @return
     */
    private ArrayList<Modification> getModList(Connection msfFileConnection, MsfVersion msfVersion,Vector<AminoAcid> aminoAcids, boolean getFixedModifications, boolean getVariableModifications) {
        StringBuilder modNumbersToFetch = new StringBuilder();
        ArrayList<Modification> modsToReturn = new ArrayList<Modification>();
        ArrayList<ProcessingNode> allNodes = processingNodes.getAllProcessingNodes(msfFileConnection, msfVersion);
        ArrayList<Integer> fixedMods = new ArrayList<Integer>();
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
                        modsToAminoAcidNumbers.put(modnumber, Arrays.asList(numbers).subList(0, numbers.length-1));
                        modNumbersToFetch.append(",");
                    }
                }
                if (getVariableModifications) {
                    if (parName.matches("(?:DynModification_\\d+|DynMod_\\d+)") && parameter.getParameterValue().contains("#")) {
                        String[] numbers = parameter.getParameterValue().split("#");
                        int modnumber = Integer.parseInt(numbers[numbers.length - 1]);
                        // Add the found amino acid references to add to the modifications later
                        modNumbersToFetch.append(modnumber);
                        modsToAminoAcidNumbers.put(modnumber, Arrays.asList(numbers).subList(0, numbers.length-1));
                        modNumbersToFetch.append(",");
                    }
                }
            }
        }
        try {
            modNumbersToFetch.deleteCharAt(modNumbersToFetch.length() - 1);
            Statement stat = msfFileConnection.createStatement();
            ResultSet rs = stat.executeQuery("select * from AminoAcidModifications where AminoAcidModificationID in (" + modNumbersToFetch.toString() + ")");
            while (rs.next()) {
                Modification modToAdd = new Modification(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getDouble(9), rs.getInt(10), rs.getInt(11), fixedMods.contains(rs.getInt(1)));
                for (String aminoAcidNumber : modsToAminoAcidNumbers.get(rs.getInt(1))){
                modToAdd.getSelectedAminoAcids().add(aminoAcids.get(Integer.parseInt(aminoAcidNumber)-1));}
                addAminoAcidsToModification(modToAdd, msfFileConnection, msfVersion,aminoAcids);
                modsToReturn.add(modToAdd);
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return modsToReturn;
    }

    private void addAminoAcidsToModification(Modification modification, Connection msfFileConnection, MsfVersion msfVersion, Vector<AminoAcid> aminoAcids) {
        try {
            Statement stat = msfFileConnection.createStatement();
            ResultSet rs = stat.executeQuery("select * from AminoAcidModificationsAminoAcids where AminoAcidModificationID = "+modification.getAminoAcidModificationId());
            while (rs.next()) {
                int lAaId = rs.getInt("AminoAcidID");
                modification.addAminoAcid(aminoAcids.get(lAaId -1));
                if (msfVersion == MsfVersion.VERSION1_3) {
                    modification.addClassificationForAminoAcid(rs.getInt("Classification"));
                }
            }
            if (msfVersion == MsfVersion.VERSION1_3) {
                NeutralLoss lLoss = null;
                //get the neutral losses
                rs = stat.executeQuery("select * from AminoAcidModificationsNeutralLosses");
                while (rs.next()) {
                    lLoss = new NeutralLoss(rs.getInt("NeutralLossID"), rs.getString("Name"), rs.getDouble("MonoisotopicMass"), rs.getDouble("AverageMass"));
                }

                //add the amino acid to the neutral losses
                rs = stat.executeQuery("select * from AminoAcidModificationsAminoAcidsNL");
                while (rs.next()) {
                    int lAaId = rs.getInt("AminoAcidID");
                    if (lLoss != null) {
                        lLoss.addAminoAcid(aminoAcids.get(lAaId -1));
                    }
                }
                //now add the neutral losses to the modification
                rs = stat.executeQuery("select * from AminoAcidModificationsAminoAcidsNL");
                while (rs.next()) {
                    if (lLoss != null) {
                        modification.addNeutralLoss(lLoss);
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
        }
    }
}
