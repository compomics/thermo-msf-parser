package com.compomics.thermo_msf_parser.gui;

import com.compomics.util.gui.UtilitiesGUIDefaults;
import org.apache.log4j.Logger;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.google.common.io.Resources;

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
        UtilitiesGUIDefaults.setLookAndFeel();

        try {
            launch();
        } catch (Exception e) {
            System.out.println("Problem launching the application! :-(");
            System.err.println(e.getMessage());
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

        // Get the jarFile path.
        String path;
        path = this.getClass().getResource("Thermo_msf_parserGUI.class").getPath();
        //logger.debug(path);
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

        String cmdLine = javaHome + "java " + options + " -cp " + quote
                + new File(path, jarFileName).getAbsolutePath()
                + quote + " com.compomics.thermo_msf_parser.gui.Thermo_msf_parserGUI";

        System.out.println(cmdLine);

        try {
            // Run the process!
            Runtime.getRuntime().exec(cmdLine);

        } catch (IOException e1) {
            System.err.println(e1.getMessage());
            e1.printStackTrace();
        } catch (Throwable t) {
            System.err.println(t.getMessage());
            t.printStackTrace();
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
            e.printStackTrace();
        }

        return p.getProperty("version");
    }

     /**
     * Retrieves the version number set in the properties file
     *
     * @return the version number of the thermo_msf_parser
     */
    public String getJava() {

        java.util.Properties p = new java.util.Properties();

        try {
            String lLocation = Resources.getResource("thermo_msf_parser.properties").toString();
            lLocation = lLocation.substring(10);
            lLocation = lLocation.substring(0,lLocation.indexOf("thermo_msf_parser-" + getVersion()));
            lLocation = lLocation + "thermo_msf_parser-" + getVersion() + System.getProperties().getProperty("file.separator") + "java.properties";
            InputStream is = new FileInputStream(lLocation);
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
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
