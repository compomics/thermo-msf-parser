package com.compomics.thermo_msf_parser.msf;

import java.sql.*;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 4/30/12
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class RatioTypesLowMem implements RatioTypesInterface{

    Vector<RatioType> lRatiotypes;
    private Vector<Integer> iQuanChannelIds;
    private Vector<Double> iIntensities;
    Vector<String> iComponents = new Vector<String>();
    HashMap<Integer,String> quanChannelID = new HashMap<Integer, String>();

    public Vector<RatioType> parseRatioTypes(Connection iConnection) throws SQLException {
        String iQuantitationMethod;
        String iQuantitationMethodName = "";
        PreparedStatement stat = iConnection.prepareStatement("select ParameterValue from ProcessingNodeParameters where ParameterName like 'QuantificationMethod'");
       ResultSet rs = stat.executeQuery();
        while(rs.next()){
            iQuantitationMethod = rs.getString(1);
            String[] lLines = iQuantitationMethod.split("\r\n");
            boolean lRatioReporting = false;

            for(int i = 0; i<lLines.length; i ++){
                String lLine = lLines[i].trim();
                if(lLine.startsWith("<ProcessingMethod")){
                    iQuantitationMethodName = lLine.substring(lLine.indexOf("name=\"") + 6, lLine.indexOf("\"", lLine.indexOf("name=\"") + 6));
                }
                if(lLine.endsWith("selected=\"QuanLabels\">")){
                    //we have a component
                    String lComponent = lLine.substring(lLine.indexOf("name=\"") + 6, lLine.indexOf("\"", lLine.indexOf("name=\"") + 6));
                    String lNextChannelLine = lLines[i + 1];
                    int lChannel = Integer.valueOf(lNextChannelLine.substring(lNextChannelLine.indexOf("ID\">") + 4, lNextChannelLine.indexOf("</")));
                    iComponents.add(lComponent);
                    iQuanChannelIds.add(lChannel);
                    quanChannelID.put(lChannel,lComponent);
                }
                if(lLine.startsWith("<MethodPart name=\"RatioCalculation\"")){
                    lRatioReporting = false;
                }
                if(lRatioReporting){
                    if(lLine.startsWith("<MethodPart")){
                        String lRatioType = lLine.substring(lLine.indexOf("name=\"") + 6, lLine.indexOf("\"", lLine.indexOf("name=\"") + 6));
                        String lNextNumeratorLine = lLines[i + 2];
                        String lNextDenominatorLine = lLines[i + 3];
                        String lNumerator = lNextNumeratorLine.substring(lNextNumeratorLine.indexOf("or\">") + 4, lNextNumeratorLine.indexOf("</"));
                        String lDenominator = lNextDenominatorLine.substring(lNextDenominatorLine.indexOf("or\">") + 4, lNextDenominatorLine.indexOf("</"));

                        lRatiotypes.add(new RatioType(lRatioType, lNumerator, lDenominator, iQuanChannelIds, iComponents));
                    }
                }
                if(lLine.startsWith("<MethodPart name=\"RatioReporting\"")){
                    lRatioReporting = true;
                }
            }
        }
    return lRatiotypes;
    }
}
