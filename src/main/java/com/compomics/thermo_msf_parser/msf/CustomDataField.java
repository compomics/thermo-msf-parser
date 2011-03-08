package com.compomics.thermo_msf_parser.msf;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 18-Feb-2011
 * Time: 10:04:47
 * To change this template use File | Settings | File Templates.
 */
public class CustomDataField {

    /**
     * The field id
     */
    private int iFieldId;
    /**
     * The name
     */
    private String iName;

    /**
     * The Constructor
     * @param iFieldId The field id
     * @param iName The name
     */
    public CustomDataField(int iFieldId, String iName) {
        this.iFieldId = iFieldId;
        this.iName = iName;
    }


    //getters

    public int getFieldId() {
        return iFieldId;
    }

    public String getName() {
        return iName;
    }
}
