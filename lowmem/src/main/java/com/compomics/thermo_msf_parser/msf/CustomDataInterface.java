package com.compomics.thermo_msf_parser.msf;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 5/25/12
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CustomDataInterface {

    /**
     * returns the Custom datafield id stored in the SQLite db
     * @return Field id
     */
    public int getFieldId();

    
    /**
     * returns the name of the custom data field
     * @return name of the custom data field
     */
    public String getName();
}
