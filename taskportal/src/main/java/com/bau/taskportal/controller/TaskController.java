package com.bau.taskportal.controller;

import com.bau.taskportal.bean.project.ProjectDetails;
import com.bau.taskportal.bean.task.RegionClass;
import com.bau.taskportal.bean.task.TaskDetails;
import com.bau.taskportal.constant.Constants;
import com.bau.taskportal.entity.ChangeHistory;
import com.bau.taskportal.entity.Task;
import com.bau.taskportal.entity.User;
import com.bau.taskportal.repository.TaskRepository;
import com.bau.taskportal.repository.UserRepository;
import com.bau.taskportal.util.changeHistory.ChangeHistoryUtilService;
import com.bau.taskportal.util.project.ProjectUtilService;
import com.bau.taskportal.util.report.ReportUtilService;
import com.bau.taskportal.util.task.TaskUtilService;
import com.bau.taskportal.util.user.UserUtilService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
public class TaskController {

    @Autowired
    TaskUtilService taskUtilService;
    @Autowired
    ProjectUtilService projectUtilService;
    @Autowired
    UserUtilService userUtilService;

    @Autowired
    ReportUtilService reportUtilService;

    @Autowired
    ChangeHistoryUtilService changeHistoryUtilService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private List<TaskDetails> taskResultList;
    private Integer projectId;
    private Integer userId;
    private User user;
    Logger logger = Logger.getLogger(TaskController.class.getName());


    @PostMapping("/create/task")
    public List<TaskDetails> createTask(@RequestBody RegionClass regionClass) {
        projectId = null;
        try {
            projectId = projectUtilService.findProjectByName(regionClass.getProjectName()).getProjectId();
        } catch (NullPointerException e) {
            logger.warning(Constants.PROJECT_NOT_FOUND_MSG + e.getMessage());
        }
        if (null != projectId) {
            logger.info(Constants.PROJECT_FOUND_MSG);
            taskResultList = new ArrayList<>();
            for (TaskDetails taskDetails : regionClass.getTaskList()) {
                try {
                    taskResultList.add(taskUtilService.createNewTask(taskDetails, projectId));
                } catch (Exception e) {
                    logger.warning(Constants.TASK_NOT_FOUND_MSG + e.getMessage());
                }
            }
        }
        if (null == taskResultList) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return taskResultList;
    }

    @PostMapping("/assign/task")
    public List<TaskDetails> assignTask(@RequestBody @NotNull RegionClass regionClass) {
        projectId = null;
        try {
            projectId = projectUtilService.findProjectByName(regionClass.getProjectName()).getProjectId();
        } catch (NullPointerException e) {
            logger.warning(Constants.PROJECT_NOT_FOUND_MSG + e.getMessage());
        }
        if (null != projectId) {
            logger.info(Constants.PROJECT_FOUND_MSG);
            taskResultList = new ArrayList<>();
            for (TaskDetails taskDetails : regionClass.getTaskList()) {
                try {
                    taskResultList.add(taskUtilService.assignTask(taskDetails, projectId));
                } catch (Exception e) {
                    logger.warning(Constants.TASK_NOT_FOUND_MSG + e.getMessage());
                }

                try
                {
                    logger.info(Constants.CHANGE_HISTORY_START_MSG);
                    ChangeHistory changeHistory = new ChangeHistory();
                    changeHistory.setProjectId(projectId);
                    changeHistory.setChangeValue(Constants.TASK);
                    changeHistory.setChangeDescription(Constants.CHANGE_DISCRIPTION);
                    changeHistory.setAssignedToOld(userUtilService.findUserId(taskDetails.getAssignedToOld()));
                    changeHistory.setAssignedToNew(userUtilService.findUserId(taskDetails.getAssignedToNew()));
                    changeHistory.setChangedBy(userUtilService.findUserId(taskDetails.getAssignedBy()));
                    changeHistory.setManagerId(userUtilService.findUserId(taskDetails.getAssignedBy()));
                    changeHistory.setChangeTimestamp(new Timestamp(new Date().getTime()));
                    changeHistoryUtilService.saveHistory(changeHistory);
                    logger.info(Constants.CHANGE_HISTORY_END_MSG);

                } catch (Exception e) {
                    logger.warning(Constants.HISTORY_NOT_SAVED_MSG + e.getMessage());
                }
            }
        }
        if (null == taskResultList) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return taskResultList;
    }

    @PostMapping("/assign/user/task")
    public List<TaskDetails> assignUserTask(@RequestBody @NotNull RegionClass regionClass) {
        projectId = null;
        userId = null;
        user = null;
        try {
            user = userUtilService.findUser(regionClass.getUserName());
            userId = user.getUserId();
            projectId = user.getProjectId();
        } catch (NullPointerException e) {
            logger.warning(Constants.USER_NOT_FOUND_MSG + e.getMessage());
        }
        if (null != projectId && null != userId) {
            logger.info(Constants.PROJECT_FOUND_MSG);
            taskResultList = new ArrayList<>();
            for (TaskDetails taskDetails : regionClass.getTaskList()) {
                try {
                    taskResultList.add(taskUtilService.assignUserTask(taskDetails, projectId));
                } catch (Exception e) {
                    logger.warning(Constants.TASK_NOT_FOUND_MSG + e.getMessage());
                }

                try
                {
                    logger.info(Constants.CHANGE_HISTORY_START_MSG);
                    ChangeHistory changeHistory = new ChangeHistory();
                    changeHistory.setProjectId(projectId);
                    changeHistory.setChangeValue(Constants.TASK);
                    changeHistory.setChangeDescription(Constants.CHANGE_USER_DISCRIPTION);
                    changeHistory.setAssignedToOld(userUtilService.findUserId(taskDetails.getAssignedToOld()));
                    changeHistory.setAssignedToNew(userUtilService.findUserId(taskDetails.getAssignedToNew()));
                    changeHistory.setChangedBy(userUtilService.findUserId(taskDetails.getAssignedBy()));
                    changeHistory.setManagerId(userUtilService.findUserId(projectUtilService.findProjectById(projectId).getAssignedFor()));
                    changeHistory.setChangeTimestamp(new Timestamp(new Date().getTime()));
                    changeHistoryUtilService.saveHistory(changeHistory);
                    logger.info(Constants.CHANGE_HISTORY_END_MSG);

                } catch (Exception e) {
                    logger.warning(Constants.HISTORY_NOT_SAVED_MSG + e.getMessage());
                }
            }
        }
        if (null == taskResultList) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return taskResultList;
    }

    @PostMapping("/update/task")
    public List<TaskDetails> updateTask(@RequestBody @NotNull RegionClass regionClass) {
        projectId = null;
        try {
            projectId = projectUtilService.findProjectByName(regionClass.getProjectName()).getProjectId();
        } catch (NullPointerException e) {
            logger.warning(Constants.PROJECT_NOT_FOUND_MSG + e.getMessage());
        }
        if (null != projectId) {
            logger.info(Constants.PROJECT_FOUND_MSG);
            taskResultList = new ArrayList<>();
            for (TaskDetails taskDetails : regionClass.getTaskList()) {
                try {
                    taskResultList.add(taskUtilService.updateTask(taskDetails, projectId));
                } catch (Exception e) {
                    logger.warning(Constants.TASK_NOT_FOUND_MSG + e.getMessage());
                }
            }
        }

        if (null == taskResultList) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return taskResultList;
    }

    @PostMapping("/update/user/task")
    public List<TaskDetails> updateUserTask(@RequestBody @NotNull RegionClass regionClass ) {
        projectId = null;
        try {
            projectId = userUtilService.findUser(regionClass.getUserName()).getProjectId();
        } catch (NullPointerException e) {
            logger.warning(Constants.USER_NOT_FOUND_MSG + e.getMessage());
        }
        if (null != projectId) {
            logger.info(Constants.USER_FOUND_PROJECT_MSG);
            taskResultList = new ArrayList<>();
            for (TaskDetails taskDetails : regionClass.getTaskList()) {
                try {
                    taskResultList.add(taskUtilService.updateTask(taskDetails, projectId));
                } catch (Exception e) {
                    logger.warning(Constants.TASK_NOT_FOUND_MSG + e.getMessage());
                }
            }
        }
        if (null == taskResultList) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return taskResultList;
    }

    @PostMapping("/delete/task")
    public List<TaskDetails> deleteTask(@RequestBody @NotNull RegionClass regionClass) {
        projectId = null;
        try {
            projectId = projectUtilService.findProjectByName(regionClass.getProjectName()).getProjectId();
        } catch (NullPointerException e) {
            logger.warning(Constants.PROJECT_NOT_FOUND_MSG + e.getMessage());
        }
        if (null != projectId) {
            logger.info(Constants.PROJECT_FOUND_MSG);
            taskResultList = new ArrayList<>();
            for (TaskDetails taskDetails : regionClass.getTaskList()) {
                try {
                    taskResultList.add(taskUtilService.deleteTask(taskDetails, projectId));
                } catch (Exception e) {
                    logger.warning(Constants.TASK_NOT_FOUND_MSG + e.getMessage());
                }
            }
        }
        if (null == taskResultList) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return taskResultList;
    }

    @PostMapping("/view/active/task")
    public List<TaskDetails> viewActiveTask(@RequestBody @NotNull RegionClass regionClass) {
        projectId = null;
        try {
            projectId = projectUtilService.findProjectByName(regionClass.getProjectName()).getProjectId();
        } catch (NullPointerException e) {
            logger.warning(Constants.PROJECT_NOT_FOUND_MSG + e.getMessage());
        }
        if (null != projectId) {
            logger.info(Constants.PROJECT_FOUND_MSG);
            taskResultList = new ArrayList<>();
            try {
                taskResultList = taskUtilService.viewActiveTask(projectId);
            } catch (Exception e) {
                logger.warning(Constants.TASK_NOT_FOUND_MSG + e.getMessage());
            }
        }
        if (null == taskResultList) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return taskResultList;
    }

    @PostMapping("/view/user/active/task")
    public List<TaskDetails> viewUserActiveTask(@RequestBody @NotNull RegionClass regionClass) {
        logger.info("Inside viewUserActiveTask");
        projectId = null;
        userId = null;
        user = null;
        try {
            user = userUtilService.findUser(regionClass.getUserName());
            userId = user.getUserId();
            projectId = user.getProjectId();
        } catch (NullPointerException e) {
            logger.warning(Constants.USER_NOT_FOUND_MSG + e.getMessage());
        }
        if (null != projectId && null != userId) {
            logger.info(Constants.USER_FOUND_PROJECT_MSG);
            taskResultList = new ArrayList<>();
            try {
                taskResultList = taskUtilService.viewUserActiveTask(projectId, userId);
            } catch (Exception e) {
                logger.warning(Constants.TASK_NOT_FOUND_MSG + e.getMessage());
            }
        }
        if (null == taskResultList) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return taskResultList;
    }

    @PostMapping("/view/all/task")
    public List<TaskDetails> viewAllTask(@RequestBody @NotNull RegionClass regionClass) {
        projectId = null;
        try {
            projectId = projectUtilService.findProjectByName(regionClass.getProjectName()).getProjectId();
        } catch (NullPointerException e) {
            logger.warning(Constants.PROJECT_NOT_FOUND_MSG + e.getMessage());
        }
        if (null != projectId) {
            logger.info(Constants.PROJECT_FOUND_MSG);
            taskResultList = new ArrayList<>();
            try {
                taskResultList = taskUtilService.viewAllTask(projectId);
            } catch (Exception e) {
                logger.warning(Constants.TASK_NOT_FOUND_MSG + e.getMessage());
            }
        }
        if (null == taskResultList) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return taskResultList;
    }

    @PostMapping("/view/user/all/task")
    public List<TaskDetails> viewUserAllTask(@RequestBody @NotNull RegionClass regionClass) {
        projectId = null;
        userId = null;
        user = null;
        try {
            user = userUtilService.findUser(regionClass.getUserName());
            userId = user.getUserId();
            projectId = user.getProjectId();
        } catch (NullPointerException e) {
            logger.warning(Constants.USER_NOT_FOUND_MSG + e.getMessage());
        }
        if (null != projectId && null != userId) {
            logger.info(Constants.USER_FOUND_PROJECT_MSG);
            taskResultList = new ArrayList<>();
            try {
                taskResultList = taskUtilService.viewUserAllTask(projectId, userId);
            } catch (Exception e) {
                logger.warning(Constants.TASK_NOT_FOUND_MSG + e.getMessage());
            }
        }
        if (null == taskResultList) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return taskResultList;
    }

    @PostMapping("/view/userAll/task")
    public List<TaskDetails> viewUserAllTasks(@RequestBody @NotNull RegionClass regionClass) {
        taskResultList = new ArrayList<>();
        projectId = null;
        userId = null;

        try {
            userId = userUtilService.findUser(regionClass.getUserName()).getUserId();
        } catch (NullPointerException e) {
            logger.warning(Constants.TASK_WARNING_MSG3 + e.getMessage());
        }
        try {
            projectId = userRepository.findByUserId(userId).getProjectId();
        } catch (NullPointerException e) {
            logger.warning(Constants.TASK_WARNING_MSG2 + e.getMessage());
        }
        if (null != projectId) {
            try {
                taskResultList = taskUtilService.viewAllTask(projectId);
            } catch (Exception e) {
                logger.warning(Constants.TASK_WARNING_MSG1 + e.getMessage());
            }
        }
        return taskResultList;
    }

    @GetMapping("/view/assignedTo/task/{taskId}/{projectId}")
    public User getTaskAssignedTo(@PathVariable int taskId, @PathVariable String projectId) {
        System.out.println("getTaskAssignedTo Task Id" + taskId);
        System.out.println("getTaskAssignedTo Project Id" + projectId);
        ProjectDetails projectDetail = null;
        int assignedTo = 0;
        User user = null;
        try
        {
            if(null != projectId)
            {
                projectDetail = projectUtilService.findProject(projectId);
            }
        }
        catch (NullPointerException e)
        {
            logger.warning(Constants.PROJECT_NOT_FOUND_MSG + e.getMessage());
        }

        try
        {
            if(null != projectDetail.getProjectId())
            {
                assignedTo = taskUtilService.fetchAssignedTo(taskId, projectDetail.getProjectId());
            }
        }
        catch (NullPointerException e)
        {
            logger.warning(Constants.PROJECT_NOT_FOUND_MSG + e.getMessage());
        }

        logger.info("assignedTo ::" + assignedTo);

        try
        {
            if(0 != assignedTo)
            {
                user = userRepository.findByUserId(assignedTo);
            }
        }
        catch (NullPointerException e)
        {
            logger.warning(Constants.USER_NOT_FOUND_MSG + e.getMessage());
        }
        return user;
    }

    @GetMapping("/view/assignedToOld/task/{taskId}")
    public User getTaskAssignedToOld(@PathVariable int taskId) {
        logger.info("getTaskAssignedToOld Task Id" + taskId);
        int assignedToOld = 0;
        User user = null;
        Optional<Task> task = taskRepository.findById(taskId);
        if(task.isPresent())
        {
            assignedToOld = task.get().getAssignedTo();
        }

        try
        {
            if(0 != assignedToOld)
            {
                user = userRepository.findByUserId(assignedToOld);
            }
        }
        catch (NullPointerException e)
        {
            logger.warning(Constants.USER_NOT_FOUND_MSG + e.getMessage());
        }
        return user;
    }

    @PostMapping("fetch/due/task")
    public List<TaskDetails> fetchDueTask(@RequestBody @NotNull RegionClass regionClass) {
        projectId = null;
        if (null != regionClass.getProjectName()) {
            try {
                projectId = projectUtilService.findProjectByName(regionClass.getProjectName()).getProjectId();
            } catch (NullPointerException e) {
                logger.warning(Constants.PROJECT_NOT_FOUND_MSG + e.getMessage());
            }
        }
        if (null != projectId && null != regionClass.getDueStartDays() && null != regionClass.getDueEndDays()) {
            logger.info(Constants.PROJECT_FOUND_MSG);
            taskResultList = new ArrayList<>();
            try {
                taskResultList = taskUtilService.fetchDueTask(projectId, Long.parseLong(regionClass.getDueStartDays()), Long.parseLong(regionClass.getDueEndDays()));
            } catch (Exception e) {
                logger.warning(Constants.TASK_NOT_FOUND_MSG + e.getMessage());
            }
        }
        if (null == taskResultList) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return taskResultList;
    }

    @PostMapping("fetch/user/due/task")
    public List<TaskDetails> fetchUserDueTask(@RequestBody @NotNull RegionClass regionClass) {
        projectId = null;
        userId = null;
        user = null;
        if (null != regionClass.getUserName()) {
            try {
                user = userUtilService.findUser(regionClass.getUserName());
                userId = user.getUserId();
                projectId = user.getProjectId();
            } catch (NullPointerException e) {
                logger.warning(Constants.USER_NOT_FOUND_MSG + e.getMessage());
            }
        }
        if (null != projectId && null != userId && null != regionClass.getDueStartDays() && null != regionClass.getDueEndDays()) {
            logger.info(Constants.USER_FOUND_PROJECT_MSG);
            taskResultList = new ArrayList<>();
            try {
                taskResultList = taskUtilService.fetchUserDueTask(projectId, userId, Long.parseLong(regionClass.getDueStartDays()), Long.parseLong(regionClass.getDueEndDays()));
            } catch (Exception e) {
                logger.warning(Constants.TASK_NOT_FOUND_MSG + e.getMessage());
            }
        }
        if (null == taskResultList) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return taskResultList;
    }

    @PostMapping("fetch/cross/due/task")
    public List<TaskDetails> fetchCrossDueTask(@RequestBody @NotNull RegionClass regionClass) {
        projectId = null;
        if (null != regionClass.getProjectName()) {
            try {
                projectId = projectUtilService.findProjectByName(regionClass.getProjectName()).getProjectId();
            } catch (NullPointerException e) {
                logger.warning(Constants.PROJECT_NOT_FOUND_MSG + e.getMessage());
            }
        }
        if (null != projectId) {
            logger.info(Constants.PROJECT_FOUND_MSG);
            taskResultList = new ArrayList<>();
            try {
                taskResultList = taskUtilService.fetchCrossDueTask(projectId);
            } catch (Exception e) {
                logger.warning(Constants.TASK_NOT_FOUND_MSG + e.getMessage());
            }
        }
        if (null == taskResultList) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return taskResultList;
    }

    @PostMapping("fetch/user/cross/due/task")
    public List<TaskDetails> fetchUserCrossDueTask(@RequestBody @NotNull RegionClass regionClass) {
        projectId = null;
        userId = null;
        user = null;
        if (null != regionClass.getUserName()) {
            try {
                user = userUtilService.findUser(regionClass.getUserName());
                userId = user.getUserId();
                projectId = user.getProjectId();
            } catch (NullPointerException e) {
                logger.warning(Constants.USER_NOT_FOUND_MSG + e.getMessage());
            }
        }
        if (null != projectId && null != userId) {
            logger.info(Constants.USER_FOUND_PROJECT_MSG);
            taskResultList = new ArrayList<>();
            try {
                taskResultList = taskUtilService.fetchUserCrossDueTask(projectId, userId);
            } catch (Exception e) {
                logger.warning(Constants.TASK_NOT_FOUND_MSG + e.getMessage());
            }
        }
        if (null == taskResultList) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return taskResultList;
    }

    @PostMapping("report/manager/due/task")
    public List<TaskDetails> reportManagerDueTask(@RequestBody @NotNull RegionClass regionClass) {
        projectId = null;
        userId = null;
        List<TaskDetails> finalList = new ArrayList<>();
        if (null != regionClass.getUserName()) {
            try {
                userId = userUtilService.findUser(regionClass.getUserName()).getUserId();
            } catch (NullPointerException e) {
                logger.warning(Constants.USER_NOT_FOUND_MSG + e.getMessage());
            }
        }
        if (null != userId && null != regionClass.getDueStartDays() && null != regionClass.getDueEndDays()) {
            logger.info(Constants.USER_FOUND_PROJECT_MSG);
            List<ProjectDetails> projectDetailsList = projectUtilService.fetchAllProjectByUserId(userId);
            if (null != projectDetailsList) {
                projectDetailsList.forEach(projectDetails -> {
                    logger.info(Constants.PROJECT_FOUND_MSG);
                    taskResultList = new ArrayList<>();
                    try {
                        taskResultList = taskUtilService.fetchDueTask(projectDetails.getProjectId(), Long.parseLong(regionClass.getDueStartDays()), Long.parseLong(regionClass.getDueEndDays()));
                    } catch (Exception e) {
                        logger.warning(Constants.TASK_NOT_FOUND_MSG + e.getMessage());
                    }
                    if (null != taskResultList) {
                        taskResultList.forEach(taskDetails -> {
                            taskDetails.setManagerName(regionClass.getUserName());
                            taskDetails.setProjectName(projectDetails.getProjectName());
                        });
                        finalList.addAll(taskResultList);
                    }
                });
            }
        }
        if (finalList.isEmpty()) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return finalList;
    }

    @PostMapping("report/manager/all/task")
    public List<TaskDetails> reportManagerAllTask(@RequestBody @NotNull RegionClass regionClass) {
        projectId = null;
        userId = null;
        List<TaskDetails> finalList = new ArrayList<>();
        if (null != regionClass.getUserName()) {
            try {
                userId = userUtilService.findUser(regionClass.getUserName()).getUserId();
            } catch (NullPointerException e) {
                logger.warning(Constants.USER_NOT_FOUND_MSG + e.getMessage());
            }
        }
        if (null != userId) {
            logger.info(Constants.USER_FOUND_PROJECT_MSG);
            List<ProjectDetails> projectDetailsList = projectUtilService.fetchAllProjectByUserId(userId);
            if (null != projectDetailsList) {
                projectDetailsList.forEach(projectDetails -> {
                    logger.info(Constants.PROJECT_FOUND_MSG);
                    taskResultList = new ArrayList<>();
                    try {
                        taskResultList = taskUtilService.viewManagerAllTask(projectDetails.getProjectId(), userId);
                    } catch (Exception e) {
                        logger.warning(Constants.TASK_NOT_FOUND_MSG + e.getMessage());
                    }
                    if (null != taskResultList) {
                        taskResultList.forEach(taskDetails -> {
                            taskDetails.setManagerName(regionClass.getUserName());
                            taskDetails.setProjectName(projectDetails.getProjectName());
                        });
                        finalList.addAll(taskResultList);
                    }
                });
            }
        }
        if (finalList.isEmpty()) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return finalList;
    }

    @PostMapping("report/manager/cross/due/task")
    public List<TaskDetails> reportManagerCrossDueTask(@RequestBody @NotNull RegionClass regionClass) {
        projectId = null;
        userId = null;
        List<TaskDetails> finalList = new ArrayList<>();
        if (null != regionClass.getUserName()) {
            try {
                userId = userUtilService.findUser(regionClass.getUserName()).getUserId();
            } catch (NullPointerException e) {
                logger.warning(Constants.USER_NOT_FOUND_MSG + e.getMessage());
            }
        }
        if (null != userId) {
            logger.info(Constants.USER_FOUND_PROJECT_MSG);
            List<ProjectDetails> projectDetailsList = projectUtilService.fetchAllProjectByUserId(userId);
            if (null != projectDetailsList) {
                projectDetailsList.forEach(projectDetails -> {
                    logger.info(Constants.PROJECT_FOUND_MSG);
                    taskResultList = new ArrayList<>();
                    try {
                        taskResultList = taskUtilService.fetchManagerCrossDueTask(projectDetails.getProjectId(), userId);
                    } catch (Exception e) {
                        logger.warning(Constants.TASK_NOT_FOUND_MSG + e.getMessage());
                    }
                    if (null != taskResultList) {
                        taskResultList.forEach(taskDetails -> {
                            taskDetails.setManagerName(regionClass.getUserName());
                            taskDetails.setProjectName(projectDetails.getProjectName());
                        });
                        finalList.addAll(taskResultList);
                    }
                });
            }
        }
        if (finalList.isEmpty()) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return finalList;
    }

    @PostMapping("/export/report")
    public void exportReport(@RequestBody RegionClass regionClass)
            throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        logger.info("Entering TaskController.exportReport()");
        reportUtilService.exportCsvData(regionClass.getTaskList());
        logger.info("Ending TaskController.exportReport()");
    }
}