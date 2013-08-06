package com.compomics.thermo_msf_parser_API.enums;

/**
 * Created by IntelliJ IDEA. User: Niklaas Date: 08/04/11 Time: 11:59 To change
 * this template use File | Settings | File Templates.
 */
public enum MsfVersion {

    VERSION1_2 ("1.2"), VERSION1_3 ("1.3"), VERSION1_4("1.4");
    
    private final String version;
    
    MsfVersion(String aVersion){
        version = aVersion;
    }
    
    public String getVersion(){
        return version;
    }
}
