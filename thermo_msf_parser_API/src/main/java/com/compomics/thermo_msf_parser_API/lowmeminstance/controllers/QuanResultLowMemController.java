package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.highmeminstance.EventAnnotation;
import com.compomics.thermo_msf_parser_API.highmeminstance.IsotopePattern;
import com.compomics.thermo_msf_parser_API.highmeminstance.QuanResult;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.QuanResultLowMem;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 10/1/12 Time: 11:03 AM To change
 * this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class QuanResultLowMemController {

    private static final Logger logger = Logger.getLogger(QuanResultLowMemController.class);

    /**
     * <p>getIsotopePatternMap.</p>
     *
     * @param msfFile * @param msfFile the proteome discoverer file to retrieve the isotope patterns from
     * @return a hashmap with key: the isotope pattern id and value: the
     * isotopePattern
     */
    public Map<Integer, IsotopePattern> getIsotopePatternMap(MsfFile msfFile) {
        Map<Integer, IsotopePattern> iIsotopePatternMap = new HashMap<Integer, IsotopePattern>();
        try {
            Statement stat = null;
            try {
                stat = msfFile.getConnection().createStatement();
                ResultSet rs = stat.executeQuery("SELECT * FROM EventAnnotations");
                try {
                    while (rs.next()) {
                        int lIsotopePatternId = rs.getInt("IsotopePatternID");
                        EventAnnotation lEventAnno = new EventAnnotation(rs.getInt("EventID"), rs.getInt("IsotopePatternID"), rs.getInt("QuanResultID"), rs.getInt("QuanChannelID"));
                        if (iIsotopePatternMap.get(lIsotopePatternId) == null) {
                            IsotopePattern lIso = new IsotopePattern(lIsotopePatternId);
                            lIso.addEventAnnotation(lEventAnno);
                            iIsotopePatternMap.put(lIsotopePatternId, lIso);
                        } else {
                            iIsotopePatternMap.get(lIsotopePatternId).addEventAnnotation(lEventAnno);
                        }
                    }

                    rs = stat.executeQuery("SELECT * FROM EventAreaAnnotations");
                    while (rs.next()) {
                        int lIsotopePatternId = rs.getInt("IsotopePatternID");
                        EventAnnotation lEventAnno = new EventAnnotation(rs.getInt("EventID"), rs.getInt("IsotopePatternID"), rs.getInt("QuanResultID"), -1);
                        if (iIsotopePatternMap.get(lIsotopePatternId) == null) {
                            IsotopePattern lIso = new IsotopePattern(lIsotopePatternId);
                            lIso.addEventAnnotation(lEventAnno);
                            iIsotopePatternMap.put(lIsotopePatternId, lIso);
                        } else {
                            iIsotopePatternMap.get(lIsotopePatternId).addEventAnnotation(lEventAnno);
                        }
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
        return iIsotopePatternMap;
    }

    /**
     * <p>getQuanResults.</p>
     *
     * @param msfFile a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile} object.
     * @return hashmap containing the quan results key:quanresultid value:quan
     * object
     */
    public HashMap getQuanResults(MsfFile msfFile) {
        HashMap<Integer, QuanResult> iQuanResultsMap = new HashMap<Integer, QuanResult>();
        try {
            Statement stat = null;
            try {
                stat = msfFile.getConnection().createStatement();
                ResultSet rs = stat.executeQuery("SELECT * FROM PrecursorIonQuanResults");
                try {
                    while (rs.next()) {
                        int lQuantId = rs.getInt("QuanResultID");
                        if (iQuanResultsMap.get(lQuantId) != null) {
                            iQuanResultsMap.get(lQuantId).addQuanValues(rs.getInt("QuanChannelID"), rs.getDouble("Mass"), rs.getInt("Charge"), rs.getDouble("Area"), rs.getDouble("RetentionTime"));
                        } else {
                            QuanResult lQuant = new QuanResult(rs.getInt("QuanResultID"));
                            lQuant.addQuanValues(rs.getInt("QuanChannelID"), rs.getDouble("Mass"), rs.getInt("Charge"), rs.getDouble("Area"), rs.getDouble("RetentionTime"));
                            iQuanResultsMap.put(lQuant.getQuanResultId(), lQuant);
                        }
                    }
                    //add the spectrumid
                    rs = stat.executeQuery("SELECT * FROM PrecursorIonAreaSearchSpectra");
                    while (rs.next()) {
                        int lQuantId = rs.getInt("QuanResultID");
                        if (iQuanResultsMap.get(lQuantId) != null) {
                            iQuanResultsMap.get(lQuantId).addSpectrumId(rs.getInt("SearchSpectrumID"));
                            iQuanResultsMap.get(lQuantId).addProcessingNodeNumber(-1);
                        } else {
                            QuanResult lQuant = new QuanResult(rs.getInt("QuanResultID"));
                            lQuant.addSpectrumId(rs.getInt("SearchSpectrumID"));
                            lQuant.addProcessingNodeNumber(-1);
                            iQuanResultsMap.put(lQuant.getQuanResultId(), lQuant);
                        }
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
        return iQuanResultsMap;
    }

    public QuanResult getQuantForSpectrum(int spectrumId, MsfFile msfFile) throws SQLException {

        PreparedStatement stat = msfFile.getConnection().prepareStatement("SELECT QuanResultID FROM PrecursorIonAreaSearchSpectra WHERE SearchSpectrumID = ?");
        stat.setInt(1, spectrumId);
        ResultSet rs = stat.executeQuery();
        QuanResult result = null;
        while (rs.next()) {
            result = new QuanResult(rs.getInt("QuanResultID"));
            stat = msfFile.getConnection().prepareStatement("SELECT QuanChannelID,Mass,Charge,Area,RetentionTime FROM PrecursorIonQuanResults WHERE QuanResultID = ?");
            stat.setInt(1, result.getQuanResultId());
            while (rs.next()) {
                result.addQuanValues(rs.getInt("QuanChannelID"), rs.getDouble("Mass"), rs.getInt("Charge"), rs.getDouble("Area"), rs.getDouble("RetentionTime"));
            }
        }
        return result;
    }
}
