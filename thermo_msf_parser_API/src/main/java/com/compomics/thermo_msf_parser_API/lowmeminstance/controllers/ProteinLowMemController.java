package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.enums.MsfVersion;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.interfaces.ProteinControllerInterface;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem;
import com.compomics.thermo_msf_parser_API.util.UtilProtein;
import com.compomics.util.protein.Header;
import java.sql.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 10/1/12 Time: 10:36 AM To change
 * this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class ProteinLowMemController extends Observable implements ProteinControllerInterface {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ProteinLowMemController.class);
    private int proteinCounter;
    private int internalcounter = 0;

    /** {@inheritDoc} */
    @Override
    public List<ProteinLowMem> getAllProteins(MsfFile msfFile) {
        proteinCounter = 0;
        List<ProteinLowMem> proteinList = new ArrayList<ProteinLowMem>();
        try {
            PreparedStatement stat = null;
            int lProteinID;
            String sequence;
            String fastaHeader;
            ProteinLowMem protein;
            try {
                stat = msfFile.getConnection().prepareStatement("select Proteins.ProteinID,Sequence,Description from Proteins,ProteinAnnotations where Proteins.ProteinID = ProteinAnnotations.ProteinID");
                ResultSet rs = null;
                try {
                    rs = stat.executeQuery();
                    while (rs.next()) {
                        lProteinID = rs.getInt("ProteinID");
                        sequence = rs.getString("Sequence");
                        fastaHeader = rs.getString("Description");
                        protein = new ProteinLowMem(Header.parseFromFASTA(fastaHeader).getAccession(), lProteinID, sequence);
                        proteinCounter++;
                        proteinList.add(protein);
                    }
                } finally {
                    if (rs != null) {
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
        return Collections.unmodifiableList(proteinList);
    }

    /**
     * <p>getNumberOfProteinsWithAPeptideAtConfidenceLevel.</p>
     *
     * @param confidenceLevel the confidence level to fetch for
     * @param msfFile the msf file to look in
     * @return the number
     */
    public int getNumberOfProteinsWithAPeptideAtConfidenceLevel(int confidenceLevel, MsfFile msfFile) {
        int numberOfProteins = 0;
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select count(distinct PeptidesProteins.ProteinID) from PeptidesProteins,Peptides where PeptidesProteins.PeptideID=Peptides.PeptideID and Peptides.confidencelevel = ?");
                stat.setInt(1, confidenceLevel);
                ResultSet rs = null;
                try {
                    rs = stat.executeQuery();
                    numberOfProteins = rs.getInt(1);
                } finally {
                    if (rs != null) {
                        rs.close();
                    }
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return numberOfProteins;
    }

    /**
     * this method returns a protein as stored by its UID
     *
     * @param aProteinId an UID stored in the msf file
     * @param msfFile the msf file to fetch in
     * @return a {@code ProteinLowMem} representation of the protein fetched
     */
    public ProteinLowMem getProteinForProteinId(int aProteinId, MsfFile msfFile) {
        int lProteinID;
        String lSequence;
        String fastaHeader;
        ProteinLowMem protein = null;
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select Proteins.ProteinID,Sequence,ProteinAnnotations.Description from Proteins,ProteinAnnotations where Proteins.ProteinID = ProteinAnnotations.ProteinID and Proteins.ProteinID =?");
                stat.setInt(1, aProteinId);
                ResultSet rs = null;
                try {
                    rs = stat.executeQuery();
                    while (rs.next()) {
                        lProteinID = rs.getInt("ProteinID");
                        lSequence = rs.getString("Sequence");
                        fastaHeader = rs.getString("Description");
                        protein = new ProteinLowMem(Header.parseFromFASTA(fastaHeader).getAccession(), lProteinID, lSequence);
                    }
                } finally {
                    if (rs != null) {
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
        return protein;
    }

    /** {@inheritDoc} */
    @Override
    public ProteinLowMem getProteinFromAccession(String proteinAccession, MsfFile msfFile) {
        int lProteinID = 0;
        String sequence = null;
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select ProteinAnnotations.ProteinID,Sequence from Proteins, ProteinAnnotations where ProteinAnnotations.Description like ? and ProteinAnnotations.ProteinID = Proteins.ProteinID");
                String queryParam = "empty_accession";
                if (!proteinAccession.isEmpty()) {
                    queryParam = new StringBuilder().append("%").append(proteinAccession).append("%").toString();
                }
                stat.setString(1, queryParam);
                ResultSet rs = null;
                try {
                    rs = stat.executeQuery();
                    while (rs.next()) {
                        lProteinID = rs.getInt("ProteinID");
                        sequence = rs.getString("Sequence");
                    }
                } finally {
                    if (rs != null) {
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
        return new ProteinLowMem(proteinAccession, lProteinID, sequence);
    }

    /** {@inheritDoc} */
    @Override
    //TODO, perhaps look at this code, if this cannot be made simpler by using Header or utilprotein for all cases
    public String getAccessionFromProteinID(int proteinID, MsfFile msfFile) {
        String accession = "";
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select Description,Sequence from ProteinAnnotations,Proteins where ProteinAnnotations.ProteinID = Proteins.ProteinID  and Proteins.ProteinID= ?");
                stat.setInt(1, proteinID);
                ResultSet rs = stat.executeQuery();
                UtilProtein iUtilProtein;
                try {
                    while (rs.next()) {
                        String lFasta = String.format("%s\n" + "%s", rs.getString(1), rs.getString(2));
                        int secondIndex;
                        if (lFasta.contains(">GENSCAN")) {
                            iUtilProtein = new UtilProtein(lFasta);
                            accession = (iUtilProtein.getHeader().toString().substring(lFasta.indexOf("GEN"), lFasta.indexOf("pep") - 1));
                        } else if (lFasta.contains(">ENS")) {
                            iUtilProtein = new UtilProtein(lFasta);
                            accession = (iUtilProtein.getHeader().toString().substring(lFasta.indexOf("ENS"), lFasta.indexOf("pep") - 1));
                        } else if ((secondIndex = (lFasta.indexOf("|", lFasta.indexOf("|") + 1))) != -1) {
                            accession = lFasta.substring(lFasta.indexOf("|") + 1, secondIndex);
                        } else {
                            iUtilProtein = new UtilProtein(lFasta);
                            accession = iUtilProtein.getHeader().getAccession();
                        }
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
        return accession;
    }

    /**
     * this method returns a compomics utilities instance of a protein stored in
     * the database
     *
     * @param proteinID the ID stored in the database for the protein
     * @param msfFile the msfFile to look in
     * @return an {@code UtilProtein} version of the protein
     */
    public UtilProtein getUtilProteinForProteinID(int proteinID, MsfFile msfFile) {
        UtilProtein iUtilProtein = null;
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select Description,Sequence from ProteinAnnotations,Proteins where ProteinAnnotations.ProteinID = ? and Proteins.ProteinID = ProteinAnnotations.ProteinID");
                stat.setInt(1, proteinID);
                ResultSet rs = null;
                try {
                    rs = stat.executeQuery();
                    while (rs.next()) {
                        String lFasta = String.format("%s\n" + "%s", rs.getString(1), rs.getString(2));
                        if (lFasta.contains(">GENSCAN")) {
                            iUtilProtein = new UtilProtein(lFasta);
                        } else if (lFasta.contains(">ENS")) {
                            try {
                                iUtilProtein = new UtilProtein(lFasta);
                            } catch (Exception e) {
                                logger.error(e);
                            }

                        } else {
                            iUtilProtein = new UtilProtein(lFasta);
                        }
                    }
                } finally {
                    if (rs != null) {
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
        return iUtilProtein;
    }

    /**
     * <p>getAllProteinAccessions.</p>
     *
     * @param msfFile the file to get from
     * @return a {@code Map} with key: the {@code String} accession value: a
     * {@code ProteinLowMem} object
     */
    public Map<String, ProteinLowMem> getAllProteinAccessions(MsfFile msfFile) {
        Map<String, ProteinLowMem> allProteinAccessions = new HashMap<String, ProteinLowMem>();
        int internalCounter = 0;
        proteinCounter = 0;
        try {
            String proteinAccession;
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select * from Proteins");
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        int lProteinID = rs.getInt("ProteinID");
                        proteinAccession = getAccessionFromProteinID(lProteinID, msfFile);
                        allProteinAccessions.put(proteinAccession, new ProteinLowMem(proteinAccession, lProteinID));
                        internalCounter++;
                        proteinCounter++;
                        if (internalCounter == 200) {
                            setChanged();
                            notifyObservers();
                            internalCounter = 0;
                        }
                    }
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return allProteinAccessions;
    }

    /**
     * this method returns all the proteins for a given confidence level in a
     * msf file
     *
     * @param confidenceLevel the confidence level of the desired proteins
     * @param msfFile the msf file to fetch from
     * @return a {@code List} containing all the proteins
     */
    public List<ProteinLowMem> getProteinsForConfidenceLevel(int confidenceLevel, MsfFile msfFile) {
        return this.getProteinsForConfidenceLevel(confidenceLevel, msfFile, false);
    }

    /**
     * this method returns all the proteins for a given confidence level in a
     * msf file and optionally reports its progress
     *
     * @param confidenceLevel the confidence level of the desired proteins
     * @param msfFile the msf file to fetch from
     * @param useCounter flag if the method should report the number of proteins
     * processed to the observers
     * @return a {@code List} containing all the proteins
     */
    public List<ProteinLowMem> getProteinsForConfidenceLevel(int confidenceLevel, MsfFile msfFile, boolean useCounter) {
        List<ProteinLowMem> changedAccessions = new ArrayList<ProteinLowMem>();
        String proteinAccession;
        if (useCounter) {
            proteinCounter = 0;
            try {
                PreparedStatement stat = null;
                try {
                    stat = msfFile.getConnection().prepareStatement("select distinct PeptidesProteins.ProteinID from PeptidesProteins, Peptides where PeptidesProteins.PeptideID=Peptides.PeptideID and Peptides.confidencelevel = ?");
                    //TODO check if this slows down significantly, otherwise change query to just select the description aswell and write a method that takes this
                    stat.setInt(1, confidenceLevel);
                    ResultSet rs = null;
                    try {
                        rs = stat.executeQuery();
                        while (rs.next()) {
                            int lProteinID = rs.getInt(1);
                            proteinAccession = getAccessionFromProteinID(lProteinID, msfFile);
                            changedAccessions.add(new ProteinLowMem(proteinAccession, lProteinID));
                            proteinCounter++;
                            internalcounter++;
                            if (internalcounter > 30) {
                                internalcounter = 0;
                                setChanged();
                                notifyObservers();
                            }
                        }
                    } finally {
                        if (rs != null) {
                            rs.close();
                        }
                    }
                } finally {
                    if (stat != null) {
                        stat.close();
                    }
                }
            } catch (SQLException sqle) {
                logger.error(sqle);
            }
        } else {
            proteinCounter = 0;
            try {
                PreparedStatement stat = null;
                try {
                    stat = msfFile.getConnection().prepareStatement("select distinct PeptidesProteins.ProteinID from PeptidesProteins ,Peptides where PeptidesProteins.PeptideID=Peptides.PeptideID and Peptides.confidencelevel = ?");
                    stat.setInt(1, confidenceLevel);
                    ResultSet rs = null;
                    try {
                        //TODO check if this slows down significantly, otherwise change query to just select the description aswell and write a method that takes this
                        rs = stat.executeQuery();
                        while (rs.next()) {
                            int lProteinID = rs.getInt(1);
                            proteinAccession = getAccessionFromProteinID(lProteinID, msfFile);
                            changedAccessions.add(new ProteinLowMem(proteinAccession, lProteinID));
                        }
                    } finally {
                        if (rs != null) {
                            rs.close();
                        }
                    }
                } finally {
                    if (stat != null) {
                        stat.close();
                    }
                }
            } catch (SQLException sqle) {
                logger.error(sqle);
            }
        }
        return changedAccessions;
    }

    /** {@inheritDoc} */
    @Override
    public String getSequenceForProteinID(int proteinID, MsfFile msfFile) {
        String lSequence = "";
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select Sequence from Proteins where ProteinID = ?");
                stat.setInt(1, proteinID);
                ResultSet rs = stat.executeQuery();
                try {
                    rs.next();
                    lSequence = rs.getString(1);
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
        return lSequence;
    }

    /**
     * {@inheritDoc}
     *
     * get all the proteins associated with a peptide
     */
    @Override
    public List<ProteinLowMem> getProteinsForPeptide(int peptideID, MsfFile msfFile) {
        proteinCounter = 0;
        List<ProteinLowMem> proteinList = new ArrayList<ProteinLowMem>();
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select Proteins.ProteinID from Proteins, PeptidesProteins where PeptideID = ? and PeptidesProteins.ProteinID = Proteins.ProteinID");
                stat.setInt(1, peptideID);
                ResultSet rs = null;
                try {
                    rs = stat.executeQuery();
                    while (rs.next()) {
                        int lProteinID = rs.getInt(1);
                        proteinCounter++;
                        proteinList.add(new ProteinLowMem(getAccessionFromProteinID(lProteinID, msfFile), lProteinID));
                    }
                } finally {
                    if (rs != null) {
                        rs.close();
                    }
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return proteinList;
    }

    /**
     * get all the proteins for each peptide in a list
     *
     * @param peptideList a {@code List} containing all the
     * {@code PeptideLowMem} objects to find proteins for in a given msfFile
     * @param msfFile the file to check in
     * @return a {@code List} containing all the proteins connected to the
     * peptides
     */
    public List<ProteinLowMem> getProteinsForPeptideList(List<PeptideLowMem> peptideList, MsfFile msfFile) {
        List<ProteinLowMem> proteinList = new ArrayList<ProteinLowMem>();
        String iAccession;
        proteinCounter = 0;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (PeptideLowMem aPeptideList : peptideList) {
                stringBuilder.append(",").append(aPeptideList.getPeptideId());
            }
            stringBuilder.replace(0, 1, "");
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select Proteins.ProteinID,Sequence from Proteins, PeptidesProteins where PeptidesProteins.ProteinID = Proteins.ProteinID and PeptideID in ?");
                stat.setString(1, String.format("('%s')", stringBuilder.toString()));
                ResultSet rs = null;
                try {
                    rs = stat.executeQuery();
                    while (rs.next()) {
                        int lProteinID = rs.getInt(1);
                        String lSequence = rs.getString(2);
                        iAccession = getAccessionFromProteinID(lProteinID, msfFile);
                        proteinList.add(new ProteinLowMem(iAccession, lProteinID, lSequence));
                        proteinCounter++;
                    }
                } finally {
                    if (rs != null) {
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
        return proteinList;
    }

    /**
     * get the accessions for all the proteins connected to a peptide
     *
     * @param peptideID the id of the peptide in the SQLite database
     * @param msfFile the file to fetch from
     * @return a {@code List} containing all the protein accessions connected to
     * the peptide
     */
    public List<String> getAllProteinAccessionsForPeptide(int peptideID, MsfFile msfFile) {
        List<String> proteinList = new ArrayList<String>();
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select Proteins.ProteinID from Proteins,PeptidesProteins where PeptidesProteins.ProteinID = Proteins.ProteinID and PeptidesProteins.peptideid = ?");
                stat.setInt(1, peptideID);
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        int lProteinID = rs.getInt(1);
                        proteinList.add(getAccessionFromProteinID(lProteinID, msfFile));
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
        return proteinList;
    }

    /**
     * get the number of peptides that are associated with a protein
     *
     * @param proteinID the id of the protein stored in the SQLite database
     * @param msfFile the msf file to fetch from
     * @return the number of peptides connected to the protein id
     */
    public int getNumberOfPeptidesForProtein(int proteinID, MsfFile msfFile) {
        int numberOfPeptides = 0;
        try {
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select count(PeptideID) from PeptidesProteins where ProteinID = ?");
                stat.setInt(1, proteinID);
                ResultSet rs = null;
                try {
                    rs = stat.executeQuery();
                    rs.next();
                    numberOfPeptides = rs.getInt(1);
                } finally {
                    if (rs != null) {
                        rs.close();
                    }
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return numberOfPeptides;
    }

    /**
     * <p>getNumberOfProteinsProcessed.</p>
     *
     * @return getter for the observers to know how many proteins are processed
     */
    public int getNumberOfProteinsProcessed() {
        return proteinCounter;
    }

    /**
     * check if the protein is the master protein of a group
     *
     * @param proteinID protein id to check
     * @param msfFile the file to check in
     * @return true if the protein is the master, otherwise false (not always
     * present)
     */
    public boolean isMasterProtein(int proteinID, MsfFile msfFile) {
        boolean isMasterProtein = false;
        if (msfFile.getVersion() != MsfVersion.VERSION1_2) {
            try {
                PreparedStatement stat = null;
                try {
                    stat = msfFile.getConnection().prepareStatement("select IsMasterProtein from Proteins where ProteinID = ?");
                    stat.setInt(1, proteinID);
                    ResultSet rs = null;
                    try {
                        rs = stat.executeQuery();
                        rs.next();
                        isMasterProtein = rs.getBoolean("IsMasterProtein");
                    } finally {
                        if (rs != null) {
                            rs.close();
                        }
                    }
                } finally {
                    if (stat != null) {
                        stat.close();
                    }
                }
            } catch (SQLException sqle) {
                logger.error(sqle);
            }
        }
        return isMasterProtein;
    }
}
