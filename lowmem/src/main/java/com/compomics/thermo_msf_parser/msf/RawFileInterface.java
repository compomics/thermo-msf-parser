package com.compomics.thermo_msf_parser.msf;

import java.sql.Connection;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/23/12
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RawFileInterface {
    
    public HashMap<Integer,String> getRawFileForFileID(int fileID,Connection aConnection);

    public String getRawFileNameForFileID(int FileID, Connection aConnection);
}
