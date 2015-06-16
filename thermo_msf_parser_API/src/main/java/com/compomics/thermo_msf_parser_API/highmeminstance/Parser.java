package com.compomics.thermo_msf_parser_API.highmeminstance;

import com.compomics.thermo_msf_parser_API.enums.MsfVersion;
import com.compomics.thermo_msf_parser_API.enums.GUID;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.RatioTypeLowMem;
import com.compomics.thermo_msf_parser_API.util.Joiner;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA. User: Niklaas Date: 18-Feb-2011 Time: 09:12:53
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class Parser {

    /**
     * Obtain a list of fasta files used
     */
    private List<String> iFastaFiles = new ArrayList<String>();
    /**
     * The modifications
     */
    private List<Modification> iModifications = new ArrayList<Modification>();
    /**
     * A map with the modificationid as key and the modification as value
     */
    private Map<Integer, Modification> iModificationsMap = new HashMap<Integer, Modification>();
    /**
     * The amino acids
     */
    private List<AminoAcid> iAminoAcids = new ArrayList<AminoAcid>();
    private Map<Character, AminoAcid> iAminoAcidsLetterMap = new HashMap<Character, AminoAcid>();
    /**
     * The neutral losses
     */
    private List<NeutralLoss> iNeutralLosses = new ArrayList<NeutralLoss>();
    /**
     * A map with the neutrallossid as key and the neutralloss as value
     */
    private Map<Integer, NeutralLoss> iNeutralLossesMap = new HashMap<Integer, NeutralLoss>();
    /**
     * A map with the aminoacidid as key and the aminoacid as value
     */
    private HashMap<Integer, AminoAcid> iAminoAcidsMap = new HashMap<Integer, AminoAcid>();
    /**
     * The enzymes
     */
    private List<Enzyme> iEnzymes = new ArrayList<Enzyme>();
    /**
     * A map with the enzymeid as key and the enzyme as value
     */
    private Map<Integer, Enzyme> iEnzymesMap = new HashMap<Integer, Enzyme>();
    /**
     * The peptides
     */
    private List<Peptide> iPeptides = new ArrayList<Peptide>();
    /**
     * A map with the peptideid as key and the peptide as value
     */
    private Map<Integer, Peptide> iPeptidesMap = new HashMap<Integer, Peptide>();
    /**
     * The peptides decoy
     */
    private List<Peptide> iPeptidesDecoy = new ArrayList<Peptide>();
    /**
     * A map with the peptideid as key and the peptidedecoy as value
     */
    private Map<Integer, Peptide> iPeptidesDecoyMap = new HashMap<Integer, Peptide>();
    /**
     * The taxonomies
     */
    private List<Taxonomy> iTaxonomies = new ArrayList<Taxonomy>();
    /**
     * A map with the taxonomyid as key and the taxonomy as value
     */
    private Map<Integer, Taxonomy> iTaxonomiesMap = new HashMap<Integer, Taxonomy>();
    /**
     * The spectra
     */
    private List<Spectrum> iSpectra = new ArrayList<Spectrum>();
    /**
     * A map with the spectrumid as key and the spectrum as value
     */
    private Map<Integer, Spectrum> iSpectraMapBySpectrumId = new HashMap<Integer, Spectrum>();
    /**
     * A map with the uniquespectrumid as key and the spectrum as value
     */
    private Map<Integer, Spectrum> iSpectraMapByUniqueSpectrumId = new HashMap<Integer, Spectrum>();
    /**
     * A map with the masspeakid as key and the spectrum as value
     */
    private Map<Integer, Spectrum> iSpectraMapByMassPeakId = new HashMap<Integer, Spectrum>();
    /**
     * The quantification method xml
     */
    private String iQuantificationMethod;
    /**
     * The quantification components
     */
    private List<String> iComponents = new ArrayList<String>();
    /**
     * The channel ids of the quantification components
     */
    private List<Integer> iChannelIds = new ArrayList<Integer>();
    /**
     * The ratio types
     */
    private List<RatioType> iRatioTypes = new ArrayList<RatioType>();
    /**
     * The scan events
     */
    private List<ScanEvent> iScanEvents = new ArrayList<ScanEvent>();
    /**
     * Scan events map
     */
    private Map<Integer, ScanEvent> iScanEventsMap = new HashMap<Integer, ScanEvent>();
    /**
     * The score types
     */
    private List<ScoreType> iScoreTypes = new ArrayList<ScoreType>();
    /**
     * main score types
     */
    private List<ScoreType> iMajorScoreTypes = new ArrayList<ScoreType>();
    /**
     * The proteins
     */
    private List<Protein> iProteins = new ArrayList<Protein>();
    /**
     * Protein Group map with proteinGroupId as key
     */
    private Map<Integer, ProteinGroup> proteinGroups = new HashMap<Integer, ProteinGroup>();
    /**
     * A map with the proteinid as key and the protein as value
     */
    private Map<Integer, Protein> iProteinsMap = new HashMap<Integer, Protein>();
    /**
     * The custom data fields
     */
    private List<CustomDataField> iCustomDataFields = new ArrayList<CustomDataField>();
    /**
     * A map with the fieldid as key and the custom data field as value
     */
    private Map<Integer, CustomDataField> iCustomDataFieldsMap = new HashMap<Integer, CustomDataField>();
    /**
     * This holds the field ids used for the peptides
     */
    private List<CustomDataField> iPeptideUsedCustomDataFields = new ArrayList<CustomDataField>();
    /**
     * This holds the field ids used for the peptides_decoys
     */
    private List<CustomDataField> iPeptideDecoyUsedCustomDataFields = new ArrayList<CustomDataField>();
    /**
     * This holds the field ids used for the proteins
     */
    private List<CustomDataField> iProteinUsedCustomDataFields = new ArrayList<CustomDataField>();
    /**
     * This holds the field ids used for the spectra
     */
    private List<CustomDataField> iSpectrumUsedCustomDataFields = new ArrayList<CustomDataField>();
    /**
     * This holds the field ids used for the processing node
     */
    private List<CustomDataField> iProcessingNodeUsedCustomDataFields = new ArrayList<CustomDataField>();
    /**
     * The workflow info
     */
    private WorkflowInfo iWorkFlowInfo;
    /**
     * The filters
     */
    private List<Filter> iFilter = new ArrayList<Filter>();
    /**
     * A map with the isotope pattern id as key and the isotopepattern as value
     */
    private Map<Integer, IsotopePattern> iIsotopePatternMap = new HashMap<Integer, IsotopePattern>();
    /**
     * The quan results
     */
    private List<QuanResult> iQuanResults = new ArrayList<QuanResult>();
    /**
     * A map with the quanresult id as key and the quanresult as value
     */
    private Map<Integer, QuanResult> iQuanResultsMap = new HashMap<Integer, QuanResult>();
    /**
     * The raw files
     */
    private List<RawFile> iRawFiles = new ArrayList<RawFile>();
    /**
     * The connection to the thermo msf file
     */
    private Connection iConnection;
    /**
     * The chromatograms
     */
    private List<Chromatogram> iChromatograms = new ArrayList<Chromatogram>();
    /**
     * The processing nodes
     */
    private List<ProcessingNode> iProcessingNodes = new ArrayList<ProcessingNode>();
    /**
     * A map with the processingnode id as key and the processingnode as value
     */
    private Map<Integer, ProcessingNode> iProcessingNodesMap = new HashMap<Integer, ProcessingNode>();
    /**
     * A boolean that indicates if we need to use a low memory footprint
     */
    private boolean iLowMemory;
    /**
     * The file location of this msf file
     */
    private String iFilePath;
    /**
     * The version of the msf file
     */
    private MsfVersion iMsfVersion;
    /**
     * The quantification method name
     */
    private String iQuantificationMethodName;
    /**
     * Flag to set if phosphoRS processing nodes are found
     */
    private boolean hasPhosphoRS = false;

    /**
     * This will parse the thermo msf file
     *
     * @param iMsfFileLocation A String with the location of the msf file
     * @param iLowMemory a boolean.
     * @throws java.lang.ClassNotFoundException This is thrown when the sqlite library
     * cannot be found
     * @throws java.sql.SQLException This is thrown when there is a problem
     * extracting the data from the thermo msf file
     */
    public Parser(String iMsfFileLocation, boolean iLowMemory) throws SQLException, ClassNotFoundException {

        iFilePath = iMsfFileLocation;
        //create the connection to the msf file
        Class.forName("org.sqlite.JDBC");
        iConnection = DriverManager.getConnection("jdbc:sqlite:" + iMsfFileLocation);

        Statement stat = iConnection.createStatement();
        ResultSet rs;
        this.iLowMemory = iLowMemory;

        //get the msf version
        rs = stat.executeQuery("select * from SchemaInfo");
        while (rs.next()) {
            String lVersion = rs.getString("SoftwareVersion");
            if (lVersion.startsWith("1.2")) {
                iMsfVersion = MsfVersion.VERSION1_2;
            } else if (lVersion.startsWith("1.3") || lVersion.startsWith("1.4")) {
                iMsfVersion = MsfVersion.VERSION1_3;
            }
        }

        //get the processing nodes
        rs = stat.executeQuery("select * from ProcessingNodes");
        while (rs.next()) {
            ProcessingNode lNode = new ProcessingNode(rs.getInt("ProcessingNodeNumber"),
                    rs.getInt("ProcessingNodeID"),
                    rs.getString("ProcessingNodeParentNumber"),
                    rs.getString("NodeName"),
                    rs.getString("FriendlyName"),
                    rs.getInt("MajorVersion"),
                    rs.getInt("MinorVersion"),
                    rs.getString("NodeComment"),
                    rs.getString("NodeGUID"));

            hasPhosphoRS = hasPhosphoRS || (lNode.getNodeGUID().equals(GUID.NODE_PTM_SCORER)
                    || lNode.getNodeGUID().equals(GUID.NODE_PTM_SCORER2)
                    || lNode.getNodeGUID().equals(GUID.NODE_PTM_SCORER3));


            iProcessingNodes.add(lNode);
            iProcessingNodesMap.put(lNode.getProcessingNodeNumber(), lNode);
        }

        if (iMsfVersion == MsfVersion.VERSION1_3) {
            //add the processing node Custom data fields
            rs = stat.executeQuery("select * from CustomDataProcessingNodes");
            while (rs.next()) {
                if (iProcessingNodesMap.get(rs.getInt("ProcessingNodeNumber")) != null) {
                    iProcessingNodesMap.get(rs.getInt("ProcessingNodeNumber")).addCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
                }
            }
            rs = stat.executeQuery("select fieldid from CustomDataProcessingNodes group by fieldid");
            while (rs.next()) {
                iProcessingNodeUsedCustomDataFields.add(iCustomDataFieldsMap.get(rs.getInt("FieldID")));
            }
        }

        //add the processing node parameters to the processing node
        rs = stat.executeQuery("select * from ProcessingNodeParameters");
        while (rs.next()) {
            ProcessingNodeParameter lNodeParameter = new ProcessingNodeParameter(rs.getInt("ProcessingNodeNumber"), rs.getInt("ProcessingNodeId"), rs.getString("ParameterName"), rs.getString("FriendlyName"), rs.getInt("IntendedPurpose"), rs.getString("PurposeDetails"), rs.getInt("Advanced"), rs.getString("Category"), rs.getInt("Position"), rs.getString("ParameterValue"), rs.getString("ValueDisplayString"));

            if (iProcessingNodesMap.get(lNodeParameter.getProcessingNodeNumber()) != null) {
                iProcessingNodesMap.get(lNodeParameter.getProcessingNodeNumber()).addProcessingNodeParameter(lNodeParameter);
            }
        }

        //get all the aminoacids
        rs = stat.executeQuery("select * from AminoAcids");
        while (rs.next()) {
            AminoAcid lAA = new AminoAcid(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getString(7));
            iAminoAcids.add(lAA);
            iAminoAcidsMap.put(rs.getInt(1), lAA);
            iAminoAcidsLetterMap.put(lAA.getOneLetterCode().toUpperCase().charAt(0), lAA);
        }


        List<Integer> fixedMods = new ArrayList<Integer>();
        List<Integer> variableMods = new ArrayList<Integer>();
        List<Integer> usedMods = new ArrayList<Integer>();
        Map<Integer, List<String>> modsToAminoAcidNumbers = new HashMap<Integer, List<String>>();
        // Process the parameters of processing nodes to extract the used modifications
        for (Iterator<ProcessingNode> it = iProcessingNodes.iterator(); it.hasNext();) {
            ProcessingNode node = it.next();
            for (ProcessingNodeParameter parameter : node.getProcessingNodeParameters()) {
                // TODO: add modNames for Z-Core, if they're not covered yet
                String parName = parameter.getParameterName();
                boolean modFound = false;
                boolean isFixed = false;

                if (parName.matches("(?:Static_\\d+|StatMod_\\d+)")) {
                    if (parameter.getParameterValue().contains("#")) {
                        modFound = true;
                        isFixed = true;
                    }
                } else if (parName.matches("(?:DynModification_\\d+|DynMod_\\d+)")) {
                    if (parameter.getParameterValue().contains("#")) {
                        modFound = true;
                    }
                }

                if (modFound) {
                    String[] numbers = parameter.getParameterValue().split("#");
                    int modnumber = Integer.parseInt(numbers[numbers.length - 1]);
                    // Add the found amino acid references to add to the modifications later
                    modsToAminoAcidNumbers.put(modnumber, Arrays.asList(numbers).subList(0, numbers.length - 1));
                    usedMods.add(modnumber);
                    if (isFixed) {
                        fixedMods.add(modnumber);
                    } else {
                        variableMods.add(modnumber);
                    }
                }
            }
        }


        //get the used modifications
        rs = stat.executeQuery("select * from AminoAcidModifications where AminoAcidModificationID in (" + Joiner.join(usedMods, ",") + ")");
        while (rs.next()) {
            boolean fixedModification = fixedMods.contains(rs.getInt(1));
            Modification lMod = new Modification(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getDouble(9), rs.getInt(10), rs.getInt(11), fixedModification);
            for (String aminoAcidNumber : modsToAminoAcidNumbers.get(rs.getInt(1))) {
                lMod.getSelectedAminoAcids().add(iAminoAcidsMap.get(Integer.parseInt(aminoAcidNumber)));
            }

            iModifications.add(lMod);
            iModificationsMap.put(rs.getInt("AminoAcidModificationID"), lMod);
        }

        //add the amino acid to the modifications
        rs = stat.executeQuery("select * from AminoAcidModificationsAminoAcids");
        while (rs.next()) {
            int lModId = rs.getInt("AminoAcidModificationID");
            int lAaId = rs.getInt("AminoAcidID");
            if (iAminoAcidsMap.get(lAaId) != null && iModificationsMap.get(lModId) != null) {
                iModificationsMap.get(lModId).addAminoAcid(iAminoAcidsMap.get(lAaId));
                if (iMsfVersion == MsfVersion.VERSION1_3) {
                    iModificationsMap.get(lModId).addClassificationForAminoAcid(rs.getInt("Classification"));
                }
            }
        }
        if (iMsfVersion == MsfVersion.VERSION1_3) {
            //get the neutral losses
            rs = stat.executeQuery("select * from AminoAcidModificationsNeutralLosses");
            while (rs.next()) {
                NeutralLoss lLoss = new NeutralLoss(rs.getInt("NeutralLossID"), rs.getString("Name"), rs.getDouble("MonoisotopicMass"), rs.getDouble("AverageMass"));
                iNeutralLosses.add(lLoss);
                iNeutralLossesMap.put(lLoss.getNeutralLossId(), lLoss);
            }

            //add the amino acid to the neutral losses
            rs = stat.executeQuery("select * from AminoAcidModificationsAminoAcidsNL");
            while (rs.next()) {
                int lAaId = rs.getInt("AminoAcidID");
                int lNlId = rs.getInt("NeutralLossID");
                if (iAminoAcidsMap.get(lAaId) != null && iNeutralLossesMap.get(lNlId) != null) {
                    iNeutralLossesMap.get(lNlId).addAminoAcid(iAminoAcidsMap.get(lAaId));
                }
            }
            //now add the neutral losses to the modification
            rs = stat.executeQuery("select * from AminoAcidModificationsAminoAcidsNL");
            while (rs.next()) {
                int lModId = rs.getInt("AminoAcidModificationID");
                int lNlId = rs.getInt("NeutralLossID");
                if (iModificationsMap.get(lModId) != null && iNeutralLossesMap.get(lNlId) != null) {
                    iModificationsMap.get(lModId).addNeutralLoss(iNeutralLossesMap.get(lNlId));
                }
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
            if (iEnzymesMap.get(lEnzymeId) != null) {
                iEnzymesMap.get(lEnzymeId).setSpecificity(rs.getInt("Specificity"));
            }
        }


        //get the peptides
        rs = stat.executeQuery("select * from Peptides as p");
        while (rs.next()) {
            Peptide lPeptide = new Peptide(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), iAminoAcidsLetterMap);
            if (iMsfVersion == MsfVersion.VERSION1_3) {
                lPeptide.setMissedCleavage(rs.getInt("MissedCleavages"));
                lPeptide.setUniquePeptideSequenceId(rs.getInt("UniquePeptideSequenceID"));
            }
            iPeptides.add(lPeptide);
            iPeptidesMap.put(lPeptide.getPeptideId(), lPeptide);
        }

        //get the fasta files
        rs = stat.executeQuery("select VirtualFileName as file from fastafiles");
        while (rs.next()) {
            iFastaFiles.add(rs.getString("file"));
        }


        //get the decoy peptides
        rs = stat.executeQuery("select * from Peptides_decoy as p");
        while (rs.next()) {
            Peptide lPeptide = new Peptide(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), "", rs.getInt("ProcessingNodeNumber"), iAminoAcidsLetterMap);
            if (iMsfVersion == MsfVersion.VERSION1_3) {
                lPeptide.setMissedCleavage(rs.getInt("MissedCleavages"));
                lPeptide.setUniquePeptideSequenceId(rs.getInt("UniquePeptideSequenceID"));
                lPeptide.setAnnotation(rs.getString("Annotation"));
            }
            iPeptidesDecoy.add(lPeptide);
            iPeptidesDecoyMap.put(lPeptide.getPeptideId(), lPeptide);
        }

        if (iMsfVersion == MsfVersion.VERSION1_3) {
            //add the processing node Custom data fields
            rs = stat.executeQuery("select * from CustomDataPeptides_Decoy");
            while (rs.next()) {
                if (iPeptidesDecoyMap.get(rs.getInt("PeptideID")) != null) {
                    iPeptidesDecoyMap.get(rs.getInt("PeptideID")).addCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
                }
            }
            rs = stat.executeQuery("select fieldid from CustomDataPeptides_Decoy group by fieldid");
            while (rs.next()) {
                iPeptideDecoyUsedCustomDataFields.add(iCustomDataFieldsMap.get(rs.getInt("FieldID")));
            }
        }

        //get the score types
        rs = stat.executeQuery("select * from ProcessingNodeScores");
        while (rs.next()) {
            ScoreType lScoreType = new ScoreType(rs.getInt("ScoreID"), rs.getString("ScoreName"), rs.getString("FriendlyName"), rs.getString("Description"), rs.getInt("ScoreCategory"), rs.getInt("IsMainScore"));
            iScoreTypes.add(lScoreType);
        }
        
        //set the major score type
        for (int i = 0; i < iScoreTypes.size(); i++) {
            if (iScoreTypes.get(i).getIsMainScore() == 1) {
                iMajorScoreTypes.add(iScoreTypes.get(i));
            }
        }
        
        //add the score to the peptides
        rs = stat.executeQuery("select * from  PeptideScores ");
        while (rs.next()) {
            if (iPeptidesMap.get(rs.getInt("PeptideID")) != null) {
                int lScoreId = rs.getInt("ScoreID");
                iPeptidesMap.get(rs.getInt("PeptideID")).setScore(rs.getDouble("ScoreValue"), lScoreId, iScoreTypes);
            }
        }
        //add the score to the peptides decoy
        rs = stat.executeQuery("select  * from  PeptideScores_decoy ");
        while (rs.next()) {
            if (iPeptidesDecoyMap.get(rs.getInt("PeptideID")) != null) {
                int lScoreId = rs.getInt("ScoreID");
                iPeptidesDecoyMap.get(rs.getInt("PeptideID")).setScore(rs.getDouble("ScoreValue"), lScoreId, iScoreTypes);
            }
        }

        if (iMsfVersion == MsfVersion.VERSION1_3) {
            //get the custom data fields
            rs = stat.executeQuery("select  * from  CustomDataFields ");
            while (rs.next()) {
                CustomDataField lField = new CustomDataField(rs.getInt("FieldID"), rs.getString("DisplayName"));
                iCustomDataFields.add(lField);
                iCustomDataFieldsMap.put(lField.getFieldId(), lField);
            }

            //add the custom data fields to the peptides
            rs = stat.executeQuery("select * from CustomDataPeptides ");
            while (rs.next()) {
                if (iPeptidesMap.get(rs.getInt("PeptideID")) != null) {
                    iPeptidesMap.get(rs.getInt("PeptideID")).addCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
                }
            }
            rs = stat.executeQuery("select * from CustomDataPeptides_decoy ");
            while (rs.next()) {
                if (iPeptidesDecoyMap.get(rs.getInt("PeptideID")) != null) {
                    iPeptidesDecoyMap.get(rs.getInt("PeptideID")).addCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
                }
            }

            rs = stat.executeQuery("select fieldid from CustomDataPeptides group by fieldid");
            while (rs.next()) {
                iPeptideUsedCustomDataFields.add(iCustomDataFieldsMap.get(rs.getInt("FieldID")));
            }
            //add the custom data fields to the proteins
            rs = stat.executeQuery("select  * from  CustomDataProteins ");
            while (rs.next()) {
                if (iProteinsMap.get(rs.getInt("ProteinID")) != null) {
                    iProteinsMap.get(rs.getInt("ProteinID")).addCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
                }
            }
            rs = stat.executeQuery("select fieldid from CustomDataProteins group by fieldid");
            while (rs.next()) {
                iProteinUsedCustomDataFields.add(iCustomDataFieldsMap.get(rs.getInt("FieldID")));
            }
            //add the custom data fields to the proteins
            rs = stat.executeQuery("select  * from  CustomDataSpectra ");
            while (rs.next()) {
                if (iSpectraMapBySpectrumId.get(rs.getInt("SpectrumID")) != null) {
                    iSpectraMapBySpectrumId.get(rs.getInt("SpectrumID")).addCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
                }
            }
            rs = stat.executeQuery("select fieldid from CustomDataSpectra group by fieldid");
            while (rs.next()) {
                iSpectrumUsedCustomDataFields.add(iCustomDataFieldsMap.get(rs.getInt("FieldID")));
            }


        }

        // Get the modifications from the db
        populateModifications(iConnection);

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
            if (iTaxonomiesMap.get(rs.getInt("TaxonomyID")) != null) {
                iTaxonomiesMap.get(rs.getInt("TaxonomyID")).setName(rs.getString("Name"));
                iTaxonomiesMap.get(rs.getInt("TaxonomyID")).setNameCategory(rs.getInt("NameCategory"));
            }
        }

        //get the spectra
        rs = stat.executeQuery("select s.*, m.FileID from spectrumheaders as s, masspeaks as m where m.masspeakid = s.masspeakid");
        while (rs.next()) {
            Spectrum lSpectrum = spectrumFromSpectrumheadersQuery(rs);
            lSpectrum.setFileId(rs.getInt("FileID"));
            iSpectra.add(lSpectrum);
            iSpectraMapByMassPeakId.put(lSpectrum.getMassPeakId(), lSpectrum);
            iSpectraMapBySpectrumId.put(lSpectrum.getSpectrumId(), lSpectrum);
            iSpectraMapByUniqueSpectrumId.put(lSpectrum.getUniqueSpectrumId(), lSpectrum);
        }

        if (!iLowMemory) {
            rs = stat.executeQuery("select * from Spectra");
            while (rs.next()) {
                int lSpectrumId = rs.getInt("UniqueSpectrumID");
                byte[] lZipped = rs.getBytes("Spectrum");
                if (iSpectraMapByUniqueSpectrumId.get(lSpectrumId) != null) {
                    iSpectraMapByUniqueSpectrumId.get(lSpectrumId).setZippedBytes(lZipped);
                }
            }
        }

        rs = stat.executeQuery("select * from SpectrumScores");
        while (rs.next()) {
            int lSpectrumId = rs.getInt("SpectrumID");
            if (iSpectraMapBySpectrumId.get(lSpectrumId) != null) {
                iSpectraMapBySpectrumId.get(lSpectrumId).addSpectrumScore(rs.getDouble("Score"), rs.getInt("ProcessingNodeNumber"));
            }
        }
        //get the scan events
        rs = stat.executeQuery("select * from ScanEvents");
        while (rs.next()) {
            ScanEvent lScanEvent = new ScanEvent(rs.getInt("ScanEventID"), rs.getInt("MSLevel"), rs.getInt("Polarity"), rs.getInt("ScanType"), rs.getInt("Ionization"), rs.getInt("MassAnalyzer"), rs.getInt("ActivationType"));
            iScanEvents.add(lScanEvent);
            iScanEventsMap.put(lScanEvent.getScanEventId(), lScanEvent);
        }

        for (Spectrum s : iSpectra) {
            s.setScanEvent(iScanEventsMap.get(s.getScanEventId()));
        }

        //get the proteins
        rs = stat.executeQuery("select * from Proteins ");
        while (rs.next()) {
            Protein lProtein = null;
            if (iLowMemory) {
                lProtein = new Protein(rs.getInt("ProteinID"), this);
                if (iMsfVersion == MsfVersion.VERSION1_3) {
                    lProtein.setMasterProtein(rs.getInt("IsMasterProtein"));
                }
            } else {
                lProtein = new Protein(rs.getInt("ProteinID"), rs.getString("Sequence"));
            }
            iProteins.add(lProtein);
            iProteinsMap.put(lProtein.getProteinId(), lProtein);
        }
        //add the description to the protein
        rs = stat.executeQuery("select  * from  ProteinAnnotations ");
        while (rs.next()) {
            if (iProteinsMap.get(rs.getInt("ProteinID")) != null) {
                iProteinsMap.get(rs.getInt("ProteinID")).setDescription(rs.getString("Description"));
            }
        }
        //add the events to the protein
        rs = stat.executeQuery("select * from events");
        //add the score to the protein
        rs = stat.executeQuery("select  * from  ProteinScores ");
        while (rs.next()) {
            if (iProteinsMap.get(rs.getInt("ProteinID")) != null) {
                iProteinsMap.get(rs.getInt("ProteinID")).addScore(rs.getDouble("ProteinScore"), rs.getInt("ProcessingNodeNumber"), rs.getDouble("Coverage"));
            }
        }

        if (iMsfVersion == MsfVersion.VERSION1_3) {
            //add the protein group to the protein
            rs = stat.executeQuery("select  * from  ProteinsProteinGroups");
            while (rs.next()) {

                if (iProteinsMap.get(rs.getInt("ProteinID")) != null) {
                    Integer proteinGroupId = rs.getInt("ProteinGroupID");
                    Protein protein = iProteinsMap.get(rs.getInt("ProteinID"));
                    protein.setProteinGroupId(proteinGroupId);
                    //create the group if it didn't exist before
                    if (!proteinGroups.containsKey(proteinGroupId)) {
                        proteinGroups.put(proteinGroupId, new ProteinGroup(proteinGroupId));
                    }
                    ProteinGroup group = proteinGroups.get(proteinGroupId);
                    group.getProteins().add(protein);
                    for (Peptide pep : protein.getPeptides()) {
                        group.getPeptideIds().add(pep.getPeptideId());
                    }
                }
            }

            rs = stat.executeQuery("select  * from  PtmAnnotationData");
            while (rs.next()) {
                Protein.PtmAnnotation lAnno = new Protein.PtmAnnotation(rs.getInt("PtmUnimodID"), rs.getInt("Position"));
                if (iProteinsMap.get(rs.getInt("ProteinID")) != null) {
                    iProteinsMap.get(rs.getInt("ProteinID")).addPtmAnnotation(lAnno);
                }
            }

            //add the custom data fields to the proteins
            rs = stat.executeQuery("select  * from  CustomDataProteins_Decoy ");
            while (rs.next()) {
                if (iProteinsMap.get(rs.getInt("ProteinID")) != null) {
                    iProteinsMap.get(rs.getInt("ProteinID")).addDecoyCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
                }
            }

            //add the decoy score to the protein
            rs = stat.executeQuery("select  * from  ProteinScores_Decoy");
            while (rs.next()) {
                if (iProteinsMap.get(rs.getInt("ProteinID")) != null) {
                    iProteinsMap.get(rs.getInt("ProteinID")).addDecoyScore(rs.getDouble("ProteinScore"), rs.getInt("ProcessingNodeNumber"), rs.getDouble("Coverage"));
                }
            }
        }



        //get the quantification method
        rs = stat.executeQuery("select ParameterValue from ProcessingNodeParameters where ParameterName = 'QuantificationMethod'");
        while (rs.next()) {
            iQuantificationMethod = rs.getString(1);
            parseQuantitationXml();
        }

        //get the workflow info
        rs = stat.executeQuery("select * from WorkflowInfo");
        while (rs.next()) {
            iWorkFlowInfo = new WorkflowInfo(rs.getString("WorkflowName"), rs.getString("WorkflowDescription"), rs.getString("User"), rs.getString("WorkflowTemplate"), null);
        }
        //add the messages to the workflow info
        rs = stat.executeQuery("select * from WorkflowMessages");
        while (rs.next()) {
            iWorkFlowInfo.addMessage(new WorkflowMessage(rs.getInt("MessageID"), rs.getInt("ProcessingNodeID"), rs.getInt("ProcessingNodeNumber"), rs.getLong("Time"), rs.getInt("MessageKind"), rs.getString("Message")));
        }
        //add the msf version info the the workflowinfo
        rs = stat.executeQuery("select * from SchemaInfo");
        while (rs.next()) {
            iWorkFlowInfo.setMsfVersionInfo(new MsfVersionInfo(rs.getInt("Version"), rs.getString("SoftwareVersion")));
        }


        if (iMsfVersion == MsfVersion.VERSION1_2) {

            //get the filters
            rs = stat.executeQuery("select * from ResultFilterSet");
            while (rs.next()) {
                String lXml = rs.getString("ResultFilterSet");
                String[] lXmlLines = lXml.split("\n");
                String lFilterSetName = lXml.substring(lXml.indexOf("\"", lXml.indexOf("FilterSetName")) + 1, lXml.indexOf("\"", lXml.indexOf("\"", lXml.indexOf("FilterSetName")) + 1));
                List<String> lFilterLines = new ArrayList<String>();
                for (int i = 0; i < lXmlLines.length; i++) {
                    String lLine = lXmlLines[i].trim();
                    if (lLine.startsWith("<FilterInfo ")) {
                        lFilterLines.add(lLine);
                    }
                    if (lLine.startsWith("<Parameter ")) {
                        lFilterLines.add(lLine);
                    }
                    if (lLine.startsWith("</FilterInfo>")) {
                        iFilter.add(new Filter(lFilterSetName, lFilterLines));
                        lFilterLines.clear();
                    }
                }
            }
        }

        //get the Event annotations
        rs = stat.executeQuery("select * from EventAnnotations");
        while (rs.next()) {
            int lIsotopePatternId = rs.getInt("IsotopePatternID");
            EventAnnotation lEventAnno = new EventAnnotation(rs.getInt("EventID"), rs.getInt("IsotopePatternID"), rs.getInt("QuanResultID"), rs.getInt("QuanChannelID"));
            if (iIsotopePatternMap.get(lIsotopePatternId) == null) {
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
            if (iIsotopePatternMap.get(lIsotopePatternId) == null) {
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
            if (iQuanResultsMap.get(lQuantId) != null) {
                iQuanResultsMap.get(lQuantId).addQuanValues(rs.getInt("QuanChannelID"), rs.getDouble("Mass"), rs.getInt("Charge"), rs.getDouble("Area"), rs.getDouble("RetentionTime"));
            } else {
                QuanResult lQuant = new QuanResult(rs.getInt("QuanResultID"));
                lQuant.addQuanValues(rs.getInt("QuanChannelID"), rs.getDouble("Mass"), rs.getInt("Charge"), rs.getDouble("Area"), rs.getDouble("RetentionTime"));
                iQuanResults.add(lQuant);
                iQuanResultsMap.put(lQuant.getQuanResultId(), lQuant);
            }
        }
        //add the spectrumid
        rs = stat.executeQuery("select * from PrecursorIonAreaSearchSpectra");
        while (rs.next()) {
            int lQuantId = rs.getInt("QuanResultID");
            if (iQuanResultsMap.get(lQuantId) != null) {
                iQuanResultsMap.get(lQuantId).addSpectrumId(rs.getInt("SearchSpectrumID"));
                iQuanResultsMap.get(lQuantId).addProcessingNodeNumber(-1);
            } else {
                QuanResult lQuant = new QuanResult(rs.getInt("QuanResultID"));
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
            if (iQuanResultsMap.get(lQuantId) != null) {
                iQuanResultsMap.get(lQuantId).addSpectrumId(rs.getInt("SearchSpectrumID"));
                iQuanResultsMap.get(lQuantId).addProcessingNodeNumber(rs.getInt("ProcessingNodeNumber"));
            }
        }


        //add the isotope patterns to the quan results
        Collection<IsotopePattern> iIsotopePatterns = iIsotopePatternMap.values();
        Iterator<IsotopePattern> lIsotopePatternIterator = iIsotopePatterns.iterator();
        while (lIsotopePatternIterator.hasNext()) {
            IsotopePattern lIso = lIsotopePatternIterator.next();
            if (iQuanResultsMap.get(lIso.getSharedQuanResultId()) != null) {
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
            if (iPeptidesMap.get(rs.getInt("PeptideID")) != null) {
                if (iProteinsMap.get(rs.getInt("ProteinId")) != null) {
                    iPeptidesMap.get(rs.getInt("PeptideID")).addProtein(iProteinsMap.get(rs.getInt("ProteinId")));
                }
            }
        }

        if (iMsfVersion == MsfVersion.VERSION1_3) {
            //add the proteins to the peptides
            rs = stat.executeQuery("select * from  PeptidesProteins_Decoy");
            while (rs.next()) {
                if (iPeptidesDecoyMap.get(rs.getInt("PeptideID")) != null) {
                    if (iProteinsMap.get(rs.getInt("ProteinId")) != null) {
                        iPeptidesDecoyMap.get(rs.getInt("PeptideID")).addDecoyProtein(iProteinsMap.get(rs.getInt("ProteinId")));
                    }
                }
            }
        }


        //add peptide to spectra
        for (int p = 0; p < iPeptides.size(); p++) {
            if (iSpectraMapBySpectrumId.get(iPeptides.get(p).getSpectrumId()) != null) {
                iSpectraMapBySpectrumId.get(iPeptides.get(p).getSpectrumId()).addPeptide(iPeptides.get(p));
            }
        }

        //add decoy peptide to spectra
        for (int p = 0; p < iPeptidesDecoy.size(); p++) {
            if (iSpectraMapBySpectrumId.get(iPeptidesDecoy.get(p).getSpectrumId()) != null) {
                iSpectraMapBySpectrumId.get(iPeptidesDecoy.get(p).getSpectrumId()).addDecoyPeptide(iPeptidesDecoy.get(p));
            }
        }


        //add ratios to spectra
        for (int i = 0; i < iQuanResults.size(); i++) {
            List<Integer> lSpectrumids = iQuanResults.get(i).getSpectrumIds();
            for (int j = 0; j < lSpectrumids.size(); j++) {
                int lSpectrumid = lSpectrumids.get(j);
                if (iSpectraMapBySpectrumId.get(lSpectrumid) != null) {
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
            if (rs.getString("DisplayName").equalsIgnoreCase("QuanChannelID")) {
                lFieldId = rs.getInt("FieldID");
            }
        }
        if (lFieldId != 0) {
            rs = stat.executeQuery("select * from CustomDataPeptides where FieldID = " + lFieldId);
            while (rs.next()) {
                if (iPeptidesMap.get(rs.getInt("PeptideID")) != null) {
                    iPeptidesMap.get(rs.getInt("PeptideID")).setChannelId(rs.getInt("FieldValue"));
                }
            }
        }

        rs.close();
    }

    /**
     * Retrieve the modifications of a peptide
     *@param conn the connection to the proteome discoverer file
     * @throws SQLException if any
     */
    private void populateModifications(Connection conn) throws SQLException {

        //Create some indexes
        conn.prepareStatement("create index if not exists [COMPOMICS_Peptideaminoacidmodifications_peptides] on [peptidesaminoacidmodifications] ([PeptideID] ASC);").execute();
        conn.prepareStatement("create index if not exists [COMPOMICS_peptideterminalmodifications_peptides] on [peptidesterminalmodifications] ([PeptideID] asc);").execute();
        conn.prepareStatement("create index if not exists [COMPOMICS_Peptideaminoacidmodifications_peptides_decoy] on [peptidesaminoacidmodifications_decoy] ([PeptideID] ASC);").execute();
        conn.prepareStatement("create index if not exists [COMPOMICS_peptideterminalmodifications_peptides_decoy] on [peptidesterminalmodifications_decoy] ([PeptideID] asc);").execute();


        ResultSet rs;
        List<Integer> pRSSiteProbabilityFieldIDs = new ArrayList<Integer>();
        List<Integer> pRSProbabilityFieldIDs = new ArrayList<Integer>();
        List<Integer> pRSScoreFieldIDs = new ArrayList<Integer>();

        // pRS probabilities only for version 1.3 and greater
        if (iMsfVersion.compareTo(MsfVersion.VERSION1_3) >= 0 && hasPhosphoRS) {
            Statement st = conn.createStatement();

            // Obtain IDs for the pRS sequence probability in customdata
            rs = st.executeQuery(new StringBuilder().append("select fieldid from customdatafields where guid='").append(GUID.PRS_SEQUENCE_PROBABILITY).append("'").toString());
            while (rs.next()) {
                pRSProbabilityFieldIDs.add(rs.getInt("FieldID"));
            }
            // Obtain the IDs specific for the pRS site probabilities in customdata
            rs = st.executeQuery("select fieldid from customdatafields where guid='" + GUID.PRS_SCORE + "'");
            while (rs.next()) {
                pRSScoreFieldIDs.add(rs.getInt("FieldID"));
            }
            // Obtain the IDs specific for the pRS site probabilities in customdata
            rs = st.executeQuery("select fieldid from customdatafields where guid='" + GUID.PRS_SITE_PROBABILITIES + "'");
            while (rs.next()) {
                pRSSiteProbabilityFieldIDs.add(rs.getInt("FieldID"));
            }
        }


        //Some rather convoluted code to deal with the convoluted way of storing decoy and normal peptides

        // Arrays with values for "normal" and "decoy" peptides
        // Array of the collections with "normal" and "decoy" peptides, parsed before
        List[] peptideCollection = new List[]{iPeptides, iPeptidesDecoy};

        PreparedStatement decoyPeptideAAModsStatement = conn.prepareStatement("select * from PeptidesAminoAcidModifications_decoy where peptideId=?");
        PreparedStatement decoyPeptideTermModsStatement = conn.prepareStatement("select * from PeptidesTerminalModifications_decoy where peptideId=?");
        PreparedStatement peptideAAModsStatement = conn.prepareStatement("select * from PeptidesAminoAcidModifications where peptideId=?");
        PreparedStatement peptideTermModsStatement = conn.prepareStatement("select * from PeptidesTerminalModifications where peptideId=?");

        PreparedStatement[] preparedAAModsStatements = new PreparedStatement[]{peptideAAModsStatement, decoyPeptideAAModsStatement};
        PreparedStatement[] preparedTermModsStatements = new PreparedStatement[]{peptideTermModsStatement, decoyPeptideTermModsStatement};


        for (int decoy = 0; decoy < 1; decoy++) {
            for (Iterator<Peptide> it = peptideCollection[decoy].iterator(); it.hasNext();) {
                Peptide pep = it.next();

                for (Integer fieldID : pRSScoreFieldIDs) {
                    if (pep.getCustomDataFieldValues().containsKey(fieldID)) {
                        pep.setPhosphoRSScore(Float.parseFloat(pep.getCustomDataFieldValues().get(fieldID)));
                    }
                }
                for (Integer fieldID : pRSProbabilityFieldIDs) {
                    if (pep.getCustomDataFieldValues().containsKey(fieldID)) {
                        pep.setPhoshpoRSSequenceProbability(Float.parseFloat(pep.getCustomDataFieldValues().get(fieldID)));
                    }
                }
                Map<Integer, Float> pRSprobabilities = new HashMap<Integer, Float>();
                for (Integer fieldID : pRSSiteProbabilityFieldIDs) {
                    if (pep.getCustomDataFieldValues().containsKey(fieldID)) {
                        pRSprobabilities.putAll(parsePRSIdentificationProbabilities(pep.getCustomDataFieldValues().get(fieldID)));
                    }
                }

                HashMap<ModificationPosition, Modification> peptideModifications = new HashMap<ModificationPosition, Modification>();
                HashMap<ModificationPosition, Modification> phosphoModifications = new HashMap<ModificationPosition, Modification>();

                preparedAAModsStatements[decoy].setInt(1, pep.getPeptideId());
                rs = preparedAAModsStatements[decoy].executeQuery();

                while (rs.next()) {
                    Integer aaModId = rs.getInt("AminoAcidModificationID");


                    Modification aaMod = iModificationsMap.get(aaModId);
                    if (aaMod != null) {
                        int location = rs.getInt("Position");

                        ModificationPosition modPos = new ModificationPosition(location, false, false);
                        if (aaMod.getModificationName().contains("Phos")) {
                            phosphoModifications.put(modPos, aaMod);
                        } else {
                            peptideModifications.put(modPos, aaMod);
                        }
                    }
                }

                preparedTermModsStatements[decoy].setInt(1, pep.getPeptideId());
                rs = preparedTermModsStatements[decoy].executeQuery();

                while (rs.next()) {
                    Integer termModId = rs.getInt("TerminalModificationID");
                    Modification termMod = iModificationsMap.get(termModId);
                    if (termMod != null) {
                        boolean isNterm = false, isCterm = false;

                        if (termMod.getPositionType() == 1) {
                            isNterm = true;
                        } else {
                            isCterm = true;
                        }

                        ModificationPosition modPos = new ModificationPosition(0, isNterm, isCterm);
                        peptideModifications.put(modPos, termMod);
                    }
                }


                if (iMsfVersion.compareTo(MsfVersion.VERSION1_3) >= 0 && !pRSprobabilities.isEmpty()) {
                    List<Entry<Integer, Float>> pRSprobabilitiesSorted = new ArrayList<Entry<Integer, Float>>();
                    pRSprobabilitiesSorted.addAll(pRSprobabilities.entrySet());

                    /**
                     * Comparator for list of pRSSiteLocalisation entries, first
                     * sorts by score and in case of ties, uses the predefined
                     * location
                     */
                    class PhosphoSiteComparator implements Comparator<Entry<Integer, Float>> {

                        private Set<Integer> preassignedLocations = new HashSet<Integer>();

                        public PhosphoSiteComparator(Map<ModificationPosition, Modification> preassignedLocations) {
                            for (ModificationPosition position : preassignedLocations.keySet()) {
                                this.preassignedLocations.add(position.getPosition());
                            }
                        }

                        @Override
                        public int compare(Entry<Integer, Float> o1, Entry<Integer, Float> o2) {
                            int compared = o2.getValue().compareTo(o1.getValue()); // Highest to lowest
                            if (compared == 0) {
                                compared = (preassignedLocations.contains(o2.getKey()) ? 1 : 0) + (preassignedLocations.contains(o1.getKey()) ? -1 : 0);
                            }
                            return compared;
                        }
                    }

                    Collections.sort(pRSprobabilitiesSorted, new PhosphoSiteComparator(phosphoModifications));

                    int index = 0;

                    for (Modification phospho : phosphoModifications.values()) {
                        Entry<Integer, Float> phosphoSite = pRSprobabilitiesSorted.get(index++);
                        ModificationPosition location = new ModificationPosition(phosphoSite.getKey(), false, false);
                        peptideModifications.put(location, phospho);
                    }
                } else {
                    for (Entry<ModificationPosition, Modification> entry : phosphoModifications.entrySet()) {
                        peptideModifications.put(entry.getKey(), entry.getValue());
                    }
                }
                for (ModificationPosition position : peptideModifications.keySet()) {
                    if (pRSprobabilities.containsKey(position.getPosition())) {
                        pep.addModification(peptideModifications.get(position), position, pRSprobabilities.get(position.getPosition()));
                    } else {
                        pep.addModification(peptideModifications.get(position), position, null);
                    }
                }
            }
        }

        //remove our custom indexes
        conn.prepareStatement("drop index if exists [COMPOMICS_Peptideaminoacidmodifications_peptides];").execute();
        conn.prepareStatement("drop index if exists [COMPOMICS_Peptideterminalmodifications_peptides];").execute();
        conn.prepareStatement("drop index if exists [COMPOMICS_Peptideaminoacidmodifications_peptides_decoy];").execute();
        conn.prepareStatement("drop index if exists [COMPOMICS_Peptidterminalmodifications_peptides_decoy];").execute();
    }

    /**
     * Create a map with position information and identification probabilities
     * from a string obtained from the customdatapeptides* tables
     *
     * @param pRSString string from customdatapeptides pRS identification
     * probabilities
     * @return map with peptide locations and probabilities
     */
    private Map<Integer, Float> parsePRSIdentificationProbabilities(String pRSString) {
        String[] parts = pRSString.split(";\\s?");
        HashMap<Integer, Float> probsPerSite = new HashMap<Integer, Float>();
        Pattern p = Pattern.compile(".*\\((\\d+)\\)\\s*?:\\s*?([0-9.]+)");
        for (String part : parts) {
            Matcher m = p.matcher(part);
            if (m.matches()) {
                probsPerSite.put(Integer.parseInt(m.group(1)) - 1, Float.parseFloat(m.group(2)) / 100f);
            }
        }
        return probsPerSite;
    }

    /**
     * Close the database connection
     *
     * @throws java.sql.SQLException if any.
     */
    public void close() throws SQLException {
        iConnection.close();
    }

    private Spectrum spectrumFromSpectrumheadersQuery(ResultSet rs) throws SQLException {
        Spectrum lSpectrum = new Spectrum(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), iConnection, this);
        return lSpectrum;
    }

    /**
     * This getter gives the name of the channel for a given channelid
     *
     * @param lChannelId Int with the channelid
     * @return String with the name of the channel
     */
    public String getQuanChannelNameById(int lChannelId) {
        for (int i = 0; i < iChannelIds.size(); i++) {
            if (iChannelIds.get(i) == lChannelId) {
                return iComponents.get(i);
            }
        }
        return null;
    }

    /**
     * This getter gives the name of the raw data file for a given fileid
     *
     * @param lFileid int with the fileid
     * @return String with the name of the raw file
     */
    public String getRawfileNameByFileId(int lFileid) {
        String lResult = null;
        for (int i = 0; i < iRawFiles.size(); i++) {
            if (lFileid == iRawFiles.get(i).getFileId()) {
                lResult = iRawFiles.get(i).getFileName().substring(iRawFiles.get(i).getFileName().lastIndexOf("\\") + 1);
            }
        }
        return lResult;
    }

    /**
     * The method will parse the quantitation xml and will set the ratio types,
     * components and channelids
     */
    private void parseQuantitationXml() {
        String[] lLines = iQuantificationMethod.split("\r\n");
        boolean lRatioReporting = false;

        for (int i = 0; i < lLines.length; i++) {
            String lLine = lLines[i].trim();
            if (lLine.startsWith("<ProcessingMethod")) {
                iQuantificationMethodName = lLine.substring(lLine.indexOf("name=\"") + 6, lLine.indexOf("\"", lLine.indexOf("name=\"") + 6));
            }
            if (lLine.endsWith("selected=\"QuanLabels\">")) {
                //we have a component
                String lComponent = lLine.substring(lLine.indexOf("name=\"") + 6, lLine.indexOf("\"", lLine.indexOf("name=\"") + 6));
                String lNextChannelLine = lLines[i + 1];
                int lChannel = Integer.valueOf(lNextChannelLine.substring(lNextChannelLine.indexOf("ID\">") + 4, lNextChannelLine.indexOf("</")));
                iComponents.add(lComponent);
                iChannelIds.add(lChannel);
            }
            if (lLine.startsWith("<MethodPart name=\"RatioCalculation\"")) {
                lRatioReporting = false;
            }
            if (lRatioReporting) {
                if (lLine.startsWith("<MethodPart")) {
                    String lRatioType = lLine.substring(lLine.indexOf("name=\"") + 6, lLine.indexOf("\"", lLine.indexOf("name=\"") + 6));
                    String lNextNumeratorLine = lLines[i + 2];
                    String lNextDenominatorLine = lLines[i + 3];
                    String lNumerator = lNextNumeratorLine.substring(lNextNumeratorLine.indexOf("or\">") + 4, lNextNumeratorLine.indexOf("</"));
                    String lDenominator = lNextDenominatorLine.substring(lNextDenominatorLine.indexOf("or\">") + 4, lNextDenominatorLine.indexOf("</"));

                    iRatioTypes.add(new RatioType(lRatioType, lNumerator, lDenominator, iChannelIds, iComponents));
                }
            }
            if (lLine.startsWith("<MethodPart name=\"RatioReporting\"")) {
                lRatioReporting = true;
            }
        }
    }

    /**
     * This getter gives a vector with all the modifications found in the msf
     * file
     *
     * @return List with all the modifications found in the msf file
     */
    public List<Modification> getModifications() {
        return iModifications;
    }

    /**
     * This getter gives a hashmap with the modificationid as key and the
     * modification as value
     *
     * @return HashMap with the modificationid as key and the modification as
     * value
     */
    public Map<Integer, Modification> getModificationsMap() {
        return iModificationsMap;
    }

    /**
     * This getter gives a vector with the amino acids found in the msf file
     *
     * @return List with the amino acids found in the msf file
     */
    public List<AminoAcid> getAminoAcids() {
        return iAminoAcids;
    }

    /**
     * This getter gives a hashmap with the amino acid id as key and the amino
     * acid as value
     *
     * @return HashMap with the amino acid id as key and the amino acid as value
     */
    public Map<Integer, AminoAcid> getAminoAcidsMap() {
        return iAminoAcidsMap;
    }

    /**
     * This getter gives a vector with the enzymes found in the msf file
     *
     * @return List with the enzymes found in the msf file
     */
    public List<Enzyme> getEnzymes() {
        return iEnzymes;
    }

    /**
     * This getter gives a hashmap with the enzyme id as key and the enzyme as
     * value
     *
     * @return HashMap with the enzyme id as key and the enzyme as value
     */
    public Map<Integer, Enzyme> getEnzymesMap() {
        return iEnzymesMap;
    }

    /**
     * This getter gives a vector with the peptides found in the msf file
     *
     * @return List with the peptides found in the msf file
     */
    public List<Peptide> getPeptides() {
        return iPeptides;
    }

    /**
     * This getter gives a hashmap with the peptide id as key and the peptide as
     * value
     *
     * @return HashMap with the peptide id as key and the peptide as value
     */
    public Map<Integer, Peptide> getPeptidesMap() {
        return iPeptidesMap;
    }

    /**
     * This getter gives a vector with the taxonomies found in the msf file
     *
     * @return List with the taxonomies found in the msf file
     */
    public List<Taxonomy> getTaxonomies() {
        return iTaxonomies;
    }

    /**
     * This getter gives a hashmap with the taxonomies id as key and the
     * taxonomies as value
     *
     * @return HashMap with the taxonomies id as key and the taxonomies as value
     */
    public Map<Integer, Taxonomy> getTaxonomiesMap() {
        return iTaxonomiesMap;
    }

    /**
     * This getter gives a vector with the spectra found in the msf file
     *
     * @return List with the spectra found in the msf file
     */
    public List<Spectrum> getSpectra() {
        return iSpectra;
    }

    /**
     * This getter gives a hashmap with the spectrum id as key and the spectrum
     * as value
     *
     * @return HashMap with the spectrum id as key and the spectrum as value
     */
    public Map<Integer, Spectrum> getSpectraMapBySpectrumId() {
        return iSpectraMapBySpectrumId;
    }

    /**
     * This getter gives a hashmap with the unique spectrum id as key and the
     * spectrum as value
     *
     * @return HashMap with the unique spectrum id as key and the spectrum as
     * value
     */
    public Map<Integer, Spectrum> getSpectraMapByUniqueSpectrumId() {
        return iSpectraMapByUniqueSpectrumId;
    }

    /**
     * This getter gives a hashmap with the mass peak id as key and the spectrum
     * as value
     *
     * @return HashMap with the unique mass peak id as key and the spectrum as
     * value
     */
    public Map<Integer, Spectrum> getSpectraMapByMassPeakId() {
        return iSpectraMapByMassPeakId;
    }

    /**
     * This getter gives a string with the xml formatted quantification method
     * found in the msf file
     *
     * @return String with the xml formatted quantification method found in the
     * msf file
     */
    public String getQuantificationMethod() {
        return iQuantificationMethod;
    }

    /**
     * This getter gives a vector with the names of the different components
     * (ex. Light, Heavy, ...) found in the msf file
     *
     * @return List with the names of the different components (ex. Light,
     * Heavy, ...) found in the msf file
     */
    public List<String> getComponents() {
        return iComponents;
    }

    /**
     * This getter gives a vector with the channel ids of the different
     * components (ex. Light <b>1</b>, Heavy <b>2</b>, ...) found in the
     * msf file
     *
     * @return List with the channel ids of the different components (ex. 1,2,
     * ...) found in the msf file
     */
    public List<Integer> getChannelIds() {
        return iChannelIds;
    }

    /**
     * This getter gives a vector with the different ratio types. This are
     * created by the parsing the quantification xml
     *
     * @return List with the different ratio types
     */
    public List<RatioType> getRatioTypes() {
        return iRatioTypes;
    }

    /**
     * This getter gives a vector with the scan events found in the msf file
     *
     * @return List with the scan events found in the msf file
     */
    public List<ScanEvent> getScanEvents() {
        return iScanEvents;
    }

    /**
     * This getter gives a vector with the score types found in the msf file
     *
     * @return List with the score types found in the msf file
     */
    public List<ScoreType> getScoreTypes() {
        return iScoreTypes;
    }

    /**
     * This getter gives a vector with the proteins found in the msf file
     *
     * @return List with the proteins found in the msf file
     */
    public List<Protein> getProteins() {
        return iProteins;
    }

    /**
     * This getter gives a hashmap with the protein id as key and the protein as
     * value
     *
     * @return HashMap with the protein id as key and the protein as value
     */
    public Map<Integer, Protein> getProteinsMap() {
        return iProteinsMap;
    }

    /**
     * Returns a map with proteingroup id as keys, groups as values
     *
     * @return proteingroup id as keys, groups as values
     */
    public Map<Integer, ProteinGroup> getProteinGroupsMap() {
        return proteinGroups;
    }

    /**
     * This getter gives a vector with the custom data fields found in the msf
     * file
     *
     * @return List with the custom data fields found in the msf file
     */
    public List<CustomDataField> getCustomDataFields() {
        return iCustomDataFields;
    }

    /**
     * This getter gives a hashmap with the custom data fields id as key and the
     * custom data fields as value
     *
     * @return HashMap with the custom data fields id as key and the custom data
     * fields as value
     */
    public Map<Integer, CustomDataField> getCustomDataFieldsMap() {
        return iCustomDataFieldsMap;
    }

    /**
     * This getter gives a vector with the custom data fields use by the
     * peptides
     *
     * @return List with the custom data fields use by the peptides
     */
    public List<CustomDataField> getPeptideUsedCustomDataFields() {
        return iPeptideUsedCustomDataFields;
    }

    /**
     * This getter gives a vector with the custom data fields use by the
     * proteins
     *
     * @return List with the custom data fields use by the proteins
     */
    public List<CustomDataField> getProteinUsedCustomDataFields() {
        return iProteinUsedCustomDataFields;
    }

    /**
     * This getter gives a vector with the custom data fields use by the spectra
     *
     * @return List with the custom data fields use by the spectra
     */
    public List<CustomDataField> getSpectrumUsedCustomDataFields() {
        return iSpectrumUsedCustomDataFields;
    }

    /**
     * This getter gives the workflow info of the msf file
     *
     * @return WorkflowInfo Workflow info of the msf file
     */
    public WorkflowInfo getWorkFlowInfo() {
        return iWorkFlowInfo;
    }

    /**
     * This getter gives a vector with the filters found in the msf file
     *
     * @return List with the filters found in the msf file
     */
    public List<Filter> getFilter() {
        return iFilter;
    }

    /**
     * This getter gives a hashmap with the isotope pattern id as key and the
     * isotope pattern as value
     *
     * @return hashmap with the isotope pattern id as key and the isotope
     * pattern as value
     */
    public Map<Integer, IsotopePattern> getIsotopePatternMap() {
        return iIsotopePatternMap;
    }

    /**
     * This getter gives a vector with the quan results found in the msf file
     *
     * @return List with the quan results found in the msf file
     */
    public List<QuanResult> getQuanResults() {
        return iQuanResults;
    }

    /**
     * This getter gives a hashmap with the quan result id as key and the quan
     * result as value
     *
     * @return hashmap with the quan result id as key and the quan result as
     * value
     */
    public Map<Integer, QuanResult> getQuanResultsMap() {
        return iQuanResultsMap;
    }

    /**
     * This getter gives a vector with the raw files found in the msf file
     *
     * @return List with the raw files found in the msf file
     */
    public List<RawFile> getRawFiles() {
        return iRawFiles;
    }

    /**
     * This getter gives the sql connection to the msf file
     *
     * @return Connection to the msf file
     */
    public Connection getConnection() {
        return iConnection;
    }

    /**
     * This getter gives a vector with the chromatograms found in the msf file
     *
     * @return List with the chromatograms found in the msf file
     */
    public List<Chromatogram> getChromatograms() {
        return iChromatograms;
    }

    /**
     * This method will give a processing node based on the given number
     *
     * @param lId Int with the id of the processing node
     * @return The requested processing node
     */
    public ProcessingNode getProcessingNodeByNumber(int lId) {
        return iProcessingNodesMap.get(lId);
    }

    /**
     * <p>getProcessingNodes.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<ProcessingNode> getProcessingNodes() {
        return iProcessingNodes;
    }

    /**
     * <p>getFilePath.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFilePath() {
        return iFilePath;
    }

    /**
     * <p>getFileName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFileName() {
        String lSub = iFilePath.substring(iFilePath.lastIndexOf(System.getProperties().getProperty("file.separator")) + 1);
        return lSub;
    }

    /**
     * <p>getQuantificationMethodName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getQuantificationMethodName() {
        return iQuantificationMethodName;
    }

    /**
     * <p>getAminoAcidsFromDb.</p>
     *
     * @param iConnection a {@link java.sql.Connection} object.
     * @return a {@link java.util.List} object.
     * @throws java.sql.SQLException if any.
     */
    public List getAminoAcidsFromDb(Connection iConnection) throws SQLException {
        PreparedStatement stat = iConnection.prepareStatement("select * from AminoAcids");
        ResultSet rs = stat.executeQuery();
        while (rs.next()) {
            AminoAcid lAA = new AminoAcid(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getString(7));
            iAminoAcids.add(lAA);
            iAminoAcidsMap.put(rs.getInt(1), lAA);
        }
        rs.close();
        stat.close();
        return iAminoAcids;
    }

    /**
     * <p>addModificationsToPeptideSequence.</p>
     *
     * @param peptide a {@link com.compomics.thermo_msf_parser_API.highmeminstance.Peptide} object.
     * @param modificationMap a {@link java.util.HashMap} object.
     * @param iConnection a {@link java.sql.Connection} object.
     * @return a {@link java.lang.String} object.
     */
    public String addModificationsToPeptideSequence(Peptide peptide, HashMap modificationMap, Connection iConnection) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>createModificationMap.</p>
     *
     * @param iConnection a {@link java.sql.Connection} object.
     * @return a {@link java.util.Map} object.
     */
    public Map createModificationMap(Connection iConnection) {
        try {
            PreparedStatement stat = iConnection.prepareStatement("select * from AminoAcidModifications");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                //Modification lMod = new Modification(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getDouble(9), rs.getInt(10), rs.getInt(11));
                //iModifications.add(lMod);
                //iModificationsMap.put(rs.getInt(1), lMod);
            }
            rs = stat.executeQuery("select * from AminoAcidModificationsAminoAcids");
            while (rs.next()) {
                int lModId = rs.getInt("AminoAcidModificationID");
                int lAaId = rs.getInt("AminoAcidID");
                if (iAminoAcidsMap.get(lAaId) != null && iModificationsMap.get(lModId) != null) {
                    iModificationsMap.get(lModId).addAminoAcid(iAminoAcidsMap.get(lAaId));
                    if (iMsfVersion == MsfVersion.VERSION1_3) {
                        iModificationsMap.get(lModId).addClassificationForAminoAcid(rs.getInt("Classification"));
                    }
                }

            }
            if (iMsfVersion == MsfVersion.VERSION1_3) {
                //get the neutral losses
                rs = stat.executeQuery("select * from AminoAcidModificationsNeutralLosses");
                while (rs.next()) {
                    NeutralLoss lLoss = new NeutralLoss(rs.getInt("NeutralLossID"), rs.getString("Name"), rs.getDouble("MonoisotopicMass"), rs.getDouble("AverageMass"));
                    iNeutralLosses.add(lLoss);
                    iNeutralLossesMap.put(lLoss.getNeutralLossId(), lLoss);
                }

                //add the amino acid to the neutral losses
                rs = stat.executeQuery("select * from AminoAcidModificationsAminoAcidsNL");
                while (rs.next()) {
                    int lAaId = rs.getInt("AminoAcidID");
                    int lNlId = rs.getInt("NeutralLossID");
                    if (iAminoAcidsMap.get(lAaId) != null && iNeutralLossesMap.get(lNlId) != null) {
                        iNeutralLossesMap.get(lNlId).addAminoAcid(iAminoAcidsMap.get(lAaId));
                    }
                }
                //now add the neutral losses to the modification
                rs = stat.executeQuery("select * from AminoAcidModificationsAminoAcidsNL");
                while (rs.next()) {
                    int lModId = rs.getInt("AminoAcidModificationID");
                    int lNlId = rs.getInt("NeutralLossID");
                    if (iModificationsMap.get(lModId) != null && iNeutralLossesMap.get(lNlId) != null) {
                        iModificationsMap.get(lModId).addNeutralLoss(iNeutralLossesMap.get(lNlId));
                    }
                }
            }
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return iModificationsMap;
    }

    /**
     * <p>createEnzymeMap.</p>
     *
     * @param iConnection a {@link java.sql.Connection} object.
     * @return a {@link java.util.Map} object.
     * @throws java.sql.SQLException if any.
     */
    public Map createEnzymeMap(Connection iConnection) throws SQLException {
        PreparedStatement stat = iConnection.prepareStatement("select * from Enzymes");
        ResultSet rs = stat.executeQuery();
        while (rs.next()) {
            Enzyme lEnzyme = new Enzyme(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
            iEnzymes.add(lEnzyme);
            iEnzymesMap.put(rs.getInt("EnzymeID"), lEnzyme);
        }
        //add the specificity to the enzymes
        rs = stat.executeQuery("select * from EnzymesCleavageSpecificities");
        while (rs.next()) {
            int lEnzymeId = rs.getInt("EnzymeID");
            if (iEnzymesMap.get(lEnzymeId) != null) {
                iEnzymesMap.get(lEnzymeId).setSpecificity(rs.getInt("Specificity"));
            }
        }
        rs = stat.executeQuery("select * from EnzymesCleavageSpecificities");
        while (rs.next()) {
            int lEnzymeId = rs.getInt("EnzymeID");
            if (iEnzymesMap.get(lEnzymeId) != null) {
                iEnzymesMap.get(lEnzymeId).setSpecificity(rs.getInt("Specificity"));
            }
        }
        rs.close();
        stat.close();
        return iEnzymesMap;
    }

    /**
     * <p>getPeptides.</p>
     *
     * @param iConnection a {@link java.sql.Connection} object.
     * @param iMsfVersion a {@link com.compomics.thermo_msf_parser_API.enums.MsfVersion} object.
     * @throws java.sql.SQLException if any.
     */
    public void getPeptides(Connection iConnection, MsfVersion iMsfVersion) throws SQLException {
        PreparedStatement stat = iConnection.prepareStatement("");
        ResultSet rs = stat.executeQuery("select * from Peptides as p");
        while (rs.next()) {
            Peptide lPeptide = new Peptide(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), rs.getString("Annotation"), rs.getInt("ProcessingNodeNumber"), iAminoAcidsLetterMap);
            if (iMsfVersion == MsfVersion.VERSION1_3) {
                lPeptide.setMissedCleavage(rs.getInt("MissedCleavages"));
                lPeptide.setUniquePeptideSequenceId(rs.getInt("UniquePeptideSequenceID"));
            }
            iPeptides.add(lPeptide);
            iPeptidesMap.put(lPeptide.getPeptideId(), lPeptide);
        }

        //get the peptides
        rs = stat.executeQuery("select * from Peptides_decoy as p");
        while (rs.next()) {
            Peptide lPeptide = new Peptide(rs.getInt("PeptideID"), rs.getInt("SpectrumID"), rs.getInt("ConfidenceLevel"), rs.getString("Sequence"), rs.getInt("TotalIonsCount"), rs.getInt("MatchedIonsCount"), "", rs.getInt("ProcessingNodeNumber"), iAminoAcidsLetterMap);
            if (iMsfVersion == MsfVersion.VERSION1_3) {
                lPeptide.setMissedCleavage(rs.getInt("MissedCleavages"));
                lPeptide.setUniquePeptideSequenceId(rs.getInt("UniquePeptideSequenceID"));
                lPeptide.setAnnotation(rs.getString("Annotation"));
            }
            iPeptidesDecoy.add(lPeptide);
            iPeptidesDecoyMap.put(lPeptide.getPeptideId(), lPeptide);
        }
        if (iMsfVersion == MsfVersion.VERSION1_3) {
            //add the processing node Custom data fields
            rs = stat.executeQuery("select * from CustomDataPeptides_Decoy");
            while (rs.next()) {
                if (iPeptidesDecoyMap.get(rs.getInt("PeptideID")) != null) {
                    iPeptidesDecoyMap.get(rs.getInt("PeptideID")).addCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
                }
            }
            rs = stat.executeQuery("select fieldid from CustomDataPeptides_Decoy group by fieldid");
            while (rs.next()) {
                iPeptideDecoyUsedCustomDataFields.add(iCustomDataFieldsMap.get(rs.getInt("FieldID")));
            }
        }
    }

    /**
     * <p>getPeptidesForProtein.</p>
     *
     * @param protein a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem} object.
     * @param iConnection a {@link java.sql.Connection} object.
     * @param iMsfVersion a {@link com.compomics.thermo_msf_parser_API.enums.MsfVersion} object.
     * @param iAminoAcids a {@link java.util.List} object.
     * @return a {@link java.util.List} object.
     * @throws java.sql.SQLException if any.
     */
    public List<Peptide> getPeptidesForProtein(ProteinLowMem protein, Connection iConnection, MsfVersion iMsfVersion, List<AminoAcid> iAminoAcids) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>getPeptidesForProtein.</p>
     *
     * @param protein a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem} object.
     * @param iMsfVersion a {@link com.compomics.thermo_msf_parser_API.enums.MsfVersion} object.
     * @param iAminoAcids a {@link java.util.List} object.
     * @return a {@link java.util.List} object.
     * @throws java.sql.SQLException if any.
     */
    public List<Peptide> getPeptidesForProtein(ProteinLowMem protein, MsfVersion iMsfVersion, List<AminoAcid> iAminoAcids) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>getPeptidesForAccession.</p>
     *
     * @param lProteinAccession a {@link java.lang.String} object.
     * @param iMsfVersion a {@link com.compomics.thermo_msf_parser_API.enums.MsfVersion} object.
     * @param iConnection a {@link java.sql.Connection} object.
     * @param iAminoAcids a {@link java.util.List} object.
     * @return a {@link java.util.List} object.
     */
    public List<Peptide> getPeptidesForAccession(String lProteinAccession, MsfVersion iMsfVersion, Connection iConnection, List<AminoAcid> iAminoAcids) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>getInformationForPeptide.</p>
     *
     * @param peptideID a int.
     * @param iConnection a {@link java.sql.Connection} object.
     * @param fullInfo a boolean.
     * @return a {@link java.util.List} object.
     */
    public List getInformationForPeptide(int peptideID, Connection iConnection, boolean fullInfo) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>getSpectrumXMLForPeptide.</p>
     *
     * @param peptideOfInterest a {@link com.compomics.thermo_msf_parser_API.highmeminstance.Peptide} object.
     * @return a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     * @throws java.sql.SQLException if any.
     */
    public String getSpectrumXMLForPeptide(Peptide peptideOfInterest) throws IOException, SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>getSpectrumForPeptide.</p>
     *
     * @param peptideOfInterest a {@link com.compomics.thermo_msf_parser_API.highmeminstance.Peptide} object.
     * @return a {@link com.compomics.thermo_msf_parser_API.highmeminstance.Spectrum} object.
     * @throws java.sql.SQLException if any.
     */
    public Spectrum getSpectrumForPeptide(Peptide peptideOfInterest) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>getSpectrumXMLForPeptide.</p>
     *
     * @param peptideOfInterest a {@link com.compomics.thermo_msf_parser_API.highmeminstance.Peptide} object.
     * @param iConnection a {@link java.sql.Connection} object.
     * @return a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     * @throws java.sql.SQLException if any.
     */
    public String getSpectrumXMLForPeptide(Peptide peptideOfInterest, Connection iConnection) throws IOException, SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>getSpectrumForPeptide.</p>
     *
     * @param peptideOfInterest a {@link com.compomics.thermo_msf_parser_API.highmeminstance.Peptide} object.
     * @param iConnection a {@link java.sql.Connection} object.
     * @return a {@link com.compomics.thermo_msf_parser_API.highmeminstance.Spectrum} object.
     * @throws java.sql.SQLException if any.
     */
    public Spectrum getSpectrumForPeptide(Peptide peptideOfInterest, Connection iConnection) throws SQLException {
        PreparedStatement stat = iConnection.prepareStatement("select s.*, m.FileID from spectrumheaders as s, masspeaks as m where m.masspeakid = s.masspeakid");
        ResultSet rs = stat.executeQuery();
        while (rs.next()) {
            Spectrum lSpectrum = new Spectrum(rs.getInt("SpectrumID"), rs.getInt("UniqueSpectrumID"), rs.getInt("MassPeakID"), rs.getInt("LastScan"), rs.getInt("FirstScan"), rs.getInt("ScanNumbers"), rs.getInt("Charge"), rs.getDouble("RetentionTime"), rs.getDouble("Mass"), rs.getInt("ScanEventID"), iConnection, this);
            lSpectrum.setFileId(rs.getInt("FileID"));
            iSpectra.add(lSpectrum);
            iSpectraMapByMassPeakId.put(lSpectrum.getMassPeakId(), lSpectrum);
            iSpectraMapBySpectrumId.put(lSpectrum.getSpectrumId(), lSpectrum);
            iSpectraMapByUniqueSpectrumId.put(lSpectrum.getUniqueSpectrumId(), lSpectrum);
        }

        if (!iLowMemory) {
            rs = stat.executeQuery("select * from Spectra");
            while (rs.next()) {
                int lSpectrumId = rs.getInt("UniqueSpectrumID");
                byte[] lZipped = rs.getBytes("Spectrum");
                if (iSpectraMapByUniqueSpectrumId.get(lSpectrumId) != null) {
                    iSpectraMapByUniqueSpectrumId.get(lSpectrumId).setZippedBytes(lZipped);
                }
            }
        }
        rs = stat.executeQuery("select * from SpectrumScores");
        while (rs.next()) {
            int lSpectrumId = rs.getInt("SpectrumID");
            if (iSpectraMapBySpectrumId.get(lSpectrumId) != null) {
                iSpectraMapBySpectrumId.get(lSpectrumId).addSpectrumScore(rs.getDouble("Score"), rs.getInt("ProcessingNodeNumber"));
            }
        }
        //get the scan events
        rs = stat.executeQuery("select * from ScanEvents");
        while (rs.next()) {
            ScanEvent lScanEvent = new ScanEvent(rs.getInt("ScanEventID"), rs.getInt("MSLevel"), rs.getInt("Polarity"), rs.getInt("ScanType"), rs.getInt("Ionization"), rs.getInt("MassAnalyzer"), rs.getInt("ActivationType"));
            iScanEvents.add(lScanEvent);
        }
        for (int i = 0; i < iSpectra.size(); i++) {
            for (int j = 0; j < iScanEvents.size(); j++) {
                if (iSpectra.get(i).getScanEventId() == iScanEvents.get(j).getScanEventId()) {
                    iSpectra.get(j).setScanEvent(iScanEvents.get(j));
                }
            }
        }
        return null;
    }

    /**
     * <p>getAllProteins.</p>
     *
     * @param iConnection a {@link java.sql.Connection} object.
     * @return a {@link java.util.Iterator} object.
     * @throws java.sql.SQLException if any.
     */
    public Iterator getAllProteins(Connection iConnection) throws SQLException {
        PreparedStatement stat = iConnection.prepareStatement("select * from Proteins");
        ResultSet rs = stat.executeQuery();
        while (rs.next()) {
            Protein lProtein = null;
            if (iLowMemory) {
                lProtein = new Protein(rs.getInt("ProteinID"), this);
                if (iMsfVersion == MsfVersion.VERSION1_3) {
                    lProtein.setMasterProtein(rs.getInt("IsMasterProtein"));
                }
            } else {
                lProtein = new Protein(rs.getInt("ProteinID"), rs.getString("Sequence"));
            }
            iProteins.add(lProtein);
            iProteinsMap.put(lProtein.getProteinId(), lProtein);
        }
        //add the description to the protein
        rs = stat.executeQuery("select  * from  ProteinAnnotations ");
        while (rs.next()) {
            if (iProteinsMap.get(rs.getInt("ProteinID")) != null) {
                iProteinsMap.get(rs.getInt("ProteinID")).setDescription(rs.getString("Description"));
            }
        }
        //add the score to the protein
        rs = stat.executeQuery("select  * from  ProteinScores ");
        while (rs.next()) {
            if (iProteinsMap.get(rs.getInt("ProteinID")) != null) {
                iProteinsMap.get(rs.getInt("ProteinID")).addScore(rs.getDouble("ProteinScore"), rs.getInt("ProcessingNodeNumber"), rs.getDouble("Coverage"));
            }
        }

        if (iMsfVersion == MsfVersion.VERSION1_3) {
            //add the protein group to the protein
            rs = stat.executeQuery("select  * from  ProteinsProteinGroups");
            while (rs.next()) {
                if (iProteinsMap.get(rs.getInt("ProteinID")) != null) {
                    iProteinsMap.get(rs.getInt("ProteinID")).setProteinGroupId(rs.getInt("ProteinGroupID"));
                }
            }

            rs = stat.executeQuery("select  * from  PtmAnnotationData");
            while (rs.next()) {
                Protein.PtmAnnotation lAnno = new Protein.PtmAnnotation(rs.getInt("PtmUnimodID"), rs.getInt("Position"));
                if (iProteinsMap.get(rs.getInt("ProteinID")) != null) {
                    iProteinsMap.get(rs.getInt("ProteinID")).addPtmAnnotation(lAnno);
                }
            }

            //add the custom data fields to the proteins
            rs = stat.executeQuery("select  * from  CustomDataProteins_Decoy ");
            while (rs.next()) {
                if (iProteinsMap.get(rs.getInt("ProteinID")) != null) {
                    iProteinsMap.get(rs.getInt("ProteinID")).addDecoyCustomDataField(rs.getInt("FieldID"), rs.getString("FieldValue"));
                }
            }

            //add the decoy score to the protein
            rs = stat.executeQuery("select  * from  ProteinScores_Decoy");
            while (rs.next()) {
                if (iProteinsMap.get(rs.getInt("ProteinID")) != null) {
                    iProteinsMap.get(rs.getInt("ProteinID")).addDecoyScore(rs.getDouble("ProteinScore"), rs.getInt("ProcessingNodeNumber"), rs.getDouble("Coverage"));
                }
            }
        }
        return iProteins.iterator();
    }

    /**
     * <p>getProteinFromAccession.</p>
     *
     * @param proteinAccession a {@link java.lang.String} object.
     * @param iConnection a {@link java.sql.Connection} object.
     * @return a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem} object.
     * @throws java.sql.SQLException if any.
     */
    public ProteinLowMem getProteinFromAccession(String proteinAccession, Connection iConnection) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>getAccessionFromProteinID.</p>
     *
     * @param proteinID a int.
     * @param iConnection a {@link java.sql.Connection} object.
     * @return a {@link java.lang.String} object.
     * @throws java.sql.SQLException if any.
     */
    public String getAccessionFromProteinID(int proteinID, Connection iConnection) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>getSequenceForProteinID.</p>
     *
     * @param proteinID a int.
     * @param iConnection a {@link java.sql.Connection} object.
     * @return a {@link java.lang.String} object.
     * @throws java.sql.SQLException if any.
     */
    public String getSequenceForProteinID(int proteinID, Connection iConnection) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>getProteinsForPeptide.</p>
     *
     * @param PeptideID a int.
     * @param iConnection a {@link java.sql.Connection} object.
     * @return a {@link java.util.List} object.
     * @throws java.sql.SQLException if any.
     */
    public List<ProteinLowMem> getProteinsForPeptide(int PeptideID, Connection iConnection) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>parseRatioTypes.</p>
     *
     * @param iConnection a {@link java.sql.Connection} object.
     * @return a {@link java.util.List} object.
     */
    public List<RatioTypeLowMem> parseRatioTypes(Connection iConnection) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>getRawFileNameForFileID.</p>
     *
     * @param fileID a int.
     * @return a {@link java.lang.String} object.
     */
    public String getRawFileNameForFileID(int fileID) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>createSpectrumXMLForPeptide.</p>
     *
     * @param peptideID a int.
     * @param iConnection a {@link java.sql.Connection} object.
     * @return a {@link java.lang.String} object.
     * @throws java.sql.SQLException if any.
     * @throws java.io.IOException if any.
     */
    public String createSpectrumXMLForPeptide(int peptideID, Connection iConnection) throws SQLException, IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>getMSMSPeaks.</p>
     *
     * @param lXml a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     * @throws java.lang.Exception if any.
     */
    public List<Peak> getMSMSPeaks(String lXml) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>getMSPeaks.</p>
     *
     * @param lXml a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     * @throws java.lang.Exception if any.
     */
    public List<Peak> getMSPeaks(String lXml) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>getFragmentedMsPeak.</p>
     *
     * @param lXml a {@link java.lang.String} object.
     * @return a {@link com.compomics.thermo_msf_parser_API.highmeminstance.Peak} object.
     * @throws java.lang.Exception if any.
     */
    public Peak getFragmentedMsPeak(String lXml) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>getRawFileForFileID.</p>
     *
     * @param fileID a int.
     * @param iconn a {@link java.sql.Connection} object.
     * @return a {@link java.util.HashMap} object.
     */
    public HashMap<Integer, String> getRawFileForFileID(int fileID, Connection iconn) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>getRawFileNameForFileID.</p>
     *
     * @param FileID a int.
     * @param iConn a {@link java.sql.Connection} object.
     * @return a {@link java.lang.String} object.
     */
    public String getRawFileNameForFileID(int FileID, Connection iConn) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * <p>hasPhosphoRS.</p>
     *
     * @return a boolean.
     */
    public boolean hasPhosphoRS() {
        return hasPhosphoRS;
    }

    /**
     * <p>getFastaFiles.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<String> getFastaFiles() {
        return iFastaFiles;
    }

    /**
     * <p>getMajorScoreTypes.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<ScoreType> getMajorScoreTypes() {
        return iMajorScoreTypes;
    }
    
    
}
