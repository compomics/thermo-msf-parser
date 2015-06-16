package com.compomics.thermo_msf_parser_API.highmeminstance;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 21-Feb-2011
 * Time: 08:57:32
 * To change this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
 */
public class WorkflowInfo {

    /**
     * The workflow name
     */
    private String iWorkflowName;
    /**
     * The workflow description
     */
    private String iWorkflowDescription;
    /**
     * The workflow user
     */
    private String iWorkflowUser;
    /**
     * The workflow template
     */
    private String iWorkflowTemplate;
    /**
     * The workflow machine
     */
    private String iWorkflowMachineName;
    /**
     * The workflow messages
     */
    private List<WorkflowMessage> iWorkflowMessages = new ArrayList<WorkflowMessage>();
    /**
     * The msf version info
     */
    private MsfVersionInfo iMsfVersionInfo;

    /**
     * The workflow info constructor
     *
     * @param iWorkflowName The workflow name
     * @param iWorkflowDescription The workflow description
     * @param iWorkflowUser The workflow user
     * @param iWorkflowTemplate The workflow template
     * @param iWorkflowMachineName The workflow machine name
     */
    public WorkflowInfo(String iWorkflowName, String iWorkflowDescription, String iWorkflowUser, String iWorkflowTemplate, String iWorkflowMachineName) {
        this.iWorkflowName = iWorkflowName;
        this.iWorkflowDescription = iWorkflowDescription;
        this.iWorkflowUser = iWorkflowUser;
        this.iWorkflowTemplate = iWorkflowTemplate;
        this.iWorkflowMachineName = iWorkflowMachineName;
    }

    /**
     * <p>addMessage.</p>
     *
     * @param workflowMessage a {@link com.compomics.thermo_msf_parser_API.highmeminstance.WorkflowMessage} object.
     */
    public void addMessage(WorkflowMessage workflowMessage) {
        this.iWorkflowMessages.add(workflowMessage);
    }

    /**
     * <p>setMsfVersionInfo.</p>
     *
     * @param msfVersionInfo a {@link com.compomics.thermo_msf_parser_API.highmeminstance.MsfVersionInfo} object.
     */
    public void setMsfVersionInfo(MsfVersionInfo msfVersionInfo) {
        this.iMsfVersionInfo = msfVersionInfo;
    }

    /**
     * <p>getWorkflowName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getWorkflowName() {
        return iWorkflowName;
    }

    /**
     * <p>getWorkflowDescription.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getWorkflowDescription() {
        return iWorkflowDescription;
    }

    /**
     * <p>getWorkflowUser.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getWorkflowUser() {
        return iWorkflowUser;
    }

    /**
     * <p>getWorkflowTemplate.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getWorkflowTemplate() {
        return iWorkflowTemplate;
    }

    /**
     * <p>getWorkflowMachineName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getWorkflowMachineName() {
        return iWorkflowMachineName;
    }

    /**
     * <p>getWorkflowMessages.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<WorkflowMessage> getWorkflowMessages() {
        return iWorkflowMessages;
    }

    /**
     * <p>getMsfVersionInfo.</p>
     *
     * @return a {@link com.compomics.thermo_msf_parser_API.highmeminstance.MsfVersionInfo} object.
     */
    public MsfVersionInfo getMsfVersionInfo() {
        return iMsfVersionInfo;
    }
}
