package com.compomics.thermo_msf_parser_GUI.convenience;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.ProteinLowMem;

/**
 *
 * @author Davy
 */
public class GUIProteinLowMem extends ProteinLowMem {
    
    MsfFile msfFileForProtein;
    
    public GUIProteinLowMem(String aAccession,int aProteinID,MsfFile aMsfFile){
        super(aAccession, aProteinID);
        this.msfFileForProtein = aMsfFile;
    }
    
        public GUIProteinLowMem(String aAccession,int aProteinID){
        super(aAccession, aProteinID);
    }
    
    public void setMsfFile(MsfFile msfFile){
        this.msfFileForProtein = msfFile;
    }
    
    public MsfFile getMsfFile(){
        return msfFileForProtein;
    }
    
}
