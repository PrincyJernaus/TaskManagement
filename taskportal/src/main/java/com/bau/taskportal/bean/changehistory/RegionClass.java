package com.bau.taskportal.bean.changehistory;

import java.sql.Timestamp;

public class RegionClass {

    private int historyId;

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    private String changeValue;

    private String changeDescription;

    private int assignedToOld;

    private int assignedToNew;

    private Timestamp changeTimestamp;

    private int changedBy;


    public Timestamp getChangeTimestamp() {
        return changeTimestamp;
    }

    public void setChangeTimestamp(Timestamp changeTimestamp) {
        this.changeTimestamp = changeTimestamp;
    }

    public int getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(int changedBy) {
        this.changedBy = changedBy;
    }

    public String getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(String changeValue) {
        this.changeValue = changeValue;
    }

    public String getChangeDescription() {
        return changeDescription;
    }

    public void setChangeDescription(String changeDescription) {
        this.changeDescription = changeDescription;
    }

    public int getAssignedToOld() {
        return assignedToOld;
    }

    public void setAssignedToOld(int assignedToOld) {
        this.assignedToOld = assignedToOld;
    }

    public int getAssignedToNew() {
        return assignedToNew;
    }

    public void setAssignedToNew(int assignedToNew) {
        this.assignedToNew = assignedToNew;
    }

    private int projectId;

    private int adminId;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    private int managerId;

    private String assignedToOldNm;

    private String assignedToNewNm;

    private String changedByNm;

    private String managerNm;

    public String getAssignedToOldNm() {
        return assignedToOldNm;
    }

    public void setAssignedToOldNm(String assignedToOldNm) {
        this.assignedToOldNm = assignedToOldNm;
    }

    public String getAssignedToNewNm() {
        return assignedToNewNm;
    }

    public void setAssignedToNewNm(String assignedToNewNm) {
        this.assignedToNewNm = assignedToNewNm;
    }

    public String getChangedByNm() {
        return changedByNm;
    }

    public void setChangedByNm(String changedByNm) {
        this.changedByNm = changedByNm;
    }

    public String getManagerNm() {
        return managerNm;
    }

    public void setManagerNm(String managerNm) {
        this.managerNm = managerNm;
    }

    public String getProjectNm() {
        return projectNm;
    }

    public void setProjectNm(String projectNm) {
        this.projectNm = projectNm;
    }

    private String projectNm;
}
