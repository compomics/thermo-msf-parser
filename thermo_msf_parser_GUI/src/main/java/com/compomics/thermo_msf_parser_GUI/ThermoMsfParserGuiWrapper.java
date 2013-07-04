package com.compomics.thermo_msf_parser_GUI;

import com.compomics.software.CompomicsWrapper;
import com.compomics.thermo_msf_parser_API.util.MsfFileFilter;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import org.apache.log4j.Logger;

/**
 * A wrapper class used to start the jar file with parameters. The parameters
 * are read from the JavaOptions file.
 *
 * @author Harald Barsnes
 */
public class ThermoMsfParserGuiWrapper extends CompomicsWrapper {

    // Class specific log4j logger for ParserStarter instances.
    private static Logger logger = Logger.getLogger(ThermoMsfParserGuiWrapper.class);


    /**
     * Starts the launcher by calling the launch method. Use this as the main
     * class in the jar file.
     *
     * @param args the arguments to pass to the tool
     */
    public ThermoMsfParserGuiWrapper(String[] args) {
        try {
            File jarFile = new File(ThermoMsfParserGuiWrapper.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            // get the splash 

            String mainClass = "com.compomics.thermo_msf_parser_GUI.Thermo_msf_parserGUI";

            StringBuilder fileLocations = new StringBuilder();

            //open file chooser
            JFileChooser fc = new JFileChooser();
            fc.setMultiSelectionEnabled(true);
            //create the file filter to choose
            javax.swing.filechooser.FileFilter lFilter = new MsfFileFilter();
            fc.setFileFilter(lFilter);
            int returnVal = fc.showOpenDialog(null);
            File[] lFiles;
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                lFiles = fc.getSelectedFiles();
                for (int i = 0; i < lFiles.length; i++) {
                    fileLocations.append(",").append(lFiles[i].getAbsolutePath());
                    //arbitrary cutoff at 500 mb
                    if (lFiles[i].length() > 524288000) {
                        mainClass = "com.compomics.thermo_msf_parser_GUI.Thermo_msf_parserGUILowMem";
                    }
                }
            }
            fileLocations.delete(0, 1);

            String[] argsAddedTo = Arrays.copyOf(args, args.length+1);
            System.out.println(argsAddedTo.length);
            argsAddedTo[argsAddedTo.length -1] = fileLocations.toString();
            
            launchTool("Thermo MSF Parser", jarFile, null, mainClass, argsAddedTo);
        } catch (URISyntaxException ex) {
            java.util.logging.Logger.getLogger(ThermoMsfParserGuiWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Starts the launcher by calling the launch method. Use this as the main
     * class in the jar file.
     *
     * @param args
     */
    public static void main(String[] args) {
        new ThermoMsfParserGuiWrapper(args);
    }
}