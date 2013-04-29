/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser.msf.util;

import java.util.Date;

/**
 *
 * @author toorn101
 */
public class DateConverter {
    /**
     * Convert NT 'ticks' values to a Java date
     * !! Assuming the conversion and analysis take place in the same time zone !!
     * @param ticks
     * @return 
     */
    public static Date ticksToDate(long ticks) {
        return new Date(ticksToEpoch(ticks));
    }

    public static long ticksToEpoch(long ticks) {
        return (ticks- 621355968000000000L)/10000;
    }
}
