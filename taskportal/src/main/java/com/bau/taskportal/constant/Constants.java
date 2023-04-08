package com.bau.taskportal.constant;

public class Constants {
    private Constants() {
        throw new IllegalStateException("Constant class");
    }

    public static final String TASK_IS_ACTIVE = "Active";

    public static final String TASK_STATUS_COMPLETED = "Completed";
    public static final String TASK_STATUS_CLOSED = "Closed";
    public static final String TASK_IS_NEW = "New";
    public static final String USER_FOUND_PROJECT_MSG = "User found with project";
    public static final String PROJECT_FOUND_MSG = "Project found";

    public static final String DUE_DATE_FORMAT = "yyyy-MM-dd";

    public static final String REQUEST_NOT_VALID_MSG = "Please check you request!!! Requested not valid ";
    public static final String INTERNAL_ERROR_MSG = "Internal Error!!! ";
    public static final String TASK_NOT_FOUND_MSG = "Task not found!!! ";
    public static final String PROJECT_NOT_FOUND_MSG = "Project not found!!! ";
    public static final String USER_NOT_FOUND_MSG = "User not found!!! ";
    public static final String TASK_NOT_ASSIGNED_FOR = " task not assigned for ";
    public static final String TASK_ALREADY_ASSIGNED_FOR = " task already assigned for ";
    public static final String TASK_CREATING_FOR = " task creating for ";
    public static final String TASK_CHANGED_FROM = " task changed from ";
    public static final String TASK_UPDATED_FOR = " task updated for ";
    public static final String TASK_DELETED_FOR = " task deleted for ";

    public static final String TASK_WARNING_MSG1 ="Task not found!!! ";
    public static final String TASK_WARNING_MSG2 ="Project Not found!!! ";
    public static final String TASK_WARNING_MSG3 ="User Not found!!! ";

    public static final String DUE_DATE_NOT_FOUND_MSG = "Due date not found!!! ";
    public static final String DUE_DATE_FOUND_MSG = " Due date found :";

    public static final String PROJECT_DELETED_FOR = " project deleted for ";
}
