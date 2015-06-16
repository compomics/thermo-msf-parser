package com.compomics.thermo_msf_parser_API.highmeminstance;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 18-Feb-2011
 * Time: 10:04:47
 * To change this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
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
     *
     * @param iFieldId The field id
     * @param iName The name
     */
    public CustomDataField(int iFieldId, String iName) {
        this.iFieldId = iFieldId;
        this.iName = iName;
    }


    //getters

    /**
     * <p>getFieldId.</p>
     *
     * @return a int.
     */
    public int getFieldId() {
        return iFieldId;
    }

    /**
     * <p>getName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return iName;
    }
}
