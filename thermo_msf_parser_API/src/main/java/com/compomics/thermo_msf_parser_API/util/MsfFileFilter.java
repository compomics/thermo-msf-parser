package com.compomics.thermo_msf_parser_API.util;

import java.io.File;
import java.util.Locale;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Davy
 */
public class MsfFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase(new Locale("en")).endsWith(".msf");
    }

    @Override
    public String getDescription() {
        return ".msf files";
    }
}
