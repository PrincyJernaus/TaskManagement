package com.bau.taskportal.bean.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
public class TaskDetails {

    private Integer taskId;

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    private String taskDescription;

    public Integer getTaskId() {
        return taskId;
    }

    private String taskCategory;
    private String assignedToOld;
    private String assignedToNew;
    private String assignedTo;
    private String assignedBy;
    private String status;
    private String activeInd;
    private String filePath;
    private String priority;
    private String frequency;
    private Date loggedDate;
    private Date dueDate;
    private String remarks;

    private String projectName;
    private String managerName;

    public Double getEfforts() {
        return efforts;
    }

    public void setEfforts(Double efforts) {
        this.efforts = efforts;
    }

    private String taskStatus;

    private Double efforts;

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    private byte[] attachment;

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getTaskCategory() {
        return taskCategory;
    }

    public String getAssignedToOld() {
        return assignedToOld;
    }

    public String getAssignedToNew() {
        return assignedToNew;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public String getStatus() {
        return status;
    }

    public String getActiveInd() {
        return activeInd;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getPriority() {
        return priority;
    }

    public String getFrequency() {
        return frequency;
    }

    public Date getLoggedDate() {
        return loggedDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }

    public void setAssignedToOld(String assignedToOld) {
        this.assignedToOld = assignedToOld;
    }

    public void setAssignedToNew(String assignedToNew) {
        this.assignedToNew = assignedToNew;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setActiveInd(String activeInd) {
        this.activeInd = activeInd;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setLoggedDate(Date loggedDate) {
        this.loggedDate = loggedDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
