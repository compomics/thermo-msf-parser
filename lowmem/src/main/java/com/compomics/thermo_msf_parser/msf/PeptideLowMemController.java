package com.compomics.thermo_msf_parser.msf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Observable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 10/1/12 Time: 10:30 AM To change
 * this template use File | Settings | File Templates.
 */
public class PeptideLowMemController extends Observable implements PeptideInterface {
    
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PeptideLowMemController.class);

    private ScoreTypeLowMemController scoreTypeInstance = new ScoreTypeLowMemController();
    private int counter;

    /**
     *
     * @param protein: a Protein object
     * @param iMsfVersion: enumeration object containing the version number of the current Msf file
     * @param iAminoAcids a Vector containing the objects returned from the AminoAcid
     * @return a vector containing all the peptides connected to the protein
     * @throws java.sql.SQLException
     */
    public Vector<PeptideLowMem> getPeptidesForProtein(ProteinLowMem protein, MsfVersion iMsfVersion, Vector<AminoAcid> iAminoAcids) {
        Vector<PeptideLowMem> foundPeptides = new Vector<PeptideLowMem>();
        try {
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
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), protein.getConnection()));
                    //TODO make initializer with fileID argument)
                    lPeptide.getParentSpectrum().setFileId(rs.getInt("m.FileID"));
                    rs = stat.executeQuery("select * from CustomDataPeptides where PeptideID = " + lPeptide.getPeptideId());
                    while (rs.next()) {
                        lPeptide.addCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
                    }
                    foundPeptides.add(lPeptide);
                }
                rs.close();
            }
            stat.close();
        } catch (SQLException ex) {
            Logger.getLogger(PeptideLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return foundPeptides;
    }

    public Vector<PeptideLowMem> getPeptidesForAccession(String lProteinAccession, MsfVersion iMsfVersion, Connection aConnection, Vector<AminoAcid> iAminoAcids) {
         int lProteinID = 0;
         String lSequence = null;
                 try {
            Statement stat = aConnection.createStatement();
            ResultSet rs = stat.executeQuery("select protanno.proteinID,Proteins.Sequence from ,(ProteinID from ProteinAnnotations where ProteinAnnotations.Description like ('%" + lProteinAccession + "%')) as protanno where protanno.ProteinID = Proteins.ProteinID");
            rs.next();
            lProteinID = rs.getInt("protanno.proteinID");
            lSequence = rs.getString("Protiens.Sequence");
            rs.close();
            stat.close();
            } catch (SQLException ex) {
            Logger.getLogger(PeptideLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return getPeptidesForProtein(new ProteinLowMem(lSequence, aConnection, lProteinID), iMsfVersion, iAminoAcids);
        
    }

    //TODO finish this

    public Vector getInformationForPeptide(int peptideID, Connection aConnection, boolean fullInfo){
        Vector peptideInfo = new Vector();
        try {
            Statement stat = aConnection.createStatement();
            ResultSet rs;
            if (fullInfo) {
                rs = stat.executeQuery("select Peptides.Sequence, SpectrumHeaders.FirstScan,SpectrumHeaders.LastScan,Peptides.Annotations,ProcessingNodes.FriendlyName from Peptides where ConfidenceLevel >= ");
            } else {
                rs = stat.executeQuery("");
            }
            while (rs.next()) {
            }
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            Logger.getLogger(PeptideLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return peptideInfo;
    }

    public Vector<PeptideLowMem> getPeptidesWithConfidenceLevel(int confidenceLevel, Connection aConnection, MsfVersion iMsfVersion, Vector<AminoAcid> iAminoAcids) {
        Vector<PeptideLowMem> confidenceLevelPeptides = new Vector<PeptideLowMem>();
        int internalcounter = 0;
        try {
            Statement stat = aConnection.createStatement();
            if (iMsfVersion == MsfVersion.VERSION1_3) {
                ResultSet rs = stat.executeQuery("select p.PeptideID,p.ConfidenceLevel,p.Sequence,p.TotalIonsCount,p.MatchedIonsCount,p.Annotation,p.ProcessingNodeNumber,p.MissedCleavages,p.UniquePeptideSequenceID, s.*, m.FileID,sp.Spectrum from spectrumheaders as s, masspeaks as m, Peptides as p,Spectra as sp where m.masspeakid = s.masspeakid and s.SpectrumID = P.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and p.ConfidenceLevel = " + confidenceLevel);
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), iAminoAcids, aConnection);
                    lPeptide.setMissedCleavage(rs.getInt("MissedCleavages"));
                    lPeptide.setUniquePeptideSequenceId(rs.getInt("UniquePeptideSequenceID"));
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), aConnection));
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
                scoreTypeInstance.getScoresForPeptideVector(confidenceLevelPeptides, aConnection);

            } else if (iMsfVersion == MsfVersion.VERSION1_2) {
                ResultSet rs = stat.executeQuery("select p.PeptideID,p.ConfidenceLevel,p.Sequence,p.TotalIonsCount,p.MatchedIonsCount,p.Annotation,p.ProcessingNodeNumber, s.*, m.FileID, sp.Spectrum from spectrumheaders as s, masspeaks as m, Peptides as p, Spectra as sp where m.masspeakid = s.masspeakid and s.SpectrumID = P.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and p.ConfidenceLevel = " + confidenceLevel);
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), iAminoAcids, aConnection);
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), aConnection));
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
                scoreTypeInstance.getScoresForPeptideVector(confidenceLevelPeptides, aConnection);
                rs.close();
                stat.close();
                counter = 0;
            }
        } catch (SQLException sqle) {
            Logger.getLogger(PeptideLowMemController.class.getName()).log(Level.SEVERE, null, sqle);
        }
        return confidenceLevelPeptides;
    }

    public int getNumberOfPeptidesForConfidenceLevel(int confidenceLevel, Connection aConnection) {
        int numberOfPeptides = 0;
        Statement stat = null;
        try {
            stat = aConnection.createStatement();
            ResultSet rs = stat.executeQuery("select count(PeptideID) from Peptides where ConfidenceLevel = " + confidenceLevel);
            numberOfPeptides = rs.getInt(1);
        } catch (SQLException e) {
            Logger.getLogger(PeptideLowMemController.class.getName()).log(Level.SEVERE, null, e);
        }
        return numberOfPeptides;
    }

    public Integer getNumberOfPeptidesProcessed() {
        return counter;
    }

    public void getPeptidesForProteinVector(Vector<ProteinLowMem> proteinLowMemVector, Connection aConnection, Vector<AminoAcid> iAminoAcids, MsfVersion iMsfVersion,int confidenceLevel) {
        try {
            String listOfProteinIds = "";
            HashMap<Integer, ProteinLowMem> protIdToProt = new HashMap<Integer, ProteinLowMem>();
            for (ProteinLowMem aProtein : proteinLowMemVector) {
                listOfProteinIds = "," + aProtein.getProteinID() + listOfProteinIds;
                protIdToProt.put(aProtein.getProteinID(), aProtein);
            }
            listOfProteinIds = listOfProteinIds.replaceFirst(",", "");
            if (iMsfVersion == MsfVersion.VERSION1_3) {
                PreparedStatement stat = aConnection.prepareStatement("select p.PeptideID,p.ConfidenceLevel,p.Sequence,p.TotalIonsCount,p.MatchedIonsCount,p.Annotation,p.ProcessingNodeNumber,p.MissedCleavages,p.UniquePeptideSequenceID, s.*, m.FileID,pp.ProteinID from spectrumheaders as s, masspeaks as m, Peptides as p,Spectra as sp, (select PeptideID,ProteinID from peptidesProteins where PeptidesProteins.ProteinID in (" + listOfProteinIds + ")) as pp where m.masspeakid = s.masspeakid and s.SpectrumID = p.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and p.PeptideID = pp.PeptideID and p.ConfidenceLevel = "+ confidenceLevel);
                ResultSet rs = stat.executeQuery();
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("p.PeptideID"), rs.getInt("SpectrumID"), rs.getInt("p.ConfidenceLevel"), rs.getString("p.Sequence"), rs.getInt("p.TotalIonsCount"), rs.getInt("p.MatchedIonsCount"), rs.getString("p.Annotation"), rs.getInt("p.ProcessingNodeNumber"), iAminoAcids, aConnection);
                    lPeptide.setMissedCleavage(rs.getInt("p.MissedCleavages"));
                    lPeptide.setUniquePeptideSequenceId(rs.getInt("p.UniquePeptideSequenceID"));
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), aConnection));
                    //TODO make initializer with fileID argument
                    lPeptide.getParentSpectrum().setFileId(rs.getInt("m.FileID"));
                    //lPeptide.getParentSpectrum().setZippedBytes(rs.getBytes("Spectrum"));
                    protIdToProt.get(rs.getInt("pp.ProteinID")).addPeptide(lPeptide);
                }
                rs.close();
                stat.close();
            } else if (iMsfVersion == MsfVersion.VERSION1_2) {
                PreparedStatement stat = aConnection.prepareStatement("select p.PeptideID,p.ConfidenceLevel,p.Sequence,p.TotalIonsCount,p.MatchedIonsCount,p.Annotation,p.ProcessingNodeNumber, s.*, m.FileID,PeptidesProteins.ProteinID from spectrumheaders as s, masspeaks as m, Peptides as p,Spectra as sp, (select PeptideID,ProteinID from peptidesProteins where PeptidesProteins.ProteinID in (" + listOfProteinIds + ")) as pp where m.masspeakid = s.masspeakid and s.SpectrumID = p.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and p.PeptideID = pp.PeptideID and p.ConfidenceLevel = "+ confidenceLevel);
                ResultSet rs = stat.executeQuery();
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), iAminoAcids, aConnection);
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), aConnection));
                    lPeptide.getParentSpectrum().setFileId(rs.getInt("FileID"));
                    //lPeptide.getParentSpectrum().setZippedBytes(rs.getBytes("Spectrum"));
                    protIdToProt.get(rs.getInt("pp.ProteinID")).addPeptide(lPeptide);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PeptideLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getPeptidesForProteinVector(Vector<ProteinLowMem> proteinLowMemVector, Connection aConnection, Vector<AminoAcid> iAminoAcids, MsfVersion iMsfVersion) {
        try {
            String listOfProteinIds = "";
            HashMap<Integer, ProteinLowMem> protIdToProt = new HashMap<Integer, ProteinLowMem>();
            for (ProteinLowMem aProtein : proteinLowMemVector) {
                listOfProteinIds = "," + aProtein.getProteinID() + listOfProteinIds;
                protIdToProt.put(aProtein.getProteinID(), aProtein);
            }
            listOfProteinIds = listOfProteinIds.replaceFirst(",", "");
            if (iMsfVersion == MsfVersion.VERSION1_3) {
                PreparedStatement stat = aConnection.prepareStatement("select p.PeptideID,p.ConfidenceLevel,p.Sequence,p.TotalIonsCount,p.MatchedIonsCount,p.Annotation,p.ProcessingNodeNumber,p.MissedCleavages,p.UniquePeptideSequenceID, s.*, m.FileID,pp.ProteinID from spectrumheaders as s, masspeaks as m, Peptides as p,Spectra as sp, (select PeptideID,ProteinID from peptidesProteins where PeptidesProteins.ProteinID in (" + listOfProteinIds + ")) as pp where m.masspeakid = s.masspeakid and s.SpectrumID = p.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and p.PeptideID = pp.PeptideID ");
                ResultSet rs = stat.executeQuery();
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("p.PeptideID"), rs.getInt("SpectrumID"), rs.getInt("p.ConfidenceLevel"), rs.getString("p.Sequence"), rs.getInt("p.TotalIonsCount"), rs.getInt("p.MatchedIonsCount"), rs.getString("p.Annotation"), rs.getInt("p.ProcessingNodeNumber"), iAminoAcids, aConnection);
                    lPeptide.setMissedCleavage(rs.getInt("p.MissedCleavages"));
                    lPeptide.setUniquePeptideSequenceId(rs.getInt("p.UniquePeptideSequenceID"));
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), aConnection));
                    //TODO make initializer with fileID argument
                    lPeptide.getParentSpectrum().setFileId(rs.getInt("m.FileID"));
                    //lPeptide.getParentSpectrum().setZippedBytes(rs.getBytes("Spectrum"));
                    protIdToProt.get(rs.getInt("pp.ProteinID")).addPeptide(lPeptide);
                }
                rs.close();
                stat.close();
            } else if (iMsfVersion == MsfVersion.VERSION1_2) {
                PreparedStatement stat = aConnection.prepareStatement("select p.PeptideID,p.ConfidenceLevel,p.Sequence,p.TotalIonsCount,p.MatchedIonsCount,p.Annotation,p.ProcessingNodeNumber, s.*, m.FileID,PeptidesProteins.ProteinID from spectrumheaders as s, masspeaks as m, Peptides as p,Spectra as sp, (select PeptideID,ProteinID from peptidesProteins where PeptidesProteins.ProteinID in (" + listOfProteinIds + ")) as pp where m.masspeakid = s.masspeakid and s.SpectrumID = p.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and p.PeptideID = pp.PeptideID");
                ResultSet rs = stat.executeQuery();
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), iAminoAcids, aConnection);
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), aConnection));
                    lPeptide.getParentSpectrum().setFileId(rs.getInt("FileID"));
                    //lPeptide.getParentSpectrum().setZippedBytes(rs.getBytes("Spectrum"));
                    protIdToProt.get(rs.getInt("ProteinID")).addPeptide(lPeptide);
                }
            }
        } catch (SQLException ex) {
        }
    }
}