package com.bau.taskportal.util.changeHistory;

import com.bau.taskportal.bean.changehistory.RegionClass;
import com.bau.taskportal.bean.member.MemberDetails;
import com.bau.taskportal.bean.project.ProjectDetails;
import com.bau.taskportal.bean.user.UserDetails;
import com.bau.taskportal.entity.ChangeHistory;
import com.bau.taskportal.entity.Task;
import com.bau.taskportal.entity.User;
import com.bau.taskportal.repository.ChangeHistoryRepository;
import com.bau.taskportal.util.project.ProjectUtilService;
import com.bau.taskportal.util.user.UserUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ChangeHistoryUtilService {

    @Autowired
    ChangeHistoryRepository historyRepository;

    @Autowired
    UserUtilService userUtilService;

    @Autowired
    ProjectUtilService projectUtilService;

    Logger logger = Logger.getLogger(ChangeHistoryUtilService.class.getName());

    public RegionClass saveHistory(ChangeHistory history) throws NullPointerException {
        ChangeHistory changeHistory = new ChangeHistory();
        changeHistory.setChangedBy(history.getChangedBy());
        changeHistory.setChangeTimestamp(history.getChangeTimestamp());
        changeHistory.setChangeDescription(history.getChangeDescription());
        changeHistory.setChangeValue(history.getChangeValue());
        changeHistory.setAssignedToNew(history.getAssignedToNew());
        changeHistory.setAssignedToOld(history.getAssignedToOld());
        changeHistory.setProjectId(history.getProjectId());
        changeHistory.setManagerId(history.getManagerId());
        changeHistory.setAdminId(history.getAdminId());
        return setHistoryDetails(historyRepository.save(changeHistory));
    }

    public List<RegionClass> fetchHistory() throws NullPointerException {
        logger.info("Inside fetchHistory" );
        List<RegionClass> changeHistoryList = new ArrayList<>();
        List<ChangeHistory> historyList = historyRepository.findAll();
        logger.info("historyList Size" +  historyList.size());
        if(!historyList.isEmpty())
        {
            logger.info("historyList inside" );
            RegionClass changeHistory = null;
            for (ChangeHistory history : historyList) {
                changeHistory = new RegionClass();
                logger.info("historyList inside"  + history.getChangedBy());
                changeHistory.setHistoryId(history.getHistoryId());
                changeHistory.setChangedBy(history.getChangedBy());
                changeHistory.setChangeTimestamp(history.getChangeTimestamp());
                changeHistory.setChangeDescription(history.getChangeDescription());
                changeHistory.setChangeValue(history.getChangeValue());
                changeHistory.setAssignedToNew(history.getAssignedToNew());
                changeHistory.setAssignedToOld(history.getAssignedToOld());
                changeHistory.setProjectId(history.getProjectId());
                changeHistory.setManagerId(history.getManagerId());
                changeHistory.setAdminId(history.getAdminId());
                changeHistory.setChangedByNm(history.getChangedBy() != 0 ? userUtilService.findUserName(history.getChangedBy()) : "");
                changeHistory.setManagerNm(history.getManagerId() != 0 ? userUtilService.findUserName(history.getManagerId()) : "");
                changeHistory.setAssignedToNewNm(history.getAssignedToNew() != 0 ? userUtilService.findUserName(history.getAssignedToNew()) : "");
                changeHistory.setAssignedToOldNm(history.getAssignedToOld() != 0 ? userUtilService.findUserName(history.getAssignedToOld())  : "");
                changeHistory.setProjectNm(history.getProjectId() != 0 ? projectUtilService.findProjectById(history.getProjectId()).getProjectName() : "");
                logger.info("historyList inside end" );
                changeHistoryList.add(changeHistory);
            }
            return changeHistoryList;
        }
        return null;
    }

    public RegionClass setHistoryDetails(ChangeHistory history) {
        if (null != history) {
            RegionClass changeHistory = new RegionClass();
            changeHistory.setChangedBy(history.getChangedBy());
            changeHistory.setChangeTimestamp(history.getChangeTimestamp());
            changeHistory.setChangeDescription(history.getChangeDescription());
            changeHistory.setChangeValue(history.getChangeValue());
            changeHistory.setAssignedToNew(history.getAssignedToNew());
            changeHistory.setAssignedToOld(history.getAssignedToOld());
            changeHistory.setProjectId(history.getProjectId());
            changeHistory.setManagerId(history.getManagerId());
            changeHistory.setAdminId(history.getAdminId());
            return changeHistory;
        }
        return null;
    }
}
