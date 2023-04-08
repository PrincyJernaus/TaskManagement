package com.bau.taskportal.util.task;

import com.bau.taskportal.bean.task.TaskDetails;
import com.bau.taskportal.constant.Constants;
import com.bau.taskportal.entity.Task;
import com.bau.taskportal.repository.TaskRepository;
import com.bau.taskportal.repository.UserRepository;
import com.bau.taskportal.util.project.ProjectUtilService;
import com.bau.taskportal.util.user.UserUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.Deflater;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class TaskUtilService {

    @Autowired
    UserUtilService userUtilService;
    @Autowired
    ProjectUtilService projectUtilService;
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private Task task = null;

    Logger logger = Logger.getLogger(TaskUtilService.class.getName());

    private SimpleDateFormat formatter = new SimpleDateFormat(Constants.DUE_DATE_FORMAT);

    public TaskDetails createNewTask(TaskDetails taskDetails, Integer projectId) throws NullPointerException {
        if (null == taskRepository.findByProjectIdAndTaskDescriptionAndTaskCategoryAndAssignedToAndActiveInd(projectId, taskDetails.getTaskDescription(), taskDetails.getTaskCategory(), userUtilService.findUserId(taskDetails.getAssignedTo()), Constants.TASK_IS_ACTIVE)) {
            task = new Task();
            task.setTaskCategory(taskDetails.getTaskCategory());
            task.setTaskDescription(taskDetails.getTaskDescription());
            task.setActiveInd(Constants.TASK_IS_ACTIVE);
            task.setAssignedBy(userUtilService.findUserId(taskDetails.getAssignedBy()));
            task.setAssignedTo(userUtilService.findUserId(taskDetails.getAssignedTo()));
            task.setDueDate(taskDetails.getDueDate());
            task.setFilePath(taskDetails.getFilePath());
            task.setFrequency(taskDetails.getFrequency());
            task.setProjectId(projectId);
            task.setRemarks(taskDetails.getRemarks());
            task.setLoggedDate(taskDetails.getLoggedDate());
            task.setStatus(taskDetails.getStatus());
            task.setPriority(taskDetails.getPriority());
            task.setCreateTimestamp(new Timestamp(new Date().getTime()));
            task.setUpdatedTimestamp(new Timestamp(new Date().getTime()));
            logger.info(taskDetails.getTaskDescription() + " " + Constants.TASK_CREATING_FOR + " " + taskDetails.getAssignedTo());
            return setTaskDetails(taskRepository.save(task));
        }
        logger.info(taskDetails.getTaskDescription() + " " + Constants.TASK_ALREADY_ASSIGNED_FOR + " " + taskDetails.getAssignedTo());
        return null;
    }

    public TaskDetails assignTask(TaskDetails taskDetails, Integer projectId) throws NullPointerException {
        task = taskRepository.findByProjectIdAndTaskDescriptionAndTaskCategoryAndAssignedToAndAssignedByAndActiveInd(projectId, taskDetails.getTaskDescription(), taskDetails.getTaskCategory(), userUtilService.findUserId(taskDetails.getAssignedToOld()), userUtilService.findUserId(taskDetails.getAssignedBy()), Constants.TASK_IS_ACTIVE);
        if (null != task) {
            if (null == taskRepository.findByProjectIdAndTaskDescriptionAndTaskCategoryAndAssignedToAndActiveInd(projectId, taskDetails.getTaskDescription(), taskDetails.getTaskCategory(), userUtilService.findUserId(taskDetails.getAssignedToNew()), Constants.TASK_IS_ACTIVE)) {
                task.setAssignedTo(userUtilService.findUserId(taskDetails.getAssignedToNew()));
                task.setUpdatedTimestamp(new Timestamp(new Date().getTime()));
                logger.info(taskDetails.getTaskDescription() + Constants.TASK_CHANGED_FROM + taskDetails.getAssignedToOld() + " to " + taskDetails.getAssignedToNew());
                return setTaskDetails(taskRepository.save(task));
            }
            logger.info(taskDetails.getTaskDescription() + Constants.TASK_ALREADY_ASSIGNED_FOR + taskDetails.getAssignedToNew());
            return null;
        }
        logger.info(taskDetails.getTaskDescription() + Constants.TASK_NOT_ASSIGNED_FOR + taskDetails.getAssignedToOld());
        return null;
    }

    public TaskDetails updateTask(TaskDetails taskDetails, Integer projectId) throws NullPointerException, IOException {
        if (null != projectUtilService.findProjectById(projectId)) {
            task = taskRepository.findByProjectIdAndTaskId(projectId, taskDetails.getTaskId());
            if (null != task) {
                task.setTaskId(task.getTaskId());
                task.setTaskCategory((String) getValue(task.getTaskCategory(), taskDetails.getTaskCategory()));
                task.setTaskDescription((String) getValue(task.getTaskDescription(), taskDetails.getTaskDescription()));
                task.setPriority((String) getValue(task.getPriority(), taskDetails.getPriority()));
                task.setActiveInd((String) getValue(task.getActiveInd(), taskDetails.getActiveInd()));
                task.setFrequency((String) getValue(task.getFrequency(), taskDetails.getFrequency()));
                task.setRemarks((String) getValue(task.getRemarks(), taskDetails.getRemarks()));
                task.setFilePath((String) getValue(task.getFilePath(), taskDetails.getFilePath()));
                task.setStatus((String) getValue(task.getStatus(), taskDetails.getStatus()));
                task.setUpdatedTimestamp(new Timestamp(new Date().getTime()));
                task.setDueDate((Date) getValue(task.getDueDate(), taskDetails.getDueDate()));
                logger.info(taskDetails.getTaskDescription() + Constants.TASK_UPDATED_FOR + taskDetails.getAssignedTo());
                return setTaskDetails(taskRepository.save(task));
            }
        }
        logger.info(taskDetails.getTaskDescription() + Constants.TASK_NOT_ASSIGNED_FOR + taskDetails.getAssignedTo());
        return null;
    }

    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    public TaskDetails deleteTask(TaskDetails taskDetails, Integer projectId) throws NullPointerException {
        task = taskRepository.findByProjectIdAndTaskDescriptionAndTaskCategoryAndAssignedToAndAssignedBy(projectId, taskDetails.getTaskDescription(), taskDetails.getTaskCategory(), userUtilService.findUserId(taskDetails.getAssignedTo()), userUtilService.findUserId(taskDetails.getAssignedBy()));
        if (null != task) {
            taskRepository.delete(task);
            logger.info(taskDetails.getTaskDescription() + Constants.TASK_DELETED_FOR + taskDetails.getAssignedTo());
            return setTaskDetails(task);
        }
        logger.info(taskDetails.getTaskDescription() + Constants.TASK_NOT_ASSIGNED_FOR + taskDetails.getAssignedTo());
        return null;
    }

    public List<TaskDetails> viewActiveTask(Integer projectId) throws NullPointerException {
        return getTaskDetailsList(taskRepository.findAllByProjectIdAndActiveInd(projectId, Constants.TASK_IS_ACTIVE));
    }

    public List<TaskDetails> viewUserActiveTask(Integer projectId, Integer userId) throws NullPointerException {
        return null != projectUtilService.findProjectById(projectId) ? getTaskDetailsList(taskRepository.findAllByProjectIdAndAssignedToAndActiveInd(projectId, userId, Constants.TASK_IS_ACTIVE)) : null;
    }

    public List<TaskDetails> viewAllTask(Integer projectId) throws NullPointerException {
        return getTaskDetailsList(taskRepository.findAllByProjectId(projectId));
    }

    public List<TaskDetails> viewUserAllTask(Integer projectId, Integer userId) throws NullPointerException {
        return null != projectUtilService.findProjectById(projectId) ? getTaskDetailsList(taskRepository.findAllByProjectIdAndAssignedTo(projectId, userId)) : null;
    }

    private Object getValue(Object oldValue, Object newValue) {
        return null != newValue ? newValue : oldValue;
    }

    private List<TaskDetails> getTaskDetailsList(List<Task> taskList) {
        List<TaskDetails> taskResultList = new ArrayList<>();
        for (Task task1 : taskList) {
            taskResultList.add(setTaskDetails(task1));
        }
        return taskResultList;
    }

    private TaskDetails setTaskDetails(Task task) {
        if (null != task) {
            logger.info("Logged Date" + task.getLoggedDate());
            logger.info("Due Date" + task.getDueDate());
            TaskDetails taskDetails = new TaskDetails();
            taskDetails.setTaskId(task.getTaskId());
            taskDetails.setTaskDescription(task.getTaskDescription());
            taskDetails.setTaskCategory(task.getTaskCategory());
            taskDetails.setAssignedTo(userUtilService.findUserName(task.getAssignedTo()));
            taskDetails.setAssignedBy(userUtilService.findUserName(task.getAssignedBy()));
            taskDetails.setStatus(task.getStatus());
            taskDetails.setActiveInd(task.getActiveInd());
            taskDetails.setFilePath(task.getFilePath());
            taskDetails.setPriority(task.getPriority());
            taskDetails.setFrequency(task.getFrequency());
            taskDetails.setRemarks(task.getRemarks());
            taskDetails.setLoggedDate(task.getLoggedDate());
            taskDetails.setDueDate(task.getDueDate());
            return taskDetails;
        }
        return null;
    }

    public int fetchAssignedTo(Integer taskId, Integer projectId) throws NullPointerException {
        if (null != projectUtilService.findProjectById(projectId)) {
            task = taskRepository.findByProjectIdAndTaskId(projectId, taskId);
            if (null != task) {
                return task.getAssignedTo();
            }
        }
        return 0;
    }

    public List<TaskDetails> fetchDueTask(Integer projectId, long dueStartDays, long dueEndDays) {
        List<Task> taskDueList = taskRepository.findAllByProjectIdAndActiveInd(projectId, Constants.TASK_IS_ACTIVE);
        if (null != taskDueList) {
            taskDueList = taskDueList.stream().filter(val -> {
                if (!Objects.equals(val.getStatus(), Constants.TASK_STATUS_COMPLETED) && !Objects.equals(val.getStatus(), Constants.TASK_STATUS_CLOSED)) {
                    try {
                        logger.info(val.getTaskDescription() + Constants.DUE_DATE_FOUND_MSG + val.getDueDate());
                        return checkDueDate(val.getDueDate(), dueStartDays, dueEndDays);
                    } catch (ParseException e) {
                        logger.warning(Constants.DUE_DATE_NOT_FOUND_MSG + e.getMessage());
                        return false;
                    }
                }
                return false;
            }).collect(Collectors.toList());
            return getTaskDetailsList(taskDueList);
        }
        logger.warning(Constants.TASK_NOT_FOUND_MSG);
        return Collections.emptyList();
    }

    public List<TaskDetails> fetchUserDueTask(Integer projectId, Integer userId, long dueStartDays, long dueEndDays) {
        List<Task> taskList = taskRepository.findAllByProjectIdAndAssignedToAndActiveInd(projectId, userId, Constants.TASK_IS_ACTIVE);
        if (null != taskList) {
            taskList = taskList.stream().filter(val -> {
                if (!Objects.equals(val.getStatus(), Constants.TASK_STATUS_COMPLETED) && !Objects.equals(val.getStatus(), Constants.TASK_STATUS_CLOSED)) {
                    try {
                        logger.info(val.getTaskDescription() + Constants.DUE_DATE_FOUND_MSG + val.getDueDate());
                        return checkDueDate(val.getDueDate(), dueStartDays, dueEndDays);
                    } catch (ParseException e) {
                        logger.warning(Constants.DUE_DATE_NOT_FOUND_MSG + e.getMessage());
                        return false;
                    }
                }
                return false;
            }).collect(Collectors.toList());
            return getTaskDetailsList(taskList);
        }
        logger.warning(Constants.TASK_NOT_FOUND_MSG);
        return Collections.emptyList();
    }

    public List<TaskDetails> fetchCrossDueTask(Integer projectId) {
        List<Task> taskDueList = taskRepository.findAllByProjectIdAndActiveInd(projectId, Constants.TASK_IS_ACTIVE);
        if (null != taskDueList) {
            taskDueList = taskDueList.stream().filter(val -> {
                if (!Objects.equals(val.getStatus(), Constants.TASK_STATUS_COMPLETED) && !Objects.equals(val.getStatus(), Constants.TASK_STATUS_CLOSED)) {
                    logger.info(val.getTaskDescription() + Constants.DUE_DATE_FOUND_MSG + val.getDueDate());
                    return checkCrossDueDate(val.getDueDate());
                }
                return false;
            }).collect(Collectors.toList());
            return getTaskDetailsList(taskDueList);
        }
        logger.warning(Constants.TASK_NOT_FOUND_MSG);
        return Collections.emptyList();
    }

    public List<TaskDetails> fetchUserCrossDueTask(Integer projectId, Integer userId) {
        List<Task> taskList = taskRepository.findAllByProjectIdAndAssignedToAndActiveInd(projectId, userId, Constants.TASK_IS_ACTIVE);
        if (null != taskList) {
            taskList = taskList.stream().filter(val -> {
                if (!Objects.equals(val.getStatus(), Constants.TASK_STATUS_COMPLETED) && !Objects.equals(val.getStatus(), Constants.TASK_STATUS_CLOSED)) {
                    logger.info(val.getTaskDescription() + Constants.DUE_DATE_FOUND_MSG + val.getDueDate());
                    return checkCrossDueDate(val.getDueDate());
                }
                return false;
            }).collect(Collectors.toList());
            return getTaskDetailsList(taskList);
        }
        logger.warning(Constants.TASK_NOT_FOUND_MSG);
        return Collections.emptyList();
    }

    public boolean checkCrossDueDate(Date dueDate) {
        return isPastDate(formatter.format(dueDate));
    }

    public boolean checkDueDate(Date dueDate, long dueStartDays, long dueEndDays) throws ParseException {
        switch ((int) dueEndDays) {
            case 2:
            case 5:
            case 14:
                return dueStartDays <= taskDayDiff(dueDate) && dueEndDays >= taskDayDiff(dueDate);
            default:
                throw new IllegalStateException("Unexpected value: " + dueEndDays);
        }
    }

    public Long taskDayDiff(Date dueDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DUE_DATE_FORMAT, Locale.ENGLISH);
        return TimeUnit.DAYS.convert(Math.abs(dueDate.getTime() - sdf.parse(formatter.format(new Date())).getTime()), TimeUnit.MILLISECONDS);
    }

    public static boolean isPastDate(final String date) {
        LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Constants.DUE_DATE_FORMAT);
        LocalDate inputDate = LocalDate.parse(date, dtf);
        return inputDate.isBefore(localDate);
    }



}
