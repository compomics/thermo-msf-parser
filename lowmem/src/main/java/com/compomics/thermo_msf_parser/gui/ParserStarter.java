package com.compomics.thermo_msf_parser.gui;

import com.compomics.util.gui.UtilitiesGUIDefaults;
import com.google.common.io.Resources;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 28-Feb-2011
 * Time: 14:31:09
 */
public class ParserStarter {
	// Class specific log4j logger for ParserStarter instances.
	 private static Logger logger = Logger.getLogger(ParserStarter.class);


    /**
     * Starts the launcher by calling the launch method. Use this as the main class in the jar file.
     */
    public ParserStarter() {
        try {
            UtilitiesGUIDefaults.setLookAndFeel();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ParserStarter.class.getName()).log(Level.SEVERE, null, ex);
        }


        try {
            launch();
        } catch (Exception e) {
            logger.info("Problem launching the application! :-(");
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Launches the jar file with parameters to the jvm.
     *
     * @throws java.lang.Exception
     */
    private void launch() throws Exception {

        /**
         * The name of the thermo_msf_parser jar file. Must be equal to the name
         * given in the pom file.
         */
        String jarFileName = "thermo_msf_parser-" + getVersion() + ".jar";
        System.out.println(jarFileName);
        // Get the jarFile path.
        String path;
        path = this.getClass().getResource("Thermo_msf_parserGUI.class").getPath();
        //logger.debug(path);
        System.out.println(path);
        path = path.substring(5, path.indexOf(jarFileName));
        //logger.debug(path);
        path = path.replace("%20", " ");
        //logger.debug(path);

        // Get Java vm options.
        String options = getJava();

        String quote = "";
        if (System.getProperty("os.name").lastIndexOf("Windows") != -1) {
            quote = "\"";
        }

        String javaHome = System.getProperty("java.home") + File.separator +
                "bin" + File.separator;
        Object[] choiceOptions = {"low memory usage (for big files)",
                "high memory usage (for small files)",
                "cancel"};
        int n = JOptionPane.showOptionDialog(null,
                "choose what instance of thermo-msf-parser you want to use",
                "Momory Usage",
                JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,
                null,
                choiceOptions,
                choiceOptions[2]);
        String cmdLine = "";
        if (n == JOptionPane.YES_OPTION){
        cmdLine = javaHome + "java " + options + " -cp " + quote
                + new File(path, jarFileName).getAbsolutePath()
                + quote + " com.compomics.thermo_msf_parser.gui.Thermo_msf_parserGUI";
        }
        else if (n == JOptionPane.NO_OPTION){
            cmdLine = javaHome + "java " + options + " -cp " + quote
                    + new File(path, jarFileName).getAbsolutePath()
                    + quote + " com.compomics.thermo_msf_parser.gui.Thermo_msf_parserGUILowMem";
        }
        logger.info(cmdLine);

        try {
            // Run the process!
            Runtime.getRuntime().exec(cmdLine);

        } catch (IOException e1) {
            logger.error(e1.getMessage());
        } catch (Throwable t) {
            logger.error(t.getMessage());
        }

        finally {
            System.exit(0);
        }
    }

     /**
     * Retrieves the version number set in the properties file
     *
     * @return the version number of the thermo-msf parser
     */
    public String getVersion() {

        java.util.Properties p = new java.util.Properties();

        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("thermo_msf_parser.properties");
            p.load( is );
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return p.getProperty("version");
    }

     /**
     * Get the Java VM options.
     *
     * @return the Java VM options
     */
    public String getJava() {

        java.util.Properties p = new java.util.Properties();

        String lLocation = null;
        try {

            lLocation = Resources.getResource("thermo_msf_parser.properties").toString();
            int i = 1;
            logger.debug(i++ + "\t" + lLocation);
            int lProtocolOffset = lLocation.lastIndexOf(":") + 1;
            lLocation = lLocation.substring(lProtocolOffset);
            logger.debug(i++ + "\t" + lLocation);
            lLocation = lLocation.replace("%20", " ");
            logger.debug(i++ + "\t" + lLocation);
            lLocation = lLocation.substring(0,lLocation.lastIndexOf("thermo_msf_parser-" + getVersion()));
            logger.debug(i++ + "\t" + lLocation);
            lLocation = lLocation + System.getProperties().getProperty("file.separator") + "java.properties";
            logger.debug(i++ + "\t" + lLocation);

            InputStream is = new FileInputStream(lLocation);
            p.load(is);
        } catch (IOException e) {
            logger.error("failed to build java properties from '" + lLocation);
            logger.error(e.getMessage(), e);
        }

        return p.getProperty("java");
    }

    /**
     * Starts the launcher by calling the launch method. Use this as the main class in the jar file.
     *
     * @param args
     */
    public static void main(String[] args) {
        new ParserStarter();
    }
}
