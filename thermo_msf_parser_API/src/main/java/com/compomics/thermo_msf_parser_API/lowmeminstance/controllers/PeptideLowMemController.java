package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.enums.MsfVersion;
import com.compomics.thermo_msf_parser_API.highmeminstance.ProcessingNode;
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
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class PeptideLowMemController extends Observable implements PeptideInterface {
/**
 * logger instance
 */
    private static final Logger logger = Logger.getLogger(PeptideLowMemController.class);
    private final ScoreTypeLowMemController scoreTypeInstance = new ScoreTypeLowMemController();
    private int counter = 0;

    //TODO: finish this only get peptides for a specific confidencelevel and implement this in such a way that getpeptideswithconfidencelevel can use this
    /**
     * <p>getPeptidesForProteinAtConfidenceLevel.</p>
     *
     * @param protein a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem} object.
     * @param msfFile a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile} object.
     * @param confidenceLevel a int.
     * @return a {@link java.util.List} object.
     */
    public List<PeptideLowMem> getPeptidesForProteinAtConfidenceLevel(ProteinLowMem protein,MsfFile msfFile,int confidenceLevel){
        List<PeptideLowMem> foundPeptides = new ArrayList<PeptideLowMem>();
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    /** {@inheritDoc} */
    @Override
    public List<PeptideLowMem> getPeptidesForProtein(ProteinLowMem protein, MsfFile msfFile) {
        counter = 0;
        List<PeptideLowMem> foundPeptides = new ArrayList<PeptideLowMem>();
        try {
            Statement stat = null;
            try {
                stat = msfFile.getConnection().createStatement();
                if (msfFile.getVersion() == MsfVersion.VERSION1_2) {
                    ResultSet rs = stat.executeQuery("select peptides.PeptideID as PeptideID,ConfidenceLevel,Sequence,TotalIonsCount,MatchedIonsCount,Annotation,ProcessingNodeNumber, s.*, FileID as file from spectrumheaders as s, masspeaks, Peptides,Spectra as sp,(select PeptideID as ID from PeptidesProteins where ProteinID =" + protein.getProteinID() + ") as pepid where pepid.ID = peptides.PeptideID and masspeaks.masspeakid = s.masspeakid and s.SpectrumID = peptides.SpectrumID and sp.UniqueSpectrumID = s.UniqueSpectrumID order by peptides.PeptideID");
                    while (rs.next()) {
                        PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), msfFile.getAminoAcids());
                        //iScoreType.addScoresToPeptide(lPeptide,protein.getConnection());
                        lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID")));
                        //TODO make initializer with fileID argument)
                        lPeptide.getParentSpectrum().setFileId(rs.getInt("file"));
                        //lPeptide.getParentSpectrum().setZippedSpectrumXML(rs.getBytes("Spectra"));
                        //TODO check speed of this
                        Statement customStat = msfFile.getConnection().createStatement();
                        ResultSet customRs = customStat.executeQuery("select * from CustomDataPeptides where PeptideID = " + lPeptide.getPeptideId());
                        while (customRs.next()) {
                            lPeptide.addCustomDataField(customRs.getInt("FieldID"), customRs.getString("FieldValue"));
                        }
                        customRs.close();
                        customStat.close();
                        foundPeptides.add(lPeptide);
                        counter++;
                    }
                    rs.close();
                } else {
                    ResultSet rs = stat.executeQuery("select peptides.PeptideID as peptideid,ConfidenceLevel,Sequence,TotalIonsCount,MatchedIonsCount,Annotation,MissedCleavages,UniquePeptideSequenceID,ProcessingNodeNumber, s.*,FileID as file from spectrumheaders as s, masspeaks, Peptides ,Spectra as sp,(select PeptideID as ID from PeptidesProteins where ProteinID =" + protein.getProteinID() + ") as pepid where pepid.ID = peptides.PeptideID and masspeaks.masspeakid = s.masspeakid and s.SpectrumID = peptides.SpectrumID and sp.UniqueSpectrumID = s.UniqueSpectrumID order by peptides.PeptideID");
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
                        counter++;
                    }
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
        return foundPeptides;
    }

    /** {@inheritDoc} */
    @Override
    public List<PeptideLowMem> getPeptidesForAccession(String lProteinAccession, MsfFile msfFile) {
        int lProteinID = 0;
        String lSequence = null;
        try {
            Statement stat = null;
            try {
                stat = msfFile.getConnection().createStatement();
                ResultSet rs = null;
                try {
                    rs = stat.executeQuery("select protanno.proteinID,Proteins.Sequence from Proteins, (select ProteinID from ProteinAnnotations where ProteinAnnotations.Description like ('%" + lProteinAccession + "%')) as protanno where protanno.ProteinID = Proteins.ProteinID");
                    rs.next();
                    lProteinID = rs.getInt("protanno.proteinID");
                    lSequence = rs.getString("Proteins.Sequence");
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
        return getPeptidesForProtein(new ProteinLowMem(lSequence, lProteinID), msfFile);
    }

    //TODO finish this
    /** {@inheritDoc} */
    @Override
    public List getInformationForPeptide(int peptideID, MsfFile msfFile, boolean fullInfo) {
        List peptideInfo = new ArrayList();
        try {
            Statement stat = null;
            try {
                stat = msfFile.getConnection().createStatement();
                ResultSet rs = null;
                try {
                    if (fullInfo) {
                        rs = stat.executeQuery("select Peptides.Sequence, SpectrumHeaders.FirstScan,SpectrumHeaders.LastScan,Peptides.Annotations,ProcessingNodes.FriendlyName from Peptides where ConfidenceLevel >= 1 ");
                    } else {
                        rs = stat.executeQuery("");
                    }
                    while (rs.next()) {
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

        return peptideInfo;
    }

    /** {@inheritDoc} */
    @Override
    public List<PeptideLowMem> getPeptidesWithConfidenceLevel(int confidenceLevel, MsfFile msfFile) {
        List<PeptideLowMem> confidenceLevelPeptides = new ArrayList<PeptideLowMem>();
        counter = 0;
        int internalcounter = 0;
        try {
            Statement stat = null;
            try {
                stat = msfFile.getConnection().createStatement();
                ResultSet rs = null;
                try {
                    if (msfFile.getVersion() == MsfVersion.VERSION1_2) {
                        rs = stat.executeQuery("select Peptides.PeptideID,ConfidenceLevel,Sequence,TotalIonsCount,MatchedIonsCount,Annotation,ProcessingNodeNumber, s.*, FileID as file, sp.Spectrum from spectrumheaders as s, masspeaks as m, Peptides, Spectra as sp where m.masspeakid = s.masspeakid and s.SpectrumID = Peptides.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and Peptides.ConfidenceLevel = " + confidenceLevel);
                        while (rs.next()) {
                            PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), msfFile.getAminoAcids());
                            lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID")));
                            lPeptide.getParentSpectrum().setFileId(rs.getInt("file"));
                            lPeptide.getParentSpectrum().setZippedSpectrumXML(rs.getBytes("Spectrum"));
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
                    } else {
                        rs = stat.executeQuery("select Peptides.PeptideID,ConfidenceLevel,Sequence,TotalIonsCount,MatchedIonsCount,Annotation,ProcessingNodeNumber,MissedCleavages,UniquePeptideSequenceID, s.*, FileID as file,sp.Spectrum from spectrumheaders as s, masspeaks as m, Peptides,Spectra as sp where m.masspeakid = s.masspeakid and s.SpectrumID = Peptides.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and Peptides.ConfidenceLevel = " + confidenceLevel);
                        while (rs.next()) {
                            PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), msfFile.getAminoAcids());
                            lPeptide.setMissedCleavage(rs.getInt("MissedCleavages"));
                            lPeptide.setUniquePeptideSequenceId(rs.getInt("UniquePeptideSequenceID"));
                            lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID")));
                            //TODO make initializer with fileID argument
                            lPeptide.getParentSpectrum().setFileId(rs.getInt("file"));
                            lPeptide.getParentSpectrum().setZippedSpectrumXML(rs.getBytes("Spectrum"));
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
                        scoreTypeInstance.getScoresForPeptideList(confidenceLevelPeptides, msfFile);
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
        return confidenceLevelPeptides;
    }

    /** {@inheritDoc} */
    @Override
    public int getNumberOfPeptidesForConfidenceLevel(int confidenceLevel, MsfFile msfFile) {
        int numberOfPeptides = 0;
        counter = 0;
        try {
            Statement stat = null;
            try {
                stat = msfFile.getConnection().createStatement();
                ResultSet rs = null;
                try {
                    rs = stat.executeQuery("select count(PeptideID) from Peptides where ConfidenceLevel = " + confidenceLevel);
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
        counter = numberOfPeptides;
        return numberOfPeptides;
    }

    /**
     * <p>getNumberOfPeptidesProcessed.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getNumberOfPeptidesProcessed() {
        return counter;
    }

    /** {@inheritDoc} */
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
            PreparedStatement stat = null;
            try {
                if (msfFile.getVersion() == MsfVersion.VERSION1_2) {
                    stat = msfFile.getConnection().prepareStatement("select Peptides.PeptideID as PeptideID,ConfidenceLevel,Sequence,TotalIonsCount,MatchedIonsCount,Annotation,ProcessingNodeNumber, s.*, FileID as file,pp.ProteinID from spectrumheaders as s, masspeaks, Peptides,Spectra as sp, (select PeptideID,ProteinID from peptidesProteins where PeptidesProteins.ProteinID in (" + listOfProteinIds + ")) as pp where masspeaks.masspeakid = s.masspeakid and s.SpectrumID = Peptides.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and Peptides.PeptideID = pp.PeptideID and Peptides.ConfidenceLevel = " + confidenceLevel);
                    ResultSet rs = null;
                    try {
                        rs = stat.executeQuery();
                        while (rs.next()) {
                            PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), msfFile.getAminoAcids());
                            lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID")));
                            lPeptide.getParentSpectrum().setFileId(rs.getInt("file"));
                            //lPeptide.getParentSpectrum().setZippedSpectrumXML(rs.getBytes("Spectrum"));
                            protIdToProt.get(rs.getInt("pp.ProteinID")).addPeptide(lPeptide);
                        }
                    } finally {
                        if (rs != null) {
                            rs.close();
                        }
                    }
                } else {
                    stat = msfFile.getConnection().prepareStatement("select Peptides.PeptideID as peptideID,ConfidenceLevel,Sequence,TotalIonsCount,MatchedIonsCount,Annotation,ProcessingNodeNumber,MissedCleavages,UniquePeptideSequenceID, s.*, FileID as file,pp.ProteinID from spectrumheaders as s, masspeaks, Peptides,Spectra as sp, (select PeptideID,ProteinID from peptidesProteins where PeptidesProteins.ProteinID in (" + listOfProteinIds + ")) as pp where masspeaks.masspeakid = s.masspeakid and s.SpectrumID = Peptides.SpectrumID and s.UniqueSpectrumID = sp.UniqueSpectrumID and Peptides.PeptideID = pp.PeptideID and Peptides.ConfidenceLevel = ?");
                    stat.setInt(1, confidenceLevel);
                    ResultSet rs = stat.executeQuery();
                    try {
                        while (rs.next()) {
                            PeptideLowMem lPeptide = new PeptideLowMem(rs.getInt("peptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), msfFile.getAminoAcids());
                            lPeptide.setMissedCleavage(rs.getInt("MissedCleavages"));
                            lPeptide.setUniquePeptideSequenceId(rs.getInt("UniquePeptideSequenceID"));
                            lPeptide.setParentSpectrum(new SpectrumLowMem(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID")));
                            lPeptide.getParentSpectrum().setFileId(rs.getInt("file"));
                            //lPeptide.getParentSpectrum().setZippedSpectrumXML(rs.getBytes("Spectrum"));
                            protIdToProt.get(rs.getInt("pp.ProteinID")).addPeptide(lPeptide);
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

    /** {@inheritDoc} */
    @Override
    public int returnNumberOfPeptides(MsfFile msfFile) {
        int numberOfPeptides = 0;
        try {
            Statement stat = null;
            try {
                stat = msfFile.getConnection().createStatement();
                ResultSet rs = null;
                try {
                    rs = stat.executeQuery("select count(peptideid) from Peptides");
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
     * <p>getPeptidesForProteinAndProcessingNode.</p>
     *
     * @param protein a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem} object.
     * @param msfFile a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile} object.
     * @param processingNode a {@link com.compomics.thermo_msf_parser_API.highmeminstance.ProcessingNode} object.
     * @return a {@link java.util.List} object.
     */
    public List<PeptideLowMem> getPeptidesForProteinAndProcessingNode(ProteinLowMem protein, MsfFile msfFile, ProcessingNode processingNode) {
        processingNode.getProcessingNodeId();
        throw new UnsupportedOperationException("not yet implemented");
    }
}
