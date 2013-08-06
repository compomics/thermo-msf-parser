/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser_API.lowmeminstance.controllers;

import com.compomics.thermo_msf_parser_API.lowmeminstance.model.MsfFile;
import com.compomics.thermo_msf_parser_API.interfaces.RatioTypeInterface;
import com.compomics.thermo_msf_parser_API.lowmeminstance.model.RatioTypeLowMem;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Davy
 */
public class RatioTypeLowMemController implements RatioTypeInterface {

    private static final Logger logger = Logger.getLogger(RatioTypeLowMemController.class);
    private List iComponents;
    private List iQuanChannelIds;
    private final Map<Integer, String> quanChannelID = new HashMap<Integer, String>();

    @Override
    public List<RatioTypeLowMem> parseRatioTypes(MsfFile msfFile) {
        List<RatioTypeLowMem> lRatioTypes = new ArrayList<RatioTypeLowMem>();
        try {
            String iQuantitationMethod;
            PreparedStatement stat = null;
            try {
                stat = msfFile.getConnection().prepareStatement("select ParameterValue from ProcessingNodeParameters where ParameterName like 'QuantificationMethod'");
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        iQuantitationMethod = rs.getString(1);
                        String[] lLines = iQuantitationMethod.split("\r\n");
                        boolean lRatioReporting = false;

                        for (int i = 0; i < lLines.length; i++) {
                            String lLine = lLines[i].trim();
                            if (lLine.endsWith("selected=\"QuanLabels\">")) {
                                //we have a component
                                String lComponent = lLine.substring(lLine.indexOf("name=\"") + 6, lLine.indexOf("\"", lLine.indexOf("name=\"") + 6));
                                String lNextChannelLine = lLines[i + 1];
                                int lChannel = Integer.valueOf(lNextChannelLine.substring(lNextChannelLine.indexOf("ID\">") + 4, lNextChannelLine.indexOf("</")));
                                iComponents.add(lComponent);
                                iQuanChannelIds.add(lChannel);
                                quanChannelID.put(lChannel, lComponent);
                            }
                            if (lLine.startsWith("<MethodPart name=\"RatioCalculation\"")) {
                                lRatioReporting = false;
                            }
                            if (lRatioReporting) {
                                if (lLine.startsWith("<MethodPart")) {
                                    String lRatioType = lLine.substring(lLine.indexOf("name=\"") + 6, lLine.indexOf("\"", lLine.indexOf("name=\"") + 6));
                                    String lNextNumeratorLine = lLines[i + 2];
                                    String lNextDenominatorLine = lLines[i + 3];
                                    String lNumerator = lNextNumeratorLine.substring(lNextNumeratorLine.indexOf("or\">") + 4, lNextNumeratorLine.indexOf("</"));
                                    String lDenominator = lNextDenominatorLine.substring(lNextDenominatorLine.indexOf("or\">") + 4, lNextDenominatorLine.indexOf("</"));

                                    lRatioTypes.add(new RatioTypeLowMem(lRatioType, lNumerator, lDenominator, iQuanChannelIds, iComponents));
                                }
                            }
                            if (lLine.startsWith("<MethodPart name=\"RatioReporting\"")) {
                                lRatioReporting = true;
                            }
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
        return lRatioTypes;
    }
}
