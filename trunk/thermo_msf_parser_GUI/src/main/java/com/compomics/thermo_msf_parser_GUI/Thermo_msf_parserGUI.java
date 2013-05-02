package com.compomics.thermo_msf_parser_GUI;

import com.compomics.thermo_msf_parser_API.highmeminstance.Chromatogram;
import com.compomics.thermo_msf_parser_API.highmeminstance.CustomDataField;
import com.compomics.thermo_msf_parser_API.highmeminstance.Parser;
import com.compomics.thermo_msf_parser_API.highmeminstance.Peak;
import com.compomics.thermo_msf_parser_API.highmeminstance.Peptide;
import com.compomics.thermo_msf_parser_API.highmeminstance.PeptideFragmentIon;
import com.compomics.thermo_msf_parser_API.highmeminstance.ProcessingNode;
import com.compomics.thermo_msf_parser_API.highmeminstance.Protein;
import com.compomics.thermo_msf_parser_API.highmeminstance.QuanResult;
import com.compomics.thermo_msf_parser_API.highmeminstance.RatioType;
import com.compomics.thermo_msf_parser_API.highmeminstance.ScoreType;
import com.compomics.thermo_msf_parser_API.highmeminstance.Spectrum;
import com.compomics.thermo_msf_parser_API.highmeminstance.Event;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 23-Feb-2011
 * Time: 08:01:12
 */
public class Thermo_msf_parserGUI extends JFrame {
    // Class specific log4j logger for Thermo_msf_parserGUI instances.
    private static Logger logger = Logger.getLogger(Thermo_msf_parserGUI.class);
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
    private JCheckBox chbLowConfidence;
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

    /**
     * A vector with the absolute paths to the msf file
     */
    private List<String> iMsfFileLocations = new ArrayList<String>();
    /**
     * A List with the parsed msf files
     */
    private List<Parser> iParsedMsfs = new ArrayList<Parser>();
    /**
     * A List with the different scoretypes found in the different files
     */
    private List<ScoreType> iMergedPeptidesScores;
    /**
     * The currently selected peptide
     */
    private Peptide iSelectedPeptide;
    /**
     * The currently selected protein
     */
    private Protein iSelectedProtein;
    /**
     * The msms fragmentation error
     */
    private double iMSMSerror = 0.5;
    /**
     * List with all the proteins
     */
    private List<Protein> iProteins = new ArrayList<Protein>();
    /**
     * List with all the proteins displayed in the protein list
     */
    private List<Protein> iDisplayedProteins = new ArrayList<Protein>();
    /**
     * List with all the proteins displayed in the protein list
     */
    private List<Protein> iDisplayedProteinsOfInterest = new ArrayList<Protein>();
    /**
     * A hashmap with the protein accession as key and the protein as value
     */
    private HashMap<String, Protein> iProteinsMap = new HashMap<String, Protein>();
    /**
     * The different custom data fields used for peptides in all the files
     */
    private List<CustomDataField> iMergedCustomPeptideData;
    /**
     * The different custom data fields used for spectra in all the files
     */
    private List<CustomDataField> iMergedCustomSpectrumData;
    /**
     * The different ratio types found in the msf files
     */
    private List<RatioType> iMergedRatioTypes;
    /**
     * The major score type
     */
    private List<ScoreType> iMajorScoreTypes = new ArrayList<ScoreType>();
    /**
     * Boolean that indicates if this is a stand alone window
     */
    private boolean iStandAlone;
    /**
     * Boolean that indicates if quantifications are found in the msf files
     */
    private boolean iQuantitationFound = false;
    /**
     * Boolean indicating phosphoRS is calculated in msf files
     */
    private boolean hasPhosphoRS;


    /**
     * The constructor
     *
     * @param lStandAlone
     */
    public Thermo_msf_parserGUI(boolean lStandAlone) {

        this.iStandAlone = lStandAlone;

        //create the gui
        jtablePeptides = new JTable();
        jscollPeptides = new JScrollPane();
        chbHighConfident = new JCheckBox("High");
        chbHighConfident.setSelected(true);
        chbMediumConfident = new JCheckBox("Medium");
        chbMediumConfident.setSelected(true);
        chbLowConfidence = new JCheckBox("Low");
        onlyHighestScoringRadioButton = new JRadioButton();
        onlyLowestScoringRadioButton = new JRadioButton();
        allRadioButton = new JRadioButton();
        proteinList = new JList(iDisplayedProteins.toArray());
        proteinList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectedProteinList = new JList(iDisplayedProteinsOfInterest.toArray());
        selectedProteinList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData(true);
            }
        }
        );
        final JMenuItem lCloseItem = new JMenuItem("Close");
        lCloseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeMethod();
            }
        }
        );
        final JMenuItem lAboutItem = new JMenuItem("About");
        lAboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HelpWindow lHelp = new HelpWindow(getFrame(), getClass().getResource("/about.html"));
                lHelp.setTitle("About Thermo MSF Viewer");
            }
        }
        );
        lInfoMenu.add(lAboutItem);
        final JMenuItem lHelpItem = new JMenuItem("Help");
        lHelpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        lHelpItem.addActionListener(new ActionListener() {
            @Override
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
        }
        );
        lInfoMenu.add(lHelpItem);

        // Create a menu item
        final JMenuItem item = new JMenuItem("Export Peptides as CSV");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //open file chooser
                final String lPath;
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showSaveDialog(getFrame());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    if (file.getAbsolutePath().endsWith(".csv")) {
                        lPath = file.getAbsolutePath();
                    } else {
                        lPath = file.getAbsolutePath() + ".csv";
                    }
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Save command cancelled by user.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                com.compomics.util.sun.SwingWorker lCsvSaver = new com.compomics.util.sun.SwingWorker() {
                    @Override
                    public Boolean construct() {
                        //create the writer
                        try {
                            BufferedWriter out = new BufferedWriter(new FileWriter(lPath));

                            //write column headers
                            String lLine = "";
                            for (int j = 0; j < jtablePeptides.getModel().getColumnCount(); j++) {

                                lLine = lLine + jtablePeptides.getModel().getColumnName(j) + ",";
                            }
                            out.write(lLine + "\n");

                            //write data
                            for (int i = 0; i < jtablePeptides.getModel().getRowCount(); i++) {
                                lLine = "";
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


                        return true;
                    }

                    @Override
                    public void finished() {
                        JOptionPane.showMessageDialog(new JFrame(), "Saving done", "Info", JOptionPane.INFORMATION_MESSAGE);

                    }

                };
                lCsvSaver.start();


            }
        }


        );


        // Create a menu item
        final JMenuItem lItemMgf = new JMenuItem("Export Spectra as MGF");
        lItemMgf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Export spectra mgf");
                //open file chooser
                final String lPath;
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showSaveDialog(getFrame());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
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
                    @Override
                    public Boolean construct() {
                        //create the writer
                        try {
                            int lTotalSpecrtra = 0;
                            for (int i = 0; i < iParsedMsfs.size(); i++) {
                                lTotalSpecrtra = lTotalSpecrtra + iParsedMsfs.get(i).getSpectra().size();
                            }
                            progressBar.setMaximum(lTotalSpecrtra + 1);
                            progressBar.setValue(0);
                            progressBar.setString("Writing all spectra to " + lPath);
                            progressBar.setStringPainted(true);
                            progressBar.setVisible(true);
                            BufferedWriter out = new BufferedWriter(new FileWriter(lPath));

                            for (int i = 0; i < iParsedMsfs.size(); i++) {
                                for (int j = 0; j < iParsedMsfs.get(i).getSpectra().size(); j++) {
                                    Spectrum lSpectrum = iParsedMsfs.get(i).getSpectra().get(j);
                                    String lSpectrumLine = "BEGIN IONS\nTITLE=" + lSpectrum.getSpectrumTitle() + "\n";
                                    Peak lMono = lSpectrum.getFragmentedMsPeak();
                                    lSpectrumLine = lSpectrumLine + "PEPMASS=" + lMono.getX() + "\t" + lMono.getY() + "\n";
                                    lSpectrumLine = lSpectrumLine + "CHARGE=" + lSpectrum.getCharge() + "+\n";
                                    lSpectrumLine = lSpectrumLine + "RTINSECONDS=" + (lSpectrum.getRetentionTime() / 60.0) + "\n";
                                    if (lSpectrum.getFirstScan() != lSpectrum.getFirstScan()) {
                                        lSpectrumLine = lSpectrumLine + "SCANS=" + lSpectrum.getFirstScan() + "." + lSpectrum.getLastScan() + "\n";
                                    } else {
                                        lSpectrumLine = lSpectrumLine + "SCANS=" + lSpectrum.getFirstScan() + "\n";
                                    }
                                    List<Peak> lMSMS = lSpectrum.getMSMSPeaks();
                                    for (int k = 0; k < lMSMS.size(); k++) {
                                        lSpectrumLine = lSpectrumLine + lMSMS.get(k).getX() + "\t" + lMSMS.get(k).getY() + "\n";
                                    }
                                    lSpectrumLine = lSpectrumLine + "END IONS\n\n";

                                    out.write(lSpectrumLine);
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
                        progressBar.setStringPainted(false);
                    }
                };
                lMgfSaver.start();
                progressBar.setString("");


            }
        }
        );


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
            @Override
            public void componentResized(ComponentEvent evt) {
                split1.setDividerLocation(split1.getHeight() / 2);
                split2.setDividerLocation(split2.getWidth() / 2);
            }
        });

        //add action listeners
        peptideInformationChb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createPeptideTable(iSelectedProtein);
                jtablePeptides.updateUI();
            }
        });
        showAllPeptidesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iSelectedProtein = null;
                proteinSequenceCoverageJEditorPane.setText("");
                sequenceCoverageJLabel.setText("");
                createPeptideTable(null);
                iDisplayedProteins.clear();
                iDisplayedProteinsOfInterest.clear();
                for (int i = 0; i < iProteins.size(); i++) {
                    iDisplayedProteins.add(iProteins.get(i));
                }
                filterDisplayedProteins();
                proteinList.updateUI();
                jtablePeptides.updateUI();
                selectedProteinList.updateUI();
            }
        }
        );

        jbuttonAlphabeticalSort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jbuttonAlphabeticalSort.getText().startsWith("A")) {
                    //Collections.sort(iDisplayedProteins, (new ProteinSorter()).compareProteinByAccession(true));
                    filterDisplayedProteins();
                    jbuttonAlphabeticalSort.setText("Z -> A");
                    proteinList.updateUI();
                    selectedProteinList.updateUI();
                } else {
                    //Collections.sort(iDisplayedProteins, (new ProteinSorter()).compareProteinByAccession(true));
                    filterDisplayedProteins();
                    jbuttonAlphabeticalSort.setText("A -> Z");
                    proteinList.updateUI();
                    selectedProteinList.updateUI();
                }
            }
        }
        );
        jbuttonNumberSort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jbuttonNumberSort.getText().startsWith("1")) {
                    //Collections.sort(iDisplayedProteins, new ProteinSorter());
                    filterDisplayedProteins();
                    jbuttonNumberSort.setText("20 -> 1");
                    proteinList.updateUI();
                    selectedProteinList.updateUI();
                } else {
                    //Collections.sort(iDisplayedProteins, new ProteinSorter());
                    filterDisplayedProteins();
                    jbuttonNumberSort.setText("1 -> 20");
                    proteinList.updateUI();
                    selectedProteinList.updateUI();
                }
            }
        }
        );
        ActionListener chbChangeActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createPeptideTable(iSelectedProtein);
                if (iSelectedProtein != null) {
                    formatProteinSequence(iSelectedProtein);
                }
                filterDisplayedProteins();
                proteinList.updateUI();
                selectedProteinList.updateUI();
                jtablePeptides.updateUI();


            }
        };
        ActionListener spectrumViewChange = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                peptidesTableMouseClicked(null);

            }
        };

        chbHighConfident.addActionListener(chbChangeActionListener);
        chbMediumConfident.addActionListener(chbChangeActionListener);
        chbLowConfidence.addActionListener(chbChangeActionListener);
        allRadioButton.addActionListener(chbChangeActionListener);
        onlyHighestScoringRadioButton.addActionListener(chbChangeActionListener);
        onlyLowestScoringRadioButton.addActionListener(chbChangeActionListener);
        chromatogramCheckBox.addActionListener(spectrumViewChange);
        msCheckBox.addActionListener(spectrumViewChange);
        msmsCheckBox.addActionListener(spectrumViewChange);
        quantCheckBox.addActionListener(spectrumViewChange);

        ChangeListener changeListener = new ChangeListener() {
            @Override
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
        }
        );


        //load data
        loadData(false);

        startRoverButton.addActionListener(new ActionListener() {


            @Override
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
        for (int i = 0; i < iParsedMsfs.size(); i++) {
            try {
                iParsedMsfs.get(i).getConnection().close();
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

            @Override
            public Boolean construct() {
                try {
                    setGuiElementsResponsive(false);

                    if (lReOpen) {

                        for (int i = 0; i < iParsedMsfs.size(); i++) {
                            try {
                                iParsedMsfs.get(i).getConnection().close();
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
                        iProteins.clear();
                        iParsedMsfs = new ArrayList<Parser>();
                        iDisplayedProteins.clear();
                        iDisplayedProteinsOfInterest.clear();
                        iMergedPeptidesScores = null;
                        iMergedCustomPeptideData = null;
                        iMergedCustomSpectrumData = null;
                        iMergedRatioTypes = null;
                        iMsfFileLocations.clear();

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
                        for (int i = 0; i < lFiles.length; i++) {
                            iMsfFileLocations.add(lFiles[i].getAbsolutePath());
                        }
                    } else {
                        JOptionPane.showMessageDialog(new JFrame(), "Open command cancelled by user.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        return true;
                    }
                    if (lFiles.length > 1) {
                        JOptionPane.showMessageDialog(getFrame(), "The workflow of the different msf files that are loaded must be the same.\nUnexpected crashes can occur if files with different workflows are loaded!", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }

                    progressBar.setVisible(true);
                    progressBar.setStringPainted(true);
                    progressBar.setMaximum(iMsfFileLocations.size() + 1);
                    //parse the msf files
                    for (int i = 0; i < iMsfFileLocations.size(); i++) {
                        try {
                            progressBar.setValue(i + 1);
                            progressBar.setString("Parsing: " + iMsfFileLocations.get(i));
                            //progressBar.updateUI();
                            iParsedMsfs.add(new Parser(iMsfFileLocations.get(i), true));
                        } catch (SQLException e) {
                            logger.info(e);
                            iParsedMsfs.add(null);
                        } catch (ClassNotFoundException e) {
                            logger.info(e);
                            iParsedMsfs.add(null);
                        }
                        System.gc();
                    }
                    
                    hasPhosphoRS = iParsedMsfs.get(0).hasPhosphoRS();
                    
                    //load processing nodes
                    processingNodeTabbedPane.removeAll();
                    for (int i = 0; i < iParsedMsfs.get(0).getProcessingNodes().size(); i++) {
                        if (iParsedMsfs.get(0).getQuantificationMethod() != null) {
                            iQuantitationFound = true;
                        }
                        
                        ProcessingNode lNode = iParsedMsfs.get(0).getProcessingNodes().get(i);
                        String lTitle = lNode.getProcessingNodeNumber() + " " + lNode.getFriendlyName();

                        //create holders for the different columns
                        List<String> lTableColumnsTitleList = new ArrayList<String>();
                        List<Boolean> lTableColumnsEditableList = new ArrayList<Boolean>();
                        List<Class> lTableColumnsClassList = new ArrayList<Class>();
                        List<Object[]> lElements = new ArrayList<Object[]>();
                        for (int j = 0; j < lNode.getProcessingNodeParameters().size(); j++) {
                            Object[] lInfo = new Object[2];
                            lInfo[0] = lNode.getProcessingNodeParameters().get(j).getFriendlyName();
                            lInfo[1] = lNode.getProcessingNodeParameters().get(j).getValueDisplayString();
                            lElements.add(lInfo);
                        }

                        //add different columns to the holders
                        lTableColumnsTitleList.add("Title");
                        lTableColumnsEditableList.add(false);
                        lTableColumnsClassList.add(String.class);
                        lTableColumnsTitleList.add("Value");
                        lTableColumnsEditableList.add(false);
                        lTableColumnsClassList.add(String.class);

                        //create the arrays for the table model
                        String[] lTableColumnsTitle = new String[lTableColumnsTitleList.size()];
                        lTableColumnsTitleList.toArray(lTableColumnsTitle);
                        final Boolean[] lTableColumnsEditable = new Boolean[lTableColumnsEditableList.size()];
                        lTableColumnsEditableList.toArray(lTableColumnsEditable);
                        final Class[] lTableColumnsClass = new Class[lTableColumnsClassList.size()];
                        lTableColumnsClassList.toArray(lTableColumnsClass);
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

                    progressBar.setIndeterminate(true);
                    progressBar.setString("Collecting all peptide and protein information");
                    progressBar.updateUI();
                    iSelectedProtein = null;
                    proteinSequenceCoverageJEditorPane.setText("");
                    sequenceCoverageJLabel.setText("");
                    createPeptideTable(null);
                    iDisplayedProteins.clear();
                    iDisplayedProteinsOfInterest.clear();
                    for (int i = 0; i < iProteins.size(); i++) {
                        iDisplayedProteins.add(iProteins.get(i));
                    }
                    filterDisplayedProteins();
                    proteinList.updateUI();
                    selectedProteinList.updateUI();
                    progressBar.setIndeterminate(false);
                    progressBar.setVisible(false);
                    progressBar.setString("");
                    progressBar.setStringPainted(false);
                    lLoaded = true;

                } catch (Exception e1) {
                    lLoaded = false;
                    logger.info(e1);
                    progressBar.setVisible(false);
                    JOptionPane.showMessageDialog(new JFrame(), "There was a problem loading your data!", "Problem loading", JOptionPane.ERROR_MESSAGE);
                }
                return true;
            }

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
        chbLowConfidence.setEnabled(lResponsive);
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
     * This method will filter the proteins in the protein list. It will
     * look if proteins still need to be displayed after that the peptide
     * confidence level is changed
     */
    public void filterDisplayedProteins() {
        List<Protein> lProteinsToRemove = new ArrayList<Protein>();
        for (Protein iDisplayedProtein : iDisplayedProteins) {
            Protein lProtein = iDisplayedProtein;
            boolean lDisplay = false;
            //check if there is one peptide that will be displayed
            for (int i = 0; i < lProtein.getPeptides().size(); i++) {
                Peptide lPeptide = lProtein.getPeptides().get(i);
                if (!lDisplay) {
                    int lConfidenceLevel = lPeptide.getConfidenceLevel();
                    if (chbHighConfident.isSelected() && lConfidenceLevel == 3) {
                        lDisplay = true;
                    }
                    if (chbMediumConfident.isSelected() && lConfidenceLevel == 2) {
                        lDisplay = true;
                    }
                    if (chbLowConfidence.isSelected() && lConfidenceLevel == 1) {
                        lDisplay = true;
                    }
                    if (onlyHighestScoringRadioButton.isSelected()) {
                        if (!lPeptide.getParentSpectrum().isHighestScoring(lPeptide, iMajorScoreTypes)) {
                            lDisplay = false;
                        }
                    }
                    if (onlyLowestScoringRadioButton.isSelected()) {
                        if (!lPeptide.getParentSpectrum().isLowestScoring(lPeptide, iMajorScoreTypes)) {
                            lDisplay = false;
                        }
                    }
                }

            }
            if (!lDisplay) {
                lProteinsToRemove.add(lProtein);
            }
        }
        for (int i = 0; i < lProteinsToRemove.size(); i++) {
            iDisplayedProteinsOfInterest.remove(lProteinsToRemove.get(i));
            iDisplayedProteins.remove(lProteinsToRemove.get(i));
        }
    }

    /**
     * This method will create the peptide table based on the given protein.
     * If no protein is given (null) all peptides found in the different msf
     * files will be displayed
     *
     * @param lProtein The protein to display the peptides off
     */
    private void createPeptideTable(Protein lProtein) {

        //some gui stuff
        if (chbHighConfident == null) {
            chbHighConfident = new JCheckBox("High");
            chbHighConfident.setSelected(true);
            chbMediumConfident = new JCheckBox("Medium");
            chbMediumConfident.setSelected(true);
            chbLowConfidence = new JCheckBox("Low");
        }

        //create holders for the different columns
        List<String> lPeptideTableColumnsTitleList = new ArrayList<String>();
        List<Boolean> lPeptideTableColumnsEditableList = new ArrayList<Boolean>();
        List<Class> lPeptideTableColumnsClassList = new ArrayList<Class>();
        List<Object[]> lPeptidesList = new ArrayList<Object[]>();

        //add different columns to the holders
        lPeptideTableColumnsTitleList.add(" ");
        lPeptideTableColumnsEditableList.add(false);
        lPeptideTableColumnsClassList.add(Integer.class);

        lPeptideTableColumnsTitleList.add("Spectrum Title");
        lPeptideTableColumnsEditableList.add(false);
        lPeptideTableColumnsClassList.add(Integer.class);

        lPeptideTableColumnsTitleList.add("Sequence");
        lPeptideTableColumnsEditableList.add(false);
        lPeptideTableColumnsClassList.add(Peptide.class);

        lPeptideTableColumnsTitleList.add("Modified Sequence");
        lPeptideTableColumnsEditableList.add(false);
        lPeptideTableColumnsClassList.add(String.class);
        
        if (hasPhosphoRS) {
            lPeptideTableColumnsTitleList.add("pRS Score");
            lPeptideTableColumnsEditableList.add(false);
            lPeptideTableColumnsClassList.add(Double.class);
            lPeptideTableColumnsTitleList.add("pRS Seq Probability");
            lPeptideTableColumnsEditableList.add(false);
            lPeptideTableColumnsClassList.add(Double.class);
        }
        
        //get the different score types and add it as columns
        if (iMergedPeptidesScores == null) {
            this.collectPeptideScoreTypes();
            //set the major score type
            for (int i = 0; i < iMergedPeptidesScores.size(); i++) {
                if (iMergedPeptidesScores.get(i).getIsMainScore() == 1) {
                    iMajorScoreTypes.add(iMergedPeptidesScores.get(i));
                }
            }
        }


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
                lPeptideTableColumnsTitleList.add(iMergedPeptidesScores.get(i).getFriendlyName());
                lPeptideTableColumnsEditableList.add(false);
                lPeptideTableColumnsClassList.add(Double.class);
            }
        }

        lPeptideTableColumnsTitleList.add("Matched Ions / Total Ions");
        lPeptideTableColumnsEditableList.add(false);
        lPeptideTableColumnsClassList.add(String.class);

        lPeptideTableColumnsTitleList.add("m/z [Da]");
        lPeptideTableColumnsEditableList.add(false);
        lPeptideTableColumnsClassList.add(Double.class);

        if (peptideInformationChb.isSelected()) {
            lPeptideTableColumnsTitleList.add("MH+ [Da]");
            lPeptideTableColumnsEditableList.add(false);
            lPeptideTableColumnsClassList.add(Double.class);
        }

        lPeptideTableColumnsTitleList.add("Charge");
        lPeptideTableColumnsEditableList.add(false);
        lPeptideTableColumnsClassList.add(Integer.class);

        lPeptideTableColumnsTitleList.add("Retention Time");
        lPeptideTableColumnsEditableList.add(false);
        lPeptideTableColumnsClassList.add(Double.class);

        lPeptideTableColumnsTitleList.add("First Scan");
        lPeptideTableColumnsEditableList.add(false);
        lPeptideTableColumnsClassList.add(Integer.class);


        if (peptideInformationChb.isSelected()) {
            lPeptideTableColumnsTitleList.add("Last Scan");
            lPeptideTableColumnsEditableList.add(false);
            lPeptideTableColumnsClassList.add(Integer.class);

            lPeptideTableColumnsTitleList.add("Annotation");
            lPeptideTableColumnsEditableList.add(false);
            lPeptideTableColumnsClassList.add(String.class);
        }


        //get the ratiotypes and add it as columns
        if (iMergedRatioTypes == null) {
            this.collectRatioTypes();
        }
        for (int i = 0; i < iMergedRatioTypes.size(); i++) {
            lPeptideTableColumnsTitleList.add(iMergedRatioTypes.get(i).getRatioType());
            lPeptideTableColumnsEditableList.add(false);
            lPeptideTableColumnsClassList.add(Double.class);
        }

        if (peptideInformationChb.isSelected()) {

            //get the custom peptide data and add it as columns
            if (iMergedCustomPeptideData == null) {
                this.collectCustomPeptideData();
            }
            for (int i = 0; i < iMergedCustomPeptideData.size(); i++) {
                lPeptideTableColumnsTitleList.add(iMergedCustomPeptideData.get(i).getName());
                lPeptideTableColumnsEditableList.add(false);
                lPeptideTableColumnsClassList.add(String.class);
            }

            //get the custom spectra data and add it as columns
            if (iMergedCustomSpectrumData == null) {
                this.collectCustomSpectrumData();
            }
            for (int i = 0; i < iMergedCustomSpectrumData.size(); i++) {
                lPeptideTableColumnsTitleList.add(iMergedCustomSpectrumData.get(i).getName());
                lPeptideTableColumnsEditableList.add(false);
                lPeptideTableColumnsClassList.add(String.class);
            }

            lPeptideTableColumnsTitleList.add("Processing Node");
            lPeptideTableColumnsEditableList.add(false);
            lPeptideTableColumnsClassList.add(ProcessingNode.class);
        }


        //find the peptides to display
        if (lProtein == null) {
            lPeptidesList = this.collectPeptides(lPeptidesList);
        } else {
            lPeptidesList = this.collectPeptidesFromProtein(lPeptidesList, lProtein);
        }

        //create the arrays for the table model
        String[] lPeptideTableColumnsTitle = new String[lPeptideTableColumnsTitleList.size()];
        lPeptideTableColumnsTitleList.toArray(lPeptideTableColumnsTitle);
        final Boolean[] lPeptideTableColumnsEditable = new Boolean[lPeptideTableColumnsEditableList.size()];
        lPeptideTableColumnsEditableList.toArray(lPeptideTableColumnsEditable);
        final Class[] lPeptideTableColumnsClass = new Class[lPeptideTableColumnsClassList.size()];
        lPeptideTableColumnsClassList.toArray(lPeptideTableColumnsClass);
        final Object[][] lPeptides = new Object[lPeptidesList.size()][];
        lPeptidesList.toArray(lPeptides);

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

        for (int p = 0; p < lPeptides.length; p++) {
            Peptide lPeptide = (Peptide) lPeptides[p][2];
            if (lLowRT > lPeptide.getParentSpectrum().getRetentionTime()) {
                lLowRT = lPeptide.getParentSpectrum().getRetentionTime();
            }
            if (lHighRT < lPeptide.getParentSpectrum().getRetentionTime()) {
                lHighRT = lPeptide.getParentSpectrum().getRetentionTime();
            }
        }
        lLowRT = lLowRT - 1.0;
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
                for (int p = 0; p < lPeptides.length; p++) {
                    if (lPeptides[p][4 + i] != null) {
                        if (lPeptides[p][4 + i].toString().indexOf("/") > 0) {
                            System.out.println(lPeptides[p][4 + i]);
                        }
                        double lScore = (Double) lPeptides[p][4 + i];
                        if (lLowScore > lScore) {
                            lLowScore = lScore;
                        }
                        if (lHighScore < lScore) {
                            lHighScore = lScore;
                        }
                    }
                }
                lLowScore = lLowScore - 1.0;
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
                peptidesTableMouseClicked(evt);
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
            setSpectrumMSMSAnnotations(iSelectedPeptide);
        }
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
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
        chbLowConfidence.setMaximumSize(new Dimension(130, 22));
        chbLowConfidence.setMinimumSize(new Dimension(130, 22));
        chbLowConfidence.setPreferredSize(new Dimension(130, 22));
        chbLowConfidence.setText("Low");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel7.add(chbLowConfidence, gbc);
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

    public class ProcessingNodeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object lProcessingNodeObject, boolean isSelected, boolean hasFocus, int row, int column) {
            ProcessingNode lProcessingNode = (ProcessingNode) lProcessingNodeObject;
            Component cell = super.getTableCellRendererComponent(table, lProcessingNode.getProcessingNodeNumber() + ": " + lProcessingNode.getFriendlyName(), isSelected, hasFocus, row, column);
            /*String lToolTipHtml = "<html><b>" + lProcessingNode.getFriendlyName() + "</b>";
            for (int i = 0; i < lProcessingNode.getProcessingNodeParameters().size(); i++) {
                lToolTipHtml = lToolTipHtml + "<br>" + lProcessingNode.getProcessingNodeParameters().get(i).getFriendlyName() + ": " + lProcessingNode.getProcessingNodeParameters().get(i).getValueDisplayString();
            }
            lToolTipHtml = lToolTipHtml + "</html>";
            setToolTipText(lToolTipHtml);           */
            return cell;
        }
    }

    /**
     * This method will initiate a spectrum annotation if a peptide is selected
     */
    private void ionsJCheckBoxActionPerformed() {
        if (iSelectedPeptide != null) {
            setSpectrumMSMSAnnotations(iSelectedPeptide);
        }
    }

    /**
     * This method will generete the object for the peptide table, based on a given protein
     *
     * @param lPeptides List to add the peptide line objects to it
     * @param lProtein  The selected protein
     * @return List with the peptide line objects
     */
    private List<Object[]> collectPeptidesFromProtein(List<Object[]> lPeptides, Protein lProtein) {

        for (int p = 0; p < lProtein.getPeptides().size(); p++) {
            Peptide lPeptide = lProtein.getPeptides().get(p);
            int lConfidenceLevel = lPeptide.getConfidenceLevel();
            boolean lUse = false;
            if (chbHighConfident.isSelected() && lConfidenceLevel == 3) {
                lUse = true;
            }
            if (chbMediumConfident.isSelected() && lConfidenceLevel == 2) {
                lUse = true;
            }
            if (chbLowConfidence.isSelected() && lConfidenceLevel == 1) {
                lUse = true;
            }
            if (onlyHighestScoringRadioButton.isSelected()) {
                if (!lPeptide.getParentSpectrum().isHighestScoring(lPeptide, iMajorScoreTypes)) {
                    lUse = false;
                }
            }
            if (onlyLowestScoringRadioButton.isSelected()) {
                if (!lPeptide.getParentSpectrum().isLowestScoring(lPeptide, iMajorScoreTypes)) {
                    lUse = false;
                }
            }

            if (lUse) {
                //only add the peptide line if we need to use it
                lPeptides.add(createPeptideLine(lPeptide).toArray());
            }

        }

        return lPeptides;
    }

    /**
     * This method will generete the object for the peptide table for all the peptides
     *
     * @param lPeptides List to add the peptide line objects to it
     * @return List with the peptide line objects
     */
    private List<Object[]> collectPeptides(List<Object[]> lPeptides) {
        boolean lCreateProteins = false;
        if (iProteins.isEmpty()) {
            //The proteins are not created yet, so we need to create them
            lCreateProteins = true;
            iProteins = new ArrayList<Protein>();
        }

        for (int i = 0; i < iParsedMsfs.size(); i++) {
            for (int p = 0; p < iParsedMsfs.get(i).getPeptides().size(); p++) {
                Peptide lPeptide = iParsedMsfs.get(i).getPeptides().get(p);
                int lConfidenceLevel = lPeptide.getConfidenceLevel();
                boolean lUse = false;
                if (chbHighConfident.isSelected() && lConfidenceLevel == 3) {
                    lUse = true;
                }
                if (chbMediumConfident.isSelected() && lConfidenceLevel == 2) {
                    lUse = true;
                }
                if (chbLowConfidence.isSelected() && lConfidenceLevel == 1) {
                    lUse = true;
                }
                if (onlyHighestScoringRadioButton.isSelected()) {
                    if (!lPeptide.getParentSpectrum().isHighestScoring(lPeptide, iMajorScoreTypes)) {
                        lUse = false;
                    }
                }
                if (onlyLowestScoringRadioButton.isSelected()) {
                    if (!lPeptide.getParentSpectrum().isLowestScoring(lPeptide, iMajorScoreTypes)) {
                        lUse = false;
                    }
                }

                if (lUse) {
                    //only add the peptide line if we need to use it
                    lPeptides.add(createPeptideLine(lPeptide).toArray());
                }

                if (lCreateProteins) {
                    //check and possibly create the protein
                    List<Protein> lCoupledProteins = lPeptide.getProteins();
                    for (int r = 0; r < lCoupledProteins.size(); r++) {
                        Protein lCoupledProtein = lCoupledProteins.get(r);
                        if (iProteinsMap.get(lCoupledProtein.getDescription()) == null) {
                            Protein lNewProtein = new Protein(lCoupledProtein.getProteinId(), lCoupledProtein.getParser());
                            lNewProtein.setDescription(lCoupledProtein.getDescription());
                            iProteinsMap.put(lNewProtein.getDescription(), lNewProtein);
                            iProteins.add(lNewProtein);
                            //iDisplayedProteins.add(lNewProtein);
                            lNewProtein.addPeptide(lPeptide);
                        } else {
                            iProteinsMap.get(lCoupledProtein.getDescription()).addPeptide(lPeptide);
                        }
                    }
                }
            }
            if (iProteins.size() % 100 == 0) {
                System.gc();
            }
        }
        if (lCreateProteins) {
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
            this.filterDisplayedProteins();
            proteinList.updateUI();
            selectedProteinList.updateUI();
        }
        return lPeptides;
    }

    /**
     * This method will create a peptide line vector for the peptide table
     *
     * @param lPeptide The peptide to create the peptide line for
     * @return List with the objects of the peptide line
     */
    public List<Object> createPeptideLine(Peptide lPeptide) {
        List<Object> lPeptideObject = new ArrayList<Object>();
        lPeptideObject.add(lPeptide.getConfidenceLevel());
        lPeptideObject.add(lPeptide.getParentSpectrum().getSpectrumTitle());
        lPeptideObject.add(lPeptide);
        lPeptideObject.add(lPeptide.getModifiedPeptide());
        
        if (hasPhosphoRS) {
            Float pRSScore = lPeptide.getPhosphoRSScore();
            if (pRSScore != null) {
                lPeptideObject.add(pRSScore.doubleValue());
            } else {
                lPeptideObject.add(null);
            }
            Float pRSProbability = lPeptide.getPhoshpoRSSequenceProbability();
            if (pRSProbability != null) {
                lPeptideObject.add(pRSProbability.doubleValue());
            } else {
                lPeptideObject.add(null);
            }
        }
        
        for (int j = 0; j < iMergedPeptidesScores.size(); j++) {
            if (!peptideInformationChb.isSelected()) {
                if (iMergedPeptidesScores.get(j).getIsMainScore() == 1) {
                    Double lScore = lPeptide.getScoreByScoreType(iMergedPeptidesScores.get(j));
                    lPeptideObject.add(lScore);
                }
            } else {
                Double lScore = lPeptide.getScoreByScoreType(iMergedPeptidesScores.get(j));
                lPeptideObject.add(lScore);
            }
        }

        lPeptideObject.add(lPeptide.getMatchedIonsCount() + "/" + lPeptide.getTotalIonsCount());
        lPeptideObject.add(lPeptide.getParentSpectrum().getMz());
        if (peptideInformationChb.isSelected()) {
            lPeptideObject.add(lPeptide.getParentSpectrum().getSinglyChargedMass());
        }
        lPeptideObject.add(lPeptide.getParentSpectrum().getCharge());
        lPeptideObject.add(lPeptide.getParentSpectrum().getRetentionTime());
        lPeptideObject.add(lPeptide.getParentSpectrum().getFirstScan());
        if (peptideInformationChb.isSelected()) {
            lPeptideObject.add(lPeptide.getParentSpectrum().getLastScan());
            if (lPeptide.getAnnotation() != null) {
                lPeptideObject.add(lPeptide.getAnnotation());
            } else {
                lPeptideObject.add("");
            }
        }

        for (int i = 0; i < iMergedRatioTypes.size(); i++) {
            //lPeptideObject.add(0.0);
            if (lPeptide.getParentSpectrum().getQuanResult() != null && lPeptide.getParentSpectrum().getQuanResult().getRatioByRatioType(iMergedRatioTypes.get(i)) != null) {
                lPeptideObject.add(lPeptide.getParentSpectrum().getQuanResult().getRatioByRatioType(iMergedRatioTypes.get(i)));
            } else {
                lPeptideObject.add(null);
            }
        }
        if (peptideInformationChb.isSelected()) {
            for (int j = 0; j < iMergedCustomPeptideData.size(); j++) {
                if (lPeptide.getCustomDataFieldValues().get(iMergedCustomPeptideData.get(j).getFieldId()) != null) {
                    lPeptideObject.add(lPeptide.getCustomDataFieldValues().get(iMergedCustomPeptideData.get(j).getFieldId()));
                } else {
                    lPeptideObject.add("");
                }
            }

            for (int j = 0; j < iMergedCustomSpectrumData.size(); j++) {
                if (lPeptide.getParentSpectrum().getCustomDataFieldValues().get(iMergedCustomSpectrumData.get(j).getFieldId()) != null) {
                    lPeptideObject.add(lPeptide.getParentSpectrum().getCustomDataFieldValues().get(iMergedCustomSpectrumData.get(j).getFieldId()));
                } else {
                    lPeptideObject.add("");
                }
            }
            lPeptideObject.add(lPeptide.getParentSpectrum().getParser().getProcessingNodeByNumber(lPeptide.getProcessingNodeNumber()));
        }

        return lPeptideObject;

    }

    /**
     * This method will collect the score types used in the different msf files
     */
    private void collectPeptideScoreTypes() {
        iMergedPeptidesScores = new ArrayList<ScoreType>();
        for (int i = 0; i < iParsedMsfs.size(); i++) {
            for (int j = 0; j < iParsedMsfs.get(i).getScoreTypes().size(); j++) {
                boolean lFound = false;
                for (int k = 0; k < iMergedPeptidesScores.size(); k++) {
                    if (iParsedMsfs.get(i).getScoreTypes().get(j).getDescription().equalsIgnoreCase(iMergedPeptidesScores.get(k).getDescription())) {
                        lFound = true;
                    }
                }
                if (!lFound) {
                    iMergedPeptidesScores.add(iParsedMsfs.get(i).getScoreTypes().get(j));
                }
            }
        }
    }

    /**
     * This method will collect the ratiotypes used in the different msf files
     */
    private void collectRatioTypes() {
        iMergedRatioTypes = new ArrayList<RatioType>();
        for (int i = 0; i < iParsedMsfs.size(); i++) {
            for (int j = 0; j < iParsedMsfs.get(i).getRatioTypes().size(); j++) {
                boolean lFound = false;
                for (int k = 0; k < iMergedRatioTypes.size(); k++) {
                    if (iParsedMsfs.get(i).getRatioTypes().get(j).getRatioType().equalsIgnoreCase(iMergedRatioTypes.get(k).getRatioType())) {
                        lFound = true;
                    }
                }
                if (!lFound) {
                    iMergedRatioTypes.add(iParsedMsfs.get(i).getRatioTypes().get(j));
                }
            }
        }

    }

    /**
     * This method will collect the custom peptide data used in the different msf files
     */
    private void collectCustomPeptideData() {
        iMergedCustomPeptideData = new ArrayList<CustomDataField>();
        for (Parser iParsedMsf : iParsedMsfs) {
            for (int j = 0; j < iParsedMsf.getPeptideUsedCustomDataFields().size(); j++) {
                boolean lFound = false;
                for (CustomDataField anIMergedCustomPeptideData : iMergedCustomPeptideData) {
                    if (iParsedMsf.getPeptideUsedCustomDataFields().get(j).getName().equalsIgnoreCase(anIMergedCustomPeptideData.getName())) {
                        lFound = true;
                    }
                }
                if (!lFound) {
                    iMergedCustomPeptideData.add(iParsedMsf.getPeptideUsedCustomDataFields().get(j));
                }
            }
        }

    }


    /**
     * This method will collect the custom spectrum data used in the different msf files
     */
    private void collectCustomSpectrumData() {
        iMergedCustomSpectrumData = new ArrayList<CustomDataField>();
        for (int i = 0; i < iParsedMsfs.size(); i++) {
            for (int j = 0; j < iParsedMsfs.get(i).getSpectrumUsedCustomDataFields().size(); j++) {
                boolean lFound = false;
                for (int k = 0; k < iMergedCustomSpectrumData.size(); k++) {
                    if (iParsedMsfs.get(i).getSpectrumUsedCustomDataFields().get(j).getName().equalsIgnoreCase(iMergedCustomSpectrumData.get(k).getName())) {
                        lFound = true;
                    }
                }
                if (!lFound) {
                    iMergedCustomSpectrumData.add(iParsedMsfs.get(i).getSpectrumUsedCustomDataFields().get(j));
                }
            }
        }

    }

    /**
     * This method will set the msms spectrum annotation for a peptide
     *
     * @param lPeptide The peptide to set the annotation for
     */
    public void setSpectrumMSMSAnnotations(Peptide lPeptide) {
        List<DefaultSpectrumAnnotation> lAnnotations = new ArrayList<DefaultSpectrumAnnotation>();
        if (iMSMSspectrumPanel != null && lPeptide != null) {
            int lMaximumCharge = iSelectedPeptide.getParentSpectrum().getCharge();
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
     * This method will generate the spectrum annotations based on the charge and the ion checkboxes
     *
     * @param lAnnotations List to add the annotations to
     * @param lCharge      The charge
     * @param lPeptide     The peptide
     * @return List with the annotations
     */
    public List<DefaultSpectrumAnnotation> addIonAnnotationByCharge(List<DefaultSpectrumAnnotation> lAnnotations, int lCharge, Peptide lPeptide) {
        List<PeptideFragmentIon.PeptideFragmentIonType> lIonTypes = new ArrayList<PeptideFragmentIon.PeptideFragmentIonType>();
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

        List<PeptideFragmentIon> lFragmentIons = lPeptide.getFragmentIonsByTypeAndCharge(lCharge, lIonTypes);

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

        }
        return lAnnotations;
    }

    /**
     * Retrieves the version number set in the properties file
     *
     * @return the version number of the thermo-msf parser
     */
    private String getVersion() {

        Properties p = new Properties();

        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("thermo.msf.parser.properties");
            p.load(is);
        } catch (IOException e) {
            logger.info(e);
        }

        return p.getProperty("version");
    }

    /**
     * @see #peptidesTableMouseClicked(java.awt.event.MouseEvent)
     */
    private void peptidesTableKeyReleased(KeyEvent evt) {
        peptidesTableMouseClicked(null);
    }


    /**
     * Formats the protein sequence such that both the covered parts of the sequence
     * and the peptide selected in the peptide table is highlighted.
     * This code is based on the compomics utilities sample code
     *
     * @param lProtein
     */
    public void formatProteinSequence(Protein lProtein) {


        // and clear the peptide sequence coverage details
        proteinSequenceCoverageJEditorPane.setText("");

        String lCleanProteinSequence;
        try {
            lCleanProteinSequence = lProtein.getSequence();
        } catch (SQLException e) {
            logger.info(e);
            return;
            //e.printStackTrace();
        }

        int selectedPeptideStart = -1;
        int selectedPeptideEnd = -1;

        // find the start end end indices for the currently selected peptide, if any
        if (iSelectedPeptide != null) {
            int lConfidenceLevel = iSelectedPeptide.getConfidenceLevel();
            boolean lUse = false;
            if (chbHighConfident.isSelected() && lConfidenceLevel == 3) {
                lUse = true;
            }
            if (chbMediumConfident.isSelected() && lConfidenceLevel == 2) {
                lUse = true;
            }
            if (chbLowConfidence.isSelected() && lConfidenceLevel == 1) {
                lUse = true;
            }
            if (onlyHighestScoringRadioButton.isSelected()) {
                if (!iSelectedPeptide.getParentSpectrum().isHighestScoring(iSelectedPeptide, iMajorScoreTypes)) {
                    lUse = false;
                }
            }
            if (onlyLowestScoringRadioButton.isSelected()) {
                if (!iSelectedPeptide.getParentSpectrum().isLowestScoring(iSelectedPeptide, iMajorScoreTypes)) {
                    lUse = false;
                }
            }

            if (lUse) {
                selectedPeptideStart = lCleanProteinSequence.indexOf(iSelectedPeptide.getSequence());
                selectedPeptideEnd = selectedPeptideStart + iSelectedPeptide.getSequence().length() + 1;
                selectedPeptideStart = selectedPeptideStart + 1;
            }
        }

        // an array containing the coverage index for each residue
        int[] coverage = new int[lCleanProteinSequence.length() + 1];

        List<Peptide> lPeptides = lProtein.getPeptides();

        // iterate the peptide table and store the coverage for each peptide
        for (int i = 0; i < lPeptides.size(); i++) {
            int lConfidenceLevel = lPeptides.get(i).getConfidenceLevel();
            boolean lUse = false;
            if (chbHighConfident.isSelected() && lConfidenceLevel == 3) {
                lUse = true;
            }
            if (chbMediumConfident.isSelected() && lConfidenceLevel == 2) {
                lUse = true;
            }
            if (chbLowConfidence.isSelected() && lConfidenceLevel == 1) {
                lUse = true;
            }
            if (onlyHighestScoringRadioButton.isSelected()) {
                if (!lPeptides.get(i).getParentSpectrum().isHighestScoring(lPeptides.get(i), iMajorScoreTypes)) {
                    lUse = false;
                }
            }
            if (onlyLowestScoringRadioButton.isSelected()) {
                if (!lPeptides.get(i).getParentSpectrum().isLowestScoring(lPeptides.get(i), iMajorScoreTypes)) {
                    lUse = false;
                }
            }
            if (lUse) {
                int tempPeptideStart = lCleanProteinSequence.indexOf(lPeptides.get(i).getSequence());
                int tempPeptideEnd = tempPeptideStart + lPeptides.get(i).getSequence().length();
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
     * @param evt
     */
    private void peptidesTableMouseClicked(MouseEvent evt) {

        // Set the cursor into the wait status.
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));


        int row = jtablePeptides.getSelectedRow();

        // Condition if one row is selected.
        if (row != -1) {
            iSelectedPeptide = (Peptide) jtablePeptides.getValueAt(row, 2);

            //update the protein list
            iDisplayedProteinsOfInterest.clear();
            for (int i = 0; i < iSelectedPeptide.getProteins().size(); i++) {
                for (int j = 0; j < iProteins.size(); j++) {
                    if (iProteins.get(j).getDescription().equalsIgnoreCase(iSelectedPeptide.getProteins().get(i).getDescription())) {
                        iDisplayedProteinsOfInterest.add(iProteins.get(j));
                    }
                }
            }
            this.filterDisplayedProteins();
            selectedProteinList.updateUI();


            try {
                if (msmsCheckBox.isSelected()) {

                    //do the MSMS
                    if (jtabpanLower.indexOfTab("MS/MS Spectrum") == -1) {
                        jtabpanLower.add("MS/MS Spectrum", jpanMSMS);
                    }
                    List<Peak> lPeaks = iSelectedPeptide.getParentSpectrum().getMSMSPeaks();
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
                            iSelectedPeptide.getParentSpectrum().getMz(),
                            iSelectedPeptide.getParentSpectrum().getSpectrumTitle(),
                            String.valueOf(iSelectedPeptide.getParentSpectrum().getCharge()),
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
                    List<Peak> lMSPeaks = iSelectedPeptide.getParentSpectrum().getMSPeaks();
                    double[] lMSMzValues = new double[lMSPeaks.size()];
                    double[] lMSIntensityValues = new double[lMSPeaks.size()];
                    List<DefaultSpectrumAnnotation> lMSAnnotations = new ArrayList<DefaultSpectrumAnnotation>();
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
                            iSelectedPeptide.getParentSpectrum().getMz(),
                            "",
                            String.valueOf(iSelectedPeptide.getParentSpectrum().getCharge()),
                            50, false, false, false);
                    iMSspectrumPanel.rescale(lMinMZvalue, lMaxMZvalue);
                    iMSspectrumPanel.setProfileMode(false);
                    iMSspectrumPanel.setAnnotations(lMSAnnotations);
                    iMSspectrumPanel.setXAxisStartAtZero(false);
                    Peak lFragmentedPeak = iSelectedPeptide.getParentSpectrum().getFragmentedMsPeak();
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

                        jtabChromatogram.removeAll();
                        if (jtabpanLower.indexOfTab("Chromatogram") == -1) {
                            jtabpanLower.add("Chromatogram", jtabChromatogram);
                        }
                        List<Chromatogram> lChros = iSelectedPeptide.getParentSpectrum().getParser().getChromatograms();
                        for (int c = 0; c < lChros.size(); c++) {
                            Chromatogram lChro = lChros.get(c);
                            if (lChro.getFileId() == iSelectedPeptide.getParentSpectrum().getFileId()) {
                                List<Chromatogram.Point> lPoints = lChro.getPoints();

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
                                    for (int p = 0; p < iSelectedProtein.getPeptides().size(); p++) {
                                        Peptide lPeptide = iSelectedProtein.getPeptides().get(p);
                                        int lConfidenceLevel = lPeptide.getConfidenceLevel();
                                        boolean lUse = false;
                                        if (chbHighConfident.isSelected() && lConfidenceLevel == 3) {
                                            lUse = true;
                                        }
                                        if (chbMediumConfident.isSelected() && lConfidenceLevel == 2) {
                                            lUse = true;
                                        }
                                        if (chbLowConfidence.isSelected() && lConfidenceLevel == 1) {
                                            lUse = true;
                                        }
                                        if (onlyHighestScoringRadioButton.isSelected()) {
                                            if (!lPeptide.getParentSpectrum().isHighestScoring(lPeptide, iMajorScoreTypes)) {
                                                lUse = false;
                                            }
                                        }
                                        if (onlyLowestScoringRadioButton.isSelected()) {
                                            if (!lPeptide.getParentSpectrum().isLowestScoring(lPeptide, iMajorScoreTypes)) {
                                                lUse = false;
                                            }
                                        }

                                        if (lUse) {
                                            if (lPeptide.getParentSpectrum().getParser().getFileName().equalsIgnoreCase(iSelectedPeptide.getParentSpectrum().getParser().getFileName()) && lPeptide.getParentSpectrum().getFileId() == iSelectedPeptide.getParentSpectrum().getFileId()) {
                                                chromatogramPanel.addReferenceAreaXAxis(new ReferenceArea(String.valueOf(iSelectedProtein.getPeptides().get(p).getParentSpectrum().getFirstScan()), iSelectedProtein.getPeptides().get(p).getParentSpectrum().getRetentionTime() - lAreaDistance, iSelectedProtein.getPeptides().get(p).getParentSpectrum().getRetentionTime() + lAreaDistance, Color.blue, 0.1f, false, false));
                                            } else {
                                                chromatogramPanel.addReferenceAreaXAxis(new ReferenceArea(String.valueOf(iSelectedProtein.getPeptides().get(p).getParentSpectrum().getFirstScan()), iSelectedProtein.getPeptides().get(p).getParentSpectrum().getRetentionTime() - lAreaDistance, iSelectedProtein.getPeptides().get(p).getParentSpectrum().getRetentionTime() + lAreaDistance, Color.green, 0.1f, false, false));
                                            }
                                        }

                                    }
                                }
                                chromatogramPanel.addReferenceAreaXAxis(new ReferenceArea(String.valueOf(iSelectedPeptide.getParentSpectrum().getFirstScan()), iSelectedPeptide.getParentSpectrum().getRetentionTime() - lAreaDistance, iSelectedPeptide.getParentSpectrum().getRetentionTime() + lAreaDistance, Color.red, 0.8f, true, false));


                                //chromatogramPanel.setMiniature(true);

                                String lTitle = iSelectedPeptide.getParentSpectrum().getParser().getRawfileNameByFileId(lChro.getFileId());
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
                } else {
                    jtabpanLower.remove(jtabChromatogram);
                    jtabChromatogram.removeAll();
                }

                if (quantCheckBox.isSelected()) {

                    try {
                        if (iSelectedPeptide.getParentSpectrum().getQuanResult() != null) {

                            jtabpanLower.add("Quantification Spectrum", jpanQuantificationSpectrumHolder);

                            QuanResult lQuan = iSelectedPeptide.getParentSpectrum().getQuanResult();
                            //get the quan events
                            List<Event> lQuanEvents = new ArrayList<Event>();
                            List<Integer> lQuanEventsIds = new ArrayList<Integer>();
                            List<List<Event>> lQuanEventsByPattern = new ArrayList<List<Event>>();
                            for (int i = 0; i < lQuan.getIsotopePatterns().size(); i++) {
                                List<Event> lIsotopePatternEvents = lQuan.getIsotopePatterns().get(i).getEventsWithQuanResult(iSelectedPeptide.getParentSpectrum().getConnection());
                                lQuanEventsByPattern.add(lIsotopePatternEvents);
                                for (int j = 0; j < lIsotopePatternEvents.size(); j++) {
                                    lQuanEvents.add(lIsotopePatternEvents.get(j));
                                    lQuanEventsIds.add(lIsotopePatternEvents.get(j).getEventId());
                                }
                            }

                            //get the quan events
                            List<List<Event>> lQuanEventsByPatternWithoutQuanChannel = new ArrayList<List<Event>>();
                            for (int i = 0; i < lQuan.getIsotopePatterns().size(); i++) {
                                List<Event> lIsotopePatternEvents = lQuan.getIsotopePatterns().get(i).getEventsWithoutQuanResult(iSelectedPeptide.getParentSpectrum().getConnection());
                                lQuanEventsByPatternWithoutQuanChannel.add(lIsotopePatternEvents);
                                for (int j = 0; j < lIsotopePatternEvents.size(); j++) {
                                    lQuanEvents.add(lIsotopePatternEvents.get(j));
                                    lQuanEventsIds.add(lIsotopePatternEvents.get(j).getEventId());
                                }
                            }

                            //get the min and max retention and mass
                            double lMinMass = Double.MAX_VALUE;
                            double lMinRT = Double.MAX_VALUE;
                            double lMaxMass = Double.MIN_VALUE;
                            double lMaxRT = Double.MIN_VALUE;

                            for (int i = 0; i < lQuanEvents.size(); i++) {
                                if (lMinMass > lQuanEvents.get(i).getMass()) {
                                    lMinMass = lQuanEvents.get(i).getMass();
                                }
                                if (lMaxMass < lQuanEvents.get(i).getMass()) {
                                    lMaxMass = lQuanEvents.get(i).getMass();
                                }
                                if (lMinRT > lQuanEvents.get(i).getRetentionTime()) {
                                    lMinRT = lQuanEvents.get(i).getRetentionTime();
                                }
                                if (lMaxRT < lQuanEvents.get(i).getRetentionTime()) {
                                    lMaxRT = lQuanEvents.get(i).getRetentionTime();
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

                            List<Event> lBackgroundEvents = Event.getEventByRetentionTimeLimitMassLimitAndFileIdExcludingIds(lMinRT, lMaxRT, lMinMass, lMaxMass, lQuanEventsIds, iSelectedPeptide.getParentSpectrum().getFileId(), iSelectedPeptide.getParentSpectrum().getConnection());


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
                                    iSelectedPeptide.getParentSpectrum().getMz() / (double) iSelectedPeptide.getParentSpectrum().getCharge(),
                                    "RT: " + lMinRT + " - " + lMaxRT,
                                    String.valueOf(iSelectedPeptide.getParentSpectrum().getCharge()),
                                    50, true, true, false);
                            iQuantificationSpectrumPanel.rescale(lMinMass, lMaxMass);
                            iQuantificationSpectrumPanel.setProfileMode(false);
                            iQuantificationSpectrumPanel.setXAxisStartAtZero(false);
                            List<DefaultSpectrumAnnotation> lQuanAnnotations = new ArrayList<DefaultSpectrumAnnotation>();
                            for (int i = 0; i < lQuan.getIsotopePatterns().size(); i++) {
                                double[] lQuanPatternMzValues = new double[lQuanEventsByPattern.get(i).size()];
                                double[] lQuanPatternIntensityValues = new double[lQuanEventsByPattern.get(i).size()];
                                for (int j = 0; j < lQuanEventsByPattern.get(i).size(); j++) {
                                    lQuanPatternMzValues[j] = lQuanEventsByPattern.get(i).get(j).getMass();
                                    lQuanPatternIntensityValues[j] = lQuanEventsByPattern.get(i).get(j).getIntensity();
                                    for (int k = 0; k < lQuan.getIsotopePatterns().get(i).getEventAnnotations().size(); k++) {
                                        if (lQuanEventsByPattern.get(i).get(j).getEventId() == lQuan.getIsotopePatterns().get(i).getEventAnnotations().get(k).getEventId()) {
                                            if (lQuan.getIsotopePatterns().get(i).getEventAnnotations().get(k).getQuanChannelId() != -1) {
                                                lQuanAnnotations.add(new DefaultSpectrumAnnotation(lQuanEventsByPattern.get(i).get(j).getMass(), 0.000000000000000000000001, Color.BLACK, "" + iSelectedPeptide.getParentSpectrum().getParser().getQuanChannelNameById(lQuan.getIsotopePatterns().get(i).getEventAnnotations().get(k).getQuanChannelId())));
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
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Thermo_msf_parserGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Thermo_msf_parserGUI(true);
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
     * Makes sure that the sequence coverage area is rescaled to fit the new size
     * of the frame.
     *
     * @param evt
     */
    private void proteinSequenceCoverageJEditorPaneResized(ComponentEvent evt) {
        if (iSelectedProtein != null) {
            formatProteinSequence(iSelectedProtein);
        }
    }


    /**
     * This method will load a protein. It will create the peptide table and format the protein sequence
     *
     * @param aFromInterestedList A boolean that indicates if the selected protein should come from the proteinList (FALSE)
     *                            or from the selectedProteinList (=TRUE)
     */
    public void loadProtein(boolean aFromInterestedList) {
        if (aFromInterestedList) {
            iSelectedProtein = (Protein) selectedProteinList.getSelectedValue();
        } else {
            iSelectedProtein = (Protein) proteinList.getSelectedValue();
        }
        /*if (iRover != null) {
            for (int i = 0; i < iRover.getProteinList().getModel().getSize(); i++) {
                QuantitativeProtein lProtein = (QuantitativeProtein) iRover.getProteinList().getModel().getElementAt(i);
                if (lProtein.getAccession().equalsIgnoreCase(iSelectedProtein.getUtilAccession())) {
                    iRover.getProteinList().setSelectedValue(lProtein, true);
                    iRover.loadProtein(true);
                }
            }
        }*/
        createPeptideTable(iSelectedProtein);
        formatProteinSequence(iSelectedProtein);
        jtablePeptides.updateUI();

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
