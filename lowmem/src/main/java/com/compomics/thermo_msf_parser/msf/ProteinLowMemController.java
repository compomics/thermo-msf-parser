package com.compomics.thermo_msf_parser.msf;

import com.compomics.thermo_msf_parser.msf.util.UtilProtein;
import java.sql.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 10/1/12 Time: 10:36 AM To change
 * this template use File | Settings | File Templates.
 */
public class ProteinLowMemController extends Observable implements ProteinInterface {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ProteinLowMemController.class);
    private int proteinCounter;
    private int internalcounter = 0;

    /**
     *
     * @param aConnection connection to the msf file
     * @return an iterator containing all the proteins from the msf file
     */
    public Iterator<ProteinLowMem> getAllProteins(Connection aConnection) {
        ArrayList<ProteinLowMem> proteinList = new ArrayList<ProteinLowMem>();
        try {
            Statement stat = aConnection.createStatement();
            int lProteinID;
            String sequence = "";
            ProteinLowMem protein;
            ResultSet rs = stat.executeQuery("select ProteinID,Sequence from Proteins");
            while (rs.next()) {
                lProteinID = rs.getInt("ProteinID");
                sequence = rs.getString("Sequence");
                protein = new ProteinLowMem(sequence, aConnection, lProteinID);
                proteinList.add(protein);
            }
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return proteinList.iterator();
    }
    
    public int getNumberOfProteinsForConfidenceLevel(int confidenceLevel, Connection msfFileConnection){
         int numberOfProteins = 0;
        Statement stat = null;
        try {
            stat = msfFileConnection.createStatement();
            ResultSet rs = stat.executeQuery("select distinct PeptidesProteins.ProteinID from PeptidesProteins ,(select Peptides.PeptideID from Peptides where Peptides.confidencelevel = " + confidenceLevel + ") as pep where PeptidesProteins.PeptideID=pep.PeptideID");
            numberOfProteins = rs.getInt(1);
            rs.close();
            stat.close();
        } catch (SQLException e) {
            logger.error(e);
        }
        return numberOfProteins;
    }

    /**
     * this method returns a protein as stored by its UID
     *
     * @param aConnection a connection to the msf file
     * @param aProteinId an UID stored in the msf file
     * @return a protein object
     */
    public ProteinLowMem getProteinForProteinId(Connection aConnection, int aProteinId) {
        int lProteinID;
        String lSequence = "";
        ProteinLowMem protein = null;
        try {
            Statement stat = aConnection.createStatement();
            ResultSet rs = stat.executeQuery("select ProteinID,Sequence from Proteins where ProteinID =" + aProteinId);
            while (rs.next()) {
                lProteinID = rs.getInt("ProteinID");
                lSequence = rs.getString("Sequence");
                protein = new ProteinLowMem(lSequence, aConnection, lProteinID);
            }
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return protein;
    }

    /**
     *
     * @param proteinAccession a protein accession
     * @param aConnection a connection to the msf file
     * @return a proteinlowmem object
     */
    @Override
    public ProteinLowMem getProteinFromAccession(String proteinAccession, Connection aConnection) {
        int lProteinID = 0;
        try {
            Statement stat = aConnection.createStatement();
            ResultSet rs = stat.executeQuery("select protanno.ProteinID,Proteins.Sequence from ,(ProteinID from ProteinAnnotations where ProteinAnnotations.Description like ('%" + proteinAccession + "%')) as protanno where protanno.ProteinID = Proteins.ProteinID");
            rs.next();
            lProteinID = rs.getInt(1);
            //String lSequence = rs.getString(2);
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return new ProteinLowMem(proteinAccession, aConnection, lProteinID);
    }

    /**
     *
     * @param proteinID a protein id in the db
     * @param aConnection a connection to the msf file
     * @return a string containing the accession of a protein
     */
    @Override
    public String getAccessionFromProteinID(int proteinID, Connection aConnection) {
        String accession = "";
        try {
            Statement stat = aConnection.createStatement();
            ResultSet rs = stat.executeQuery("select Description,Sequence from ProteinAnnotations,Proteins where ProteinAnnotations.ProteinID = " + proteinID + " and Proteins.ProteinID = " + proteinID);
            UtilProtein iUtilProtein;
            while (rs.next()) {
                String lFasta = rs.getString(1) + "\n" + rs.getString(2);
                int secondIndex;
                if (lFasta.contains(">GENSCAN")) {
                    iUtilProtein = new UtilProtein(lFasta);
                    accession = (iUtilProtein.getHeader().toString().substring(lFasta.indexOf("GEN"), lFasta.indexOf("pep") - 1));
                } else if (lFasta.contains(">ENS")) {
                    iUtilProtein = new UtilProtein(lFasta);
                    accession = (iUtilProtein.getHeader().toString().substring(lFasta.indexOf("ENS"), lFasta.indexOf("pep") - 1));
                }
                 else if ((secondIndex = (lFasta.indexOf("|",lFasta.indexOf("|")+1))) != -1) {       
                    accession = lFasta.substring(lFasta.indexOf("|")+1,secondIndex);
                } else {
                    iUtilProtein = new UtilProtein(lFasta);
                    accession = iUtilProtein.getHeader().getAccession();
                }
            }
            rs.close();
            stat.close();
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
     * @param aConnection a connection to the msf file
     * @return a compomics utilities protein version of the protein
     */
    public com.compomics.util.protein.Protein getUtilProteinForProteinID(int proteinID, Connection aConnection) {
        com.compomics.util.protein.Protein iUtilProtein = null;
        try {
            Statement stat = aConnection.createStatement();
            ResultSet rs = stat.executeQuery("select Description,Sequence from ProteinAnnotations,Proteins where ProteinAnnotations.ProteinID = " + proteinID + " and Proteins.ProteinID = " + proteinID);
            while (rs.next()) {
                String lFasta = rs.getString(1) + "\n" + rs.getString(2);
                if (lFasta.contains(">GENSCAN")) {
                    iUtilProtein = new com.compomics.util.protein.Protein(lFasta);
                    } else if (lFasta.contains(">ENS")) {
                    try {
                        iUtilProtein = new com.compomics.util.protein.Protein(lFasta);
                    } catch (Exception e) {
                        logger.error(e);
                    }

                } else {
                    iUtilProtein = new com.compomics.util.protein.Protein(lFasta);
                }
            }
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return iUtilProtein;
    }

    /**
     *
     * @param aConnection a connection to an msf file
     * @return a hashmap with key: the accession value: a proteinlowmem object
     */
    public HashMap<String, ProteinLowMem> getAllProteinAccessions(Connection aConnection) {
        HashMap<String, ProteinLowMem> allProteinAccessions = new HashMap<String, ProteinLowMem>();
        int internalCounter = 0;
        try {
            String proteinAccession;
            PreparedStatement stat = aConnection.prepareStatement("select * from Proteins");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                int lProteinID = rs.getInt("ProteinID");
                proteinAccession = getAccessionFromProteinID(lProteinID, aConnection);
                allProteinAccessions.put(proteinAccession, new ProteinLowMem(proteinAccession, aConnection, lProteinID));
                internalCounter++;
                proteinCounter++;
                if (internalCounter == 200) {
                    setChanged();
                    notifyObservers();
                    internalCounter = 0;
                }
            }
            proteinCounter = 0;
            rs.close();
            stat.close();
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
     * @param aConnection a connection to the msf file
     * @return an arrayList containing all the proteins in proteinLowMem objects
     */
    public ArrayList<ProteinLowMem> getProteinsForConfidenceLevel(int confidenceLevel, Connection aConnection) {
        return this.getProteinsForConfidenceLevel(confidenceLevel, aConnection, false);
    }

    /**
     * this method returns all the proteins for a given confidence level in a
     * msf file and optionally reports its progress
     *
     * @param confidenceLevel the confidence level of the desired proteins
     * @param aConnection a connection to the msf file
     * @param useCounter flag if the method should report the number of proteins
     * processed to the observers
     * @return an arrayList containing all the proteins in proteinLowMem objects
     */
    public ArrayList<ProteinLowMem> getProteinsForConfidenceLevel(int confidenceLevel, Connection aConnection, boolean useCounter) {
        ArrayList<ProteinLowMem> changedAccessions = new ArrayList<ProteinLowMem>();
        String proteinAccession;
        if (useCounter) {
            proteinCounter = 0;
            try {
                Statement stat = aConnection.createStatement();
                //TODO check if this slows down significantly, otherwise change query to just select the description aswell and write a method that takes this
                ResultSet rs = stat.executeQuery("select distinct PeptidesProteins.ProteinID from PeptidesProteins ,(select Peptides.PeptideID from Peptides where Peptides.confidencelevel = " + confidenceLevel + ") as pep where PeptidesProteins.PeptideID=pep.PeptideID");
                while (rs.next()) {
                    int lProteinID = rs.getInt(1);
                    proteinAccession = getAccessionFromProteinID(lProteinID, aConnection);
                    changedAccessions.add(new ProteinLowMem(proteinAccession, aConnection, lProteinID));
                    proteinCounter++;
                    internalcounter++;
                    if (internalcounter > 30) {
                        internalcounter = 0;
                        setChanged();
                        notifyObservers();
                    }
                }
                rs.close();
                stat.close();
            } catch (SQLException sqle) {
                logger.error(sqle);
            }
        } else {
            proteinCounter = 0;
            try {
                Statement stat = aConnection.createStatement();
                //TODO check if this slows down significantly, otherwise change query to just select the description aswell and write a method that takes this
                ResultSet rs = stat.executeQuery("select PeptidesProteins.ProteinID from PeptidesProteins ,(select Peptides.PeptideID from Peptides where Peptides.confidencelevel = " + confidenceLevel + ") as pep where PeptidesProteins.PeptideID=pep.PeptideID");
                while (rs.next()) {
                    int lProteinID = rs.getInt(1);
                    proteinAccession = getAccessionFromProteinID(lProteinID, aConnection);
                    changedAccessions.add(new ProteinLowMem(proteinAccession, aConnection, lProteinID));
                }
                rs.close();
                stat.close();
            } catch (SQLException sqle) {
                logger.error(sqle);
            }
        }
        return changedAccessions;
    }

    /**
     *
     * @param proteinID the ID of the protein in the SQLite DB
     * @param aConnection a connection to the msf file
     * @return the sequence for the protein id
     * @throws java.sql.SQLException
     */
    @Override
    public String getSequenceForProteinID(int proteinID, Connection aConnection) {
        String lSequence = "";
        try {
            PreparedStatement stat = aConnection.prepareStatement("select Sequence from Proteins where ProteinID = " + proteinID);
            ResultSet rs = stat.executeQuery();
            rs.next();
            lSequence = rs.getString(1);
            rs.close();
            stat.close();
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return lSequence;
    }

    /**
     *
     * @param peptideID peptideID stored in the SQLite DB
     * @param aConnection a connection to the msf file
     * @return a vector containing all the proteins in protein objects connected
     * to the peptide
     */
    @Override
    public Vector<ProteinLowMem> getProteinsForPeptide(int peptideID, Connection aConnection) {
        Vector<ProteinLowMem> proteinVector = new Vector<ProteinLowMem>();
        try {
            PreparedStatement stat = aConnection.prepareStatement("select Proteins.ProteinID from Proteins,(select ProteinID from PeptidesProteins where PeptideID = " + peptideID + " ) as protid where protid.ProteinID = Proteins.ProteinID");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                int lProteinID = rs.getInt(1);
                proteinVector.add(new ProteinLowMem(getAccessionFromProteinID(lProteinID, aConnection), aConnection, lProteinID));
            }
            rs.close();
            stat.close();
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return proteinVector;
    }

    /**
     *
     * @param peptideVector a vector containing all the peptides to find
     * proteins for
     * @param aConnection a connection to an msf file
     * @return a vector containing all the proteins connected to the peptides
     */
    public Vector<ProteinLowMem> getProteinsForPeptideVector(Vector<Peptide> peptideVector, Connection aConnection) {
        Vector<ProteinLowMem> proteinVector = new Vector<ProteinLowMem>();
        String iAccession;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (Peptide aPeptideVector : peptideVector) {
                stringBuilder.append(",").append(aPeptideVector.getPeptideId());
            }
            stringBuilder.replace(0, 1, "");
            PreparedStatement stat = aConnection.prepareStatement("select Proteins.ProteinID,Sequence from Proteins,(select PeptidesProteins.ProteinID from PeptidesProteins where PeptideID in (" + stringBuilder.toString() + " )) as protid where protid.ProteinID = Proteins.ProteinID");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                int lProteinID = rs.getInt(1);
                //String lSequence = rs.getString(2);
                iAccession = getAccessionFromProteinID(lProteinID, aConnection);
                proteinVector.add(new ProteinLowMem(iAccession, aConnection, lProteinID));
            }
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return proteinVector;
    }

    /**
     *
     * @param peptideID the id of the peptide in the SQLite database
     * @param aConnection connection to the SQLite database
     * @return a vector containing all the protein accessions connected to the
     * peptide
     */
    public Vector<String> getAllProteinAccessionsForPeptide(int peptideID, Connection aConnection) {
        Vector<String> proteinVector = new Vector<String>();
        try {
            PreparedStatement stat = aConnection.prepareStatement("select Proteins.ProteinID from Proteins,(select ProteinID from PeptidesProteins where PeptideID = " + peptideID + " ) as protid where protid.ProteinID = Proteins.ProteinID");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                int lProteinID = rs.getInt(1);
                proteinVector.add(getAccessionFromProteinID(lProteinID, aConnection));
            }
            rs.close();
            stat.close();
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return proteinVector;
    }

    /**
     *
     * @param proteinID the id of the protein stored in the SQLite database
     * @param aConnection connection to the SQLite database
     * @return the number of peptides connected to the protein id
     */
    public Integer getNumberOfPeptidesForProtein(int proteinID, Connection aConnection) {
        Integer numberOfPeptides = 0;
        try {
            Statement stat = aConnection.createStatement();
            ResultSet rs = stat.executeQuery("select count(PeptideID) from PeptidesProteins where ProteinID = " + proteinID);
            rs.next();
            numberOfPeptides = rs.getInt(1);
            rs.close();
            stat.close();
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return numberOfPeptides;
    }

    /**
     *
     * @return getter for the observers to know how many proteins are processed
     */
    public int getNumberOfProteinsProcessed() {
        return proteinCounter;
    }

    public boolean isMasterProtein(int proteinID, Connection msfFileConnection) {
        boolean isMasterProtein = false;
        try {
            Statement stat = msfFileConnection.createStatement();
            ResultSet rs = stat.executeQuery("select IsMasterProtein from Proteins where ProteinID = " + proteinID);
            rs.next();
            isMasterProtein = rs.getBoolean("IsMasterProtein");
            rs.close();
            stat.close();
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return isMasterProtein;
    }
}