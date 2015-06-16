/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser_API.util;

import java.util.Date;

/**
 * <p>DateConverter class.</p>
 *
 * @author toorn101
 * @version $Id: $Id
 */
public class DateConverter {
    /**
     * Convert NT 'ticks' values to a Java date
     * !! Assuming the conversion and analysis take place in the same time zone !!
     *
     * @param ticks a long.
     * @return a {@link java.util.Date} object.
     */
    public static Date ticksToDate(long ticks) {
        return new Date(ticksToEpoch(ticks));
    }

    /**
     * <p>ticksToEpoch.</p>
     *
     * @param ticks a long.
     * @return a long.
     */
    public static long ticksToEpoch(long ticks) {
        return (ticks- 621355968000000000L)/10000;
    }
}
