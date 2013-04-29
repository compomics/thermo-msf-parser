package com.compomics.thermo_msf_parser.msf;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 22-Feb-2011
 * Time: 08:42:26
 */

/**
 * This class represent an event
 */
public class Event {
    // Class specific log4j logger for Thermo_msf_parserGUI instances.
	 private static Logger logger = Logger.getLogger(Event.class);
    /**
     * The event id
     */
    private int iEventId;
    /**
     * The mass
     */
    private double iMass;
    /**
     * The average mass
     */
    private double iAverageMass;
    /**
     * The area
     */
    private double iArea;
    /**
     * The intensity
     */
    private double iIntensity;
    /**
     * The peak width
     */
    private double iPeakWidth;
    /**
     * The retention time
     */
    private double iRetentionTime;
    /**
     * The left retention time
     */
    private double iLeftRetentionTime;
    /**
     * The right retention time
     */
    private double iRightRetentionTime;
    /**
     * The SN
     */
    private double iSN;
    /**
     * The file id
     */
    private int iFileId;

    /**
     * The constructor for the event
     * @param lRs A resultset from the sql lite msf file
     * @throws SQLException Throws an exception when there is a problem with the sql library
     */
    public Event(ResultSet lRs) throws SQLException {
        this.iEventId = lRs.getInt("EventID");
        this.iMass = lRs.getDouble("Mass");
        this.iAverageMass = lRs.getDouble("MassAvg");
        this.iArea = lRs.getDouble("Area");
        this.iIntensity = lRs.getDouble("Intensity");
        this.iPeakWidth = lRs.getDouble("PeakWidth");
        this.iRetentionTime = lRs.getDouble("RT");
        this.iLeftRetentionTime = lRs.getDouble("LeftRT");
        this.iRightRetentionTime = lRs.getDouble("RightRT"); 
        this.iSN = lRs.getDouble("SN"); 
        this.iFileId = lRs.getInt("FileID"); 
    }


    /**
     * Getter for the event id
     * @return int with the event id
     */
    public int getEventId() {
        return iEventId;
    }

    /**
     * Getter for the mass
     * @return double with the mass
     */
    public double getMass() {
        return iMass;
    }

    /**
     * Getter for the average mass
     * @return double with the average
     */
    public double getAverageMass() {
        return iAverageMass;
    }

    /**
     * Getter for the area
     * @return double with the area
     */
    public double getArea() {
        return iArea;
    }

    /**
     * Getter for the intensity
     * @return double with the intensity
     */
    public double getIntensity() {
        return iIntensity;
    }

    /**
     * Getter for the peak width
     * @return double with the peak width
     */
    public double getPeakWidth() {
        return iPeakWidth;
    }

    /**
     * Getter for the retention time
     * @return double with the retention time
     */
    public double getRetentionTime() {
        return iRetentionTime;
    }

    /**
     * Getter for the left retention time
     * @return double with the left retention time
     */
    public double getLeftRetentionTime() {
        return iLeftRetentionTime;
    }

    /**
     * Getter for the right retention time
     * @return double with the right retention time
     */
    public double getRightRetentionTime() {
        return iRightRetentionTime;
    }

    /**
     * Getter for the SN
     * @return double with the sn
     */
    public double getSN() {
        return iSN;
    }

    /**
     * Getter for the file id
     * @return int with the file id
     */
    public int getFileId() {
        return iFileId;
    }

    /**
     * This method gives events by the given event ids
     * @param lIds The ids of the wanted events
     * @param lConn The connection to the msf file
     * @return Vector<Event> The events
     * @throws SQLException An exception is thrown when there is a problem with the connection to the msf file
     */
    public static Vector<Event> getEventByIds(Vector<Integer> lIds, Connection lConn) throws SQLException {
        Vector<Event> lEvents = new Vector<Event>(); 

        String lIdsString =  "";
        for(int i = 0; i<lIds.size(); i ++){
            lIdsString = lIdsString  + lIds.get(i) + ",";
        }
        if(lIds.size() > 0){

            lIdsString = lIdsString.substring(0, lIdsString.lastIndexOf(","));

            Statement stat = lConn.createStatement();
            ResultSet rs;

            //get all the events
            rs = stat.executeQuery("select * from Events where EventID in (" + lIdsString + ")");

            while(rs.next()){
                lEvents.add(new Event(rs));
            }
        }

        return lEvents;
    }


    /**
     * This method gives the events by a range of the retention time
     * @param lLowerRT The lower retention time
     * @param lUpperRT The upper retention time
     * @param lFileId The file id
     * @param lConn The connection to the msf file
     * @return Vector<Event> The events
     * @throws SQLException An exception is thrown when there is a problem with the connection to the msf file
     */
    public static Vector<Event> getEventByRetentionTimeLimitAndFileId(double lLowerRT, double lUpperRT, int lFileId, Connection lConn) throws SQLException {
        Vector<Event> lEvents = new Vector<Event>();

        Statement stat = lConn.createStatement();
        ResultSet rs;

        //get all the events
        rs = stat.executeQuery("select * from Events where RT > " + lLowerRT + " and RT < " + lUpperRT + " and FileID = " + lFileId);

        while(rs.next()){
            lEvents.add(new Event(rs));
        }

        return lEvents;
    }

    /**
     * This method gives the events by a range of the retention time and mass
     * @param lLowerRT The lower retention time
     * @param lUpperRT The upper retention time
     * @param lLowerMass The lower mass
     * @param lUpperMass The upper mass
     * @param lFileId The file id
     * @param lConn The connection to the msf file
     * @return Vector<Event> The events
     * @throws SQLException An exception is thrown when there is a problem with the connection to the msf file
     */
    public static Vector<Event> getEventByRetentionTimeLimitMassLimitAndFileId(double lLowerRT, double lUpperRT, double lLowerMass, double lUpperMass, int lFileId, Connection lConn) throws SQLException {
        Vector<Event> lEvents = new Vector<Event>();

        Statement stat = lConn.createStatement();
        ResultSet rs;

        //get all the events
        rs = stat.executeQuery("select * from Events where RT > " + lLowerRT + " and RT < " + lUpperRT + " and Mass > " + lLowerMass + " and Mass < " + lUpperMass + " and FileID = " + lFileId);

        while(rs.next()){
            lEvents.add(new Event(rs));
        }

        return lEvents;
    }

    /**
     * This method gives the events by a range of the retention time and mass and excluding some events by id
     * @param lLowerRT The lower retention time
     * @param lUpperRT The upper retention time
     * @param lLowerMass The lower mass
     * @param lUpperMass The upper mass
     * @param lIds A vector with the excluded ids (Integer)
     * @param lFileId The file id
     * @param lConn The connection to the msf file
     * @return Vector<Event> The events
     * @throws SQLException An exception is thrown when there is a problem with the connection to the msf file
     */
    public static Vector<Event> getEventByRetentionTimeLimitMassLimitAndFileIdExcludingIds(double lLowerRT, double lUpperRT, double lLowerMass, double lUpperMass, Vector<Integer> lIds, int lFileId, Connection lConn) throws SQLException {
        Vector<Event> lEvents = new Vector<Event>();
        String lIdsString =  "";
        for(int i = 0; i<lIds.size(); i ++){
            lIdsString = lIdsString  + lIds.get(i) + ",";
        }
        if(lIdsString.indexOf(",") >= 0){
            lIdsString = lIdsString.substring(0, lIdsString.lastIndexOf(","));
        }

        Statement stat = lConn.createStatement();
        ResultSet rs;

        //get all the events
        rs = stat.executeQuery("select * from Events where RT > " + lLowerRT + " and RT < " + lUpperRT + " and Mass > " + lLowerMass + " and Mass < " + lUpperMass + " and EventID not in (" + lIdsString + ")and FileID = " + lFileId);

        while(rs.next()){
            lEvents.add(new Event(rs));
        }

        return lEvents;
    }
}
