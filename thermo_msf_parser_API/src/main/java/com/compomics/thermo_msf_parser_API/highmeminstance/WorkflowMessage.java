package com.compomics.thermo_msf_parser_API.highmeminstance;

import com.compomics.thermo_msf_parser_API.util.DateConverter;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 21-Feb-2011
 * Time: 09:01:34
 * To change this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class WorkflowMessage {

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
     *
     * @param iMessageId The message id
     * @param iProcessingNodeId The processing node id
     * @param iProcessingNodeNumber The processing node number
     * @param iTime The time
     * @param iMessageKind The message kind
     * @param iMessageId The message id
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

    /**
     * <p>getMessageId.</p>
     *
     * @return a int.
     */
    public int getMessageId() {
        return iMessageId;
    }

    /**
     * <p>getProcessingNodeId.</p>
     *
     * @return a int.
     */
    public int getProcessingNodeId() {
        return iProcessingNodeId;
    }

    /**
     * <p>getProcessingNodeNumber.</p>
     *
     * @return a int.
     */
    public int getProcessingNodeNumber() {
        return iProcessingNodeNumber;
    }

    /**
     * <p>getTimeTicks.</p>
     *
     * @return a long.
     */
    public long getTimeTicks() {
        return iTime;
    }
    
    /**
     * <p>getDate.</p>
     *
     * @return a {@link java.util.Date} object.
     */
    public Date getDate() {
        return DateConverter.ticksToDate(iTime);
    }
    
    /**
     * <p>getUnixTime.</p>
     *
     * @return a long.
     */
    public long getUnixTime() {
        return DateConverter.ticksToEpoch(iTime);
    }

    /**
     * <p>getMessageKind.</p>
     *
     * @return a int.
     */
    public int getMessageKind() {
        return iMessageKind;
    }

    /**
     * <p>getMessage.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getMessage() {
        return iMessage;
    }
}
