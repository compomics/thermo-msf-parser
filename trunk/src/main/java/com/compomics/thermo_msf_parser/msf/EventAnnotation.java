package com.compomics.thermo_msf_parser.msf;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 21-Feb-2011
 * Time: 11:18:48
 */

/**
 * This class represents the event annotation
 */
public class EventAnnotation {
    /**
     * The event id
     */
    private int iEventId;
    /**
     * The isotope pattern
     */
    private int iIsotopePatternId;
    /**
     * The quan result id
     */
    private int iQuanResultId;
    /**
     * The quan channel id
     */
    private int iQuanChannelId;

    /**
     * The event annotation constructor
     * @param iEventId The event id
     * @param iIsotopePatternId The isotope pattern
     * @param iQuanResultId The quan result id
     * @param iQuanChannelId The quan channel id
     */
    public EventAnnotation(int iEventId, int iIsotopePatternId, int iQuanResultId, int iQuanChannelId) {
        this.iEventId = iEventId;
        this.iIsotopePatternId = iIsotopePatternId;
        this.iQuanResultId = iQuanResultId;
        this.iQuanChannelId = iQuanChannelId;
    }

    /**
     * Getter for the event id
      * @return int with the event id
     */
    public int getEventId() {
        return iEventId;
    }

    /**
     * Getter for the isotope pattern id
     * @return int with the isotope pattern
     */
    public int getIsotopePatternId() {
        return iIsotopePatternId;
    }

    /**
     * Getter for the quan result id
     * @return int with the result id
     */
    public int getQuanResultId() {
        return iQuanResultId;
    }

    /**
     * Getter for the quan channel id
     * @return int with the quan channel id
     */
    public int getQuanChannelId() {
        return iQuanChannelId;
    }
}
