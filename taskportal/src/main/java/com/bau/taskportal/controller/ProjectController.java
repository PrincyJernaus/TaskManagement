package com.bau.taskportal.controller;

import com.bau.taskportal.bean.project.ProjectDetails;
import com.bau.taskportal.bean.project.RegionClass;
import com.bau.taskportal.bean.member.MemberDetails;
import com.bau.taskportal.bean.task.TaskDetails;
import com.bau.taskportal.bean.user.UserDetails;
import com.bau.taskportal.constant.Constants;
import com.bau.taskportal.entity.Project;
import com.bau.taskportal.entity.User;
import com.bau.taskportal.repository.ProjectRepository;
import com.bau.taskportal.repository.UserRepository;
import com.bau.taskportal.util.project.ProjectUtilService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class ProjectController {

    @Autowired
    ProjectUtilService projectUtilService;

    private ProjectDetails projectDetails;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    private Integer projectId;

    Logger logger = Logger.getLogger(ProjectController.class.getName());

    @PostMapping("/create/project")
    public ProjectDetails createProject(@RequestBody @NotNull RegionClass regionClass) {
        projectDetails = null;
        if (null == projectUtilService.findProjectByName(regionClass.getProjectName())) {
            try {
                projectDetails = projectUtilService.createNewProject(regionClass);
            } catch (NullPointerException e) {
                logger.warning(Constants.INTERNAL_ERROR_MSG + e.getMessage());
            }
            try {
                projectUtilService.assignManager(regionClass.getAssignedFor(), projectDetails);
            } catch (NullPointerException e) {
                logger.warning(Constants.INTERNAL_ERROR_MSG + e.getMessage());
                projectDetails = null;
            }
        }

        if (null == projectDetails) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return projectDetails;
    }

    @PostMapping("/update/project")
    public ProjectDetails updateProject(@RequestBody @NotNull Project project) {
        projectId = null;
        Project projectOld = null;
        try {
            projectId = project.getProjectId();
            projectOld = findProject(projectId);
        } catch (NullPointerException e) {
            logger.warning(Constants.PROJECT_NOT_FOUND_MSG + e.getMessage());
        }
        if (null != projectId) {
            logger.info(Constants.PROJECT_FOUND_MSG);
            projectDetails = new ProjectDetails();
            try {
                projectDetails = projectUtilService.updateProject(project);
            } catch (NullPointerException e) {
                logger.warning(Constants.INTERNAL_ERROR_MSG + e.getMessage());
            }

            logger.info("Old assigned For" + projectOld.getAssignedFor());
            logger.info("New assigned For" + project.getAssignedFor());

            if(projectOld.getAssignedFor() != project.getAssignedFor()) {
                logger.info("Assigned Manager Changed");
                try {
                    projectUtilService.assignManager(userRepository.findByUserId(project.getAssignedFor()).getUserName(), projectDetails);
                } catch (NullPointerException e) {
                    logger.warning(Constants.INTERNAL_ERROR_MSG + e.getMessage());
                    projectDetails = null;
                }
            }

        }
        if (null == projectDetails) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return projectDetails;
    }

    @PostMapping("/delete/project/{projectId}")
    public ProjectDetails deleteProject(@PathVariable Integer projectId) {
            try {
                projectDetails = projectUtilService.deleteProject(projectId);
            } catch (Exception e) {
                logger.warning(Constants.PROJECT_NOT_FOUND_MSG + e.getMessage());
            }

        return projectDetails;
    }

    @PostMapping("/assign/teamMembers")
    public List<UserDetails> assignTeamMembers(@RequestBody @NotNull RegionClass regionClass) {
        List<UserDetails> userDetailsList = new ArrayList<>();
        for (MemberDetails teamMember : regionClass.getTeamMembers()) {
            try {
                userDetailsList.add(projectUtilService.assignUser(teamMember, projectUtilService.findProjectByName(regionClass.getProjectName())));
            } catch (NullPointerException e) {
                logger.warning(Constants.INTERNAL_ERROR_MSG + e.getMessage());
            }
        }
        if (userDetailsList.isEmpty()) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return userDetailsList;
    }

    @PostMapping("/fetch/project")
    public ProjectDetails fetchProject(@RequestBody @NotNull RegionClass regionClass) {
        projectDetails = null;
        try {
            projectDetails = projectUtilService.fetchProject(regionClass);
        } catch (NullPointerException e) {
            logger.warning(Constants.INTERNAL_ERROR_MSG + e.getMessage());
        }
        if (null == projectDetails) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return projectDetails;
    }

    @GetMapping("/find/project/{projectId}")
    public Project findProject(@PathVariable Integer projectId) {
        Project projectDetails = null;
        try {
            projectDetails = projectRepository.findByProjectId(projectId);
        } catch (NullPointerException e) {
            logger.warning(Constants.INTERNAL_ERROR_MSG + e.getMessage());
        }
        return projectDetails;
    }

    @PostMapping("/fetch/all/project")
    public List<ProjectDetails> fetchAllProject(@RequestBody @NotNull RegionClass regionClass) {
        List<ProjectDetails> taskResultList = new ArrayList<>();
        try {
            System.out.println("Assigned For ::" + regionClass.getAssignedFor());
            taskResultList = projectUtilService.fetchAllProject(regionClass);
        } catch (NullPointerException e) {
            logger.warning(Constants.INTERNAL_ERROR_MSG + e.getMessage());
        }
        if (taskResultList.isEmpty()) logger.warning(Constants.REQUEST_NOT_VALID_MSG);
        return taskResultList;
    }

    @PostMapping("/fetch/admin/projects")
    public List<ProjectDetails> fetchAdminProjects(@RequestBody RegionClass regionClass) {
        List<ProjectDetails> taskResultList = new ArrayList<>();
        if (null != regionClass.getCreatedBy())
            taskResultList = projectUtilService.fetchAdminProjects(regionClass);
        return taskResultList;
    }

    @PostMapping("/fetch/manager/projects")
    public List<ProjectDetails> fetchManagerProjects(@RequestBody RegionClass regionClass) {
        List<ProjectDetails> taskResultList = new ArrayList<>();
        if (null != regionClass.getAssignedFor())
            taskResultList = projectUtilService.fetchManagerProjects(regionClass);
        return taskResultList;
    }

    @GetMapping("/projects/{managerName}")
    public List<Project> getProjectsForManager(@PathVariable String managerName) {
        System.out.println("Inside getProjectsForManager");
        User user = userRepository.findAllByUserName(managerName);
        List<Project> projectList = (List<Project>) projectUtilService.findByProject(user.getProjectId());
        System.out.println("projectList ::" + projectList.size());
        return projectList;
    }
}
