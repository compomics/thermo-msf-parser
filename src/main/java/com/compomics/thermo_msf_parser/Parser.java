package com.compomics.thermo_msf_parser;

import com.compomics.thermo_msf_parser.msf.*;

import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 18-Feb-2011
 * Time: 09:12:53
 */
public class Parser {

    /**
     * The modifications
     */
    private Vector<Modification> iModifications = new Vector<Modification>();
    /**
     * A map with the modificationid as key and the modification as value
     */
    private HashMap<Integer, Modification> iModificationsMap = new HashMap<Integer, Modification>();
    /**
     * The amino acids
     */
    private Vector<AminoAcid> iAminoAcids = new Vector<AminoAcid>();
    /**
     * A map with the aminoacidid as key and the aminoacid as value
     */
    private HashMap<Integer, AminoAcid> iAminoAcidsMap = new HashMap<Integer, AminoAcid>();
    /**
     * The enzymes
     */
    private Vector<Enzyme> iEnzymes = new Vector<Enzyme>();
    /**
     * A map with the enzymeid as key and the enzyme as value
     */
    private HashMap<Integer, Enzyme> iEnzymesMap = new HashMap<Integer, Enzyme>();
    /**
     * The peptides
     */
    private Vector<Peptide> iPeptides = new Vector<Peptide>();
    /**
     * A map with the peptideid as key and the peptide as value
     */
    private HashMap<Integer, Peptide> iPeptidesMap = new HashMap<Integer, Peptide>();
    /**
     * The peptides decoy
     */
    private Vector<Peptide> iPeptidesDecoy = new Vector<Peptide>();
    /**
     * A map with the peptideid as key and the peptidedecoy as value
     */
    private HashMap<Integer, Peptide> iPeptidesDecoyMap = new HashMap<Integer, Peptide>();
    /**
     * The taxonomies
     */
    private Vector<Taxonomy> iTaxonomies = new Vector<Taxonomy>();
    /**
     * A map with the taxonomyid as key and the taxonomy as value
     */
    private HashMap<Integer, Taxonomy> iTaxonomiesMap = new HashMap<Integer, Taxonomy>();
    /**
     * The spectra
     */
    private Vector<Spectrum> iSpectra = new Vector<Spectrum>();
    /**
     * A map with the spectrumid as key and the spectrum as value
     */
    private HashMap<Integer, Spectrum> iSpectraMapBySpectrumId = new HashMap<Integer, Spectrum>();
    /**
     * A map with the uniquespectrumid as key and the spectrum as value
     */
    private HashMap<Integer, Spectrum> iSpectraMapByUniqueSpectrumId = new HashMap<Integer, Spectrum>();
    /**
     * A map with the masspeakid as key and the spectrum as value
     */
    private HashMap<Integer, Spectrum> iSpectraMapByMassPeakId = new HashMap<Integer, Spectrum>();
    /**
     * The quantification method xml
     */
    private String iQuantificationMethod;
    /**
     * The quantification components
     */
    private Vector<String> iComponents = new Vector<String>();
    /**
     * The channel ids of the quantification components
     */
    private Vector<Integer> iChannelIds = new Vector<Integer>();
    /**
     * The ratio types
     */
    private Vector<RatioType> iRatioTypes = new Vector<RatioType>();
    /**
     * The scan events
     */
    private Vector<ScanEvent> iScanEvents = new Vector<ScanEvent>();
    /**
     * The score types
     */
    private Vector<ScoreType> iScoreTypes = new Vector<ScoreType>();
    /**
     * The proteins
     */
    private Vector<Protein> iProteins = new Vector<Protein>();
    /**
     * A map with the proteinid as key and the protein as value
     */
    private HashMap<Integer, Protein> iProteinsMap = new HashMap<Integer, Protein>();
    /**
     * The custom data fields
     */
    private Vector<CustomDataField> iCustomDataFields = new Vector<CustomDataField>();
    /**
     * A map with the fieldid as key and the custom data field as value
     */
    private HashMap<Integer,CustomDataField> iCustomDataFieldsMap = new HashMap<Integer,CustomDataField>();
    /**
     * This holds the field ids used for the peptides
     */
    private Vector<CustomDataField> iPeptideUsedCustomDataFields = new Vector<CustomDataField>();
    /**
     * This holds the field ids used for the proteins
     */
    private Vector<CustomDataField> iProteinUsedCustomDataFields = new Vector<CustomDataField>();
    /**
     * This holds the field ids used for the spectra
     */
    private Vector<CustomDataField> iSpectrumUsedCustomDataFields = new Vector<CustomDataField>();
    /**
     * The workflow info
     */
    private WorkflowInfo iWorkFlowInfo;
    /**
     * The filters
     */
    private Vector<Filter> iFilter = new Vector<Filter>();
    /**
     * A map with the isotope pattern id as key and the isotopepattern as value
     */
    private HashMap<Integer,IsotopePattern> iIsotopePatternMap = new HashMap<Integer,IsotopePattern>();
    /**
     * The quan results
     */
    private Vector<QuanResult> iQuanResults = new Vector<QuanResult>();
    /**
     * A map with the quanresult id as key and the quanresult as value
     */
    private HashMap<Integer,QuanResult> iQuanResultsMap = new HashMap<Integer,QuanResult>();
    /**
     * The raw files
     */
    private Vector<RawFile> iRawFiles = new Vector<RawFile>();
    /**
     * The connection to the thermo msf file
     */
    private Connection iConnection;
    /**
     * The chromatograms
     */
    private Vector<Chromatogram> iChromatograms = new Vector<Chromatogram>();
    /**
     * The processing nodes
     */
    private Vector<ProcessingNode> iProcessingNodes = new Vector<ProcessingNode>();
    /**
     * A map with the processingnode id as key and the processingnode as value
     */
    private HashMap<Integer, ProcessingNode> iProcessingNodesMap = new HashMap<Integer,ProcessingNode>();
    /**
     * A boolean that indicates if we need to use a low memory footprint
     */
    private boolean iLowMemory;
    /**
     * The file location of this msf file
     */
    private String iFilePath;

    /**
     * This will parse the thermo msf file
     * @param iMsfFileLocation A String with the location of the msf file
     * @param iLowMemory
     * @throws ClassNotFoundException This is thrown when the sqlite library cannot be found
     * @throws java.sql.SQLException This is thrown when there is a problem extracting the data from the thermo msf file
     */
    public Parser(String iMsfFileLocation, boolean iLowMemory) throws SQLException, ClassNotFoundException {

        iFilePath = iMsfFileLocation;
        //create the connection to the msf file
        Class.forName("org.sqlite.JDBC");
        iConnection = DriverManager.getConnection("jdbc:sqlite:" + iMsfFileLocation);
        Statement stat = iConnection.createStatement();
        ResultSet rs;
        this.iLowMemory = iLowMemory;


        //get all the aminoacids
        rs = stat.executeQuery("select * from AminoAcids");
        while (rs.next()) {
            AminoAcid lAA = new AminoAcid(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getString(7));
            iAminoAcids.add(lAA);
            iAminoAcidsMap.put(rs.getInt(1), lAA);
        }
        //get all the modifications
        rs = stat.executeQuery("select * from AminoAcidModifications");
        while (rs.next()) {
            Modification lMod = new Modification(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getDouble(9), rs.getInt(10), rs.getInt(11));
            iModifications.add(lMod);
            iModificationsMap.put(rs.getInt(1), lMod);
        }
        //add the amino acid to the modifications
        rs = stat.executeQuery("select * from AminoAcidModificationsAminoAcids");
        while (rs.next()) {
            int lModId = rs.getInt("AminoAcidModificationID");
            int lAaId = rs.getInt("AminoAcidID");
            if(iAminoAcidsMap.get(lAaId) != null && iModificationsMap.get(lModId) != null){
                iModificationsMap.get(lModId).addAminoAcid(iAminoAcidsMap.get(lAaId));
            }
        }

        //get the enzymes
        rs = stat.executeQuery("select * from Enzymes");
        while (rs.next()) {
            Enzyme lEnzyme = new Enzyme(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
            iEnzymes.add(lEnzyme);
            iEnzymesMap.put(rs.getInt("EnzymeID"), lEnzyme);
        }
        //add the specificity to the enzymes
        rs = stat.executeQuery("select * from EnzymesCleavageSpecificities");
        while (rs.next()) {
            int lEnzymeId = rs.getInt("EnzymeID");
            if(iEnzymesMap.get(lEnzymeId) != null){
                iEnzymesMap.get(lEnzymeId).setSpecificity(rs.getInt("Specificity"));
            }
        }

        //get the peptides
        rs = stat.executeQuery("select * from Peptides as p");
        while (rs.next()) {
            Peptide lPeptide = new Peptide(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), iAminoAcids);
            iPeptides.add(lPeptide);
            iPeptidesMap.put(lPeptide.getPeptideId(), lPeptide);
        }

        //get the peptides
        rs = stat.executeQuery("select * from Peptides_decoy as p");
        while (rs.next()) {
            Peptide lPeptide = new Peptide(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), "", rs.getInt("ProcessingNodeNumber"), iAminoAcids);
            iPeptidesDecoy.add(lPeptide);
            iPeptidesDecoyMap.put(lPeptide.getPeptideId(), lPeptide);
        }
        //get the score types
        rs = stat.executeQuery("select * from ProcessingNodeScores");
        while (rs.next()) {
            ScoreType lScoreType = new ScoreType(rs.getInt("ScoreID"), rs.getString("ScoreName"), rs.getString("FriendlyName"), rs.getString("Description"), rs.getInt("ScoreCategory"), rs.getInt("IsMainScore"));
            iScoreTypes.add(lScoreType);
        }
        //add the score to the peptides
        rs = stat.executeQuery("select  * from  PeptideScores ");
        while (rs.next()) {
            if(iPeptidesMap.get(rs.getInt("PeptideID")) != null){
                int lScoreId = rs.getInt("ScoreID");
                iPeptidesMap.get(rs.getInt("PeptideID")).setScore(rs.getDouble("ScoreValue"), lScoreId, iScoreTypes);
            }
        }
        //add the score to the peptides decoy
        rs = stat.executeQuery("select  * from  PeptideScores_decoy ");
        while (rs.next()) {
            if(iPeptidesDecoyMap.get(rs.getInt("PeptideID")) != null){
                int lScoreId = rs.getInt("ScoreID");
                iPeptidesDecoyMap.get(rs.getInt("PeptideID")).setScore(rs.getDouble("ScoreValue"), lScoreId, iScoreTypes);
            }
        }
        //add the modifications to the peptide
        rs = stat.executeQuery("select  * from  PeptidesTerminalModifications");
        while (rs.next()) {
            if(iPeptidesMap.get(rs.getInt("PeptideID")) != null){
                if(iModificationsMap.get(rs.getInt("TerminalModificationID")) != null){
                    Modification lMod = iModificationsMap.get(rs.getInt("TerminalModificationID"));
                    boolean lNterm = false;
                    if(lMod.getPositionType() == 1){
                        lNterm = true;
                    }
                    ModificationPosition lModPos = new ModificationPosition(0, lNterm, !lNterm);
                    iPeptidesMap.get(rs.getInt("PeptideID")).addModification(lMod, lModPos);
                }
            }
        }
        rs = stat.executeQuery("select  * from  PeptidesAminoAcidModifications");
        while (rs.next()) {
            if(iPeptidesMap.get(rs.getInt("PeptideID")) != null){
                if(iModificationsMap.get(rs.getInt("AminoAcidModificationID")) != null){
                    Modification lMod = iModificationsMap.get(rs.getInt("AminoAcidModificationID"));
                    ModificationPosition lModPos = new ModificationPosition(rs.getInt("Position"), false, false);
                    iPeptidesMap.get(rs.getInt("PeptideID")).addModification(lMod, lModPos);
                }
            }
        }

        //add the modifications to the peptide
        rs = stat.executeQuery("select  * from  PeptidesTerminalModifications_decoy");
        while (rs.next()) {
            if(iPeptidesDecoyMap.get(rs.getInt("PeptideID")) != null){
                if(iModificationsMap.get(rs.getInt("TerminalModificationID")) != null){
                    Modification lMod = iModificationsMap.get(rs.getInt("TerminalModificationID"));
                    boolean lNterm = false;
                    if(lMod.getPositionType() == 1){
                        lNterm = true;
                    }
                    ModificationPosition lModPos = new ModificationPosition(0, lNterm, !lNterm);
                    iPeptidesDecoyMap.get(rs.getInt("PeptideID")).addModification(lMod, lModPos);
                }
            }
        }
        rs = stat.executeQuery("select  * from  PeptidesAminoAcidModifications_decoy");
        while (rs.next()) {
            if(iPeptidesDecoyMap.get(rs.getInt("PeptideID")) != null){
                if(iModificationsMap.get(rs.getInt("AminoAcidModificationID")) != null){
                    Modification lMod = iModificationsMap.get(rs.getInt("AminoAcidModificationID"));
                    ModificationPosition lModPos = new ModificationPosition(rs.getInt("Position"), false, false);
                    iPeptidesDecoyMap.get(rs.getInt("PeptideID")).addModification(lMod, lModPos);
                }
            }
        }

        //get the taxonomies
        rs = stat.executeQuery("select * from TaxonomyNodes");
        while (rs.next()) {
            Taxonomy lTaxonomy = new Taxonomy(rs.getInt("TaxonomyID"), rs.getInt("ParentTaxonomyID"), rs.getInt("TaxonomyRank"), rs.getInt("LeftNodeIndex"), rs.getInt("RightNodeIndex"));
            iTaxonomies.add(lTaxonomy);
            iTaxonomiesMap.put(lTaxonomy.getTaxonomyId(), lTaxonomy);
        }
        //add the name to the taxonomy
        rs = stat.executeQuery("select  * from  TaxonomyNames ");
        while (rs.next()) {
            if(iTaxonomiesMap.get(rs.getInt("TaxonomyID")) != null){
                iTaxonomiesMap.get(rs.getInt("TaxonomyID")).setName(rs.getString("Name"));
                iTaxonomiesMap.get(rs.getInt("TaxonomyID")).setNameCategory(rs.getInt("NameCategory"));
            }
        }

        //get the spectra
        rs = stat.executeQuery("select s.*, m.FileID from spectrumheaders as s, masspeaks as m where m.masspeakid = s.masspeakid");
        while (rs.next()) {
            Spectrum lSpectrum = new Spectrum(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), iConnection, this);
            lSpectrum.setFileId(rs.getInt("FileID"));
            iSpectra.add(lSpectrum);
            iSpectraMapByMassPeakId.put(lSpectrum.getMassPeakId(), lSpectrum);
            iSpectraMapBySpectrumId.put(lSpectrum.getSpectrumId(), lSpectrum);
            iSpectraMapByUniqueSpectrumId.put(lSpectrum.getUniqueSpectrumId(), lSpectrum);
        }

        if(!iLowMemory){
            rs = stat.executeQuery("select * from Spectra");
            while (rs.next()) {
                int lSpectrumId =  rs.getInt("UniqueSpectrumID");
                byte[] lZipped = rs.getBytes("Spectrum");
                if(iSpectraMapByUniqueSpectrumId.get(lSpectrumId) != null){
                    iSpectraMapByUniqueSpectrumId.get(lSpectrumId).setZippedBytes(lZipped);
                }
            }
        }
        rs = stat.executeQuery("select * from SpectrumScores");
        while (rs.next()) {
            int lSpectrumId =  rs.getInt("SpectrumID");
            if(iSpectraMapBySpectrumId.get(lSpectrumId) != null){
                iSpectraMapBySpectrumId.get(lSpectrumId).addSpectrumScore(rs.getDouble("Score"), rs.getInt("ProcessingNodeNumber"));
            }
        }
        //get the scan events
        rs = stat.executeQuery("select * from ScanEvents");
        while (rs.next()) {
            ScanEvent lScanEvent = new ScanEvent(rs.getInt("ScanEventID"), rs.getInt("MSLevel"), rs.getInt("Polarity"), rs.getInt("ScanType"), rs.getInt("Ionization"), rs.getInt("MassAnalyzer"), rs.getInt("ActivationType"));
            iScanEvents.add(lScanEvent);
        }
        for(int i = 0; i<iSpectra.size(); i ++){
            for(int j = 0; j<iScanEvents.size(); j ++){
                if(iSpectra.get(i).getScanEventId() == iScanEvents.get(j).getScanEventId()){
                    iSpectra.get(j).setScanEvent(iScanEvents.get(j));
                }
            }
        }

        //get the proteins
        rs = stat.executeQuery("select * from Proteins ");
        while (rs.next()) {
            Protein lProtein = null;
            if(iLowMemory){
                lProtein = new Protein(rs.getInt("ProteinID"), this);
            } else {
                lProtein = new Protein(rs.getInt("ProteinID"), rs.getString("Sequence"));
            }
            iProteins.add(lProtein);
            iProteinsMap.put(lProtein.getProteinId(), lProtein);
        }
        //add the description to the protein
        rs = stat.executeQuery("select  * from  ProteinAnnotations ");
        while (rs.next()) {
            if(iProteinsMap.get(rs.getInt("ProteinID")) != null){
                iProteinsMap.get(rs.getInt("ProteinID")).setDescription(rs.getString("Description"));
            }
        }
        //add the score to the protein
        rs = stat.executeQuery("select  * from  ProteinScores ");
        while (rs.next()) {
            if(iProteinsMap.get(rs.getInt("ProteinID")) != null){
                iProteinsMap.get(rs.getInt("ProteinID")).setScore(rs.getDouble("ProteinScore"));
            }
        }


        //get the custom data fields
        rs = stat.executeQuery("select  * from  CustomDataFields ");
        while (rs.next()) {
            CustomDataField lField = new CustomDataField(rs.getInt("FieldID"), rs.getString("DisplayName"));
            iCustomDataFields.add(lField);
            iCustomDataFieldsMap.put(lField.getFieldId(), lField);
        }
        //add the custom data fields to the peptides
        rs = stat.executeQuery("select  * from  CustomDataPeptides ");
        while (rs.next()) {
            if(iPeptidesMap.get(rs.getInt("PeptideID")) != null){
                iPeptidesMap.get(rs.getInt("PeptideID")).addCustomDataField(rs.getInt("FieldID"),rs.getString("FieldValue"));
                
            }
        }
        //add the custom data fields to the peptides
        /*rs = stat.executeQuery("select  * from  CustomDataPeptides ");
        while (rs.next()) {
            if(iPeptidesDecoyMap.get(rs.getInt("PeptideID")) != null){
                iPeptidesDecoyMap.get(rs.getInt("PeptideID")).addCustomDataField(rs.getInt("FieldID"),rs.getString("FieldValue"));

            }
        } */
        rs = stat.executeQuery("select fieldid from CustomDataPeptides group by fieldid");
        while (rs.next()) {
            iPeptideUsedCustomDataFields.add(iCustomDataFieldsMap.get(rs.getInt("FieldID")));
        }
        //add the custom data fields to the proteins
        rs = stat.executeQuery("select  * from  CustomDataProteins ");
        while (rs.next()) {
            if(iProteinsMap.get(rs.getInt("ProteinID")) != null){
                iProteinsMap.get(rs.getInt("ProteinID")).addCustomDataField(rs.getInt("FieldID"),rs.getString("FieldValue"));
            }
        }
        rs = stat.executeQuery("select fieldid from CustomDataProteins group by fieldid");
        while (rs.next()) {
            iProteinUsedCustomDataFields.add(iCustomDataFieldsMap.get(rs.getInt("FieldID")));
        }
        //add the custom data fields to the proteins
        rs = stat.executeQuery("select  * from  CustomDataSpectra ");
        while (rs.next()) {
            if(iSpectraMapBySpectrumId.get(rs.getInt("SpectrumID")) != null){
                iSpectraMapBySpectrumId.get(rs.getInt("SpectrumID")).addCustomDataField(rs.getInt("FieldID"),rs.getString("FieldValue"));
            }
        }
        rs = stat.executeQuery("select fieldid from CustomDataSpectra group by fieldid");
        while (rs.next()) {
            iSpectrumUsedCustomDataFields.add(iCustomDataFieldsMap.get(rs.getInt("FieldID")));
        }
        

        //get the quantification method
        rs = stat.executeQuery("select ParameterValue from ProcessingNodeParameters where ParameterName = 'QuantificationMethod'");
        while (rs.next()) {
            iQuantificationMethod =  rs.getString(1);
            parseQuantitationXml();
        }

        //get the workflow info
        rs = stat.executeQuery("select * from WorkflowInfo");
        while (rs.next()) {
            iWorkFlowInfo = new WorkflowInfo(rs.getString("WorkflowName"), rs.getString("WorkflowDescription"), rs.getString("User"), rs.getString("WorkflowTemplate"), rs.getString("MachineName"));
        }
        //add the messages to the workflow info
        rs = stat.executeQuery("select * from WorkflowMessages");
        while (rs.next()) {
            iWorkFlowInfo.addMessage(new WorkflowMessage(rs.getInt("MessageID"), rs.getInt("ProcessingNodeID"), rs.getInt("ProcessingNodeNumber"), rs.getInt("Time"), rs.getInt("MessageKind"), rs.getString("Message")));
        }
        //add the msf version info the the workflowinfo
        rs = stat.executeQuery("select * from SchemaInfo");
        while (rs.next()) {
            iWorkFlowInfo.setMsfVersionInfo(new MsfVersionInfo(rs.getInt("Version"), rs.getString("SoftwareVersion")));
        }

        //get the processing nodes
        rs = stat.executeQuery("select * from ProcessingNodes");
        while (rs.next()) {
            ProcessingNode lNode = new ProcessingNode(rs.getInt("ProcessingNodeNumber"), rs.getInt("ProcessingNodeID"), rs.getString("ProcessingNodeParentNumber"), rs.getString("NodeName"), rs.getString("FriendlyName"), rs.getInt("MajorVersion"), rs.getInt("MinorVersion"), rs.getString("NodeComment"));
            iProcessingNodes.add(lNode);
            iProcessingNodesMap.put(lNode.getProcessingNodeNumber(),lNode);
        }
        //add the processing node parameters to the processing node
        rs = stat.executeQuery("select * from ProcessingNodeParameters");
        while (rs.next()) {
            ProcessingNodeParameter lNodeParameter = new ProcessingNodeParameter(rs.getInt("ProcessingNodeNumber"), rs.getInt("ProcessingNodeId"), rs.getString("ParameterName"), rs.getString("FriendlyName"), rs.getInt("IntendedPurpose"), rs.getString("PurposeDetails"), rs.getInt("Advanced"), rs.getString("Category"), rs.getInt("Position"), rs.getString("ParameterValue"), rs.getString("ValueDisplayString"));

            if(iProcessingNodesMap.get(lNodeParameter.getProcessingNodeNumber()) != null){
                iProcessingNodesMap.get(lNodeParameter.getProcessingNodeNumber()).addProcessingNodeParameter(lNodeParameter);
            }
        }
        //get the filters
        rs = stat.executeQuery("select * from ResultFilterSet");
        while (rs.next()) {
            String lXml = rs.getString("ResultFilterSet");
            String[] lXmlLines = lXml.split("\n");
            String lFilterSetName = lXml.substring(lXml.indexOf("\"", lXml.indexOf("FilterSetName")) + 1,lXml.indexOf("\"", lXml.indexOf("\"", lXml.indexOf("FilterSetName")) + 1));
            Vector<String> lFilterLines = new Vector<String>();
            for(int i = 0; i<lXmlLines.length; i ++){
                String lLine = lXmlLines[i].trim();
                if(lLine.startsWith("<FilterInfo ")){
                    lFilterLines.add(lLine);
                }
                if(lLine.startsWith("<Parameter ")){
                    lFilterLines.add(lLine);
                }
                if(lLine.startsWith("</FilterInfo>")){
                    iFilter.add(new Filter(lFilterSetName,lFilterLines));
                    lFilterLines.removeAllElements();
                }
            }
        }

        //get the Event annotations
        rs = stat.executeQuery("select * from EventAnnotations");
        while (rs.next()) {
            int lIsotopePatternId = rs.getInt("IsotopePatternID");
            EventAnnotation lEventAnno = new EventAnnotation(rs.getInt("EventID"), rs.getInt("IsotopePatternID"), rs.getInt("QuanResultID"), rs.getInt("QuanChannelID"));
            if(iIsotopePatternMap.get(lIsotopePatternId) == null){
                IsotopePattern lIso = new IsotopePattern(lIsotopePatternId);
                lIso.addEventAnnotation(lEventAnno);
                iIsotopePatternMap.put(lIsotopePatternId, lIso);
            } else {
                iIsotopePatternMap.get(lIsotopePatternId).addEventAnnotation(lEventAnno);
            }
        }
        rs = stat.executeQuery("select * from EventAreaAnnotations");
        while (rs.next()) {
            int lIsotopePatternId = rs.getInt("IsotopePatternID");
            EventAnnotation lEventAnno = new EventAnnotation(rs.getInt("EventID"), rs.getInt("IsotopePatternID"), rs.getInt("QuanResultID"), -1);
            if(iIsotopePatternMap.get(lIsotopePatternId) == null){
                IsotopePattern lIso = new IsotopePattern(lIsotopePatternId);
                lIso.addEventAnnotation(lEventAnno);
                iIsotopePatternMap.put(lIsotopePatternId, lIso);
            } else {
                iIsotopePatternMap.get(lIsotopePatternId).addEventAnnotation(lEventAnno);
            }
        }


        //get the quan results
        rs = stat.executeQuery("select * from PrecursorIonQuanResults");
        while (rs.next()) {
            int lQuantId = rs.getInt("QuanResultID");
            if(iQuanResultsMap.get(lQuantId) != null){
                iQuanResultsMap.get(lQuantId).addQuanValues( rs.getInt("QuanChannelID"), rs.getDouble("Mass"), rs.getInt("Charge"), rs.getDouble("Area"), rs.getDouble("RetentionTime"));
            } else {
                QuanResult  lQuant = new QuanResult(rs.getInt("QuanResultID"));
                lQuant.addQuanValues( rs.getInt("QuanChannelID"), rs.getDouble("Mass"), rs.getInt("Charge"), rs.getDouble("Area"), rs.getDouble("RetentionTime"));
                iQuanResults.add(lQuant);
                iQuanResultsMap.put(lQuant.getQuanResultId(), lQuant);
            }
        }
        //add the spectrumid
        rs = stat.executeQuery("select * from PrecursorIonAreaSearchSpectra");
        while (rs.next()) {
            int lQuantId = rs.getInt("QuanResultID");
            if(iQuanResultsMap.get(lQuantId) != null){
                iQuanResultsMap.get(lQuantId).addSpectrumId(rs.getInt("SearchSpectrumID"));
                iQuanResultsMap.get(lQuantId).addProcessingNodeNumber(-1);
            } else {
                QuanResult  lQuant = new QuanResult(rs.getInt("QuanResultID"));
                lQuant.addSpectrumId(rs.getInt("SearchSpectrumID"));
                lQuant.addProcessingNodeNumber(-1);
                iQuanResults.add(lQuant);
                iQuanResultsMap.put(lQuant.getQuanResultId(), lQuant);
            }
        }
        //set the spectrumid and the processing node number
        rs = stat.executeQuery("select * from PrecursorIonQuanResultsSearchSpectra");
        while (rs.next()) {
            int lQuantId = rs.getInt("QuanResultID");
            if(iQuanResultsMap.get(lQuantId) != null){
                iQuanResultsMap.get(lQuantId).addSpectrumId(rs.getInt("SearchSpectrumID"));
                iQuanResultsMap.get(lQuantId).addProcessingNodeNumber(rs.getInt("ProcessingNodeNumber"));
            }
        }


        //add the isotope patterns to the quan results
        Collection iIsotopePatterns = iIsotopePatternMap.values();
        Iterator lIsotopePatternIterator = iIsotopePatterns.iterator();
        while(lIsotopePatternIterator.hasNext()){
            IsotopePattern lIso = (IsotopePattern) lIsotopePatternIterator.next();
            if(iQuanResultsMap.get(lIso.getSharedQuanResultId()) != null){
                iQuanResultsMap.get(lIso.getSharedQuanResultId()).addIsotopePattern(lIso);
            }
        }


        //get the rawdata names
        rs = stat.executeQuery("select FileId, FileName from FileInfos");
        while (rs.next()) {
            iRawFiles.add(new RawFile(rs.getInt(1), rs.getString(2)));
        }


        //add the proteins to the peptides
        rs = stat.executeQuery("select * from  PeptidesProteins");
        while (rs.next()) {
            if(iPeptidesMap.get(rs.getInt("PeptideID")) != null){
                if(iProteinsMap.get(rs.getInt("ProteinId")) != null){
                    iPeptidesMap.get(rs.getInt("PeptideID")).addProtein(iProteinsMap.get(rs.getInt("ProteinId")));
                }
            }
        }


        //add peptide to spectra
        for(int p = 0; p<iPeptides.size(); p ++){
            if(iSpectraMapBySpectrumId.get(iPeptides.get(p).getSpectrumId()) != null){
                iSpectraMapBySpectrumId.get(iPeptides.get(p).getSpectrumId()).addPeptide(iPeptides.get(p));
            }
        }

        //add decoy peptide to spectra
        for(int p = 0; p<iPeptidesDecoy.size(); p ++){
            if(iSpectraMapBySpectrumId.get(iPeptidesDecoy.get(p).getSpectrumId()) != null){
                iSpectraMapBySpectrumId.get(iPeptidesDecoy.get(p).getSpectrumId()).addDecoyPeptide(iPeptidesDecoy.get(p));
            }
        }


        //add ratios to spectra
        for(int i = 0; i<iQuanResults.size(); i ++){
            Vector<Integer> lSpectrumids = iQuanResults.get(i).getSpectrumIds();
            for(int j = 0; j<lSpectrumids.size(); j++){
                int lSpectrumid = lSpectrumids.get(j);
                if(iSpectraMapBySpectrumId.get(lSpectrumid) != null){
                    iSpectraMapBySpectrumId.get(lSpectrumid).setQuanResult(iQuanResults.get(i));
                }
            }
        }


        //get the chromatograms
        rs = stat.executeQuery("select * from Chromatograms");
        while (rs.next()) {
            iChromatograms.add(new Chromatogram(rs.getInt("FileID"), rs.getInt("TraceType"), rs.getBytes("Chromatogram")));
        }

        //try to set the quan channel
        int lFieldId = 0;
        rs = stat.executeQuery("select * from CustomDataFields");
        while (rs.next()) {
            if(rs.getString("DisplayName").equalsIgnoreCase("QuanChannelID")){
                lFieldId = rs.getInt("FieldID");
            }
        }
        if(lFieldId != 0){
            rs = stat.executeQuery("select * from CustomDataPeptides where FieldID = " + lFieldId);
            while (rs.next()) {
                if(iPeptidesMap.get(rs.getInt("PeptideID")) != null){
                    iPeptidesMap.get(rs.getInt("PeptideID")).setChannelId(rs.getInt("FieldValue"));
                }
            }
        }

        rs.close();
    }

    /**
     * This getter gives the name of the channel for a given channelid
     * @param lChannelId Int with the channelid
     * @return String with the name of the channel
     */
    public String getQuanChannelNameById(int lChannelId){
        for(int i = 0; i<iChannelIds.size(); i ++){
            if(iChannelIds.get(i)==lChannelId){
                return iComponents.get(i);
            }
        }
        return null;
    }

    /**
     * This getter gives the name of the raw data file for a given fileid
     * @param lFileid int with the fileid
     * @return String with the name of the raw file
     */
    public String getRawfileNameByFileId(int lFileid){
        String lResult = null;
        for(int i = 0; i<iRawFiles.size(); i ++){
            if(lFileid == iRawFiles.get(i).getFileId()){
                lResult = iRawFiles.get(i).getFileName().substring(iRawFiles.get(i).getFileName().lastIndexOf("\\") + 1);
            }
        }
        return lResult;
    }


    /**
     * The method will parse the quantitation xml and will set the ratio types, components and channelids
     */
    public void parseQuantitationXml(){
        String[] lLines = iQuantificationMethod.split("\r\n");
        boolean lRatioReporting = false;

        for(int i = 0; i<lLines.length; i ++){
            String lLine = lLines[i].trim();
            if(lLine.endsWith("selected=\"QuanLabels\">")){
                //we have a component
                String lComponent = lLine.substring(lLine.indexOf("name=\"") + 6, lLine.indexOf("\"", lLine.indexOf("name=\"") + 6));
                String lNextChannelLine = lLines[i + 1];
                int lChannel = Integer.valueOf(lNextChannelLine.substring(lNextChannelLine.indexOf("ID\">") + 4, lNextChannelLine.indexOf("</")));
                iComponents.add(lComponent);
                iChannelIds.add(lChannel);
            }
            if(lLine.startsWith("<MethodPart name=\"RatioCalculation\"")){
                lRatioReporting = false;
            }
            if(lRatioReporting){
                if(lLine.startsWith("<MethodPart")){
                    String lRatioType = lLine.substring(lLine.indexOf("name=\"") + 6, lLine.indexOf("\"", lLine.indexOf("name=\"") + 6));
                    String lNextNumeratorLine = lLines[i + 2];
                    String lNextDenominatorLine = lLines[i + 3];
                    String lNumerator = lNextNumeratorLine.substring(lNextNumeratorLine.indexOf("or\">") + 4, lNextNumeratorLine.indexOf("</"));
                    String lDenominator = lNextDenominatorLine.substring(lNextDenominatorLine.indexOf("or\">") + 4, lNextDenominatorLine.indexOf("</"));

                    iRatioTypes.add(new RatioType(lRatioType, lNumerator, lDenominator, iChannelIds, iComponents));
                }
            }
            if(lLine.startsWith("<MethodPart name=\"RatioReporting\"")){
                lRatioReporting = true;
            }
        }
    }


    /**
     * This getter gives a vector with all the modifications found in the msf file
     * @return Vector with all the modifications found in the msf file
     */
    public Vector<Modification> getModifications() {
        return iModifications;
    }

    /**
     * This getter gives a hashmap with the modificationid as key and the modification as value
     * @return HashMap with the modificationid as key and the modification as value
     */
    public HashMap<Integer, Modification> getModificationsMap() {
        return iModificationsMap;
    }

    /**
     * This getter gives a vector with the amino acids found in the msf file
     * @return Vector with the amino acids found in the msf file
     */
    public Vector<AminoAcid> getAminoAcids() {
        return iAminoAcids;
    }

    /**
     * This getter gives a hashmap with the amino acid id as key and the amino acid as value
     * @return HashMap with the amino acid id as key and the amino acid as value
     */
    public HashMap<Integer, AminoAcid> getAminoAcidsMap() {
        return iAminoAcidsMap;
    }

    /**
     * This getter gives a vector with the enzymes found in the msf file
     * @return Vector with the enzymes found in the msf file
     */
    public Vector<Enzyme> getEnzymes() {
        return iEnzymes;
    }

    /**
     * This getter gives a hashmap with the enzyme id as key and the enzyme as value
     * @return HashMap with the enzyme id as key and the enzyme as value
     */
    public HashMap<Integer, Enzyme> getEnzymesMap() {
        return iEnzymesMap;
    }

    /**
     * This getter gives a vector with the peptides found in the msf file
     * @return Vector with the peptides found in the msf file
     */
    public Vector<Peptide> getPeptides() {
        return iPeptides;
    }

    /**
     * This getter gives a hashmap with the peptide id as key and the peptide as value
     * @return HashMap with the peptide id as key and the peptide as value
     */
    public HashMap<Integer, Peptide> getPeptidesMap() {
        return iPeptidesMap;
    }

    /**
     * This getter gives a vector with the taxonomies found in the msf file
     * @return Vector with the taxonomies found in the msf file
     */
    public Vector<Taxonomy> getTaxonomies() {
        return iTaxonomies;
    }

    /**
     * This getter gives a hashmap with the taxonomies id as key and the taxonomies as value
     * @return HashMap with the taxonomies id as key and the taxonomies as value
     */
    public HashMap<Integer, Taxonomy> getTaxonomiesMap() {
        return iTaxonomiesMap;
    }

    /**
     * This getter gives a vector with the spectra found in the msf file
     * @return Vector with the spectra found in the msf file
     */
    public Vector<Spectrum> getSpectra() {
        return iSpectra;
    }

    /**
     * This getter gives a hashmap with the spectrum id as key and the spectrum as value
     * @return HashMap with the spectrum id as key and the spectrum as value
     */
    public HashMap<Integer, Spectrum> getSpectraMapBySpectrumId() {
        return iSpectraMapBySpectrumId;
    }

    /**
     * This getter gives a hashmap with the unique spectrum id as key and the spectrum as value
     * @return HashMap with the unique spectrum id as key and the spectrum as value
     */
    public HashMap<Integer, Spectrum> getSpectraMapByUniqueSpectrumId() {
        return iSpectraMapByUniqueSpectrumId;
    }

    /**
     * This getter gives a hashmap with the mass peak id as key and the spectrum as value
     * @return HashMap with the unique mass peak id as key and the spectrum as value
     */
    public HashMap<Integer, Spectrum> getSpectraMapByMassPeakId() {
        return iSpectraMapByMassPeakId;
    }

    /**
     * This getter gives a string with the xml formatted quantification method found in the msf file
     * @return String with the xml formatted quantification method found in the msf file
     */
    public String getQuantificationMethod() {
        return iQuantificationMethod;
    }

    /**
     * This getter gives a vector with the names of the different components (ex. Light, Heavy, ...) found in the msf file
     * @return Vector with the names of the different components (ex. Light, Heavy, ...) found in the msf file
     */
    public Vector<String> getComponents() {
        return iComponents;
    }

    /**
     * This getter gives a vector with the channel ids of the different components (ex. Light => <b>1</b>, Heavy  => <b>2</b>, ...) found in the msf file
     * @return Vector with the channel ids of the different components (ex. 1,2, ...) found in the msf file
     */
    public Vector<Integer> getChannelIds() {
        return iChannelIds;
    }

    /**
     * This getter gives a vector with the different ratio types. This are created by the parsing the quantification xml
     * @return Vector with the different ratio types
     */
    public Vector<RatioType> getRatioTypes() {
        return iRatioTypes;
    }

    /**
     * This getter gives a vector with the scan events found in the msf file
     * @return Vector with the scan events found in the msf file
     */
    public Vector<ScanEvent> getScanEvents() {
        return iScanEvents;
    }

    /**
     * This getter gives a vector with the score types found in the msf file
     * @return Vector with the score types found in the msf file
     */
    public Vector<ScoreType> getScoreTypes() {
        return iScoreTypes;
    }

    /**
     * This getter gives a vector with the proteins found in the msf file
     * @return Vector with the proteins found in the msf file
     */
    public Vector<Protein> getProteins() {
        return iProteins;
    }

    /**
     * This getter gives a hashmap with the protein id as key and the protein as value
     * @return HashMap with the protein id as key and the protein as value
     */
    public HashMap<Integer, Protein> getProteinsMap() {
        return iProteinsMap;
    }

    /**
     * This getter gives a vector with the custom data fields found in the msf file
     * @return Vector with the custom data fields found in the msf file
     */
    public Vector<CustomDataField> getCustomDataFields() {
        return iCustomDataFields;
    }

    /**
     * This getter gives a hashmap with the custom data fields id as key and the custom data fields as value
     * @return HashMap with the custom data fields id as key and the custom data fields as value
     */
    public HashMap<Integer, CustomDataField> getCustomDataFieldsMap() {
        return iCustomDataFieldsMap;
    }

    /**
     * This getter gives a vector with the custom data fields use by the peptides
     * @return Vector with the custom data fields use by the peptides
     */
    public Vector<CustomDataField> getPeptideUsedCustomDataFields() {
        return iPeptideUsedCustomDataFields;
    }

    /**
     * This getter gives a vector with the custom data fields use by the proteins
     * @return Vector with the custom data fields use by the proteins
     */
    public Vector<CustomDataField> getProteinUsedCustomDataFields() {
        return iProteinUsedCustomDataFields;
    }

    /**
     * This getter gives a vector with the custom data fields use by the spectra
     * @return Vector with the custom data fields use by the spectra
     */
    public Vector<CustomDataField> getSpectrumUsedCustomDataFields() {
        return iSpectrumUsedCustomDataFields;
    }

    /**
     * This getter gives the workflow info of the msf file
     * @return WorkflowInfo Workflow info of the msf file
     */
    public WorkflowInfo getWorkFlowInfo() {
        return iWorkFlowInfo;
    }

    /**
     * This getter gives a vector with the filters found in the msf file
     * @return Vector with the filters found in the msf file
     */
    public Vector<Filter> getFilter() {
        return iFilter;
    }

    /**
     * This getter gives a hashmap with the isotope pattern id as key and the isotope pattern as value
     * @return hashmap with the isotope pattern id as key and the isotope pattern as value 
     */
    public HashMap<Integer, IsotopePattern> getIsotopePatternMap() {
        return iIsotopePatternMap;
    }

    /**
     * This getter gives a vector with the quan results found in the msf file
     * @return Vector with the quan results found in the msf file
     */
    public Vector<QuanResult> getQuanResults() {
        return iQuanResults;
    }

    /**
     * This getter gives a hashmap with the quan result id as key and the quan result as value
     * @return hashmap with the quan result id as key and the quan result as value
     */
    public HashMap<Integer, QuanResult> getQuanResultsMap() {
        return iQuanResultsMap;
    }

    /**
     * This getter gives a vector with the raw files found in the msf file
     * @return Vector with the raw files found in the msf file
     */
    public Vector<RawFile> getRawFiles() {
        return iRawFiles;
    }

    /**
     * This getter gives the sql connection to the msf file
     * @return Connection to the msf file
     */
    public Connection getConnection() {
        return iConnection;
    }

    /**
     * This getter gives a vector with the chromatograms found in the msf file
     * @return Vector with the chromatograms found in the msf file
     */
    public Vector<Chromatogram> getChromatograms() {
        return iChromatograms;
    }

    /**
     * This method will give a processing node based on the given number
     * @param lId Int with the id of the processing node
     * @return The requested processing node
     */
    public ProcessingNode getProcessingNodeByNumber(int lId){
        return iProcessingNodesMap.get(lId);
    }

    public Vector<ProcessingNode> getProcessingNodes() {
        return iProcessingNodes;
    }

    public String getFilePath() {
        return iFilePath;
    }

    public String getFileName(){
        String lSub = iFilePath.substring(iFilePath.lastIndexOf(System.getProperties().getProperty("file.separator"))+1);
        return lSub;
    }
}
