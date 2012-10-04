package com.compomics.thermo_msf_parser.msf;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 5/25/12
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EnzymeInterface {

         public HashMap createEnzymeMap (Connection iConnection) throws SQLException;

}
