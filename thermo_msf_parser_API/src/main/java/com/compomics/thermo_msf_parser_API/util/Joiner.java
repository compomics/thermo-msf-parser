/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser_API.util;

import java.util.Iterator;

/**
 * <p>Joiner class.</p>
 *
 * @author toorn101
 * @version $Id: $Id
 */
public class Joiner {

    /**
     * <p>join.</p>
     *
     * @param someIterable a {@link java.lang.Iterable} object.
     * @param joinString a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
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
