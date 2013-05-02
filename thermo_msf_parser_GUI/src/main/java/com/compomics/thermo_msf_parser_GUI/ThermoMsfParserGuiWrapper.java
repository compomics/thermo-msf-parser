package com.compomics.thermo_msf_parser_GUI;

import com.compomics.software.CompomicsWrapper;
import java.io.*;
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
     */
    public ThermoMsfParserGuiWrapper() {
        this(null);
    }

    /**
     * Starts the launcher by calling the launch method. Use this as the main
     * class in the jar file.
     *
     * @param args the arguments to pass to the tool
     */
    public ThermoMsfParserGuiWrapper(String[] args) {

        // get the version number set in the pom file
        String jarFileName = "thermo_msf_parser_GUI-" + getVersion() + ".jar";

        String path = this.getClass().getResource("ThermoMsfParserGuiWrapper.class").getPath();
        // remove starting 'file:' tag if there
        if (path.startsWith("file:")) {
            path = path.substring("file:".length(), path.indexOf(jarFileName));
        } else {
            path = path.substring(0, path.indexOf(jarFileName));
        }
        path = path.replace("%20", " ");
        path = path.replace("%5b", "[");
        path = path.replace("%5d", "]");
        File jarFile = new File(path, jarFileName);
        // get the splash 
        String mainClass = "com.compomics.thermo_msf_parser_GUI.Thermo_msf_parserGUI";

        launchTool("Thermo MSF Parser", jarFile, null, mainClass, args);
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

    /**
     * Retrieves the version number set in the properties file.
     *
     * @return the version number of the thermo-msf parser
     */
    public String getVersion() {

        java.util.Properties p = new java.util.Properties();

        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("thermo.msf.parser.properties");
            p.load(is);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return p.getProperty("thermo.msf.parser.version");
    }
}