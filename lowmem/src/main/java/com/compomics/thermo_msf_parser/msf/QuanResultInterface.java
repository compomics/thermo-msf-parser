package com.compomics.thermo_msf_parser.msf;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/23/12
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface QuanResultInterface {

    /*

    */

    public Double getRatioForRatioType(RatioType lType);



    public Vector getRatioTypes(String quantitationMethodXML);
}
