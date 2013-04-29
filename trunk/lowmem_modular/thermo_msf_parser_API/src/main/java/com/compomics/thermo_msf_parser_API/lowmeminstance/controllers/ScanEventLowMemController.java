/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.highmeminstance.ScanEvent;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.SpectrumLowMem;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;

/**
 *
 * @author Davy
 */
public class ScanEventLowMemController {

    private static final Logger logger = Logger.getLogger(ScanEventLowMemController.class);
    
    ScanEvent getScanEventForScanEventID(int scanEventID, MsfFile msfFile) {
        
        ScanEvent scanEvent = null;
        try {
            Statement stat = msfFile.getConnection().createStatement();
            ResultSet rs = stat.executeQuery("select * from ScanEvents where ScanEventID =" + scanEventID);
            while (rs.next()) {
                scanEvent = new ScanEvent(rs.getInt("ScanEventID"), rs.getInt("MSLevel"), rs.getInt("Polarity"), rs.getInt("ScanType"), rs.getInt("Ionization"), rs.getInt("MassAnalyzer"), rs.getInt("ActivationType"));
            }
            stat.close();
            rs.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return scanEvent;
    }

    public void addScanEventToSpectrum(SpectrumLowMem spectrum, MsfFile msfFile) {
        spectrum.setScanEvent(getScanEventForScanEventID(spectrum.getScanEventId(), msfFile));
    }

    public ScanEvent getScanEventForSpectrum(SpectrumLowMem spectrum, MsfFile msfFile) {
        ScanEvent scanEvent = null;
        try {
            Statement stat = msfFile.getConnection().createStatement();
            ResultSet rs = stat.executeQuery("select * from ScanEvents where ScanEventID =" + spectrum.getScanEventId());
            while (rs.next()) {
                scanEvent = new ScanEvent(rs.getInt("ScanEventID"), rs.getInt("MSLevel"), rs.getInt("Polarity"), rs.getInt("ScanType"), rs.getInt("Ionization"), rs.getInt("MassAnalyzer"), rs.getInt("ActivationType"));
            }
            stat.close();
            rs.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return scanEvent;
    }
}
