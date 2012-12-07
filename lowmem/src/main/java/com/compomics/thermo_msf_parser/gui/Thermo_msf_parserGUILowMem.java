package com.compomics.thermo_msf_parser.gui;

//TODO: remember last selection folder
import com.compomics.thermo_msf_parser.msf.*;
import com.compomics.thermo_msf_parser.msf.Event;
import com.compomics.thermo_msf_parser.msf.proteinsorter.ProteinSorter;
import com.compomics.util.Util;
import com.compomics.util.examples.HelpWindow;
import com.compomics.util.gui.UtilitiesGUIDefaults;
import com.compomics.util.gui.protein.ProteinSequencePane;
import com.compomics.util.gui.spectrum.DefaultSpectrumAnnotation;
import com.compomics.util.io.StartBrowser;
import no.uib.jsparklines.renderers.JSparklinesBarChartTableCellRenderer;
import no.uib.jsparklines.renderers.JSparklinesIntegerColorTableCellRenderer;
import no.uib.jsparklines.renderers.JSparklinesIntervalChartTableCellRenderer;
import no.uib.jsparklines.renderers.util.GradientColorCoding;
import org.apache.log4j.Logger;
import org.jfree.chart.plot.PlotOrientation;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 4/25/12 Time: 1:45 PM
 */
public class Thermo_msf_parserGUILowMem extends JFrame implements Observer {

    // Class specific log4j logger for Thermo_msf_parserGUI instances.
    private static Logger logger = Logger.getLogger(Thermo_msf_parserGUILowMem.class);
    //gui elements
    private JPanel jpanContent;
    private JTabbedPane jtabpanSpectrum;
    private JTabbedPane jtabpanLower;
    private JPanel jpanMSMS;
    private JPanel jpanMSHolder;
    private JTable jtablePeptides;
    private JScrollPane jscollPeptides;
    private JCheckBox aIonsJCheckBox;
    private JCheckBox bIonsJCheckBox;
    private JCheckBox cIonsJCheckBox;
    private JCheckBox xIonsJCheckBox;
    private JCheckBox yIonsJCheckBox;
    private JCheckBox zIonsJCheckBox;
    private JCheckBox chargeOneJCheckBox;
    private JCheckBox chargeTwoJCheckBox;
    private JCheckBox chargeOverTwoJCheckBox;
    private JPanel jpanMSMSLeft;
    private JCheckBox nh3IonsJCheckBox;
    private JCheckBox h2oIonsJCheckBox;
    private JTextField txtMSMSerror;
    private JTabbedPane jtabChromatogram;
    private JPanel jpanMS;
    private JPanel jpanProtein;
    private JPanel jpanQuantitationSpectrum;
    private JPanel jpanQuantificationSpectrumHolder;
    private JButton showAllPeptidesButton;
    private JList proteinList;
    private JButton jbuttonNumberSort;
    private JButton jbuttonAlphabeticalSort;
    private JPanel jpanProteinLeft;
    private JScrollPane proteinCoverageJScrollPane;
    private JLabel sequenceCoverageJLabel;
    private JCheckBox chbHighConfident;
    private JCheckBox chbMediumConfident;
    private JCheckBox chbLowConfident;
    private JProgressBar progressBar;
    private JTabbedPane processingNodeTabbedPane;
    private JCheckBox chromatogramCheckBox;
    private JCheckBox msCheckBox;
    private JCheckBox quantCheckBox;
    private JCheckBox msmsCheckBox;
    private JRadioButton onlyHighestScoringRadioButton;
    private JRadioButton onlyLowestScoringRadioButton;
    private JRadioButton allRadioButton;
    private JSplitPane split1;
    private JSplitPane split2;
    private JCheckBox peptideInformationChb;
    private JButton startRoverButton;
    private JPanel contentPane;
    private JTabbedPane jSuperTabbedPane;
    private JList selectedProteinList;
    private JLabel lblProteinOfIntersest;
    private JEditorPane proteinSequenceCoverageJEditorPane;
    private SpectrumPanel iMSMSspectrumPanel;
    private SpectrumPanel iMSspectrumPanel;
    private SpectrumPanel iQuantificationSpectrumPanel;
    private DefaultListModel selectedProteinListModel = new DefaultListModel();
    /**
     * A vector with the absolute paths to the msf file
     */
    private Vector<File> iMsfFileLocations = new Vector<File>();
    /**
     * A Vector with the parsed msf files
     */
    private Vector<MsfFile> iParsedMsfs = new Vector<MsfFile>();
    /**
     * A Vector with the different scoretypes found in the different files
     */
    private Vector<ScoreTypeLowMem> iMergedPeptidesScores;
    /**
     * The currently selected peptide
     */
    private PeptideLowMem iSelectedPeptide;
    /**
     * The currently selected protein
     */
    private ProteinLowMem iSelectedProtein;
    /**
     * The msms fragmentation error
     */
    private double iMSMSerror = 0.5;
    /**
     * Vector with all the proteins
     */
    private Vector<ProteinLowMem> iProteins = new Vector<ProteinLowMem>();
    /**
     * Vector with all the proteins displayed in the protein list
     */
    private Vector<ProteinLowMem> iDisplayedProteins = new Vector<ProteinLowMem>();
    /**
     * Vector with all the proteins displayed in the protein list
     */
    private Vector<ProteinLowMem> iDisplayedProteinsOfInterest = new Vector<ProteinLowMem>();
    /**
     * The different custom data fields used for peptides in all the files
     */
    private ArrayList<CustomDataField> iMergedCustomPeptideData = new ArrayList<CustomDataField>();
    /**
     * The different custom data fields used for spectra in all the files
     */
    private Vector<CustomDataField> iMergedCustomSpectrumData = new Vector<CustomDataField>();
    /**
     * The different ratio types found in the msf files
     */
    private Vector<RatioTypeLowMem> iMergedRatioTypes = new Vector<RatioTypeLowMem>();
    /**
     * The major score type
     */
    private Vector<ScoreTypeLowMem> iMajorScoreTypes = new Vector<ScoreTypeLowMem>();
    /**
     * Boolean that indicates if this is a stand alone window
     */
    private boolean iStandAlone;
    /**
     * Boolean that indicates if quantifications are found in the msf files
     */
    private boolean iQuantitationFound = false;
    /**
     * instance of the ProteinLowMem
     */
    private ProteinLowMemController proteinLowMemInstance = new ProteinLowMemController();
    /*
     * a msf file
     */
    private MsfFile msfFile;
    /**
     * msfversion enumeration object
     */
    private MsfVersion iMsfVersion;
    /**
     * resultset
     */
    private ResultSet rs;
    /**
     * the Prepared statement
     */
    private PreparedStatement stat;
    /**
     * instance ProcessingNodeLowMem
     */
    private ProcessingNodeLowMemController processingNodeLowMemInstance = new ProcessingNodeLowMemController();
    /**
     * instance peptideLowMem
     */
    private PeptideLowMemController peptideLowMemInstance = new PeptideLowMemController();
    /**
     * vector of amino acids in the msf files
     */
    private Vector<AminoAcid> iAminoAcids;
    /**
     * instance for protein sorter
     */
    ProteinSorter lProtSorter = new ProteinSorter();
    /**
     * instance scoretypelowmem
     */
    private ScoreTypeLowMemController scoreTypeLowMemInstance = new ScoreTypeLowMemController();
    private RatioTypeLowMemController ratioTypeLowMemInstance = new RatioTypeLowMemController();
    private CustomDataLowMemController customDataLowMemControllerInstance = new CustomDataLowMemController();
    private SpectrumLowMemController spectrumLowMemInstance = new SpectrumLowMemController();
    private ChromatogramLowMemController chromatogramLowMemInstance = new ChromatogramLowMemController();
    private HashMap<Integer, CustomDataField> customData;
    private RawFileLowMemController rawFileLowMemInstance = new RawFileLowMemController();
    private ProcessingNodeLowMemController ProcessingNodeLowMemInstance = new ProcessingNodeLowMemController();
    private ProgressBarMiddleMan progressBarIntFiller = new ProgressBarMiddleMan(peptideLowMemInstance, proteinLowMemInstance);

    /**
     * The constructor
     *
     * @param lStandAlone
     */
    public Thermo_msf_parserGUILowMem(boolean lStandAlone) {

        this.iStandAlone = lStandAlone;

        //create the gui
        jtablePeptides = new JTable();
        jscollPeptides = new JScrollPane();
        chbHighConfident = new JCheckBox("High");
        chbHighConfident.setSelected(true);
        chbMediumConfident = new JCheckBox("Medium");
        chbMediumConfident.setSelected(true);
        chbLowConfident = new JCheckBox("Low");
        onlyHighestScoringRadioButton = new JRadioButton();
        onlyLowestScoringRadioButton = new JRadioButton();
        allRadioButton = new JRadioButton();
        proteinList = new JList(iDisplayedProteins);
        proteinList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectedProteinList = new JList(iDisplayedProteinsOfInterest);
        selectedProteinList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectedProteinList.setModel(selectedProteinListModel);
        $$$setupUI$$$();

        // Create the menu bar
        final JMenuBar menuBar = new JMenuBar();

        // Create a menu
        final JMenu lFileMenu = new JMenu("File");
        lFileMenu.setMnemonic('F');
        menuBar.add(lFileMenu);
        final JMenu lExportMenu = new JMenu("Export");
        lExportMenu.setMnemonic('E');
        menuBar.add(lExportMenu);
        final JMenu lInfoMenu = new JMenu("Help");
        lInfoMenu.setMnemonic('H');
        menuBar.add(lInfoMenu);

        // Create a menu item
        final JMenuItem lOpenItem = new JMenuItem("Open");
        lOpenItem.setMnemonic('O');
        lOpenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        lOpenItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadData(true);
            }
        });
        final JMenuItem lCloseItem = new JMenuItem("Close");
        lCloseItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeMethod();
            }
        });
        final JMenuItem lAboutItem = new JMenuItem("About");
        lAboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HelpWindow lHelp = new HelpWindow(getFrame(), getClass().getResource("/about.html"));
                lHelp.setTitle("About Thermo MSF Viewer");
            }
        });
        lInfoMenu.add(lAboutItem);
        final JMenuItem lHelpItem = new JMenuItem("Help");
        lHelpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        lHelpItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int lResult = JOptionPane.showOptionDialog(getFrame(),
                        "Are you experiencing unexpected failures or errors?\n"
                        + "Or have a question about the program?",
                        "Help",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        UIManager.getIcon("OptionPane.errorIcon"),
                        new Object[]{"Yes, report an issue!", "Cancel"},
                        "Report Issue");

                if (lResult == JOptionPane.OK_OPTION) {
                    String lIssuesPage = "http://code.google.com/p/thermo-msf-parser/issues";
                    StartBrowser.start(lIssuesPage);
                }
            }
        });
        lInfoMenu.add(lHelpItem);

        // Create a menu item
        final JMenuItem item = new JMenuItem("Export Peptides as CSV");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //open file chooser
                final String lPath;
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showSaveDialog(getFrame());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    if (fc.getSelectedFile().getAbsolutePath().endsWith(".csv")) {
                        lPath = fc.getSelectedFile().getAbsolutePath();
                    } else {
                        lPath = fc.getSelectedFile().getAbsolutePath() + ".csv";
                    }
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Save command cancelled by user.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                com.compomics.util.sun.SwingWorker lCsvSaver = new com.compomics.util.sun.SwingWorker() {
                    public Boolean construct() {
                        //create the writer
                        BufferedWriter out = null;
                        try {
                            out = new BufferedWriter(new FileWriter(lPath));

                            //write column headers
                            String lLine = "";
                            for (int j = 0; j < jtablePeptides.getModel().getColumnCount(); j++) {

                                lLine = lLine + jtablePeptides.getModel().getColumnName(j) + ",";
                            }
                            out.write(lLine + "\n");

                            //write data
                            for (int i = 0; i < jtablePeptides.getModel().getRowCount(); i++) {
                                lLine = "confidence_level";
                                for (int j = 0; j < jtablePeptides.getModel().getColumnCount(); j++) {

                                    lLine = lLine + jtablePeptides.getModel().getValueAt(i, j) + ",";
                                }
                                out.write(lLine + "\n");
                            }

                            out.flush();
                            out.close();
                        } catch (IOException e1) {
                            logger.info(e1);
                            JOptionPane.showMessageDialog(new JFrame(), "There was a problem saving your data!", "Problem saving", JOptionPane.ERROR_MESSAGE);
                        }
                        finally{
                            try {
                                out.close();
                            } catch (IOException ex) {
                                java.util.logging.Logger.getLogger(Thermo_msf_parserGUILowMem.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        return true;
                    }

                    @Override
                    public void finished() {
                        JOptionPane.showMessageDialog(new JFrame(), "Saving done", "Info", JOptionPane.INFORMATION_MESSAGE);

                    }
                };
                lCsvSaver.start();


            }
        });


        // Create a menu item
        final JMenuItem lItemMgf = new JMenuItem("Export Spectra as MGF");
        lItemMgf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //open file chooser
                final String lPath;
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showSaveDialog(getFrame());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    if (fc.getSelectedFile().getAbsolutePath().endsWith(".mgf")) {
                        lPath = fc.getSelectedFile().getAbsolutePath();
                    } else {
                        lPath = fc.getSelectedFile().getAbsolutePath() + ".mgf";
                    }
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Save command cancelled by user.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                com.compomics.util.sun.SwingWorker lMgfSaver = new com.compomics.util.sun.SwingWorker() {
                    public Boolean construct() {
                        //create the writer
                        Vector<SpectrumLowMem> lTotalSpectra = new Vector<SpectrumLowMem>();
                        try {
                            for (MsfFile iParsedMsf1 : iParsedMsfs) {
                                lTotalSpectra.addAll(spectrumLowMemInstance.getAllSpectra(iParsedMsf1.getConnection()));
                            }
                            progressBar.setMaximum(lTotalSpectra.size() + 1);
                            progressBar.setValue(0);
                            progressBar.setString("Writing all spectra to " + lPath);
                            progressBar.setStringPainted(true);
                            progressBar.setVisible(true);
                            BufferedWriter out = new BufferedWriter(new FileWriter(lPath));
                            StringBuilder stringBuffer = new StringBuilder();
                            //if works use counting for this
                            for (MsfFile iParsedMsf : iParsedMsfs) {
                                for (SpectrumLowMem lSpectrum : lTotalSpectra) {
                                    stringBuffer.append("BEGIN IONS\nTITLE=");
                                    stringBuffer.append(spectrumLowMemInstance.getSpectrumTitle(rawFileLowMemInstance.getRawFileNameForFileID(lSpectrum.getFileId(), iParsedMsf.getConnection()), lSpectrum));
                                    stringBuffer.append("\n");
                                    spectrumLowMemInstance.createSpectrumXMLForSpectrum(lSpectrum);
                                    Peak lMono = spectrumLowMemInstance.getFragmentedMsPeak(lSpectrum.getSpectrumXML());
                                    stringBuffer.append("PEPMASS=");
                                    stringBuffer.append(lMono.getX());
                                    stringBuffer.append("\t");
                                    stringBuffer.append(lMono.getY());
                                    stringBuffer.append("\n");
                                    stringBuffer.append("CHARGE=");
                                    stringBuffer.append(lSpectrum.getCharge());
                                    stringBuffer.append("\n");
                                    stringBuffer.append("RTINSECONDS=");
                                    stringBuffer.append(lSpectrum.getRetentionTime() / 60.0);
                                    stringBuffer.append("\n");
                                    if (lSpectrum.getFirstScan() != lSpectrum.getFirstScan()) {
                                        stringBuffer.append("SCANS="); 
                                        stringBuffer.append(lSpectrum.getFirstScan());
                                        stringBuffer.append("."); 
                                        stringBuffer.append(lSpectrum.getLastScan()); 
                                        stringBuffer.append("\n");
                                    } else {
                                        stringBuffer.append("SCANS="); 
                                        stringBuffer.append(lSpectrum.getFirstScan());
                                        stringBuffer.append("\n");
                                    }
                                    Vector<Peak> lMSMS = spectrumLowMemInstance.getMSMSPeaks(lSpectrum.getSpectrumXML());
                                    for (Peak lMSM : lMSMS) {
                                        stringBuffer.append(lMSM.getX());
                                        stringBuffer.append("\t");
                                        stringBuffer.append(lMSM.getY());
                                        stringBuffer.append("\n");
                                    }
                                    stringBuffer.append("END IONS\n\n");
                                    out.write(stringBuffer.toString());
                                    progressBar.setValue(progressBar.getValue() + 1);
                                }
                            }
                            out.flush();
                            out.close();
                        } catch (Exception e1) {
                            logger.info(e1);
                            progressBar.setVisible(false);
                            JOptionPane.showMessageDialog(new JFrame(), "There was a problem saving your data!", "Problem saving", JOptionPane.ERROR_MESSAGE);
                        }
                        return true;
                    }

                    @Override
                    public void finished() {
                        JOptionPane.showMessageDialog(new JFrame(), "Saving done", "Info", JOptionPane.INFORMATION_MESSAGE);
                        progressBar.setVisible(false);
                    }
                };
                lMgfSaver.start();
                progressBar.setString("");
            }
        });


        //add the menuitems to the menu
        lFileMenu.add(lOpenItem);
        lFileMenu.add(lCloseItem);
        lExportMenu.add(item);
        lExportMenu.add(lItemMgf);

        // Install the menu bar in the frame
        this.setJMenuBar(menuBar);

        //create JFrame parameters
        this.setTitle("Thermo MSF Viewer - v" + getVersion());
        this.setContentPane(contentPane);
        this.setLocationRelativeTo(null);
        this.setMinimumSize(new Dimension(1200, 800));
        this.setPreferredSize(new Dimension(1200, 800));
        this.setMaximumSize(new Dimension(1200, 800));
        this.setIconImage(new ImageIcon(getClass().getResource("/logo.png")).getImage());
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setVisible(true);
        this.update(this.getGraphics());
        split1.setDividerLocation(split1.getHeight() / 2);
        split2.setDividerLocation(split2.getWidth() / 2);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                split1.setDividerLocation(split1.getHeight() / 2);
                split2.setDividerLocation(split2.getWidth() / 2);
            }
        });

        //add action listeners
        peptideInformationChb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedProteinList.isSelectionEmpty()) {
                    createPeptideTable(null);
                } else {
                    createPeptideTable(iSelectedProtein);
                }
                jtablePeptides.updateUI();
            }
        });
        showAllPeptidesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iSelectedProtein = null;
                proteinSequenceCoverageJEditorPane.setText("");
                sequenceCoverageJLabel.setText("");
                new Thread(new Runnable() {
                    public void run() {
                        setGuiElementsResponsive(false);
                        progressBar.setVisible(true);
                        createPeptideTable(null);
                        iDisplayedProteinsOfInterest.removeAllElements();
                        proteinList.clearSelection();
                        selectedProteinList.clearSelection();
                        proteinList.updateUI();
                        jtablePeptides.updateUI();
                        selectedProteinList.updateUI();
                        progressBar.setVisible(false);
                        setGuiElementsResponsive(true);
                    }
                }).start();
            }
        });

        jbuttonAlphabeticalSort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (jbuttonAlphabeticalSort.getText().startsWith("A")) {
                    lProtSorter.compareProteinByAccession(true);
                    Collections.sort(iDisplayedProteins, lProtSorter);
                    jbuttonAlphabeticalSort.setText("Z -> A");
                    proteinList.updateUI();
                    selectedProteinList.updateUI();
                } else {
                    lProtSorter.compareProteinByAccession(false);
                    Collections.sort(iDisplayedProteins, lProtSorter);
                    jbuttonAlphabeticalSort.setText("A -> Z");
                    proteinList.updateUI();
                    selectedProteinList.updateUI();
                }
            }
        });
        jbuttonNumberSort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (jbuttonNumberSort.getText().startsWith("1")) {
                    lProtSorter.compareProteinByNumberOfPeptides(false);
                    Collections.sort(iDisplayedProteins, lProtSorter);
                    jbuttonNumberSort.setText("20 -> 1");
                    proteinList.updateUI();
                    selectedProteinList.updateUI();
                } else {
                    lProtSorter.compareProteinByNumberOfPeptides(true);
                    Collections.sort(iDisplayedProteins, lProtSorter);
                    jbuttonNumberSort.setText("1 -> 20");
                    proteinList.updateUI();
                    selectedProteinList.updateUI();
                }
            }
        });
        ActionListener chbChangeActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        progressBar.setValue(0);
                        progressBar.setVisible(true);
                        setGuiElementsResponsive(false);
                        createPeptideTable(iSelectedProtein);
                        if (iSelectedProtein != null) {
                            formatProteinSequence(iSelectedProtein);
                        }
                        proteinList.updateUI();
                        selectedProteinList.updateUI();
                        jtablePeptides.updateUI();
                        progressBar.setVisible(false);
                        setGuiElementsResponsive(true);
                    }
                }).start();
            }
        };
        ActionListener spectrumViewChange = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    peptidesTableMouseClicked(null);
                } catch (SQLException e1) {
                    logger.error(e1);
                }
            }
        };

        chbHighConfident.addActionListener(chbChangeActionListener);
        chbMediumConfident.addActionListener(chbChangeActionListener);
        chbLowConfident.addActionListener(chbChangeActionListener);
        allRadioButton.addActionListener(chbChangeActionListener);
        onlyHighestScoringRadioButton.addActionListener(chbChangeActionListener);
        onlyLowestScoringRadioButton.addActionListener(chbChangeActionListener);
        chromatogramCheckBox.addActionListener(spectrumViewChange);
        msCheckBox.addActionListener(spectrumViewChange);
        msmsCheckBox.addActionListener(spectrumViewChange);
        quantCheckBox.addActionListener(spectrumViewChange);

        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                if (index == 0) {
                    menuBar.setVisible(true);
                } else {
                    menuBar.setVisible(false);
                }
            }
        };
        jSuperTabbedPane.addChangeListener(changeListener);

        progressBar.setVisible(false);
        //add a closing window listener
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                closeMethod();
            }
        });


        //load data
        loadData(false);
        startRoverButton.addActionListener(new ActionListener() {
            //start rover with reflection and file constructor
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

    public void closeMethod() {
        try {
            //try to delete the zip file
            File lZip = File.createTempFile("zip", null);
            lZip.delete();
        } catch (Exception e) {
            //do nothing
        }
        for (MsfFile iParsedMsf : iParsedMsfs) {
            try {
                iParsedMsf.getConnection().close();
            } catch (SQLException e) {
                logger.info(e);
            }
        }
        if (iStandAlone) {
            System.exit(0);
        } else {
            getFrame().setVisible(false);
        }
    }

    private void loadData(boolean lReopen) {
        final boolean lReOpen = lReopen;
        //open a new thread to parse the files found by the file chooser
        com.compomics.util.sun.SwingWorker lParser = new com.compomics.util.sun.SwingWorker() {
            boolean lLoaded = false;

            public Boolean construct() {
                try {
                    setGuiElementsResponsive(false);
                    //clean up if opening a new file
                    if (lReOpen) {
                        for (MsfFile iParsedMsf : iParsedMsfs) {
                            try {
                                iParsedMsf.getConnection().close();
                            } catch (SQLException e) {
                                logger.info(e);
                            }
                        }
                        if (jtabpanLower.indexOfTab("Chromatogram") > -1) {
                            jtabpanLower.remove(jtabChromatogram);
                            jtabChromatogram.validate();
                            jtabChromatogram.repaint();
                        }
                        if (jtabpanLower.indexOfTab("Quantification Spectrum") > -1) {
                            jtabpanLower.remove(jpanQuantificationSpectrumHolder);
                            jpanQuantificationSpectrumHolder.validate();
                            jpanQuantificationSpectrumHolder.repaint();
                        }
                        if (jtabpanLower.indexOfTab("MS/MS Spectrum") > -1) {
                            jtabpanLower.remove(jpanMSMS);
                            jpanMSMS.validate();
                            jpanMSMS.repaint();
                        }
                        if (jtabpanLower.indexOfTab("MS Spectrum") > -1) {
                            jtabpanLower.remove(jpanMSHolder);
                            jpanMSHolder.validate();
                            jpanMSHolder.repaint();
                        }
                        iProteins.removeAllElements();
                        iParsedMsfs = new Vector<MsfFile>();
                        iDisplayedProteins.removeAllElements();
                        iDisplayedProteinsOfInterest.removeAllElements();
                        iMergedPeptidesScores = null;
                        iMergedCustomPeptideData = null;
                        iMergedCustomSpectrumData = null;
                        iMergedRatioTypes = null;
                        iMsfFileLocations.removeAllElements();
                        proteinList.updateUI();
                        selectedProteinList.updateUI();
                        ((DefaultTableModel) jtablePeptides.getModel()).setNumRows(0);
                        //jtablePeptides.removeRowSelectionInterval(0, jtablePeptides.getRowCount());
                        sequenceCoverageJLabel.setText("Protein Coverage: ");
                        proteinSequenceCoverageJEditorPane.setText("");
                        iSelectedPeptide = null;
                        iSelectedProtein = null;
                    }

                    //open file chooser
                    JFileChooser fc = new JFileChooser();
                    fc.setMultiSelectionEnabled(true);
                    //create the file filter to choose
                    FileFilter lFilter = new MsfFileFilter();
                    fc.setFileFilter(lFilter);
                    int returnVal = fc.showOpenDialog(getFrame());
                    File[] lFiles;
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        lFiles = fc.getSelectedFiles();
                        Collections.addAll(iMsfFileLocations, lFiles);
                    } else {
                        JOptionPane.showMessageDialog(new JFrame(), "Open command cancelled by user.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        return true;
                    }
                    if (lFiles.length > 1) {
                        JOptionPane.showMessageDialog(getFrame(), "The workflow of the different msf files that are loaded must be the same.\nUnexpected crashes can occur if files with different workflows are loaded!", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                    progressBar.setIndeterminate(true);
                    progressBar.setVisible(true);
                    progressBar.setStringPainted(true);
                    progressBar.setMaximum(iMsfFileLocations.size() + 1);
                    progressBar.setIndeterminate(false);
                    //parse the msf files
                    /*int confidencelevel = 3;
                    if (chbHighConfident.isSelected()) {
                        confidencelevel = 3;

                    }
                    if (chbMediumConfident.isSelected()) {
                        confidencelevel = 2;

                    }
                    if (chbLowConfident.isSelected()) {
                        confidencelevel = 1;

                    }*/
                    for (int i = 0; i < iMsfFileLocations.size(); i++) {
                        msfFile = new MsfFile(iMsfFileLocations.get(i));
                        progressBar.setValue(i + 1);
                        progressBar.setString("Parsing: " + iMsfFileLocations.get(i).getName());
                        progressBar.updateUI();

                        proteinList.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseReleased(MouseEvent me) {
                                if (me.getButton() == 1) {
                                    loadProtein(false);
                                    selectedProteinList.setSelectedIndex(-1);
                                    selectedProteinList.updateUI();
                                }
                            }
                        });

                        proteinList.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyReleased(KeyEvent e) {
                                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                                    loadProtein(false);
                                    selectedProteinList.setSelectedIndex(-1);
                                    selectedProteinList.updateUI();
                                }
                            }
                        });
                        selectedProteinList.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseReleased(MouseEvent me) {
                                if (me.getButton() == 1) {
                                    loadProtein(true);
                                    proteinList.setSelectedIndex(-1);
                                    proteinList.updateUI();
                                }
                            }
                        });

                        selectedProteinList.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyReleased(KeyEvent e) {
                                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                                    loadProtein(true);
                                    proteinList.setSelectedIndex(-1);
                                    proteinList.updateUI();
                                }
                            }
                        });
                        proteinList.updateUI();
                        selectedProteinList.updateUI();

                        iAminoAcids = msfFile.getAminoAcids();
                        iParsedMsfs.add(msfFile);
                    }

                    //fetch the software version --> to separate method?
                    Statement stat = msfFile.getConnection().createStatement();
                    rs = stat.executeQuery("select * from SchemaInfo");
                    while (rs.next()) {
                        String lVersion = rs.getString("SoftwareVersion");
                        if (lVersion.startsWith("1.2")) {
                            iMsfVersion = MsfVersion.VERSION1_2;
                        } else if (lVersion.startsWith("1.3")) {
                            iMsfVersion = MsfVersion.VERSION1_3;
                        }
                    }

                    //load processing nodes
                    processingNodeTabbedPane.removeAll();
                    if (processingNodeLowMemInstance.getQuantitationMethod(msfFile.getConnection()).equals("")) {
                        iQuantitationFound = false;
                    }
                    ArrayList<ProcessingNode> processingNodes = processingNodeLowMemInstance.getAllProcessingNodes(msfFile.getConnection(), iMsfVersion);
                    for (ProcessingNode lNode : processingNodes) {
                        String lTitle = lNode.getProcessingNodeNumber() + " " + lNode.getFriendlyName();

                        //create holders for the different columns
                        Vector<String> lTableColumnsTitleVector = new Vector<String>();
                        Vector<Boolean> lTableColumnsEditableVector = new Vector<Boolean>();
                        Vector<Class<String>> lTableColumnsClassVector = new Vector<Class<String>>();
                        Vector<Object[]> lElements = new Vector<Object[]>();
                        for (int j = 0; j < lNode.getProcessingNodeParameters().size(); j++) {
                            Object[] lInfo = new Object[2];
                            lInfo[0] = lNode.getProcessingNodeParameters().get(j).getFriendlyName();
                            lInfo[1] = lNode.getProcessingNodeParameters().get(j).getValueDisplayString();
                            lElements.add(lInfo);
                        }

                        //add different columns to the holders
                        lTableColumnsTitleVector.add("Title");
                        lTableColumnsEditableVector.add(false);
                        lTableColumnsClassVector.add(String.class);
                        lTableColumnsTitleVector.add("Value");
                        lTableColumnsEditableVector.add(false);
                        lTableColumnsClassVector.add(String.class);

                        //create the arrays for the table model
                        String[] lTableColumnsTitle = new String[lTableColumnsTitleVector.size()];
                        lTableColumnsTitleVector.toArray(lTableColumnsTitle);
                        final Boolean[] lTableColumnsEditable = new Boolean[lTableColumnsEditableVector.size()];
                        lTableColumnsEditableVector.toArray(lTableColumnsEditable);
                        final Class[] lTableColumnsClass = new Class[lTableColumnsClassVector.size()];
                        lTableColumnsClassVector.toArray(lTableColumnsClass);
                        final Object[][] ls = new Object[lElements.size()][];
                        lElements.toArray(ls);

                        //create the table model
                        DefaultTableModel jtablePeptideModel = new DefaultTableModel(
                                ls,
                                lTableColumnsTitle) {
                            Class[] types = lTableColumnsClass;
                            Boolean[] canEdit = lTableColumnsEditable;

                            @Override
                            public Class getColumnClass(int columnIndex) {
                                return types[columnIndex];
                            }

                            @Override
                            public boolean isCellEditable(int rowIndex, int columnIndex) {
                                return canEdit[columnIndex];
                            }
                        };
                        JTable lTable = new JTable();
                        lTable.setModel(jtablePeptideModel);
                        lTable.getTableHeader().setReorderingAllowed(false);
                        JScrollPane lScrollPanel = new JScrollPane();
                        lScrollPanel.setViewportView(lTable);
                        GridBagConstraints gbc = new GridBagConstraints();
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        gbc.gridwidth = 4;
                        gbc.weightx = 1.0;
                        gbc.weighty = 1.0;
                        gbc.fill = GridBagConstraints.BOTH;
                        //lPanel.add(lScrollPanel, gbc);
                        //lPanel.setBackground(Color.BLACK);
                        processingNodeTabbedPane.add(lTitle, lScrollPanel);

                    }
                    int totalNumberOfPeptides = 0;
                    for (MsfFile iParsedMsf : iParsedMsfs) {
                        totalNumberOfPeptides += peptideLowMemInstance.getNumberOfPeptidesForConfidenceLevel(3, iParsedMsf.getConnection());
                    }
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(0);
                    progressBar.setMaximum(totalNumberOfPeptides + 1);
                    progressBar.setString("Collecting all peptides");
                    progressBar.updateUI();
                    iSelectedProtein = null;
                    proteinSequenceCoverageJEditorPane.setText("");
                    sequenceCoverageJLabel.setText("");
                    createPeptideTable(null);
                    proteinList.updateUI();
                    selectedProteinList.updateUI();
                    progressBar.setIndeterminate(false);
                    progressBar.setVisible(false);
                    progressBar.setString("");
                    lLoaded = true;
                } catch (Exception e1) {
                    lLoaded = false;
                    logger.info(e1);
                    progressBar.setVisible(false);
                    JOptionPane.showMessageDialog(new JFrame(), "There was a problem loading your data!", "Problem loading", JOptionPane.ERROR_MESSAGE);
                }
                return true;
            }

            @Override
            public void finished() {
                setGuiElementsResponsive(true);
                if (iQuantitationFound) {
                    startRoverButton.setVisible(true);
                } else {
                    startRoverButton.setVisible(false);
                }
                if (lLoaded) {
                    //give a message to the user that everything is loaded
                    JOptionPane.showMessageDialog(null, "All data was loaded", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };
        lParser.start();
    }

    public void setGuiElementsResponsive(boolean lResponsive) {
        aIonsJCheckBox.setEnabled(lResponsive);
        bIonsJCheckBox.setEnabled(lResponsive);
        cIonsJCheckBox.setEnabled(lResponsive);
        xIonsJCheckBox.setEnabled(lResponsive);
        yIonsJCheckBox.setEnabled(lResponsive);
        zIonsJCheckBox.setEnabled(lResponsive);
        startRoverButton.setEnabled(lResponsive);
        chargeOneJCheckBox.setEnabled(lResponsive);
        chargeTwoJCheckBox.setEnabled(lResponsive);
        chargeOverTwoJCheckBox.setEnabled(lResponsive);
        nh3IonsJCheckBox.setEnabled(lResponsive);
        h2oIonsJCheckBox.setEnabled(lResponsive);
        showAllPeptidesButton.setEnabled(lResponsive);
        jbuttonNumberSort.setEnabled(lResponsive);
        jbuttonAlphabeticalSort.setEnabled(lResponsive);
        chbHighConfident.setEnabled(lResponsive);
        chbMediumConfident.setEnabled(lResponsive);
        chbLowConfident.setEnabled(lResponsive);
        chromatogramCheckBox.setEnabled(lResponsive);
        msCheckBox.setEnabled(lResponsive);
        quantCheckBox.setEnabled(lResponsive);
        msmsCheckBox.setEnabled(lResponsive);
        onlyHighestScoringRadioButton.setEnabled(lResponsive);
        onlyLowestScoringRadioButton.setEnabled(lResponsive);
        allRadioButton.setEnabled(lResponsive);
        peptideInformationChb.setEnabled(lResponsive);
    }

    /**
     * Getter for the JFrame
     *
     * @return JFrame
     */
    public JFrame getFrame() {
        return this;
    }

    /**
     * This method will create the peptide table based on the given protein. If
     * no protein is given (null) all peptides found in the different msf files
     * will be displayed
     *
     * @param lProtein The protein to display the peptides of
     */
    private void createPeptideTable(ProteinLowMem lProtein) {
        peptideLowMemInstance.addObserver(this);
        //some gui stuff
        if (chbHighConfident == null) {
            chbHighConfident = new JCheckBox("High");
            chbHighConfident.setSelected(true);
            chbMediumConfident = new JCheckBox("Medium");
            chbMediumConfident.setSelected(true);
            chbLowConfident = new JCheckBox("Low");
        }
        progressBar.setString("creating peptide table");

        //create holders for the different columns
        Vector<String> lPeptideTableColumnsTitleVector = new Vector<String>();
        Vector<Boolean> lPeptideTableColumnsEditableVector = new Vector<Boolean>();
        Vector<Class> lPeptideTableColumnsClassVector = new Vector<Class>();
        Vector<Object[]> lPeptideHash = new Vector<Object[]>();

        //add different columns to the holders
        lPeptideTableColumnsTitleVector.add(" ");
        lPeptideTableColumnsEditableVector.add(false);
        lPeptideTableColumnsClassVector.add(Integer.class);

        lPeptideTableColumnsTitleVector.add("Spectrum Title");
        lPeptideTableColumnsEditableVector.add(false);
        lPeptideTableColumnsClassVector.add(Integer.class);

        lPeptideTableColumnsTitleVector.add("Sequence");
        lPeptideTableColumnsEditableVector.add(false);
        lPeptideTableColumnsClassVector.add(Peptide.class);

        lPeptideTableColumnsTitleVector.add("Modified Sequence");
        lPeptideTableColumnsEditableVector.add(false);
        lPeptideTableColumnsClassVector.add(String.class);

        //get the different score types and add it as columns
        if (iMergedPeptidesScores == null) {
            this.collectPeptideScoreTypes();
            //set the major score type
            for (ScoreTypeLowMem iMergedPeptidesScore : iMergedPeptidesScores) {
                if (iMergedPeptidesScore.getIsMainScore() == 1) {
                    iMajorScoreTypes.add(iMergedPeptidesScore);
                }
            }
        }

        for (ScoreTypeLowMem iMergedPeptidesScore : iMergedPeptidesScores) {
            boolean lUse = false;
            if (peptideInformationChb.isSelected()) {
                lUse = true;
            } else {
                if (iMergedPeptidesScore.getIsMainScore() == 1) {
                    lUse = true;
                }
            }
            if (lUse) {
                lPeptideTableColumnsTitleVector.add(iMergedPeptidesScore.getFriendlyName());
                lPeptideTableColumnsEditableVector.add(false);
                lPeptideTableColumnsClassVector.add(Double.class);
            }
        }

        lPeptideTableColumnsTitleVector.add("Matched Ions / Total Ions");
        lPeptideTableColumnsEditableVector.add(false);
        lPeptideTableColumnsClassVector.add(String.class);

        lPeptideTableColumnsTitleVector.add("m/z [Da]");
        lPeptideTableColumnsEditableVector.add(false);
        lPeptideTableColumnsClassVector.add(Double.class);

        if (peptideInformationChb.isSelected()) {
            lPeptideTableColumnsTitleVector.add("MH+ [Da]");
            lPeptideTableColumnsEditableVector.add(false);
            lPeptideTableColumnsClassVector.add(Double.class);
        }

        lPeptideTableColumnsTitleVector.add("Charge");
        lPeptideTableColumnsClassVector.add(Integer.class);

        lPeptideTableColumnsTitleVector.add("Retention Time");
        lPeptideTableColumnsEditableVector.add(false);
        lPeptideTableColumnsClassVector.add(Double.class);

        lPeptideTableColumnsTitleVector.add("First Scan");
        lPeptideTableColumnsEditableVector.add(false);
        lPeptideTableColumnsClassVector.add(Integer.class);

        if (peptideInformationChb.isSelected()) {
            lPeptideTableColumnsTitleVector.add("Last Scan");
            lPeptideTableColumnsEditableVector.add(false);
            lPeptideTableColumnsClassVector.add(Integer.class);

            lPeptideTableColumnsTitleVector.add("Annotation");
            lPeptideTableColumnsEditableVector.add(false);
            lPeptideTableColumnsClassVector.add(String.class);
        }

        //get the ratiotypes and add it as columns
        if (iMergedRatioTypes == null) {
            this.collectRatioTypes();
        }
        for (RatioTypeLowMem iMergedRatioType : iMergedRatioTypes) {
            lPeptideTableColumnsTitleVector.add(iMergedRatioType.getRatioType());
            lPeptideTableColumnsEditableVector.add(false);
            lPeptideTableColumnsClassVector.add(Double.class);
        }

        if (peptideInformationChb.isSelected()) {
            //get the custom peptide data and add it as columns
            if (iMergedCustomPeptideData.isEmpty()) {
                this.collectCustomPeptideData();
            }
            for (CustomDataField anIMergedCustomPeptideData : iMergedCustomPeptideData) {
                lPeptideTableColumnsTitleVector.add(anIMergedCustomPeptideData.getName());
                lPeptideTableColumnsEditableVector.add(false);
                lPeptideTableColumnsClassVector.add(String.class);
            }

            //get the custom spectra data and add it as columns
            if (iMergedCustomSpectrumData.isEmpty()) {
                this.collectCustomSpectrumData();
            }
            for (CustomDataField anIMergedCustomSpectrumData : iMergedCustomSpectrumData) {
                lPeptideTableColumnsTitleVector.add(anIMergedCustomSpectrumData.getName());
                lPeptideTableColumnsEditableVector.add(false);
                lPeptideTableColumnsClassVector.add(String.class);
            }

            lPeptideTableColumnsTitleVector.add("Processing Node");
            lPeptideTableColumnsEditableVector.add(false);
            lPeptideTableColumnsClassVector.add(ProcessingNode.class);

        }

        //find the peptides to display
        if (lProtein == null) {
            lPeptideHash = this.collectPeptides(lPeptideHash);
        } else {
            lPeptideHash = this.collectPeptidesFromProtein(lPeptideHash, lProtein);
        }
        //create the arrays for the table model
        String[] lPeptideTableColumnsTitle = new String[lPeptideTableColumnsTitleVector.size()];
        lPeptideTableColumnsTitleVector.toArray(lPeptideTableColumnsTitle);
        final Boolean[] lPeptideTableColumnsEditable = new Boolean[lPeptideTableColumnsEditableVector.size()];
        lPeptideTableColumnsEditableVector.toArray(lPeptideTableColumnsEditable);
        final Class[] lPeptideTableColumnsClass = new Class[lPeptideTableColumnsClassVector.size()];
        lPeptideTableColumnsClassVector.toArray(lPeptideTableColumnsClass);
        final Object[][] lPeptides = new Object[lPeptideHash.size()][];
        lPeptideHash.toArray(lPeptides);

        //create the table model
        DefaultTableModel jtablePeptideModel = new DefaultTableModel(
                lPeptides,
                lPeptideTableColumnsTitle) {
            Class[] types = lPeptideTableColumnsClass;
            Boolean[] canEdit = lPeptideTableColumnsEditable;

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };

        jtablePeptides = new JTable();
        jtablePeptides.setModel(jtablePeptideModel);
        jtablePeptides.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtablePeptides.getTableHeader().setReorderingAllowed(false);

        // set up the confidence color map
        HashMap<Integer, Color> confidenceColorMap = new HashMap<Integer, Color>();
        HashMap<Integer, String> confidenceTipMap = new HashMap<Integer, String>();
        confidenceColorMap.put(1, new Color(255, 51, 51)); // low
        confidenceTipMap.put(1, "Low Confidence");
        confidenceColorMap.put(2, Color.ORANGE); // medium
        confidenceTipMap.put(2, "Medium Confidence");
        confidenceColorMap.put(3, new Color(110, 196, 97)); // high
        confidenceTipMap.put(3, "High Confidence");

        //set some cell renderers
        jtablePeptides.getColumn(" ").setCellRenderer(new JSparklinesIntegerColorTableCellRenderer(Color.LIGHT_GRAY, confidenceColorMap, confidenceTipMap));
        if (peptideInformationChb.isSelected()) {
            jtablePeptides.getColumn("Processing Node").setCellRenderer(new ProcessingNodeRenderer());
        }

        // set the maximum width for the confidence column
        jtablePeptides.getColumn(" ").setMaxWidth(40);

        double lLowRT = Double.MAX_VALUE;
        double lHighRT = Double.MIN_VALUE;

        for (Object[] lPeptide1 : lPeptides) {
            PeptideLowMem lPeptide = (PeptideLowMem) lPeptide1[2];
                SpectrumLowMem spectrumOfPeptide = spectrumLowMemInstance.getSpectrumForPeptideID(lPeptide.getPeptideId(), lPeptide.getConnection());
                if (lLowRT > spectrumOfPeptide.getRetentionTime()) {
                    lLowRT = spectrumOfPeptide.getRetentionTime();
                }
                if (lHighRT < spectrumOfPeptide.getRetentionTime()) {
                    lHighRT = spectrumOfPeptide.getRetentionTime();
                }
        }
        lLowRT -= 1.0;
        double widthOfMarker = (lHighRT / lLowRT) / 4;

        JSparklinesIntervalChartTableCellRenderer lRTCellRenderer = new JSparklinesIntervalChartTableCellRenderer(
                PlotOrientation.HORIZONTAL, lLowRT - widthOfMarker / 2, lHighRT + widthOfMarker / 2, widthOfMarker, Color.YELLOW, Color.BLUE);
        jtablePeptides.getColumn("Retention Time").setCellRenderer(lRTCellRenderer);
        lRTCellRenderer.showNumberAndChart(true, 50);


        for (int i = 0; i < iMergedPeptidesScores.size(); i++) {

            boolean lUse = false;
            if (peptideInformationChb.isSelected()) {
                lUse = true;
            } else {
                if (iMergedPeptidesScores.get(i).getIsMainScore() == 1) {
                    lUse = true;
                }
            }
            if (lUse) {
                double lLowScore = Double.MAX_VALUE;
                double lHighScore = Double.MIN_VALUE;
                for (Object[] lPeptide : lPeptides) {
                    if (lPeptide[4 + i] != null) {
                        if (lPeptide[4 + i].toString().indexOf("/") > 0) {
                            System.out.println(lPeptide[4 + i]);
                        }
                        double lScore = (Double) lPeptide[4 + i];
                        if (lLowScore > lScore) {
                            lLowScore = lScore;
                        }
                        if (lHighScore < lScore) {
                            lHighScore = lScore;
                        }
                    }
                }
                lLowScore -= 1.0;
                JSparklinesBarChartTableCellRenderer lScoreCellRenderer;
                lScoreCellRenderer = new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, lLowScore, lHighScore, Color.RED, Color.GREEN);
                lScoreCellRenderer.setGradientColoring(GradientColorCoding.ColorGradient.BlueBlackGreen);
                jtablePeptides.getColumn(iMergedPeptidesScores.get(i).getFriendlyName()).setCellRenderer(lScoreCellRenderer);
                lScoreCellRenderer.showNumberAndChart(true, 50);
            }
        }


        /*for (int i = 0; i < iMergedRatioTypes.size(); i++) {
         double lLowScore = Double.MAX_VALUE;
         double lHighScore = Double.MIN_VALUE;

         for (int p = 0; p < lPeptides.length; p++) {
         Peptide lPeptide = (Peptide) lPeptides[p][2];
         if (lPeptide.getParentSpectrum().getQuanResult() != null && lPeptide.getParentSpectrum().getQuanResult().getRatioByRatioType(iMergedRatioTypes.get(i)) != null) {
         lPeptide.getParentSpectrum().getQuanResult().getRatioByRatioType(iMergedRatioTypes.get(i));
         double lScore = lPeptide.getParentSpectrum().getQuanResult().getRatioByRatioType(iMergedRatioTypes.get(i));
         if (lLowScore > lScore) {
         lLowScore = lScore;
         }
         if (lHighScore < lScore) {
         lHighScore = lScore;
         }
         }
         }


         JSparklinesBarChartTableCellRenderer lScoreCellRenderer;
         lScoreCellRenderer = new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, lLowScore, lHighScore, Color.RED, Color.GREEN);
         jtablePeptides.getColumn(iMergedRatioTypes.get(i).getRatioType()).setCellRenderer(lScoreCellRenderer);
         lScoreCellRenderer.showNumberAndChart(true, 50);
         } */

        jtablePeptides.setOpaque(false);
        jtablePeptides.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                peptidesTableKeyReleased(evt);
            }
        });
        jtablePeptides.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                try {
                    peptidesTableMouseClicked(evt);
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        });
        if (jscollPeptides == null) {
            jscollPeptides = new JScrollPane();
        }

        jscollPeptides.setViewportView(jtablePeptides);
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(jtablePeptideModel);
        jtablePeptides.setRowSorter(sorter);

        if (peptideInformationChb.isSelected()) {
            jtablePeptides.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        } else {
            jtablePeptides.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        }
    }

    private void collectCustomPeptideData() {
        ArrayList<CustomDataField> customPeptideData = new ArrayList<CustomDataField>();
        for (MsfFile iParsedMsf : iParsedMsfs) {
            customPeptideData = customDataLowMemControllerInstance.getCustomPeptideData(customData, iParsedMsf.getConnection());
            if (iMergedCustomPeptideData != null) {
                for (CustomDataField aCustomDataField : customPeptideData) {
                    boolean lFound = false;
                    for (CustomDataField anIMergedCustomPeptideData : iMergedCustomPeptideData) {
                        if (aCustomDataField.equals(anIMergedCustomPeptideData)) {
                            lFound = true;
                        }
                    }
                    if (!lFound) {
                        iMergedCustomSpectrumData.add(aCustomDataField);
                    }
                }
            }
        }
    }

    /**
     * This method will change the ms ms fragmentation error
     */
    private void changeMSMSerror() {
        String lMsMsError = txtMSMSerror.getText();
        try {
            iMSMSerror = Double.valueOf(lMsMsError);
        } catch (NumberFormatException e) {
            txtMSMSerror.setText(String.valueOf(iMSMSerror));
            JOptionPane.showMessageDialog(this, lMsMsError + " is not a valid value!", "Number error", JOptionPane.WARNING_MESSAGE);
        }

        //get the selected peptide
        if (iSelectedPeptide != null) {
            try {
                setSpectrumMSMSAnnotations(iSelectedPeptide);
            } catch (SQLException sqle) {
                logger.error(sqle);
            }
        }
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT
     * edit this method OR call it in your code!
     *
     *
     *

     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        jSuperTabbedPane = new JTabbedPane();
        jSuperTabbedPane.setTabLayoutPolicy(0);
        jSuperTabbedPane.setTabPlacement(3);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(jSuperTabbedPane, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        jSuperTabbedPane.addTab("Thermo MSF Viewer", panel1);
        jpanContent = new JPanel();
        jpanContent.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(jpanContent, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        jpanContent.add(panel2, gbc);
        showAllPeptidesButton = new JButton();
        showAllPeptidesButton.setMinimumSize(new Dimension(150, 25));
        showAllPeptidesButton.setText("Show all");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel2.add(showAllPeptidesButton, gbc);
        split1 = new JSplitPane();
        split1.setDividerLocation(310);
        split1.setOneTouchExpandable(true);
        split1.setOrientation(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(split1, gbc);
        split2 = new JSplitPane();
        split2.setDividerLocation(609);
        split1.setRightComponent(split2);
        jtabpanLower = new JTabbedPane();
        split2.setLeftComponent(jtabpanLower);
        jtabChromatogram = new JTabbedPane();
        jtabChromatogram.setBackground(new Color(-3407770));
        jtabpanLower.addTab("Chromatogram", jtabChromatogram);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        jtabChromatogram.addTab("No Chromatogram Loaded", panel3);
        jpanMSHolder = new JPanel();
        jpanMSHolder.setLayout(new GridBagLayout());
        jtabpanLower.addTab("MS Spectrum", jpanMSHolder);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        jpanMSHolder.add(jpanMS, gbc);
        jpanQuantificationSpectrumHolder = new JPanel();
        jpanQuantificationSpectrumHolder.setLayout(new GridBagLayout());
        jtabpanLower.addTab("Quantification Spectrum", jpanQuantificationSpectrumHolder);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        jpanQuantificationSpectrumHolder.add(jpanQuantitationSpectrum, gbc);
        jpanMSMS = new JPanel();
        jpanMSMS.setLayout(new GridBagLayout());
        jpanMSMS.setMaximumSize(new Dimension(400, 100));
        jpanMSMS.setMinimumSize(new Dimension(400, 100));
        jpanMSMS.setPreferredSize(new Dimension(400, 100));
        jtabpanLower.addTab("MS/MS Spectrum", jpanMSMS);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.95;
        gbc.weighty = 0.95;
        gbc.fill = GridBagConstraints.BOTH;
        jpanMSMS.add(jpanMSMSLeft, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        jpanMSMS.add(panel4, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(1, 1, 1, 1);
        panel4.add(cIonsJCheckBox, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(1, 1, 1, 1);
        panel4.add(zIonsJCheckBox, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 10;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(1, 1, 1, 1);
        panel4.add(chargeOverTwoJCheckBox, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(1, 1, 1, 1);
        panel4.add(aIonsJCheckBox, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(1, 1, 1, 1);
        panel4.add(bIonsJCheckBox, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(1, 1, 1, 1);
        panel4.add(xIonsJCheckBox, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(1, 1, 1, 1);
        panel4.add(yIonsJCheckBox, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 8;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(1, 1, 1, 1);
        panel4.add(chargeOneJCheckBox, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 9;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(1, 1, 1, 1);
        panel4.add(chargeTwoJCheckBox, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 21;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(1, 1, 1, 1);
        panel4.add(nh3IonsJCheckBox, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 22;
        gbc.gridy = 0;
        gbc.gridwidth = 10;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(1, 1, 1, 1);
        panel4.add(h2oIonsJCheckBox, gbc);
        txtMSMSerror.setText("0.5");
        txtMSMSerror.setToolTipText("The MS/MS fragmentation error (in Da)");
        gbc = new GridBagConstraints();
        gbc.gridx = 32;
        gbc.gridy = 0;
        gbc.gridwidth = 10;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(1, 1, 1, 1);
        panel4.add(txtMSMSerror, gbc);
        final JSeparator separator1 = new JSeparator();
        separator1.setOrientation(1);
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(1, 1, 1, 1);
        panel4.add(separator1, gbc);
        final JSeparator separator2 = new JSeparator();
        separator2.setOrientation(1);
        gbc = new GridBagConstraints();
        gbc.gridx = 11;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(1, 1, 1, 1);
        panel4.add(separator2, gbc);
        final JSeparator separator3 = new JSeparator();
        separator3.setOrientation(1);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(1, 1, 1, 1);
        panel4.add(separator3, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 42;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer1, gbc);
        jtabpanSpectrum = new JTabbedPane();
        split2.setRightComponent(jtabpanSpectrum);
        jpanProtein = new JPanel();
        jpanProtein.setLayout(new GridBagLayout());
        jtabpanSpectrum.addTab("Protein", jpanProtein);
        jpanProteinLeft = new JPanel();
        jpanProteinLeft.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        jpanProtein.add(jpanProteinLeft, gbc);
        jpanProteinLeft.setBorder(BorderFactory.createTitledBorder("Protein Coverage"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanProteinLeft.add(proteinCoverageJScrollPane, gbc);
        sequenceCoverageJLabel = new JLabel();
        sequenceCoverageJLabel.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanProteinLeft.add(sequenceCoverageJLabel, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        jtabpanSpectrum.addTab("Processing Nodes", panel5);
        processingNodeTabbedPane = new JTabbedPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel5.add(processingNodeTabbedPane, gbc);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridBagLayout());
        processingNodeTabbedPane.addTab("Nothing Loaded", panel6);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridBagLayout());
        split1.setLeftComponent(panel7);
        panel7.setBorder(BorderFactory.createTitledBorder(null, "Peptides", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel7.add(jscollPeptides, gbc);
        jscollPeptides.setViewportView(jtablePeptides);
        final JLabel label1 = new JLabel();
        label1.setText("Peptide Confidence Level: ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel7.add(label1, gbc);
        chbHighConfident.setMaximumSize(new Dimension(130, 22));
        chbHighConfident.setMinimumSize(new Dimension(130, 22));
        chbHighConfident.setPreferredSize(new Dimension(130, 22));
        chbHighConfident.setSelected(true);
        chbHighConfident.setText("High");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel7.add(chbHighConfident, gbc);
        chbMediumConfident.setMaximumSize(new Dimension(130, 22));
        chbMediumConfident.setMinimumSize(new Dimension(130, 22));
        chbMediumConfident.setPreferredSize(new Dimension(130, 22));
        chbMediumConfident.setSelected(false);
        chbMediumConfident.setText("Medium");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel7.add(chbMediumConfident, gbc);
        chbLowConfident.setMaximumSize(new Dimension(130, 22));
        chbLowConfident.setMinimumSize(new Dimension(130, 22));
        chbLowConfident.setPreferredSize(new Dimension(130, 22));
        chbLowConfident.setText("Low");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel7.add(chbLowConfident, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Peptide Spectrum Match: ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel7.add(label2, gbc);
        onlyHighestScoringRadioButton.setSelected(true);
        onlyHighestScoringRadioButton.setText("Only Highest Scoring");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel7.add(onlyHighestScoringRadioButton, gbc);
        onlyLowestScoringRadioButton.setText("Only Lowest Scoring");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel7.add(onlyLowestScoringRadioButton, gbc);
        allRadioButton.setSelected(false);
        allRadioButton.setText("All");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel7.add(allRadioButton, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Load Spectrum: ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel7.add(label3, gbc);
        chromatogramCheckBox = new JCheckBox();
        chromatogramCheckBox.setMaximumSize(new Dimension(130, 22));
        chromatogramCheckBox.setMinimumSize(new Dimension(130, 22));
        chromatogramCheckBox.setPreferredSize(new Dimension(130, 22));
        chromatogramCheckBox.setSelected(true);
        chromatogramCheckBox.setText("Chromatogram");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel7.add(chromatogramCheckBox, gbc);
        msCheckBox = new JCheckBox();
        msCheckBox.setMaximumSize(new Dimension(130, 22));
        msCheckBox.setMinimumSize(new Dimension(130, 22));
        msCheckBox.setPreferredSize(new Dimension(130, 22));
        msCheckBox.setSelected(true);
        msCheckBox.setText("MS");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel7.add(msCheckBox, gbc);
        quantCheckBox = new JCheckBox();
        quantCheckBox.setMaximumSize(new Dimension(130, 22));
        quantCheckBox.setMinimumSize(new Dimension(130, 22));
        quantCheckBox.setPreferredSize(new Dimension(130, 22));
        quantCheckBox.setSelected(true);
        quantCheckBox.setText("Quant");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel7.add(quantCheckBox, gbc);
        msmsCheckBox = new JCheckBox();
        msmsCheckBox.setMaximumSize(new Dimension(130, 22));
        msmsCheckBox.setMinimumSize(new Dimension(130, 22));
        msmsCheckBox.setPreferredSize(new Dimension(130, 22));
        msmsCheckBox.setSelected(true);
        msmsCheckBox.setText("MS/MS");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel7.add(msmsCheckBox, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Full Peptide Information: ");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 0);
        panel7.add(label4, gbc);
        peptideInformationChb = new JCheckBox();
        peptideInformationChb.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel7.add(peptideInformationChb, gbc);
        startRoverButton = new JButton();
        startRoverButton.setText("Start Rover");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel7.add(startRoverButton, gbc);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(panel8, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.66;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel8.add(scrollPane1, gbc);
        scrollPane1.setViewportView(proteinList);
        final JScrollPane scrollPane2 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.34;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel8.add(scrollPane2, gbc);
        scrollPane2.setViewportView(selectedProteinList);
        jbuttonAlphabeticalSort = new JButton();
        jbuttonAlphabeticalSort.setMaximumSize(new Dimension(80, 25));
        jbuttonAlphabeticalSort.setMinimumSize(new Dimension(80, 25));
        jbuttonAlphabeticalSort.setPreferredSize(new Dimension(80, 25));
        jbuttonAlphabeticalSort.setText("A -> Z");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel8.add(jbuttonAlphabeticalSort, gbc);
        jbuttonNumberSort = new JButton();
        jbuttonNumberSort.setMaximumSize(new Dimension(80, 25));
        jbuttonNumberSort.setMinimumSize(new Dimension(80, 25));
        jbuttonNumberSort.setPreferredSize(new Dimension(80, 25));
        jbuttonNumberSort.setText("20 -> 1");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel8.add(jbuttonNumberSort, gbc);
        lblProteinOfIntersest = new JLabel();
        lblProteinOfIntersest.setText("Matching protein(s)");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel8.add(lblProteinOfIntersest, gbc);
        progressBar = new JProgressBar();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(progressBar, gbc);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(onlyHighestScoringRadioButton);
        buttonGroup.add(onlyLowestScoringRadioButton);
        buttonGroup.add(allRadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    public void update(Observable o, Object arg) {
        progressBar.setValue(progressBarIntFiller.progressBarReturn());
    }

    private class ProcessingNodeRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object lProcessingNodeObject, boolean isSelected, boolean hasFocus, int row, int column) {
            ProcessingNode lProcessingNode = (ProcessingNode) lProcessingNodeObject;
            /*String lToolTipHtml = "<html><b>" + lProcessingNode.getFriendlyName() + "</b>";
             for (int i = 0; i < lProcessingNode.getProcessingNodeParameters().size(); i++) {
             lToolTipHtml = lToolTipHtml + "<br>" + lProcessingNode.getProcessingNodeParameters().get(i).getFriendlyName() + ": " + lProcessingNode.getProcessingNodeParameters().get(i).getValueDisplayString();
             }
             lToolTipHtml = lToolTipHtml + "</html>";
             setToolTipText(lToolTipHtml);           */
            return super.getTableCellRendererComponent(table, lProcessingNode.getProcessingNodeNumber() + ": " + lProcessingNode.getFriendlyName(), isSelected, hasFocus, row, column);
        }
    }

    /**
     * This method will initiate a spectrum annotation if a peptide is selected
     */
    private void ionsJCheckBoxActionPerformed() {
        try {
            if (iSelectedPeptide != null) {
                setSpectrumMSMSAnnotations(iSelectedPeptide);
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
    }

    /**
     * This method will generate the object for the peptide table, based on a
     * given protein
     *
     * @param lPeptides Vector to add the peptide line objects to it
     * @param lProtein The selected protein
     * @return Vector with the peptide line objects
     */
    private Vector<Object[]> collectPeptidesFromProtein(Vector<Object[]> lPeptides, ProteinLowMem lProtein) {
            Vector<PeptideLowMem> peptides = peptideLowMemInstance.getPeptidesForProtein(lProtein, iMsfVersion, iAminoAcids);
            for (PeptideLowMem lPeptide : peptides) {
                int lConfidenceLevel = lPeptide.getConfidenceLevel();
                boolean lUse = false;
                if (chbHighConfident.isSelected() && lConfidenceLevel == 3) {
                    lUse = true;
                }
                if (chbMediumConfident.isSelected() && lConfidenceLevel == 2) {
                    lUse = true;
                }
                if (chbLowConfident.isSelected() && lConfidenceLevel == 1) {
                    lUse = true;
                }
                /*if (onlyHighestScoringRadioButton.isSelected()) {
                 if (!lPeptide.getParentSpectrum().isHighestScoring(lPeptide, iMajorScoreTypes)) {
                 lUse = false;
                 }
                 }
                 if (onlyLowestScoringRadioButton.isSelected()) {
                 if (!lPeptide.getParentSpectrum().isLowestScoring(lPeptide, iMajorScoreTypes)) {
                 lUse = false;
                 }
                 } */
                if (lUse) {
                    //only add the peptide line if we need to use it
                    lPeptides.add(createPeptideLine(lPeptide));
                }
            }
        return lPeptides;
    }

    /**
     * This method will generate the object for the peptide table for all the
     * peptides
     *
     * @param lPeptideLines Vector to add the peptide line objects to it
     * @return Vector with the peptide line objects
     */
    private Vector<Object[]> collectPeptides(Vector<Object[]> lPeptideLines) {
        progressBar.setString("fetching proteins and populating peptide table");
        Vector<PeptideLowMem> lPeptides = new Vector<PeptideLowMem>();
        boolean lCreateProteins = false;
        if (iDisplayedProteins.isEmpty()) {
            //The proteins are not created yet, so we need to create them
            lCreateProteins = true;
        }
        int totalNumberOfPeptides = 0;
        int confidencelevel = 0;
        if (chbHighConfident.isSelected()) {
            confidencelevel = 3;
            for (MsfFile iParsedMsf : iParsedMsfs) {
                totalNumberOfPeptides += peptideLowMemInstance.getNumberOfPeptidesForConfidenceLevel(confidencelevel, iParsedMsf.getConnection());
            }
        }
        if (chbMediumConfident.isSelected()) {
            confidencelevel = 2;
            for (MsfFile iParsedMsf : iParsedMsfs) {
                totalNumberOfPeptides += peptideLowMemInstance.getNumberOfPeptidesForConfidenceLevel(confidencelevel, iParsedMsf.getConnection());
            }
        }
        if (chbLowConfident.isSelected()) {
            confidencelevel = 1;
            for (MsfFile iParsedMsf : iParsedMsfs) {
                totalNumberOfPeptides += peptideLowMemInstance.getNumberOfPeptidesForConfidenceLevel(confidencelevel, iParsedMsf.getConnection());
            }
        }
        progressBar.setMaximum(totalNumberOfPeptides);

        for (MsfFile iParsedMsf : iParsedMsfs) {
            if (chbHighConfident.isSelected()) {
                iDisplayedProteins.addAll(proteinLowMemInstance.getProteinsForConfidenceLevel(confidencelevel, msfFile.getConnection(), true));
            }
            if (chbMediumConfident.isSelected()) {
                iDisplayedProteins.addAll(proteinLowMemInstance.getProteinsForConfidenceLevel(confidencelevel, msfFile.getConnection(), true));
            }
            if (chbLowConfident.isSelected()) {
                iDisplayedProteins.addAll(proteinLowMemInstance.getProteinsForConfidenceLevel(confidencelevel, msfFile.getConnection(), true));
            }
            //TODO implement later
                    /*if (onlyHighestScoringRadioButton.isSelected()) {
             if (!lPeptide.getParentSpectrum().isHighestScoring(lPeptide, iMajorScoreTypes)) {
             lUse = false;
             }
             }
             if (onlyLowestScoringRadioButton.isSelected()) {
             if (!lPeptide.getParentSpectrum().isLowestScoring(lPeptide, iMajorScoreTypes)) {
             lUse = false;
             }
             }*/
            if (chbHighConfident.isSelected()) {
                peptideLowMemInstance.getPeptidesForProteinVector(iDisplayedProteins, msfFile.getConnection(), iAminoAcids, iMsfVersion, 3);
            }
            if (chbMediumConfident.isSelected()) {
                peptideLowMemInstance.getPeptidesForProteinVector(iDisplayedProteins, msfFile.getConnection(), iAminoAcids, iMsfVersion, 2);
            }
            if (chbLowConfident.isSelected()) {
                peptideLowMemInstance.getPeptidesForProteinVector(iDisplayedProteins, msfFile.getConnection(), iAminoAcids, iMsfVersion, 1);
            }

            //only add the peptide line if we need to use it
            Vector<ProteinLowMem> proteinsToRemove = new Vector<ProteinLowMem>();
            for (ProteinLowMem lProtein : iDisplayedProteins) {
                lPeptides = lProtein.getPeptidesForProtein();
                if (!lPeptides.isEmpty()) {
                    for (PeptideLowMem lPeptide : lPeptides) {
                        lPeptideLines.add(createPeptideLine(lPeptide));
                    }
                } else {
                    //TODO make this concurrent >.>
                    proteinsToRemove.add(lProtein);
                }
            }
            iDisplayedProteins.removeAll(proteinsToRemove);
            if (iProteins.size() % 100 == 0) {
                System.gc();
            }
            proteinList.setListData(iDisplayedProteins);
        }
        if (lCreateProteins) {
            progressBar.setString("creating list of proteins");
            progressBar.setIndeterminate(true);
            //create the protein list and add the listeners
            proteinList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent me) {
                    if (me.getButton() == 1) {
                        loadProtein(false);
                        selectedProteinList.setSelectedIndex(-1);
                        selectedProteinList.updateUI();
                    }
                }
            });

            proteinList.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                        loadProtein(false);
                        selectedProteinList.setSelectedIndex(-1);
                        selectedProteinList.updateUI();
                    }
                }
            });
            selectedProteinList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent me) {
                    if (me.getButton() == 1) {
                        loadProtein(true);
                        proteinList.setSelectedIndex(-1);
                        proteinList.updateUI();
                    }
                }
            });

            selectedProteinList.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                        loadProtein(true);
                        proteinList.setSelectedIndex(-1);
                        proteinList.updateUI();
                    }
                }
            });
            proteinList.updateUI();
            selectedProteinList.updateUI();
        }
        return lPeptideLines;
    }

    /**
     * This method will create a peptide line vector for the peptide table
     *
     *
     * @param lPeptide The peptide to create the peptide line for
     * @return Vector with the objects of the peptide line
     */
    public Object[] createPeptideLine(PeptideLowMem lPeptide) {
        Vector<Object> lPeptideObject = new Vector<Object>();
        lPeptideObject.add(lPeptide.getConfidenceLevel());
            SpectrumLowMem spectrumOfPeptide = lPeptide.getParentSpectrum();
            lPeptideObject.add(spectrumLowMemInstance.getSpectrumTitle(rawFileLowMemInstance.getRawFileNameForFileID(spectrumOfPeptide.getFileId(), lPeptide.getConnection()), spectrumOfPeptide));
            lPeptideObject.add(lPeptide);
            lPeptideObject.add(lPeptide.getModifiedPeptide());
            for (ScoreTypeLowMem iMergedPeptidesScore : iMergedPeptidesScores) {
                if (!peptideInformationChb.isSelected()) {
                    if (iMergedPeptidesScore.getIsMainScore() == 1) {
                        Double lScore = lPeptide.getScoreByScoreType(iMergedPeptidesScore);
                        lPeptideObject.add(lScore);
                    }
                } else {
                    Double lScore = lPeptide.getScoreByScoreType(iMergedPeptidesScore);
                    lPeptideObject.add(lScore);
                }
            }

            lPeptideObject.add(lPeptide.getMatchedIonsCount() + "/" + lPeptide.getTotalIonsCount());
            lPeptideObject.add(spectrumOfPeptide.getMz());
            if (peptideInformationChb.isSelected()) {
                lPeptideObject.add(spectrumOfPeptide.getSinglyChargedMass());
            }
            lPeptideObject.add(spectrumOfPeptide.getCharge());
            lPeptideObject.add(spectrumOfPeptide.getRetentionTime());
            lPeptideObject.add(spectrumOfPeptide.getFirstScan());
            if (peptideInformationChb.isSelected()) {
                lPeptideObject.add(spectrumOfPeptide.getLastScan());
                if (lPeptide.getAnnotation() != null) {
                    lPeptideObject.add(lPeptide.getAnnotation());
                } else {
                    lPeptideObject.add("");
                }
            }

            for (RatioTypeLowMem iMergedRatioType : iMergedRatioTypes) {
                //lPeptideObject.add(0.0);
                if (spectrumOfPeptide.getQuanResult() != null && spectrumOfPeptide.getQuanResult().getRatioByRatioType(iMergedRatioType) != null) {
                    lPeptideObject.add(spectrumOfPeptide.getQuanResult().getRatioByRatioType(iMergedRatioType));
                } else {
                    lPeptideObject.add(null);
                }
            }
            if (peptideInformationChb.isSelected()) {
                for (CustomDataField anIMergedCustomPeptideData : iMergedCustomPeptideData) {
                    if ((lPeptide.getCustomDataFieldValues().get(anIMergedCustomPeptideData.getFieldId())) != null) {
                        lPeptideObject.add(lPeptide.getCustomDataFieldValues().get(anIMergedCustomPeptideData.getFieldId()));
                    } else {
                        lPeptideObject.add("");
                    }
                }

                for (CustomDataField anIMergedCustomSpectrumData : iMergedCustomSpectrumData) {
                    if (spectrumOfPeptide.getCustomDataFieldValues().get(anIMergedCustomSpectrumData.getFieldId()) != null) {
                        lPeptideObject.add(spectrumOfPeptide.getCustomDataFieldValues().get(anIMergedCustomSpectrumData.getFieldId()));
                    } else {
                        lPeptideObject.add("");
                    }
                }
                lPeptideObject.add(ProcessingNodeLowMemInstance.getAllProcessingNodes(spectrumOfPeptide.getConnection(), iMsfVersion).get(lPeptide.getProcessingNodeNumber()));
            }
        return lPeptideObject.toArray();
    }

    /**
     * This method will collect the score types used in the different msf files
     */
    private void collectPeptideScoreTypes() {
        iMergedPeptidesScores = new Vector<ScoreTypeLowMem>();
        Vector<ScoreTypeLowMem> tempvector;
        for (MsfFile iParsedMsf : iParsedMsfs) {
                if (iMergedPeptidesScores.isEmpty()) {
                    iMergedPeptidesScores = scoreTypeLowMemInstance.getScoreTypes(iParsedMsf.getConnection());
                } else {
                    tempvector = scoreTypeLowMemInstance.getScoreTypes(iParsedMsf.getConnection());
                    for (ScoreTypeLowMem temptype : tempvector) {
                        if (!iMergedPeptidesScores.contains(temptype)) {
                            iMergedPeptidesScores.add(temptype);
                        }
                    }
                }
        }
    }

    /**
     * This method will collect the ratiotypes used in the different msf files
     */
    private void collectRatioTypes() {
        Vector<RatioTypeLowMem> tempRatioTypes;
            for (MsfFile iParsedMsf : iParsedMsfs) {
                tempRatioTypes = ratioTypeLowMemInstance.parseRatioTypes(iParsedMsf.getConnection());
                if (iMergedRatioTypes.isEmpty()) {
                    iMergedRatioTypes = ratioTypeLowMemInstance.parseRatioTypes(iParsedMsf.getConnection());
                } else {
                    for (int k = 0; k < iMergedRatioTypes.size(); k++) {
                        if (tempRatioTypes.contains(iMergedRatioTypes.get(k))) {
                            iMergedRatioTypes.add(tempRatioTypes.get(k));
                        }
                    }
                }
            }
    }

    /**
     * This method will collect the custom peptide data used in the different
     * msf files
     */
    private void collectCustomSpectrumData() {
        try {
            for (MsfFile iParsedMsf : iParsedMsfs) {
                ArrayList<CustomDataField> customSpectraData = customDataLowMemControllerInstance.getCustomSpectraData(customDataLowMemControllerInstance.getCustomFieldMap(iParsedMsf.getConnection()), iParsedMsf.getConnection());
                for (CustomDataField aCustomSpectraData : customSpectraData) {
                    boolean lFound = false;
                    for (CustomDataField anIMergedCustomSpectrumData : iMergedCustomSpectrumData) {
                        if (aCustomSpectraData.equals(anIMergedCustomSpectrumData)) {
                            lFound = true;
                        }
                    }
                    if (!lFound) {
                        iMergedCustomSpectrumData.add(aCustomSpectraData);
                    }
                }
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
    }

    /**
     * This method will set the msms spectrum annotation for a peptide
     *
     * @param lPeptide The peptide to set the annotation for
     * @throws java.sql.SQLException jdlkajfla
     */
    public void setSpectrumMSMSAnnotations(PeptideLowMem lPeptide) throws SQLException {
        Vector<DefaultSpectrumAnnotation> lAnnotations = new Vector<DefaultSpectrumAnnotation>();
        if (iMSMSspectrumPanel != null && lPeptide != null) {
            int lMaximumCharge = (iSelectedPeptide).getParentSpectrum().getCharge();
            if (chargeOneJCheckBox.isSelected()) {
                addIonAnnotationByCharge(lAnnotations, 1, lPeptide);
            }
            if (chargeTwoJCheckBox.isSelected()) {
                addIonAnnotationByCharge(lAnnotations, 2, lPeptide);
            }
            if (chargeOverTwoJCheckBox.isSelected()) {
                for (int i = 3; i <= lMaximumCharge; i++) {
                    addIonAnnotationByCharge(lAnnotations, 3, lPeptide);
                }
            }
        }
        iMSMSspectrumPanel.setAnnotations(lAnnotations);
        iMSMSspectrumPanel.validate();
        iMSMSspectrumPanel.repaint();
    }

    /**
     * This method will generate the spectrum annotations based on the charge
     * and the ion checkboxes
     *
     * @param lAnnotations Vector to add the annotations to
     * @param lCharge The charge
     * @param lPeptide The peptide
     * @return Vector with the annotations
     */
    public Vector<DefaultSpectrumAnnotation> addIonAnnotationByCharge(Vector<DefaultSpectrumAnnotation> lAnnotations, int lCharge, PeptideLowMem lPeptide) {
        Vector<PeptideFragmentIon.PeptideFragmentIonType> lIonTypes = new Vector<PeptideFragmentIon.PeptideFragmentIonType>();
        if (aIonsJCheckBox.isSelected()) {
            lIonTypes.add(PeptideFragmentIon.PeptideFragmentIonType.A_ION);
            if (nh3IonsJCheckBox.isSelected()) {
                lIonTypes.add(PeptideFragmentIon.PeptideFragmentIonType.ANH3_ION);
            }
            if (h2oIonsJCheckBox.isSelected()) {
                lIonTypes.add(PeptideFragmentIon.PeptideFragmentIonType.AH2O_ION);
            }
        }
        if (bIonsJCheckBox.isSelected()) {
            lIonTypes.add(PeptideFragmentIon.PeptideFragmentIonType.B_ION);
            if (nh3IonsJCheckBox.isSelected()) {
                lIonTypes.add(PeptideFragmentIon.PeptideFragmentIonType.BNH3_ION);
            }
            if (h2oIonsJCheckBox.isSelected()) {
                lIonTypes.add(PeptideFragmentIon.PeptideFragmentIonType.BH2O_ION);
            }
        }
        if (cIonsJCheckBox.isSelected()) {
            lIonTypes.add(PeptideFragmentIon.PeptideFragmentIonType.C_ION);
        }
        if (xIonsJCheckBox.isSelected()) {
            lIonTypes.add(PeptideFragmentIon.PeptideFragmentIonType.X_ION);
        }
        if (yIonsJCheckBox.isSelected()) {
            lIonTypes.add(PeptideFragmentIon.PeptideFragmentIonType.Y_ION);
            if (nh3IonsJCheckBox.isSelected()) {
                lIonTypes.add(PeptideFragmentIon.PeptideFragmentIonType.YNH3_ION);
            }
            if (h2oIonsJCheckBox.isSelected()) {
                lIonTypes.add(PeptideFragmentIon.PeptideFragmentIonType.YH2O_ION);
            }
        }
        if (zIonsJCheckBox.isSelected()) {
            lIonTypes.add(PeptideFragmentIon.PeptideFragmentIonType.Z_ION);
        }

        Vector<PeptideFragmentIon> lFragmentIons = lPeptide.getFragmentIonsByTypeAndCharge(lCharge, lIonTypes);

        String chargeAsString = "";

        for (int i = 0; i < lCharge; i++) {
            chargeAsString += "+";
        }

        if (lCharge == 1) {
            chargeAsString = "";
        }

        for (int i = 0; i < lFragmentIons.size(); i++) {
            DefaultSpectrumAnnotation lAnno;
            PeptideFragmentIon lIon = lFragmentIons.get(i);

            String label = lIon.getIonType() + lIon.getNumber() + chargeAsString + lIon.getNeutralLoss();
            lAnno = new DefaultSpectrumAnnotation(lIon.theoreticMass, iMSMSerror, SpectrumPanel.determineColorOfPeak(label), label);

            if (lAnno != null) {
                lAnnotations.add(lAnno);
            }
        }
        return lAnnotations;
    }

    /**
     * Retrieves the version number set in the properties file
     *
     * @return the version number of the thermo-msf parser
     */
    public final String getVersion() {
        Properties p = new Properties();
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("thermo_msf_parser.properties");
            p.load(is);
        } catch (IOException e) {
            logger.info(e);
        }
        return p.getProperty("version");
    }

    private void peptidesTableKeyReleased(KeyEvent evt) {
        try {
            peptidesTableMouseClicked(null);
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
    }

    /**
     * Formats the protein sequence such that both the covered parts of the
     * sequence and the peptide selected in the peptide table is highlighted.
     * This code is based on the compomics utilities sample code
     *
     * @param lProtein protein object
     */
    public void formatProteinSequence(ProteinLowMem lProtein) {
        // and clear the peptide sequence coverage details
        proteinSequenceCoverageJEditorPane.setText("");
        String lCleanProteinSequence;
        try {
            lCleanProteinSequence = proteinLowMemInstance.getSequenceForProteinID(lProtein.getProteinID(), lProtein.getConnection());
        } catch (SQLException e) {
            logger.info(e);
            return;
        }
        int selectedPeptideStart = -1;
        int selectedPeptideEnd = -1;

        // find the start end end indices for the currently selected peptide, if any
        if (iSelectedPeptide != null) {
            SpectrumLowMem tempspectrum = iSelectedPeptide.getParentSpectrum();
            int lConfidenceLevel = iSelectedPeptide.getConfidenceLevel();
            boolean lUse = false;
            if (chbHighConfident.isSelected() && lConfidenceLevel == 3) {
                lUse = true;
            }
            if (chbMediumConfident.isSelected() && lConfidenceLevel == 2) {
                lUse = true;
            }
            if (chbLowConfident.isSelected() && lConfidenceLevel == 1) {
                lUse = true;
            }
            /*if (onlyHighestScoringRadioButton.isSelected()) {
             if (!tempspectrum.isHighestScoring(iSelectedPeptide, iMajorScoreTypes)) {
             lUse = false;
             }
             }
             if (onlyLowestScoringRadioButton.isSelected()) {
             if (!tempspectrum.isLowestScoring(iSelectedPeptide, iMajorScoreTypes)) {
             lUse = false;
             }*/

            selectedPeptideStart = lCleanProteinSequence.indexOf(iSelectedPeptide.getSequence());
            selectedPeptideEnd = selectedPeptideStart + iSelectedPeptide.getSequence().length() + 1;
            selectedPeptideStart = selectedPeptideStart + 1;

        }
        // an array containing the coverage index for each residue
        int[] coverage = new int[lCleanProteinSequence.length() + 1];

        Vector<PeptideLowMem> lPeptides = null;
            lPeptides = peptideLowMemInstance.getPeptidesForProtein(lProtein, iMsfVersion, iAminoAcids);
        // iterate the peptide table and store the coverage for each peptide
        for (PeptideLowMem lPeptide : lPeptides) {
            int lConfidenceLevel = lPeptide.getConfidenceLevel();
            boolean lUse = false;
            if (chbHighConfident.isSelected() && lConfidenceLevel == 3) {
                lUse = true;
            }
            if (chbMediumConfident.isSelected() && lConfidenceLevel == 2) {
                lUse = true;
            }
            if (chbLowConfident.isSelected() && lConfidenceLevel == 1) {
                lUse = true;
            }
            SpectrumLowMem tempspectrum = lPeptide.getParentSpectrum();

            if (onlyHighestScoringRadioButton.isSelected()) {
                if (!tempspectrum.isHighestScoring(lPeptide, iMajorScoreTypes)) {
                    lUse = false;
                }
            }
            if (onlyLowestScoringRadioButton.isSelected()) {
                if (!tempspectrum.isLowestScoring(lPeptide, iMajorScoreTypes)) {
                    lUse = false;
                }
            }
            if (lUse) {
                int tempPeptideStart = lCleanProteinSequence.indexOf(lPeptide.getSequence());
                int tempPeptideEnd = tempPeptideStart + lPeptide.getSequence().length();
                tempPeptideStart = tempPeptideStart + 1;

                for (int j = tempPeptideStart; j <= tempPeptideEnd; j++) {
                    coverage[j]++;
                }
            }
        }

        ArrayList<Integer> selectedPeptideStartList = new ArrayList<Integer>();
        selectedPeptideStartList.add(selectedPeptideStart);

        ArrayList<Integer> selectedPeptideEndList = new ArrayList<Integer>();
        selectedPeptideEndList.add(selectedPeptideEnd);

        // format and display the protein sequence coverage
        double sequenceCoverage = ProteinSequencePane.formatProteinSequence(proteinSequenceCoverageJEditorPane, lCleanProteinSequence, selectedPeptideStartList, selectedPeptideEndList, coverage);

        // display the percent sequence coverage
        sequenceCoverageJLabel.setText("Protein Coverage: " + Util.roundDouble(sequenceCoverage, 2) + "%");
    }

    /**
     * Update the tables based on the spectrum selected.
     *
     * @throws java.sql.SQLException
     */
    private void peptidesTableMouseClicked(MouseEvent evt) throws SQLException {

        // Set the cursor into the wait status.
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        int row = jtablePeptides.getSelectedRow();

        // Condition if one row is selected.
        if (row != -1) {
            selectedProteinListModel.clear();
            iSelectedPeptide = (PeptideLowMem) jtablePeptides.getValueAt(row, 2);
            iDisplayedProteinsOfInterest = proteinLowMemInstance.getProteinsForPeptide(iSelectedPeptide.getPeptideId(), iSelectedPeptide.getConnection());
            if (!proteinList.isSelectionEmpty()) {
                iDisplayedProteinsOfInterest.remove(proteinList.getSelectedValue());
            }
            for (ProteinLowMem aVectorEntry : iDisplayedProteinsOfInterest) {

                selectedProteinListModel.addElement(aVectorEntry);
            }
            SpectrumLowMem spectrumOfPeptide = iSelectedPeptide.getParentSpectrum();
            spectrumLowMemInstance.unzipXMLforSpectrum(spectrumOfPeptide);

            try {
                if (msmsCheckBox.isSelected()) {

                    //do the MSMS
                    if (jtabpanLower.indexOfTab("MS/MS Spectrum") == -1) {
                        jtabpanLower.add("MS/MS Spectrum", jpanMSMS);
                    }
                    Vector<Peak> lPeaks = spectrumLowMemInstance.getMSMSPeaks(spectrumOfPeptide.getSpectrumXML());
                    double[] lMzValues = new double[lPeaks.size()];
                    double[] lIntensityValues = new double[lPeaks.size()];
                    for (int i = 0; i < lPeaks.size(); i++) {
                        lMzValues[i] = lPeaks.get(i).getX();
                        lIntensityValues[i] = lPeaks.get(i).getY();
                    }

                    // Empty the spectrum panel.
                    while (this.jpanMSMSLeft.getComponents().length > 0) {
                        this.jpanMSMSLeft.remove(0);
                    }

                    // Updating the spectrum panel
                    iMSMSspectrumPanel = new SpectrumPanel(
                            lMzValues,
                            lIntensityValues,
                            spectrumOfPeptide.getMz(),
                            spectrumLowMemInstance.getSpectrumTitle(rawFileLowMemInstance.getRawFileNameForFileID(spectrumOfPeptide.getFileId(), iSelectedPeptide.getConnection()), spectrumOfPeptide),
                            String.valueOf(spectrumOfPeptide.getCharge()),
                            50, false, false, false);

                    this.jpanMSMSLeft.add(iMSMSspectrumPanel);
                    this.jpanMSMSLeft.validate();
                    this.jpanMSMSLeft.repaint();
                    this.setSpectrumMSMSAnnotations(iSelectedPeptide);
                } else {
                    jtabpanLower.remove(jpanMSMS);
                    // Empty the spectrum panel.
                    while (this.jpanMSMSLeft.getComponents().length > 0) {
                        this.jpanMSMSLeft.remove(0);
                    }
                    this.jpanMSMSLeft.validate();
                    this.jpanMSMSLeft.repaint();
                    this.jpanMSMSLeft.setMinimumSize(new Dimension(800, 50));
                    this.jpanMSMSLeft.setMinimumSize(new Dimension(800, 50));
                }

                if (msCheckBox.isSelected()) {
                    //do the MS
                    if (jtabpanLower.indexOfTab("MS Spectrum") == -1) {
                        jtabpanLower.add("MS Spectrum", jpanMSHolder);
                    }
                    Vector<Peak> lMSPeaks = spectrumLowMemInstance.getMSPeaks(spectrumOfPeptide.getSpectrumXML());
                    double[] lMSMzValues = new double[lMSPeaks.size()];
                    double[] lMSIntensityValues = new double[lMSPeaks.size()];
                    Vector<DefaultSpectrumAnnotation> lMSAnnotations = new Vector<DefaultSpectrumAnnotation>();
                    double lMinMZvalue = Double.MAX_VALUE;
                    double lMaxMZvalue = Double.MIN_VALUE;
                    for (int i = 0; i < lMSPeaks.size(); i++) {
                        lMSMzValues[i] = lMSPeaks.get(i).getX();
                        if (lMSMzValues[i] > lMaxMZvalue) {
                            lMaxMZvalue = lMSMzValues[i];
                        }
                        if (lMSMzValues[i] < lMinMZvalue) {
                            lMinMZvalue = lMSMzValues[i];
                        }
                        lMSIntensityValues[i] = lMSPeaks.get(i).getY();
                        if (lMSPeaks.get(i).getZ() != 0) {
                            lMSAnnotations.add(new DefaultSpectrumAnnotation(lMSPeaks.get(i).getX(), 0.001, Color.BLACK, lMSPeaks.get(i).getX() + "(Z = " + lMSPeaks.get(i).getZ() + ")"));
                        }
                    }

                    // Empty the spectrum panel.
                    while (this.jpanMS.getComponents().length > 0) {
                        this.jpanMS.remove(0);
                    }

                    // Updating the spectrum panel
                    iMSspectrumPanel = new SpectrumPanel(
                            lMSMzValues,
                            lMSIntensityValues,
                            spectrumOfPeptide.getMz(),
                            "",
                            String.valueOf(spectrumOfPeptide.getCharge()),
                            50, false, false, false);
                    iMSspectrumPanel.rescale(lMinMZvalue, lMaxMZvalue);
                    iMSspectrumPanel.setProfileMode(false);
                    iMSspectrumPanel.setAnnotations(lMSAnnotations);
                    iMSspectrumPanel.setXAxisStartAtZero(false);
                    Peak lFragmentedPeak = spectrumLowMemInstance.getFragmentedMsPeak(spectrumOfPeptide.getSpectrumXML());
                    double lDistance = (lMaxMZvalue - lMinMZvalue) / 50.0;
                    iMSspectrumPanel.addReferenceAreaXAxis(new ReferenceArea("", lFragmentedPeak.getX() - lDistance, lFragmentedPeak.getX() + lDistance, Color.blue, 0.1f, false, true));

                    this.jpanMS.add(iMSspectrumPanel);
                    this.jpanMS.validate();
                    this.jpanMS.repaint();
                } else {
                    jtabpanLower.remove(jpanMSHolder);
                    // Empty the spectrum panel.
                    while (this.jpanMS.getComponents().length > 0) {
                        this.jpanMS.remove(0);
                    }
                    this.jpanMS.validate();
                    this.jpanMS.repaint();
                }

                if (chromatogramCheckBox.isSelected()) {

                    //add the chromatograms
                    try {
                        jtabChromatogram.removeAll();
                        if (jtabpanLower.indexOfTab("Chromatogram") == -1) {
                            jtabpanLower.add("Chromatogram", jtabChromatogram);
                        }
                        //TODO add check for chromatogram downloading if neccesary or not (to save on speed) --> do this on startup also unzip the chromatogram files
                        Vector<Chromatogram> peptideChromatograms = chromatogramLowMemInstance.getChromatogramFileForPeptideID(iSelectedPeptide.getPeptideId(), iSelectedPeptide.getConnection());

                        for (int c = 0; c < peptideChromatograms.size(); c++) {
                            Chromatogram lChro = peptideChromatograms.get(c);
                            if (lChro.getFileId() == spectrumOfPeptide.getFileId()) {
                                Vector<Chromatogram.Point> lPoints = lChro.getPoints();

                                double[] lXvalues = new double[lPoints.size()];
                                double[] lYvalues = new double[lPoints.size()];

                                double lMaxY = 0.0;
                                for (int p = 0; p < lPoints.size(); p++) {
                                    if (lPoints.get(p).getY() > 0.0) {
                                        lXvalues[p] = lPoints.get(p).getT();
                                        lYvalues[p] = lPoints.get(p).getY();
                                        if (lPoints.get(p).getY() > lMaxY) {
                                            lMaxY = lPoints.get(p).getY();
                                        }
                                    }
                                }

                                // create the chromatogram
                                ChromatogramPanel chromatogramPanel = new ChromatogramPanel(
                                        lXvalues, lYvalues, "Time (minutes)", "Intensity");
                                chromatogramPanel.setMaxPadding(65);

                                double lAreaDistance = chromatogramPanel.getMaxXAxisValue() / 500.0;
                                if (iSelectedProtein != null) {
                                    Vector<PeptideLowMem> peptideVector = peptideLowMemInstance.getPeptidesForProtein(iSelectedProtein, iMsfVersion, iAminoAcids);
                                    for (PeptideLowMem lPeptide : peptideVector) {
                                        SpectrumLowMem tempSpectrum = lPeptide.getParentSpectrum();
                                        int lConfidenceLevel = lPeptide.getConfidenceLevel();
                                        boolean lUse = false;
                                        if (chbHighConfident.isSelected() && lConfidenceLevel == 3) {
                                            lUse = true;
                                        }
                                        if (chbMediumConfident.isSelected() && lConfidenceLevel == 2) {
                                            lUse = true;
                                        }
                                        if (chbLowConfident.isSelected() && lConfidenceLevel == 1) {
                                            lUse = true;
                                        }
                                        if (onlyHighestScoringRadioButton.isSelected()) {
                                            if (!tempSpectrum.isHighestScoring(lPeptide, iMajorScoreTypes)) {
                                                lUse = false;
                                            }
                                        }
                                        if (onlyLowestScoringRadioButton.isSelected()) {
                                            if (!tempSpectrum.isLowestScoring(lPeptide, iMajorScoreTypes)) {
                                                lUse = false;
                                            }
                                        }


                                        if (lUse) {
                                            if (rawFileLowMemInstance.getRawFileNameForFileID(tempSpectrum.getFileId(), tempSpectrum.getConnection()).equalsIgnoreCase(rawFileLowMemInstance.getRawFileNameForFileID(tempSpectrum.getFileId(), tempSpectrum.getConnection()))) {
                                                chromatogramPanel.addReferenceAreaXAxis(new ReferenceArea(String.valueOf(tempSpectrum.getFirstScan()), tempSpectrum.getRetentionTime() - lAreaDistance, tempSpectrum.getRetentionTime() + lAreaDistance, Color.blue, 0.1f, false, false));
                                            } else {
                                                chromatogramPanel.addReferenceAreaXAxis(new ReferenceArea(String.valueOf(tempSpectrum.getFirstScan()), tempSpectrum.getRetentionTime() - lAreaDistance, tempSpectrum.getRetentionTime() + lAreaDistance, Color.green, 0.1f, false, false));
                                            }
                                        }

                                    }
                                }
                                chromatogramPanel.addReferenceAreaXAxis(new ReferenceArea(String.valueOf(spectrumOfPeptide.getFirstScan()), spectrumOfPeptide.getRetentionTime() - lAreaDistance, spectrumOfPeptide.getRetentionTime() + lAreaDistance, Color.red, 0.8f, true, false));


                                //chromatogramPanel.setMiniature(true);

                                String lTitle = rawFileLowMemInstance.getRawFileNameForFileID(spectrumOfPeptide.getFileId(), iSelectedPeptide.getConnection());
                                lTitle = lTitle + " - " + lChro.getTraceType();
                                jtabChromatogram.add(lTitle, chromatogramPanel);
                                // remove the default chromatogram panel border, given that our
                                // chromatogram panel already has a border
                                chromatogramPanel.setBorder(null);

                                // add the chromatogram panel to the frame
                                chromatogramPanel.validate();
                                chromatogramPanel.repaint();
                            }
                        }
                    } catch (IOException e) {
                        logger.info(e);
                    }
                } else {
                    jtabpanLower.remove(jtabChromatogram);
                    jtabChromatogram.removeAll();
                }

                if (quantCheckBox.isSelected()) {

                    try {
                        if (spectrumOfPeptide.getQuanResult() != null) {
                            jtabpanLower.add("Quantification Spectrum", jpanQuantificationSpectrumHolder);
                            QuanResultLowMem lQuan = spectrumOfPeptide.getQuanResult();
                            //get the quan events
                            Vector<Event> lQuanEvents = new Vector<Event>();
                            Vector<Integer> lQuanEventsIds = new Vector<Integer>();
                            Vector<Vector<Event>> lQuanEventsByPattern = new Vector<Vector<Event>>();
                            for (int i = 0; i < lQuan.getIsotopePatterns().size(); i++) {
                                Vector<Event> lIsotopePatternEvents = lQuan.getIsotopePatterns().get(i).getEventsWithQuanResult(iSelectedPeptide.getConnection());
                                lQuanEventsByPattern.add(lIsotopePatternEvents);
                                for (Event lIsotopePatternEvent : lIsotopePatternEvents) {
                                    lQuanEvents.add(lIsotopePatternEvent);
                                    lQuanEventsIds.add(lIsotopePatternEvent.getEventId());
                                }
                            }

                            //get the quan events
                            Vector<Vector<Event>> lQuanEventsByPatternWithoutQuanChannel = new Vector<Vector<Event>>();
                            for (int i = 0; i < lQuan.getIsotopePatterns().size(); i++) {
                                Vector<Event> lIsotopePatternEvents = lQuan.getIsotopePatterns().get(i).getEventsWithoutQuanResult(iSelectedPeptide.getConnection());
                                lQuanEventsByPatternWithoutQuanChannel.add(lIsotopePatternEvents);
                                for (Event lIsotopePatternEvent : lIsotopePatternEvents) {
                                    lQuanEvents.add(lIsotopePatternEvent);
                                    lQuanEventsIds.add(lIsotopePatternEvent.getEventId());
                                }
                            }

                            //get the min and max retention and mass
                            double lMinMass = Double.MAX_VALUE;
                            double lMinRT = Double.MAX_VALUE;
                            double lMaxMass = Double.MIN_VALUE;
                            double lMaxRT = Double.MIN_VALUE;

                            for (Event lQuanEvent : lQuanEvents) {
                                if (lMinMass > lQuanEvent.getMass()) {
                                    lMinMass = lQuanEvent.getMass();
                                }
                                if (lMaxMass < lQuanEvent.getMass()) {
                                    lMaxMass = lQuanEvent.getMass();
                                }
                                if (lMinRT > lQuanEvent.getRetentionTime()) {
                                    lMinRT = lQuanEvent.getRetentionTime();
                                }
                                if (lMaxRT < lQuanEvent.getRetentionTime()) {
                                    lMaxRT = lQuanEvent.getRetentionTime();
                                }
                            }
                            //calculate the borders
                            double lMassDiff = Math.abs(lMaxMass - lMinMass);
                            if (lMassDiff == 0) {
                                lMassDiff = 15.0;
                            }
                            lMinMass = lMinMass - (lMassDiff / 3.0);
                            lMaxMass = lMaxMass + (lMassDiff / 3.0);
                            lMinRT = lMinRT - 0.5;
                            lMaxRT = lMaxRT + 0.5;

                            Vector<Event> lBackgroundEvents = Event.getEventByRetentionTimeLimitMassLimitAndFileIdExcludingIds(lMinRT, lMaxRT, lMinMass, lMaxMass, lQuanEventsIds, spectrumOfPeptide.getFileId(), iSelectedPeptide.getConnection());
                            double[] lQuanMzValues = new double[lBackgroundEvents.size()];
                            double[] lQuanIntensityValues = new double[lBackgroundEvents.size()];

                            for (int i = 0; i < lBackgroundEvents.size(); i++) {
                                lQuanMzValues[i] = lBackgroundEvents.get(i).getMass();
                                lQuanIntensityValues[i] = lBackgroundEvents.get(i).getIntensity();
                            }

                            // Empty the spectrum panel.
                            while (this.jpanQuantitationSpectrum.getComponents().length > 0) {
                                this.jpanQuantitationSpectrum.remove(0);
                            }

                            // Updating the spectrum panel
                            iQuantificationSpectrumPanel = new SpectrumPanel(
                                    lQuanMzValues,
                                    lQuanIntensityValues,
                                    spectrumOfPeptide.getMz() / (double) spectrumOfPeptide.getCharge(),
                                    "RT: " + lMinRT + " - " + lMaxRT,
                                    String.valueOf(spectrumOfPeptide.getCharge()),
                                    50, true, true, false);
                            iQuantificationSpectrumPanel.rescale(lMinMass, lMaxMass);
                            iQuantificationSpectrumPanel.setProfileMode(false);
                            iQuantificationSpectrumPanel.setXAxisStartAtZero(false);
                            Vector<DefaultSpectrumAnnotation> lQuanAnnotations = new Vector<DefaultSpectrumAnnotation>();
                            for (int i = 0; i < lQuan.getIsotopePatterns().size(); i++) {
                                double[] lQuanPatternMzValues = new double[lQuanEventsByPattern.get(i).size()];
                                double[] lQuanPatternIntensityValues = new double[lQuanEventsByPattern.get(i).size()];
                                for (int j = 0; j < lQuanEventsByPattern.get(i).size(); j++) {
                                    lQuanPatternMzValues[j] = lQuanEventsByPattern.get(i).get(j).getMass();
                                    lQuanPatternIntensityValues[j] = lQuanEventsByPattern.get(i).get(j).getIntensity();
                                    for (int k = 0; k < lQuan.getIsotopePatterns().get(i).getEventAnnotations().size(); k++) {
                                        if (lQuanEventsByPattern.get(i).get(j).getEventId() == lQuan.getIsotopePatterns().get(i).getEventAnnotations().get(k).getEventId()) {
                                            if (lQuan.getIsotopePatterns().get(i).getEventAnnotations().get(k).getQuanChannelId() != -1) {
                                                lQuanAnnotations.add(new DefaultSpectrumAnnotation(lQuanEventsByPattern.get(i).get(j).getMass(), 0.000000000000000000000001, Color.BLACK, "" + getQuanChannelNameById(lQuan.getIsotopePatterns().get(i).getEventAnnotations().get(k).getQuanChannelId())));
                                            }
                                        }
                                    }
                                }
                                if (lQuanPatternMzValues.length > 0) {
                                    iQuantificationSpectrumPanel.addAdditionalDataset(lQuanPatternMzValues, lQuanPatternIntensityValues, Color.GREEN, Color.GREEN);
                                }
                            }

                            for (int i = 0; i < lQuan.getIsotopePatterns().size(); i++) {
                                double[] lQuanPatternMzValues = new double[lQuanEventsByPatternWithoutQuanChannel.get(i).size()];
                                double[] lQuanPatternIntensityValues = new double[lQuanEventsByPatternWithoutQuanChannel.get(i).size()];
                                for (int j = 0; j < lQuanEventsByPatternWithoutQuanChannel.get(i).size(); j++) {
                                    lQuanPatternMzValues[j] = lQuanEventsByPatternWithoutQuanChannel.get(i).get(j).getMass();
                                    lQuanPatternIntensityValues[j] = lQuanEventsByPatternWithoutQuanChannel.get(i).get(j).getIntensity();
                                }
                                if (lQuanPatternMzValues.length > 0) {
                                    iQuantificationSpectrumPanel.addAdditionalDataset(lQuanPatternMzValues, lQuanPatternIntensityValues, Color.BLUE, Color.BLUE);
                                }
                            }

                            iQuantificationSpectrumPanel.setAnnotations(lQuanAnnotations);

                            this.jpanQuantitationSpectrum.add(iQuantificationSpectrumPanel);
                            this.jpanQuantitationSpectrum.validate();
                            this.jpanQuantitationSpectrum.repaint();


                        } else {
                            jtabpanLower.remove(jpanQuantificationSpectrumHolder);
                            // Empty the spectrum panel.
                            while (this.jpanQuantitationSpectrum.getComponents().length > 0) {
                                this.jpanQuantitationSpectrum.remove(0);
                            }
                            this.jpanQuantitationSpectrum.validate();
                            this.jpanQuantitationSpectrum.repaint();
                        }
                    } catch (SQLException e) {
                        logger.info(e);
                    }
                } else {
                    jtabpanLower.remove(jpanQuantificationSpectrumHolder);
                    // Empty the spectrum panel.
                    while (this.jpanQuantitationSpectrum.getComponents().length > 0) {
                        this.jpanQuantitationSpectrum.remove(0);
                    }
                    this.jpanQuantitationSpectrum.validate();
                    this.jpanQuantitationSpectrum.repaint();
                }


                //check if the protein coverage has to be changed
                if (iSelectedProtein != null) {
                    formatProteinSequence(iSelectedProtein);
                }

            } catch (IOException e) {
                logger.info(e);
            } catch (Exception e) {
                logger.info(e);
            }

        } else {
            if (!chromatogramCheckBox.isSelected()) {
                if (jtabpanLower.indexOfTab("Chromatogram") > -1) {
                    jtabpanLower.remove(jtabChromatogram);
                    jtabChromatogram.validate();
                    jtabChromatogram.repaint();
                }
            }
            if (!quantCheckBox.isSelected()) {
                if (jtabpanLower.indexOfTab("Quantification Spectrum") > -1) {
                    jtabpanLower.remove(jpanQuantificationSpectrumHolder);
                    jpanQuantificationSpectrumHolder.validate();
                    jpanQuantificationSpectrumHolder.repaint();
                }
            }
            if (!msmsCheckBox.isSelected()) {
                if (jtabpanLower.indexOfTab("MS/MS Spectrum") > -1) {
                    jtabpanLower.remove(jpanMSMS);
                    jpanMSMS.validate();
                    jpanMSMS.repaint();
                }
            }
            if (!msCheckBox.isSelected()) {
                if (jtabpanLower.indexOfTab("MS Spectrum") > -1) {
                    jtabpanLower.remove(jpanMSHolder);
                    jpanMSHolder.validate();
                    jpanMSHolder.repaint();
                }
            }
        }

        // At the end set the cursor back to default.
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Main method
     *
     * @param args no arguments are expected
     */
    public static void main(String[] args) {
        try {
            UtilitiesGUIDefaults.setLookAndFeel();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        new Thermo_msf_parserGUILowMem(true);
    }

    /**
     * This method will create custom gui elements
     */
    private void createUIComponents() {
        jpanMSMSLeft = new JPanel();
        jpanMS = new JPanel();
        jpanQuantitationSpectrum = new JPanel();
        aIonsJCheckBox = new JCheckBox("a");
        bIonsJCheckBox = new JCheckBox("b");
        cIonsJCheckBox = new JCheckBox("c");
        yIonsJCheckBox = new JCheckBox("y");
        xIonsJCheckBox = new JCheckBox("x");
        zIonsJCheckBox = new JCheckBox("z");
        chargeOneJCheckBox = new JCheckBox("+");
        chargeTwoJCheckBox = new JCheckBox("++");
        chargeOverTwoJCheckBox = new JCheckBox(">2");
        nh3IonsJCheckBox = new JCheckBox("");
        h2oIonsJCheckBox = new JCheckBox("");
        txtMSMSerror = new JTextField();
        txtMSMSerror.setText("0.5");
        txtMSMSerror.setToolTipText("The MS/MS fragmentation error (in Da)");
        txtMSMSerror.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                changeMSMSerror();
            }
        });

        this.jpanMSMSLeft.setLayout(new BoxLayout(this.jpanMSMSLeft, BoxLayout.LINE_AXIS));
        this.jpanMS.setLayout(new BoxLayout(this.jpanMS, BoxLayout.LINE_AXIS));
        this.jpanQuantitationSpectrum.setLayout(new BoxLayout(this.jpanQuantitationSpectrum, BoxLayout.LINE_AXIS));

        aIonsJCheckBox.setSelected(true);
        aIonsJCheckBox.setText("a");
        aIonsJCheckBox.setToolTipText("Show a-ions");
        aIonsJCheckBox.setMaximumSize(new Dimension(39, 23));
        aIonsJCheckBox.setMinimumSize(new Dimension(39, 23));
        aIonsJCheckBox.setPreferredSize(new Dimension(39, 23));
        aIonsJCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ionsJCheckBoxActionPerformed();
            }
        });

        bIonsJCheckBox.setSelected(true);
        bIonsJCheckBox.setText("b");
        bIonsJCheckBox.setToolTipText("Show b-ions");
        bIonsJCheckBox.setMaximumSize(new Dimension(39, 23));
        bIonsJCheckBox.setMinimumSize(new Dimension(39, 23));
        bIonsJCheckBox.setPreferredSize(new Dimension(39, 23));
        bIonsJCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ionsJCheckBoxActionPerformed();
            }
        });

        cIonsJCheckBox.setSelected(true);
        cIonsJCheckBox.setText("c");
        cIonsJCheckBox.setToolTipText("Show c-ions");
        cIonsJCheckBox.setMaximumSize(new Dimension(39, 23));
        cIonsJCheckBox.setMinimumSize(new Dimension(39, 23));
        cIonsJCheckBox.setPreferredSize(new Dimension(39, 23));
        cIonsJCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ionsJCheckBoxActionPerformed();
            }
        });

        yIonsJCheckBox.setSelected(true);
        yIonsJCheckBox.setText("y");
        yIonsJCheckBox.setToolTipText("Show y-ions");
        yIonsJCheckBox.setMaximumSize(new Dimension(39, 23));
        yIonsJCheckBox.setMinimumSize(new Dimension(39, 23));
        yIonsJCheckBox.setPreferredSize(new Dimension(39, 23));
        yIonsJCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ionsJCheckBoxActionPerformed();
            }
        });

        xIonsJCheckBox.setSelected(true);
        xIonsJCheckBox.setText("x");
        xIonsJCheckBox.setToolTipText("Show x-ions");
        xIonsJCheckBox.setMaximumSize(new Dimension(39, 23));
        xIonsJCheckBox.setMinimumSize(new Dimension(39, 23));
        xIonsJCheckBox.setPreferredSize(new Dimension(39, 23));
        xIonsJCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ionsJCheckBoxActionPerformed();
            }
        });

        zIonsJCheckBox.setSelected(true);
        zIonsJCheckBox.setText("z");
        zIonsJCheckBox.setToolTipText("Show z-ions");
        zIonsJCheckBox.setMaximumSize(new Dimension(39, 23));
        zIonsJCheckBox.setMinimumSize(new Dimension(39, 23));
        zIonsJCheckBox.setPreferredSize(new Dimension(39, 23));
        zIonsJCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ionsJCheckBoxActionPerformed();
            }
        });

        chargeOneJCheckBox.setSelected(true);
        chargeOneJCheckBox.setText("+");
        chargeOneJCheckBox.setToolTipText("Show ions with charge 1");
        chargeOneJCheckBox.setMaximumSize(new Dimension(39, 23));
        chargeOneJCheckBox.setMinimumSize(new Dimension(39, 23));
        chargeOneJCheckBox.setPreferredSize(new Dimension(39, 23));
        chargeOneJCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ionsJCheckBoxActionPerformed();
            }
        });

        chargeTwoJCheckBox.setSelected(true);
        chargeTwoJCheckBox.setText("++");
        chargeTwoJCheckBox.setToolTipText("Show ions with charge 2");
        chargeTwoJCheckBox.setMaximumSize(new Dimension(39, 23));
        chargeTwoJCheckBox.setMinimumSize(new Dimension(39, 23));
        chargeTwoJCheckBox.setPreferredSize(new Dimension(39, 23));
        chargeTwoJCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ionsJCheckBoxActionPerformed();
            }
        });

        chargeOverTwoJCheckBox.setSelected(true);
        chargeOverTwoJCheckBox.setText(">2");
        chargeOverTwoJCheckBox.setToolTipText("Show ions with charge >2");
        chargeOverTwoJCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ionsJCheckBoxActionPerformed();
            }
        });

        nh3IonsJCheckBox.setSelected(true);
        nh3IonsJCheckBox.setText("NH3");
        nh3IonsJCheckBox.setToolTipText("Show NH3-loss");
        nh3IonsJCheckBox.setMaximumSize(new Dimension(50, 23));
        nh3IonsJCheckBox.setMinimumSize(new Dimension(50, 23));
        nh3IonsJCheckBox.setPreferredSize(new Dimension(50, 23));
        nh3IonsJCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ionsJCheckBoxActionPerformed();
            }
        });

        h2oIonsJCheckBox.setSelected(true);
        h2oIonsJCheckBox.setText("H20");
        h2oIonsJCheckBox.setToolTipText("Show H20-loss");
        h2oIonsJCheckBox.setMaximumSize(new Dimension(50, 23));
        h2oIonsJCheckBox.setMinimumSize(new Dimension(50, 23));
        h2oIonsJCheckBox.setPreferredSize(new Dimension(50, 23));
        h2oIonsJCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ionsJCheckBoxActionPerformed();
            }
        });


        aIonsJCheckBox.setSelected(false);
        cIonsJCheckBox.setSelected(false);
        xIonsJCheckBox.setSelected(false);
        zIonsJCheckBox.setSelected(false);
        chargeTwoJCheckBox.setSelected(false);
        chargeOverTwoJCheckBox.setSelected(false);
        nh3IonsJCheckBox.setSelected(false);
        h2oIonsJCheckBox.setSelected(false);


        proteinCoverageJScrollPane = new JScrollPane();
        proteinSequenceCoverageJEditorPane = new JEditorPane();
        proteinCoverageJScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        proteinSequenceCoverageJEditorPane.setContentType("text/html");
        proteinSequenceCoverageJEditorPane.setEditable(false);
        proteinSequenceCoverageJEditorPane.setText("<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n\n    </p>\r\n  </body>\r\n</html>\r\n");
        proteinSequenceCoverageJEditorPane.setMargin(new Insets(10, 10, 10, 10));
        proteinSequenceCoverageJEditorPane.setMinimumSize(new Dimension(22, 22));
        proteinSequenceCoverageJEditorPane.setPreferredSize(new Dimension(22, 22));
        proteinCoverageJScrollPane.setViewportView(proteinSequenceCoverageJEditorPane);

        proteinCoverageJScrollPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                proteinSequenceCoverageJEditorPaneResized(evt);
            }
        });
    }

    /**
     * Makes sure that the sequence coverage area is rescaled to fit the new
     * size of the frame.
     *
     *
     */
    private void proteinSequenceCoverageJEditorPaneResized(ComponentEvent evt) {
        if (iSelectedProtein != null) {
            formatProteinSequence(iSelectedProtein);
        }
    }

    /**
     * This method will load a protein. It will create the peptide table and
     * format the protein sequence
     *
     * @param aFromInterestedList A boolean that indicates if the selected
     * protein should come from the proteinList (FALSE) or from the
     * selectedProteinList (=TRUE)
     */
    public void loadProtein(boolean aFromInterestedList) {
        if (aFromInterestedList) {

            iSelectedProtein = (ProteinLowMem) selectedProteinList.getSelectedValue();
        } else {
            iSelectedProtein = (ProteinLowMem) proteinList.getSelectedValue();
        }
        /*if (iRover != null) {
         for (int i = 0; i < iRover.getProteinList().getModel().getSize(); i++) {
         QuantitativeProtein lProtein = (QuantitativeProtein) iRover.getProteinList().getModel().getElementAt(i);
         if (lProtein.getAccession().equalsIgnoreCase(iSelectedProtein.getAccession())) {
         iRover.getProteinList().setSelectedValue(lProtein, true);
         iRover.loadProtein(true);
         }
         }
         }*/
        createPeptideTable(iSelectedProtein);
        formatProteinSequence(iSelectedProtein);
        jtablePeptides.updateUI();

    }

    public String getQuanChannelNameById(int lChannelId) {
        /*for (int i = 0; i < iQuanChannelIds.size(); i++) {
         if (iQuanChannelIds.get(i) == lChannelId) {
         return iComponents.get(i);
         }
         }            */
        return null;
    }

    /**
     * A .msf file filter
     */
    class MsfFileFilter extends FileFilter {

        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".msf");
        }

        public String getDescription() {
            return ".msf files";
        }
    }
}