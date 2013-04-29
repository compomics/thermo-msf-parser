package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.enums.MsfVersion;
import com.compomics.thermo_msf_parser_API.interfaces.PeptideInterface;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.PeptideLowMem;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.SpectrumLowMem;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 10/1/12 Time: 10:30 AM To change
 * this template use File | Settings | File Templates.
 */
public class PeptideLowMemController extends Observable implements PeptideInterface {

    private static final Logger logger = Logger.getLogger(PeptideLowMemController.class);
    private final ScoreTypeLowMemController scoreTypeInstance = new ScoreTypeLowMemController();
    private int counter;

    /**
     *
     * @param protein: a Protein object
     * @param iMsfVersion: enumeration object containing the version number of
     * the current Msf file
     * @param iAminoAcids a List containing the objects returned from the
     * AminoAcid
     * @return a vector containing all the peptides connected to the protein
     * @throws java.sql.SQLException
     */
    @Override
    public List<PeptideLowMem> getPeptidesForProtein(ProteinLowMem protein,MsfFile msfFile) {
        List<PeptideLowMem> foundPeptides = new ArrayList<PeptideLowMem>();
        try {
            Statement stat = msfFile.getConnection().createStatement();

            if (msfFile.getVersion() == MsfVersion.VERSION1_2) {
                ResultSet rs = stat.executeQuery("select PeptideID,ConfidenceLevel,Sequence,TotalIonsCount,MatchedIonsCount,Annotation,ProcessingNodeNumber, s.*, FileID as file from spectrumheaders as s, masspeaks, Peptides as p,Spectra as sp,(select PeptideID as ID from PeptidesProteins where ProteinID =" + protein.getProteinID() + ") as pepid where pepid.ID = p.PeptideID and masspeaks.masspeakid = s.masspeakid and s.SpectrumID = P.SpectrumID and sp.UniqueSpectrumID = s.UniqueSpectrumID order by p.PeptideID");
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), msfFile.getAminoAcids());
                    //iScoreType.addScoresToPeptide(lPeptide,protein.getConnection());
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID")));
                    //TODO make initializer with fileID argument)
                    lPeptide.getParentSpectrum().setFileId(rs.getInt("file"));
                    //lPeptide.getParentSpectrum().setZippedBytes(rs.getBytes("Spectra"));
                    //TODO check speed of this
                    Statement customStat = msfFile.getConnection().createStatement();
                    ResultSet customRs = customStat.executeQuery("select * from CustomDataPeptides where PeptideID = " + lPeptide.getPeptideId());
                    while (customRs.next()) {
                        lPeptide.addCustomDataField(customRs.getInt("FieldID"), customRs.getString("FieldValue"));
                    }
                    customRs.close();
                    customStat.close();
                    foundPeptides.add(lPeptide);
                }
                rs.close();
            } else if (msfFile.getVersion() == MsfVersion.VERSION1_3) {
                ResultSet rs = stat.executeQuery("select PeptideID,ConfidenceLevel,Sequence,TotalIonsCount,MatchedIonsCount,Annotation,MissedCleavages,UniquePeptideSequenceID,ProcessingNodeNumber, s.*,FileID as file from spectrumheaders as s, masspeaks, Peptides as p,Spectra as sp,(select PeptideID as ID from PeptidesProteins where ProteinID =" + protein.getProteinID() + ") as pepid where pepid.ID = p.PeptideID and masspeaks.masspeakid = s.masspeakid and s.SpectrumID = P.SpectrumID and sp.UniqueSpectrumID = s.UniqueSpectrumID order by p.PeptideID");
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), msfFile.getAminoAcids());
                    lPeptide.setMissedCleavage(rs.getInt("MissedCleavages"));
                    lPeptide.setUniquePeptideSequenceId(rs.getInt("UniquePeptideSequenceID"));
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID")));
                    //TODO make initializer with fileID argument)
                    lPeptide.getParentSpectrum().setFileId(rs.getInt("file"));
                    Statement customStat = msfFile.getConnection().createStatement();
                    ResultSet customRs = customStat.executeQuery("select * from CustomDataPeptides where PeptideID = " + lPeptide.getPeptideId());
                    while (customRs.next()) {
                        lPeptide.addCustomDataField(customRs.getInt("FieldID"), customRs.getString("FieldValue"));
                    }
                    customRs.close();
                    customStat.close();
                    foundPeptides.add(lPeptide);
                }
                rs.close();
            }
            stat.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return foundPeptides;
    }

    @Override
    public List<PeptideLowMem> getPeptidesForAccession(String lProteinAccession,MsfFile msfFile) {
        int lProteinID = 0;
        String lSequence = null;
        try {
            Statement stat = msfFile.getConnection().createStatement();
            ResultSet rs = stat.executeQuery("select protanno.proteinID,Proteins.Sequence from ,(ProteinID from ProteinAnnotations where ProteinAnnotations.Description like ('%" + lProteinAccession + "%')) as protanno where protanno.ProteinID = Proteins.ProteinID");
            rs.next();
            lProteinID = rs.getInt("protanno.proteinID");
            lSequence = rs.getString("Protiens.Sequence");
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return getPeptidesForProtein(new ProteinLowMem(lSequence, lProteinID),msfFile);

    }

    //TODO finish this
    @Override
    public List getInformationForPeptide(int peptideID, Connection aConnection, boolean fullInfo) {
        List peptideInfo = new ArrayList();
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
            logger.error(ex);
        }
        return peptideInfo;
    }

    @Override
    public List<PeptideLowMem> getPeptidesWithConfidenceLevel(int confidenceLevel, MsfFile msfFile) {
        List<PeptideLowMem> confidenceLevelPeptides = new ArrayList<PeptideLowMem>();
        int internalcounter = 0;
        try {
            Statement stat = msfFile.getConnection().createStatement();
            if (msfFile.getVersion() == MsfVersion.VERSION1_3) {
                ResultSet rs = stat.executeQuery("select PeptideID,ConfidenceLevel,Sequence,TotalIonsCount,MatchedIonsCount,Annotation,ProcessingNodeNumber,MissedCleavages,UniquePeptideSequenceID, s.*, FileID as file,sp.Spectrum from spectrumheaders as s, masspeaks as m, Peptides as p,Spectra as sp where m.masspeakid = s.masspeakid and s.SpectrumID = P.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and p.ConfidenceLevel = " + confidenceLevel);
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), msfFile.getAminoAcids());
                    lPeptide.setMissedCleavage(rs.getInt("MissedCleavages"));
                    lPeptide.setUniquePeptideSequenceId(rs.getInt("UniquePeptideSequenceID"));
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID")));
                    //TODO make initializer with fileID argument
                    lPeptide.getParentSpectrum().setFileId(rs.getInt("file"));
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
                counter = 0;
                scoreTypeInstance.getScoresForPeptideList(confidenceLevelPeptides, msfFile);

            } else if (msfFile.getVersion() == MsfVersion.VERSION1_2) {
                ResultSet rs = stat.executeQuery("select PeptideID,ConfidenceLevel,Sequence,TotalIonsCount,MatchedIonsCount,Annotation,ProcessingNodeNumber, s.*, FileID as file, sp.Spectrum from spectrumheaders as s, masspeaks as m, Peptides as p, Spectra as sp where m.masspeakid = s.masspeakid and s.SpectrumID = P.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and p.ConfidenceLevel = " + confidenceLevel);
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), msfFile.getAminoAcids());
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID")));
                    lPeptide.getParentSpectrum().setFileId(rs.getInt("file"));
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
                scoreTypeInstance.getScoresForPeptideList(confidenceLevelPeptides, msfFile);
                rs.close();
                counter = 0;
            }
            stat.close();
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return confidenceLevelPeptides;
    }

    @Override
    public int getNumberOfPeptidesForConfidenceLevel(int confidenceLevel,MsfFile msfFile) {
        int numberOfPeptides = 0;
        try {
            Statement stat = msfFile.getConnection().createStatement();
            ResultSet rs = stat.executeQuery("select count(PeptideID) from Peptides where ConfidenceLevel = " + confidenceLevel);
            numberOfPeptides = rs.getInt(1);
            rs.close();
            stat.close();
        } catch (SQLException e) {
            logger.error(e);
        }
        return numberOfPeptides;
    }

    public Integer getNumberOfPeptidesProcessed() {
        return counter;
    }

    @Override
    public void getPeptidesForProteinList(List<ProteinLowMem> proteinLowMemList, MsfFile msfFile, int confidenceLevel) {
        try {
            String listOfProteinIds = "";
            HashMap<Integer, ProteinLowMem> protIdToProt = new HashMap<Integer, ProteinLowMem>();
            for (ProteinLowMem aProtein : proteinLowMemList) {
                listOfProteinIds = "," + aProtein.getProteinID() + listOfProteinIds;
                protIdToProt.put(aProtein.getProteinID(), aProtein);
            }
            listOfProteinIds = listOfProteinIds.replaceFirst(",", "");
            if (msfFile.getVersion() == MsfVersion.VERSION1_3) {
                PreparedStatement stat = msfFile.getConnection().prepareStatement("select p.PeptideID as PeptideID,ConfidenceLevel,Sequence,TotalIonsCount,MatchedIonsCount,Annotation,ProcessingNodeNumber,MissedCleavages,UniquePeptideSequenceID, s.*, FileID as file,pp.ProteinID from spectrumheaders as s, masspeaks, Peptides as p,Spectra as sp, (select PeptideID,ProteinID from peptidesProteins where PeptidesProteins.ProteinID in (" + listOfProteinIds + ")) as pp where masspeaks.masspeakid = s.masspeakid and s.SpectrumID = p.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and p.PeptideID = pp.PeptideID and p.ConfidenceLevel = " + confidenceLevel);
                ResultSet rs = stat.executeQuery();
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), msfFile.getAminoAcids());
                    lPeptide.setMissedCleavage(rs.getInt("MissedCleavages"));
                    lPeptide.setUniquePeptideSequenceId(rs.getInt("UniquePeptideSequenceID"));
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID")));
                    lPeptide.getParentSpectrum().setFileId(rs.getInt("file"));
                    //lPeptide.getParentSpectrum().setZippedBytes(rs.getBytes("Spectrum"));
                    protIdToProt.get(rs.getInt("pp.ProteinID")).addPeptide(lPeptide);
                }
                rs.close();
                stat.close();
            } else if (msfFile.getVersion() == MsfVersion.VERSION1_2) {
                PreparedStatement stat = msfFile.getConnection().prepareStatement("select p.PeptideID as PeptideID,ConfidenceLevel,Sequence,TotalIonsCount,MatchedIonsCount,Annotation,ProcessingNodeNumber, s.*, FileID as file,pp.ProteinID from spectrumheaders as s, masspeaks, Peptides as p,Spectra as sp, (select PeptideID,ProteinID from peptidesProteins where PeptidesProteins.ProteinID in (" + listOfProteinIds + ")) as pp where masspeaks.masspeakid = s.masspeakid and s.SpectrumID = p.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and p.PeptideID = pp.PeptideID and p.ConfidenceLevel = " + confidenceLevel);
                ResultSet rs = stat.executeQuery();
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), msfFile.getAminoAcids());
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID")));
                    lPeptide.getParentSpectrum().setFileId(rs.getInt("file"));
                    //lPeptide.getParentSpectrum().setZippedBytes(rs.getBytes("Spectrum"));
                    protIdToProt.get(rs.getInt("pp.ProteinID")).addPeptide(lPeptide);
                }
                rs.close();
                stat.close();
            }
        } catch (SQLException ex) {
            logger.error(ex);
        }
    }

    @Override
    public void getPeptidesForProteinList(List<ProteinLowMem> proteinLowMemList, MsfFile msfFile) {
        try {
            String listOfProteinIds = "";
            HashMap<Integer, ProteinLowMem> protIdToProt = new HashMap<Integer, ProteinLowMem>();
            for (ProteinLowMem aProtein : proteinLowMemList) {
                listOfProteinIds = "," + aProtein.getProteinID() + listOfProteinIds;
                protIdToProt.put(aProtein.getProteinID(), aProtein);
            }
            listOfProteinIds = listOfProteinIds.replaceFirst(",", "");
            if (msfFile.getVersion() == MsfVersion.VERSION1_3) {
                PreparedStatement stat = msfFile.getConnection().prepareStatement("select PeptideID,ConfidenceLevel,Sequence,p.TotalIonsCount,p.MatchedIonsCount,p.Annotation,p.ProcessingNodeNumber,p.MissedCleavages,p.UniquePeptideSequenceID, s.*, file,pp.ProteinID from spectrumheaders as s, masspeaks as m, Peptides as p,Spectra as sp, (select PeptideID,ProteinID from peptidesProteins where PeptidesProteins.ProteinID in (" + listOfProteinIds + ")) as pp where m.masspeakid = s.masspeakid and s.SpectrumID = p.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and p.PeptideID = pp.PeptideID ");
                ResultSet rs = stat.executeQuery();
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("p.MatchedIonsCount"), rs.getString("p.Annotation"), rs.getInt("p.ProcessingNodeNumber"), msfFile.getAminoAcids());
                    lPeptide.setMissedCleavage(rs.getInt("MissedCleavages"));
                    lPeptide.setUniquePeptideSequenceId(rs.getInt("UniquePeptideSequenceID"));
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID")));
                    //TODO make initializer with fileID argument
                    lPeptide.getParentSpectrum().setFileId(rs.getInt("file"));
                    //lPeptide.getParentSpectrum().setZippedBytes(rs.getBytes("Spectrum"));
                    protIdToProt.get(rs.getInt("pp.ProteinID")).addPeptide(lPeptide);
                }
                rs.close();
                stat.close();
            } else if (msfFile.getVersion() == MsfVersion.VERSION1_2) {
                PreparedStatement stat = msfFile.getConnection().prepareStatement("select PeptideID,ConfidenceLevel,Sequence,TotalIonsCount,MatchedIonsCount,Annotation,ProcessingNodeNumber, s.*, FileID as file,PeptidesProteins.ProteinID from spectrumheaders as s, masspeaks as m, Peptides as p,Spectra as sp, (select PeptideID,ProteinID from peptidesProteins where PeptidesProteins.ProteinID in (" + listOfProteinIds + ")) as pp where m.masspeakid = s.masspeakid and s.SpectrumID = p.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and p.PeptideID = pp.PeptideID");
                ResultSet rs = stat.executeQuery();
                while (rs.next()) {
                    PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), msfFile.getAminoAcids());
                    lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID")));
                    lPeptide.getParentSpectrum().setFileId(rs.getInt("file"));
                    //lPeptide.getParentSpectrum().setZippedBytes(rs.getBytes("Spectrum"));
                    protIdToProt.get(rs.getInt("ProteinID")).addPeptide(lPeptide);
                }
                rs.close();
                stat.close();
            }
        } catch (SQLException ex) {
            logger.error(ex);
        }
    }

    @Override
    public int returnNumberOfPeptides(MsfFile msfFile) {
        int numberOfPeptides = 0;
        try {
            Statement stat = msfFile.getConnection().createStatement();
            ResultSet rs = stat.executeQuery("select count(peptideid) from Peptides");
            rs.next();
            numberOfPeptides = rs.getInt(1);
            rs.close();
            stat.close();
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return numberOfPeptides;
    }
}