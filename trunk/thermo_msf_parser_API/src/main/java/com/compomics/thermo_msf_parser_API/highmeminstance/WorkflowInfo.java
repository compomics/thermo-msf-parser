package com.compomics.thermo_msf_parser_API.highmeminstance;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 21-Feb-2011
 * Time: 08:57:32
 * To change this template use File | Settings | File Templates.
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

    public void addMessage(WorkflowMessage workflowMessage) {
        this.iWorkflowMessages.add(workflowMessage);
    }

    public void setMsfVersionInfo(MsfVersionInfo msfVersionInfo) {
        this.iMsfVersionInfo = msfVersionInfo;
    }

    public String getWorkflowName() {
        return iWorkflowName;
    }

    public String getWorkflowDescription() {
        return iWorkflowDescription;
    }

    public String getWorkflowUser() {
        return iWorkflowUser;
    }

    public String getWorkflowTemplate() {
        return iWorkflowTemplate;
    }

    public String getWorkflowMachineName() {
        return iWorkflowMachineName;
    }

    public List<WorkflowMessage> getWorkflowMessages() {
        return iWorkflowMessages;
    }

    public MsfVersionInfo getMsfVersionInfo() {
        return iMsfVersionInfo;
    }
}
