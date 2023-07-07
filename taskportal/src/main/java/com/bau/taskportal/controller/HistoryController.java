package com.bau.taskportal.controller;

import com.bau.taskportal.bean.changehistory.RegionClass;
import com.bau.taskportal.constant.Constants;
import com.bau.taskportal.entity.ChangeHistory;
import com.bau.taskportal.util.changeHistory.ChangeHistoryUtilService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class HistoryController {

    Logger logger = Logger.getLogger(HistoryController.class.getName());

    @Autowired
    private ChangeHistoryUtilService changeHistoryUtilService;

    private RegionClass historyDetails;

    private ChangeHistory changeHistory;

    private List<RegionClass> historyList;

    @PostMapping("/saveHistory")
    public RegionClass registerUser(@RequestBody @NotNull ChangeHistory changeHistory) {
        logger.info("Change User :: " + changeHistory.getChangedBy());
        historyDetails = null;
        try {
            historyDetails = changeHistoryUtilService.saveHistory(changeHistory);
        } catch (NullPointerException e) {
            logger.warning(Constants.INTERNAL_ERROR_MSG + e.getMessage());
        }
        return historyDetails;
    }

    @GetMapping("/changeHistory")
    public List<RegionClass> getHistoryDetails() {
        logger.info("Inside getHistoryDetails" );
        historyList = new ArrayList<>();
        try {
            historyList = changeHistoryUtilService.fetchHistory();
        } catch (NullPointerException e) {
            logger.warning(Constants.INTERNAL_ERROR_MSG + e.getMessage());
        }
        return historyList;
    }
}
