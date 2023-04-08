package com.bau.taskportal.bean.task;

import java.util.List;
public class RegionClass {

    private String projectName;

    private String dueStartDays;
    private String dueEndDays;

    private String userName;

    private List<TaskDetails> taskList;

    public String getDueStartDays() {
        return dueStartDays;
    }

    public String getDueEndDays() {
        return dueEndDays;
    }

    public List<TaskDetails> getTaskList() {
        return taskList;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getUserName() {
        return userName;
    }
}
