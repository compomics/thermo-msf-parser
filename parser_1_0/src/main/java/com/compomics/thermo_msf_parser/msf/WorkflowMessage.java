package com.compomics.thermo_msf_parser.msf;

import com.compomics.thermo_msf_parser.msf.util.DateConverter;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 21-Feb-2011
 * Time: 09:01:34
 * To change this template use File | Settings | File Templates.
 */
public class WorkflowMessage {
    // Class specific log4j logger for Thermo_msf_parserGUI instances.
	 private static Logger logger = Logger.getLogger(WorkflowMessage.class);
    /**
     * The message id
     */
    private int iMessageId;
    /**
     * The processing node id that produced this message
     */
    private int iProcessingNodeId;
    /**
     * The processing node number that produced this message
     */
    private int iProcessingNodeNumber;
    /**
     * The time
     */
    private long iTime;
    /**
     * The message kind
     */
    private int iMessageKind;
    /**
     * The message
     */
    private String iMessage;

    /**
     * The constructor for the message
     * @param iMessageId The message id
     * @param iProcessingNodeId The processing node id
     * @param iProcessingNodeNumber The processing node number
     * @param iTime The time
     * @param iMessageKind The message kind
     * @param iMessage The message
     */
    public WorkflowMessage(int iMessageId, int iProcessingNodeId, int iProcessingNodeNumber, long iTime, int iMessageKind, String iMessage) {
        this.iMessageId = iMessageId;
        this.iProcessingNodeId = iProcessingNodeId;
        this.iProcessingNodeNumber = iProcessingNodeNumber;
        this.iTime = iTime;
        this.iMessageKind = iMessageKind;
        this.iMessage = iMessage;
    }

    public int getMessageId() {
        return iMessageId;
    }

    public int getProcessingNodeId() {
        return iProcessingNodeId;
    }

    public int getProcessingNodeNumber() {
        return iProcessingNodeNumber;
    }

    public long getTimeTicks() {
        return iTime;
    }
    
    public Date getDate() {
        return DateConverter.ticksToDate(iTime);
    }
    
    public long getUnixTime() {
        return DateConverter.ticksToEpoch(iTime);
    }

    public int getMessageKind() {
        return iMessageKind;
    }

    public String getMessage() {
        return iMessage;
    }
}
