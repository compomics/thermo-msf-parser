package com.compomics.thermo_msf_parser_API.lowmeminstance.model;

import java.io.File;

/**
 *
 * @author Davy
 */
public class SpectrumLowMemXMLFile extends File {
    
    //TODO not finished yet
    private String XML;
    
    SpectrumLowMemXMLFile(String fileLocation){
        super(fileLocation);
    }

    
    public String getXML(){
        return XML;
    }
}
