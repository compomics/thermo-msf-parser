package com.compomics.thermo_msf_parser.msf;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 21-Feb-2011
 * Time: 09:01:34
 * To change this template use File | Settings | File Templates.
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
    private int iTime;
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
    public WorkflowMessage(int iMessageId, int iProcessingNodeId, int iProcessingNodeNumber, int iTime, int iMessageKind, String iMessage) {
        this.iMessageId = iMessageId;
        this.iProcessingNodeId = iProcessingNodeId;
        this.iProcessingNodeNumber = iProcessingNodeNumber;
        this.iTime = iTime;
        this.iMessageKind = iMessageKind;
        this.iMessage = iMessage;
    }
}
