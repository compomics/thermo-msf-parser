/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser.msf;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Davy
 */
public class ProteinGroupLowMemController {
private static final Logger logger = Logger.getLogger(ProteinGroupLowMemController.class);
    
    public int getProteinGroupIDForProteinID(int proteinID, Connection msfFileConnection) {
        int proteinGroupID = 0;
        try{
            Statement stat = msfFileConnection.createStatement();
            ResultSet rs = stat.executeQuery("select ProteinGroupID from ProteinsProteinGroups where ProteinID = "+proteinID);
            rs.next();
            proteinGroupID = rs.getInt("ProteinGroupID");
            rs.close();
            stat.close();
        }catch(SQLException sqle) {
            logger.error(sqle);
        }
        return proteinGroupID;
    }
    
    public ProteinGroupLowMem getProteinGroupForProteinID(int proteinID, Connection msfFileConnection) {
            return new ProteinGroupLowMem(getProteinGroupIDForProteinID(proteinID,msfFileConnection));

    }
}