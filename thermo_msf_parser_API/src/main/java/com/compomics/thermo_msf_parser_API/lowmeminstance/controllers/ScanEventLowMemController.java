/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.highmeminstance.ScanEvent;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.SpectrumLowMem;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select * from ScanEvents where ScanEventID = ?");
                stat.setInt(1, scanEventID);
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        scanEvent = new ScanEvent(rs.getInt("ScanEventID"), rs.getInt("MSLevel"), rs.getInt("Polarity"), rs.getInt("ScanType"), rs.getInt("Ionization"), rs.getInt("MassAnalyzer"), rs.getInt("ActivationType"));
                    }
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
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
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select * from ScanEvents where ScanEventID = ?");
                stat.setInt(1, spectrum.getScanEventId());
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        scanEvent = new ScanEvent(rs.getInt("ScanEventID"), rs.getInt("MSLevel"), rs.getInt("Polarity"), rs.getInt("ScanType"), rs.getInt("Ionization"), rs.getInt("MassAnalyzer"), rs.getInt("ActivationType"));
                    }
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }

        } catch (SQLException ex) {
            logger.error(ex);
        }
        return scanEvent;
    }
}
