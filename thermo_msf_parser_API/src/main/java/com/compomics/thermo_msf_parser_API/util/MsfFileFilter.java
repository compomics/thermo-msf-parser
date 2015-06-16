package com.compomics.thermo_msf_parser_API.util;

import java.io.File;
import java.util.Locale;
import javax.swing.filechooser.FileFilter;

/**
 * <p>MsfFileFilter class.</p>
 *
 * @author Davy
 * @version $Id: $Id
 */
public class MsfFileFilter extends FileFilter {

    /** {@inheritDoc} */
    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase(new Locale("en")).endsWith(".msf");
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription() {
        return ".msf files";
    }
}
