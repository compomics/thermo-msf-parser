package com.compomics.thermo_msf_parser_API.interfaces;

import com.compomics.thermo_msf_parser_API.highmeminstance.RatioType;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/23/12
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public interface QuanResultInterface {

    /*

    */

    /**
     * <p>getRatioForRatioType.</p>
     *
     * @param lType a {@link com.compomics.thermo_msf_parser_API.highmeminstance.RatioType} object.
     * @return a {@link java.lang.Double} object.
     */
    public Double getRatioForRatioType(RatioType lType);



    /**
     * <p>getRatioTypes.</p>
     *
     * @param quantitationMethodXML a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     */
    public List getRatioTypes(String quantitationMethodXML);
}
