package com.compomics.thermo_msf_parser_API.lowmeminstance.model;

import com.compomics.thermo_msf_parser_API.highmeminstance.RatioType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA. User: Davy Date: 7/31/12 Time: 2:05 PM To change
 * this template use File | Settings | File Templates.
 */
public class QuantitationXMLLowMem {

    private static final Logger logger = Logger.getLogger(QuantitationXMLLowMem.class);
    private List iComponents = new ArrayList<String>();
    private List<Integer> iChannelIds = new ArrayList<Integer>();
    private List<RatioType> iRatioTypes = new ArrayList<RatioType>();

    public void parseQuantitationXML(String aQuantitationXML) {
        //String iQuantitationMethodName;
        boolean lRatioReporting;
        String[] lLines = aQuantitationXML.split("\r\n");
        lRatioReporting = false;

        for (int i = 0; i < lLines.length; i++) {
            String lLine = lLines[i].trim();
            // if(lLine.startsWith("<ProcessingMethod")){
            // iQuantitationMethodName = lLine.substring(lLine.indexOf("name=\"") + 6, lLine.indexOf("\"", lLine.indexOf("name=\"") + 6));
            //}
            if (lLine.endsWith("selected=\"QuanLabels\">")) {
                //we have a component
                String lComponent = lLine.substring(lLine.indexOf("name=\"") + 6, lLine.indexOf("\"", lLine.indexOf("name=\"") + 6));
                String lNextChannelLine = lLines[i + 1];
                int lChannel = Integer.valueOf(lNextChannelLine.substring(lNextChannelLine.indexOf("ID\">") + 4, lNextChannelLine.indexOf("</")));
                iComponents.add(lComponent);
                iChannelIds.add(lChannel);
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

                    iRatioTypes.add(new RatioType(lRatioType, lNumerator, lDenominator, iChannelIds, iComponents));
                }
            }
            if (lLine.startsWith("<MethodPart name=\"RatioReporting\"")) {
                lRatioReporting = true;
            }
        }

    }

    public String getQuantitationXML(Connection iConnection) throws SQLException {
        String iQuantitationMethod = null;
        try {
            PreparedStatement stat = null;
            try {
                stat = iConnection.prepareStatement("select ParameterValue from ProcessingNodeParameters where ParameterName = 'QuantificationMethod'");
                ResultSet rs = stat.executeQuery();
                try {
                    while (rs.next()) {
                        iQuantitationMethod = rs.getString(1);
                    }
                } finally {
                    rs.close();
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
        return iQuantitationMethod;
    }

    public List<String> getComponents() {
        return iComponents;
    }

    public void setComponents(List aComponentsList) {
        this.iComponents = aComponentsList;
    }

    public List<Integer> getChannelIds() {
        return iChannelIds;
    }

    public void setChannelIds(List<Integer> aChannelIdsList) {
        this.iChannelIds = aChannelIdsList;
    }

    public List<RatioType> getRatioTypes() {
        return iRatioTypes;
    }

    public void setRatioTypes(List aRatioTypeList) {
        this.iRatioTypes = aRatioTypeList;
    }
}
