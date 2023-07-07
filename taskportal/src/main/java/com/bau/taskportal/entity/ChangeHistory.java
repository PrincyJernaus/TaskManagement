package com.bau.taskportal.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "change_history")
public class ChangeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer historyId;

    private String changeValue;

    private String changeDescription;

    private int assignedToOld;

    private int assignedToNew;

    public Integer getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
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

    private Timestamp changeTimestamp;

    private int changedBy;

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
}
