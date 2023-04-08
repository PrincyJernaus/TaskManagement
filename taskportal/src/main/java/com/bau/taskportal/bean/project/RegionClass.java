package com.bau.taskportal.bean.project;

import com.bau.taskportal.bean.member.MemberDetails;

import java.util.List;
public class RegionClass {
    private String projectName;
    private String projectDesc;

    private String assignedFor;
//    private MemberDetails managerDetails;
    private String createdBy;
    private List<MemberDetails> teamMembers;

    public String getAssignedFor() {
        return assignedFor;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectDesc() {
        return projectDesc;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public List<MemberDetails> getTeamMembers() {
        return teamMembers;
    }
}
