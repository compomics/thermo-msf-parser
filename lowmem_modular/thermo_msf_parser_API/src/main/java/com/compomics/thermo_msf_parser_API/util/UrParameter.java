/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser_API.util;

import java.io.Serializable;

/**
 *
 * @author Davy
 */
public interface UrParameter extends Serializable {
     /**
     * The version UID for Serialization/Deserialization compatibility
     */
    static final long serialVersionUID = 6808590175195298797L;

    /**
     * This method returns the family name of the parameter. Shall not contain '|'.
     * @return family name
     */
    public String getFamilyName();

    /**
     * This method returns the index of the parameter. Shall not contain '|'.
     * @return the index of the parameter
     */
    public int getIndex();
}
