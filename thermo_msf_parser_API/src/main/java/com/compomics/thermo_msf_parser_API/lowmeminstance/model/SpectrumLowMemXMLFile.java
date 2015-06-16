package com.compomics.thermo_msf_parser_API.lowmeminstance.model;

import java.io.File;

/**
 * <p>SpectrumLowMemXMLFile class.</p>
 *
 * @author Davy
 * @version $Id: $Id
 */
public class SpectrumLowMemXMLFile extends File {
    
    //TODO not finished yet
    private String XML;
    
    SpectrumLowMemXMLFile(String fileLocation){
        super(fileLocation);
    }

    
    /**
     * <p>getXML.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getXML(){
        return XML;
    }
}
