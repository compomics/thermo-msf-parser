/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser.msf.util;

import java.util.Iterator;

/**
 *
 * @author toorn101
 */
public class Joiner {

    public static String join(Iterable someIterable, String joinString) {
        Iterator i = someIterable.iterator();
        StringBuilder builder = new StringBuilder();
        while (i.hasNext()) {
            builder.append(i.next());

            if (i.hasNext()) {
                builder.append(joinString);
            }
        }
        return builder.toString();
    }
}
