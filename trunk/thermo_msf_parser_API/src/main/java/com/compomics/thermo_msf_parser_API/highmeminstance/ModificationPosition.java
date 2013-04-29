package com.compomics.thermo_msf_parser_API.highmeminstance;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 18-Feb-2011
 * Time: 10:48:27
 * To change this template use File | Settings | File Templates.
 */
public class ModificationPosition {
    /**
     * The position
     */
    private int iPosition;
    /**
     * Boolean that indicates if it is a N term
     */
    private boolean iIsNterm;
    /**
     * Boolean that indicates if it is a C term
     */
    private boolean iIsCterm;

    /**
     * The modificationposition constructor
     * @param iPosition The position
     * @param iIsNterm Boolean that indicates if it is a N term
     * @param iIsCterm Boolean that indicates if it is a C term
     */
    public ModificationPosition(int iPosition, boolean iIsNterm, boolean iIsCterm) {
        this.iPosition = iPosition;
        this.iIsNterm = iIsNterm;
        this.iIsCterm = iIsCterm;
    }


    /**
     * Getter for the position
     * @return int with the position
     */
    public int getPosition() {
        return iPosition;
    }

    /**
     * Getter for a boolean that indicates if this is a N-terminal modification
     * @return boolean that indicates if this is a N-terminal modification
     */
    public boolean isNterm() {
        return iIsNterm;
    }

    /**
     * Getter for a boolean that indicates if this is a C-terminal modification
     * @return boolean that indicates if this is a C-terminal modification
     */
    public boolean isCterm() {
        return iIsCterm;
    }
}
