package com.compomics.thermo_msf_parser.msf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 10/1/12 Time: 10:30 AM To change
 * this template use File | Settings | File Templates.
 */
public class PeptideLowMemController extends Observable implements PeptideInterface {

    private ScoreTypeLowMem iScoreType = new ScoreTypeLowMem();
    private int counter;

    /**
     *
     * @param protein: a Protein object
     * @param iMsfVersion: enumeration object containing the version number of the current Msf file
     * @param iAminoAcids a Vector containing the objects returned from the AminoAcid
     * @return a vector containing all the peptides connected to the protein
     * @throws java.sql.SQLException
     */
    public Vector<PeptideLowMem> getPeptidesForProtein(ProteinLowMem protein, MsfVersion iMsfVersion, Vector<AminoAcid> iAminoAcids) throws SQLException {
        Vector<PeptideLowMem> foundPeptides = new Vector<PeptideLowMem>();
        Statement stat = protein.getConnection().createStatement();
        if (iMsfVersion == MsfVersion.VERSION1_2) {
            ResultSet rs = stat.executeQuery("select p.PeptideID,p.ConfidenceLevel,p.Sequence,p.TotalIonsCount,p.MatchedIonsCount,p.Annotation,p.ProcessingNodeNumber, s.*, m.FileID from spectrumheaders as s, masspeaks as m, Peptides as p,Spectra as sp,(select PeptideID as ID from PeptidesProteins where ProteinID =" + protein.getProteinID() + ") as pepid where pepid.ID = p.PeptideID and m.masspeakid = s.masspeakid and s.SpectrumID = P.SpectrumID and sp.UniqueSpectrumID = s.UniqueSpectrumID");
            while (rs.next()) {
                PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("p.PeptideID"), rs.getInt("SpectrumID"), rs.getInt("p.ConfidenceLevel"), rs.getString("p.Sequence"), rs.getInt("p.TotalIonsCount"), rs.getInt("p.MatchedIonsCount"), rs.getString("p.Annotation"), rs.getInt("p.ProcessingNodeNumber"), iAminoAcids, protein.getConnection());
                //iScoreType.addScoresToPeptide(lPeptide,protein.getConnection());
                lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), protein.getConnection()));
                //TODO make initializer with fileID argument)
                lPeptide.getParentSpectrum().setFileId(rs.getInt("m.FileID"));
                //lPeptide.getParentSpectrum().setZippedBytes(rs.getBytes("Spectra"));
                //TODO check speed of this
                rs = stat.executeQuery("select * from CustomDataPeptides where PeptideID = " + lPeptide.getPeptideId());
                while (rs.next()) {
                    lPeptide.addCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
                }
                foundPeptides.add(lPeptide);
            }
            rs.close();
        } else if (iMsfVersion == MsfVersion.VERSION1_3) {
            ResultSet rs = stat.executeQuery("select p.PeptideID,p.ConfidenceLevel,p.Sequence,p.TotalIonsCount,p.MatchedIonsCount,p.Annotation,p.ProcessingNodeNumber,p.MissedCleavages,p.UniquePeptideSequenceID, s.*, m.FileID,sp.Spectrum from spectrumheaders as s, masspeaks as m, Peptides as p,Spectra as sp,(select PeptideID as ID from PeptidesProteins where ProteinID =" + protein.getProteinID() + ") as pepid where pepid.ID = p.PeptideID and m.masspeakid = s.masspeakid and s.SpectrumID = P.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID");
            while (rs.next()) {
                PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("p.PeptideID"), rs.getInt("SpectrumID"), rs.getInt("p.ConfidenceLevel"), rs.getString("p.Sequence"), rs.getInt("p.TotalIonsCount"), rs.getInt("p.MatchedIonsCount"), rs.getString("p.Annotation"), rs.getInt("p.ProcessingNodeNumber"), iAminoAcids, protein.getConnection());
                lPeptide.setMissedCleavage(rs.getInt("p.MissedCleavages"));
                lPeptide.setUniquePeptideSequenceId(rs.getInt("p.UniquePeptideSequenceID"));
                //iScoreType.addScoresToPeptide(lPeptide,protein.getConnection());
                lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), protein.getConnection()));
                //TODO make initializer with fileID argument)
                lPeptide.getParentSpectrum().setFileId(rs.getInt("m.FileID"));
                //lPeptide.getParentSpectrum().setZippedBytes(rs.getBytes("sp.Spectrum"));
                rs = stat.executeQuery("select * from CustomDataPeptides where PeptideID = " + lPeptide.getPeptideId());
                while (rs.next()) {
                    lPeptide.addCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
                }
                foundPeptides.add(lPeptide);
            }
            rs.close();
        }
        stat.close();
        return foundPeptides;
    }

    /**
     *
     * @param lProteinAccession: a string containing the accession of the
     * protein
     * @param iMsfVersion: enumeration object containing the version number of
     * the current Msf file
     * @param iConnection: a connection to the SQLite database
     * @param iAminoAcids a Vector containing the objects returned from the
     * AminoAcid
     * @return a vector containing the peptides connected to a protein accession
     * @throws SQLException
     */
    public Vector<PeptideLowMem> getPeptidesForAccession(String lProteinAccession, MsfVersion iMsfVersion, Connection iConnection, Vector<AminoAcid> iAminoAcids) throws SQLException {
        Statement stat = iConnection.createStatement();
        ResultSet rs = stat.executeQuery("select protanno.proteinID,Proteins.Sequence from ,(ProteinID from ProteinAnnotations where ProteinAnnotations.Description like ('%" + lProteinAccession + "%')) as protanno where protanno.ProteinID = Proteins.ProteinID");
        rs.next();
        int lProteinID = rs.getInt(1);
        String lSequence = rs.getString(2);
        rs.close();
        stat.close();
        return getPeptidesForProtein(new ProteinLowMem(lSequence, iConnection, lProteinID), iMsfVersion, iAminoAcids);
    }

    //TODO finish this
    /**
     *
     * @param peptideID: the peptide ID in the sqlite database
     * @param iConnection: a connection to the SQLite database
     * @param fullInfo if the returned information should be consise or not
     * @return vector containing the info related to the peptide id
     * @throws SQLException
     */
    public Vector getInformationForPeptide(int peptideID, Connection iConnection, boolean fullInfo) throws SQLException {
        Statement stat = iConnection.createStatement();
        ResultSet rs;
        Vector peptideInfo = new Vector();
        if (fullInfo) {
            rs = stat.executeQuery("select Peptides.Sequence, SpectrumHeaders.FirstScan,SpectrumHeaders.LastScan,Peptides.Annotations,ProcessingNodes.FriendlyName from Peptides where ConfidenceLevel >= ");
        } else {
            rs = stat.executeQuery("");
        }
        while (rs.next()) {
        }
        rs.close();
        stat.close();
        return peptideInfo;
    }

    /**
     *
     * @param confidenceLevel the confidence level of the peptides we want
     * @param iConnection connection to the msf file
     * @param iMsfVersion the version with which the msf file is made
     * @param iAminoAcids vector with the amino acids fetched from the
     * aminoacidlowmem class
     * @return
     */
    public Vector<PeptideLowMem> getPeptidesWithConfidenceLevel(int confidenceLevel, Connection iConnection, MsfVersion iMsfVersion, Vector<AminoAcid> iAminoAcids) {
        Vector<PeptideLowMem> confidenceLevelPeptides = new Vector<PeptideLowMem>();
        int internalcounter = 0;
        try {
            Statement stat = iConnection.createStatement();
            if (iMsfVersion == MsfVersion.VERSION1_3) {
                ResultSet rs = stat.executeQuery("select p.PeptideID,p.ConfidenceLevel,p.Sequence,p.TotalIonsCount,p.MatchedIonsCount,p.Annotation,p.ProcessingNodeNumber,p.MissedCleavages,p.UniquePeptideSequenceID, s.*, m.FileID,sp.Spectrum from spectrumheaders as s, masspeaks as m, Peptides as p,Spectra as sp where m.masspeakid = s.masspeakid and s.SpectrumID = P.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and p.ConfidenceLevel = " + confidenceLevel);
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), iAminoAcids, iConnection);
                    lPeptide.setMissedCleavage(rs.getInt("MissedCleavages"));
                    lPeptide.setUniquePeptideSequenceId(rs.getInt("UniquePeptideSequenceID"));
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), iConnection));
                    //TODO make initializer with fileID argument
                    lPeptide.getParentSpectrum().setFileId(rs.getInt("FileID"));
                    lPeptide.getParentSpectrum().setZippedBytes(rs.getBytes("Spectrum"));
                    confidenceLevelPeptides.add(lPeptide);
                    counter++;
                    internalcounter++;
                    if (internalcounter > 30) {
                        internalcounter = 0;
                        setChanged();
                        notifyObservers();
                    }
                }
                rs.close();
                stat.close();
                counter = 0;
                iScoreType.getScoresForPeptideVector(confidenceLevelPeptides, iConnection);

            } else if (iMsfVersion == MsfVersion.VERSION1_2) {
                ResultSet rs = stat.executeQuery("select p.PeptideID,p.ConfidenceLevel,p.Sequence,p.TotalIonsCount,p.MatchedIonsCount,p.Annotation,p.ProcessingNodeNumber, s.*, m.FileID, sp.Spectrum from spectrumheaders as s, masspeaks as m, Peptides as p, Spectra as sp where m.masspeakid = s.masspeakid and s.SpectrumID = P.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and p.ConfidenceLevel = " + confidenceLevel);
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), iAminoAcids, iConnection);
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), iConnection));
                    lPeptide.getParentSpectrum().setFileId(rs.getInt("FileID"));
                    lPeptide.getParentSpectrum().setZippedBytes(rs.getBytes("Spectrum"));
                    confidenceLevelPeptides.add(lPeptide);
                    counter++;
                    internalcounter++;
                    if (internalcounter > 30) {
                        internalcounter = 0;
                        setChanged();
                        notifyObservers();
                    }
                }
                iScoreType.getScoresForPeptideVector(confidenceLevelPeptides, iConnection);
                rs.close();
                stat.close();
                counter = 0;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return confidenceLevelPeptides;
    }

    public int getNumberOfPeptidesForConfidenceLevel(int confidenceLevel, Connection iConnection) {
        int numberOfPeptides = 0;
        Statement stat = null;
        try {
            stat = iConnection.createStatement();
            ResultSet rs = stat.executeQuery("select count(PeptideID) from Peptides where ConfidenceLevel = " + confidenceLevel);
            numberOfPeptides = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfPeptides;
    }

    public Integer getNumberOfPeptidesProcessed() {
        return counter;
    }

    public void getPeptidesForProteinVector(Vector<ProteinLowMem> proteinLowMemVector, Connection iConnection, Vector<AminoAcid> iAminoAcids, MsfVersion iMsfVersion,int confidenceLevel) {
        try {
            int counter = 0;
            String listOfProteinIds = "";
            HashSet<String> peptides = new HashSet<String>();
            HashMap<Integer, ProteinLowMem> protIdToProt = new HashMap<Integer, ProteinLowMem>();
            for (ProteinLowMem aProtein : proteinLowMemVector) {
                listOfProteinIds = "," + aProtein.getProteinID() + listOfProteinIds;
                protIdToProt.put(aProtein.getProteinID(), aProtein);
            }
            listOfProteinIds = listOfProteinIds.replaceFirst(",", "");
            if (iMsfVersion == MsfVersion.VERSION1_3) {
                PreparedStatement stat = iConnection.prepareStatement("select p.PeptideID,p.ConfidenceLevel,p.Sequence,p.TotalIonsCount,p.MatchedIonsCount,p.Annotation,p.ProcessingNodeNumber,p.MissedCleavages,p.UniquePeptideSequenceID, s.*, m.FileID,pp.ProteinID from spectrumheaders as s, masspeaks as m, Peptides as p,Spectra as sp, (select PeptideID,ProteinID from peptidesProteins where PeptidesProteins.ProteinID in (" + listOfProteinIds + ")) as pp where m.masspeakid = s.masspeakid and s.SpectrumID = p.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and p.PeptideID = pp.PeptideID and p.ConfidenceLevel = "+ confidenceLevel);
                ResultSet rs = stat.executeQuery();
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("p.PeptideID"), rs.getInt("SpectrumID"), rs.getInt("p.ConfidenceLevel"), rs.getString("p.Sequence"), rs.getInt("p.TotalIonsCount"), rs.getInt("p.MatchedIonsCount"), rs.getString("p.Annotation"), rs.getInt("p.ProcessingNodeNumber"), iAminoAcids, iConnection);
                    lPeptide.setMissedCleavage(rs.getInt("p.MissedCleavages"));
                    lPeptide.setUniquePeptideSequenceId(rs.getInt("p.UniquePeptideSequenceID"));
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), iConnection));
                    //TODO make initializer with fileID argument
                    lPeptide.getParentSpectrum().setFileId(rs.getInt("m.FileID"));
                    //lPeptide.getParentSpectrum().setZippedBytes(rs.getBytes("Spectrum"));
                    protIdToProt.get(rs.getInt("pp.ProteinID")).addPeptide(lPeptide);
                    counter++;
                    System.out.println(counter);
                    peptides.add(lPeptide.getSequence());
                }
                rs.close();
                stat.close();
            } else if (iMsfVersion == MsfVersion.VERSION1_2) {
                PreparedStatement stat = iConnection.prepareStatement("select p.PeptideID,p.ConfidenceLevel,p.Sequence,p.TotalIonsCount,p.MatchedIonsCount,p.Annotation,p.ProcessingNodeNumber, s.*, m.FileID,PeptidesProteins.ProteinID from spectrumheaders as s, masspeaks as m, Peptides as p,Spectra as sp, (select PeptideID,ProteinID from peptidesProteins where PeptidesProteins.ProteinID in (" + listOfProteinIds + ")) as pp where m.masspeakid = s.masspeakid and s.SpectrumID = p.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and p.PeptideID = pp.PeptideID and p.ConfidenceLevel = "+ confidenceLevel);
                ResultSet rs = stat.executeQuery();
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), iAminoAcids, iConnection);
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), iConnection));
                    lPeptide.getParentSpectrum().setFileId(rs.getInt("FileID"));
                    //lPeptide.getParentSpectrum().setZippedBytes(rs.getBytes("Spectrum"));
                    protIdToProt.get(rs.getInt("ProteinID")).addPeptide(lPeptide);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PeptideLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getPeptidesForProteinVector(Vector<ProteinLowMem> proteinLowMemVector, Connection iConnection, Vector<AminoAcid> iAminoAcids, MsfVersion iMsfVersion) {
        try {
            int counter = 0;
            String listOfProteinIds = "";
            HashSet<String> peptides = new HashSet<String>();
            HashMap<Integer, ProteinLowMem> protIdToProt = new HashMap<Integer, ProteinLowMem>();
            for (ProteinLowMem aProtein : proteinLowMemVector) {
                listOfProteinIds = "," + aProtein.getProteinID() + listOfProteinIds;
                protIdToProt.put(aProtein.getProteinID(), aProtein);
            }
            listOfProteinIds = listOfProteinIds.replaceFirst(",", "");
            if (iMsfVersion == MsfVersion.VERSION1_3) {
                PreparedStatement stat = iConnection.prepareStatement("select p.PeptideID,p.ConfidenceLevel,p.Sequence,p.TotalIonsCount,p.MatchedIonsCount,p.Annotation,p.ProcessingNodeNumber,p.MissedCleavages,p.UniquePeptideSequenceID, s.*, m.FileID,pp.ProteinID from spectrumheaders as s, masspeaks as m, Peptides as p,Spectra as sp, (select PeptideID,ProteinID from peptidesProteins where PeptidesProteins.ProteinID in (" + listOfProteinIds + ")) as pp where m.masspeakid = s.masspeakid and s.SpectrumID = p.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and p.PeptideID = pp.PeptideID ");
                ResultSet rs = stat.executeQuery();
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("p.PeptideID"), rs.getInt("SpectrumID"), rs.getInt("p.ConfidenceLevel"), rs.getString("p.Sequence"), rs.getInt("p.TotalIonsCount"), rs.getInt("p.MatchedIonsCount"), rs.getString("p.Annotation"), rs.getInt("p.ProcessingNodeNumber"), iAminoAcids, iConnection);
                    lPeptide.setMissedCleavage(rs.getInt("p.MissedCleavages"));
                    lPeptide.setUniquePeptideSequenceId(rs.getInt("p.UniquePeptideSequenceID"));
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), iConnection));
                    //TODO make initializer with fileID argument
                    lPeptide.getParentSpectrum().setFileId(rs.getInt("m.FileID"));
                    //lPeptide.getParentSpectrum().setZippedBytes(rs.getBytes("Spectrum"));
                    protIdToProt.get(rs.getInt("pp.ProteinID")).addPeptide(lPeptide);
                    counter++;
                    System.out.println(counter);
                    peptides.add(lPeptide.getSequence());
                }
                rs.close();
                stat.close();
            } else if (iMsfVersion == MsfVersion.VERSION1_2) {
                PreparedStatement stat = iConnection.prepareStatement("select p.PeptideID,p.ConfidenceLevel,p.Sequence,p.TotalIonsCount,p.MatchedIonsCount,p.Annotation,p.ProcessingNodeNumber, s.*, m.FileID,PeptidesProteins.ProteinID from spectrumheaders as s, masspeaks as m, Peptides as p,Spectra as sp, (select PeptideID,ProteinID from peptidesProteins where PeptidesProteins.ProteinID in (" + listOfProteinIds + ")) as pp where m.masspeakid = s.masspeakid and s.SpectrumID = p.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and p.PeptideID = pp.PeptideID");
                ResultSet rs = stat.executeQuery();
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), iAminoAcids, iConnection);
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), iConnection));
                    lPeptide.getParentSpectrum().setFileId(rs.getInt("FileID"));
                    //lPeptide.getParentSpectrum().setZippedBytes(rs.getBytes("Spectrum"));
                    protIdToProt.get(rs.getInt("ProteinID")).addPeptide(lPeptide);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PeptideLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}