/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser.msf;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Davy
 */
public class ScanEventLowMemController {

    ScanEvent getScanEventForScanEventID(int scanEventID, Connection msfFileConnection) {
        ScanEvent scanEvent = null;
        try {
            Statement stat = msfFileConnection.createStatement();
            ResultSet rs = stat.executeQuery("select * from ScanEvents where ScanEventID =" + scanEventID);
            while (rs.next()) {
                scanEvent = new ScanEvent(rs.getInt("ScanEventID"), rs.getInt("MSLevel"), rs.getInt("Polarity"), rs.getInt("ScanType"), rs.getInt("Ionization"), rs.getInt("MassAnalyzer"), rs.getInt("ActivationType"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ScanEventLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return scanEvent;
    }

    public void addScanEventToSpectrum(SpectrumLowMem spectrum, Connection msfFileConnection) {
        spectrum.setScanEvent(getScanEventForScanEventID(spectrum.getScanEventId(), msfFileConnection));
    }

    public ScanEvent getScanEventForSpectrum(SpectrumLowMem spectrum, Connection msfFileConnection) {
        ScanEvent scanEvent = null;
        try {
            Statement stat = msfFileConnection.createStatement();
            ResultSet rs = stat.executeQuery("select * from ScanEvents where ScanEventID =" + spectrum.getScanEventId());
            while (rs.next()) {
                scanEvent = new ScanEvent(rs.getInt("ScanEventID"), rs.getInt("MSLevel"), rs.getInt("Polarity"), rs.getInt("ScanType"), rs.getInt("Ionization"), rs.getInt("MassAnalyzer"), rs.getInt("ActivationType"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ScanEventLowMemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return scanEvent;
    }
}
