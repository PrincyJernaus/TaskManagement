package com.bau.taskportal.util.report;

import com.bau.taskportal.bean.task.TaskDetails;
import com.bau.taskportal.util.user.UserUtilService;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ReportUtilService {
    Logger logger = Logger.getLogger(ReportUtilService.class.getName());

    @Autowired
    UserUtilService userUtilService;

    public void exportCsvData(List<TaskDetails> taskList) throws IOException {
        String home = System.getProperty("user.home");

        File file = new File(home + "/Downloads/report_" + String.valueOf(new Date().getTime()).substring(9) + ".csv");
        // create FileWriter object with file as parameter
        FileWriter outputfile = new FileWriter(file);
        // create CSVWriter object filewriter object as parameter
        CSVWriter writer = new CSVWriter(outputfile);
        // adding header to csv
        String[] header = {"Task Description", "Task Category", "Assigned To", "Task Status", "Active Indicator", "FilePath", "Priority",
                "Frequency", "Logged Date", "Due Date", "Efforts", "Remarks", "Project Name", "Manager Name", "Task Status"};
        writer.writeNext(header);
        taskList.forEach(taskDetails -> {
            String[] data = {taskDetails.getTaskDescription(), taskDetails.getTaskCategory(), taskDetails.getAssignedTo(),
                    taskDetails.getStatus(), taskDetails.getActiveInd(), taskDetails.getFilePath(), taskDetails.getPriority(), taskDetails.getFrequency(), String.valueOf(taskDetails.getLoggedDate()), String.valueOf(taskDetails.getDueDate()),
                    taskDetails.getEfforts() != null ? String.valueOf(taskDetails.getEfforts()) : "",
                    taskDetails.getRemarks(), taskDetails.getProjectName(), taskDetails.getManagerName(), taskDetails.getTaskStatus()};
            writer.writeNext(data);
        });
        writer.close();
    }
}
