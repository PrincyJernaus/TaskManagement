package com.bau.taskportal.util.project;

import com.bau.taskportal.bean.member.MemberDetails;
import com.bau.taskportal.bean.project.ProjectDetails;
import com.bau.taskportal.bean.task.TaskDetails;
import com.bau.taskportal.bean.user.UserDetails;
import com.bau.taskportal.constant.Constants;
import com.bau.taskportal.entity.Project;
import com.bau.taskportal.bean.project.RegionClass;
import com.bau.taskportal.entity.Task;
import com.bau.taskportal.entity.User;
import com.bau.taskportal.repository.ProjectRepository;
import com.bau.taskportal.repository.UserRepository;
import com.bau.taskportal.util.task.TaskUtilService;
import com.bau.taskportal.util.user.UserUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ProjectUtilService {

    @Autowired
    UserUtilService userUtilService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    private Project project = null;

    Logger logger = Logger.getLogger(ProjectUtilService.class.getName());

    public void assignManager(String managerUName, ProjectDetails projectDetails) throws NullPointerException {
        logger.info("Manager Name" + managerUName);
        logger.info("Project Id" + projectDetails.getProjectId());
        User user = userUtilService.findUser(managerUName);
        user.setProjectId(projectDetails.getProjectId());
        userUtilService.saveUserDetails(user);
    }

    public UserDetails assignUser(MemberDetails memberDetails, ProjectDetails projectDetails) throws NullPointerException {
        User user = userUtilService.findUser(memberDetails.getUsername());
        user.setProjectId(projectDetails.getProjectId());
        return userUtilService.setUserDetails(userUtilService.saveUserDetails(user));
    }

    public ProjectDetails createNewProject(RegionClass regionClass) throws NullPointerException {
        Project project = new Project();
        project.setProjectName(regionClass.getProjectName());
        project.setDescription(regionClass.getProjectDesc());
        project.setStatus(Constants.TASK_IS_NEW);
        project.setCreatedTimestamp(new Timestamp(new Date().getTime()));
        project.setUpdatedTimestamp(new Timestamp(new Date().getTime()));
        project.setCreatedBy(userUtilService.findUserId(regionClass.getCreatedBy()));
        project.setAssignedFor(userUtilService.findUserId(regionClass.getAssignedFor()));
        return setProjectDetails(projectRepository.save(project));
    }

    public ProjectDetails updateProject(Project projectNew) throws NullPointerException {
        logger.info("Project id to update" + projectNew.getProjectId());
        if (null != findProjectById(projectNew.getProjectId())) {
            logger.info("Project is there" );
            project = projectRepository.findByProjectId(projectNew.getProjectId());
            logger.info("Project is there sas" + project);
            if (null != project) {
                project.setProjectId(projectNew.getProjectId());
                project.setProjectName(projectNew.getProjectName());
                project.setDescription(projectNew.getDescription());
                project.setAssignedFor(projectNew.getAssignedFor());
                return setProjectDetails(projectRepository.save(project));
            }
        }
        logger.info(project.getProjectName());
        return null;
    }

    public ProjectDetails deleteProject(int projectId) throws NullPointerException {
        logger.info("projectId :: " + projectId);
        Project project = projectRepository.findByProjectId(projectId);
        projectRepository.deleteById(projectId);
        logger.info(project.getDescription() + Constants.PROJECT_DELETED_FOR + project.getAssignedFor());
        return setProjectDetails(project);
    }

    public ProjectDetails fetchProject(RegionClass regionClass) throws NullPointerException {
        return setProjectDetails(projectRepository.findByProjectNameAndAssignedFor(regionClass.getProjectName(), userUtilService.findUserId(regionClass.getAssignedFor())));
    }

    public List<ProjectDetails> fetchAllProject(RegionClass regionClass) throws NullPointerException {
        return getProjectDetailsList(projectRepository.findAllByAssignedFor(userUtilService.findUserId(regionClass.getAssignedFor())));
    }

    public ProjectDetails findProjectByName(String projectName) throws NullPointerException {
        return setProjectDetails(projectRepository.findByProjectName(projectName));
    }

    public ProjectDetails findProjectById(int projectId) throws NullPointerException {
        return setProjectDetails(projectRepository.findByProjectId(projectId));
    }

    List<ProjectDetails> getProjectDetailsList(List<Project> projectList) {
        List<ProjectDetails> projectDetailsList = new ArrayList<>();
        for (Project project1 : projectList) {
            projectDetailsList.add(setProjectDetails(project1));
        }
        return projectDetailsList;
    }

    ProjectDetails setProjectDetails(Project project) {
        if (null != project) {
            ProjectDetails projectDetails = new ProjectDetails();
            projectDetails.setProjectId(project.getProjectId());
            projectDetails.setProjectName(project.getProjectName());
            projectDetails.setDescription(project.getDescription());
            projectDetails.setAssignedFor(userUtilService.findUserName(project.getAssignedFor()));
            projectDetails.setCreatedBy(userUtilService.findUserName(project.getCreatedBy()));
            projectDetails.setStatus(project.getStatus());
            projectDetails.setCreatedTimestamp(project.getCreatedTimestamp());
            projectDetails.setUpdatedTimestamp(project.getUpdatedTimestamp());
            return projectDetails;
        }
        return null;
    }

    public ProjectDetails findProject(String projectName) {
        return setProjectDetails(projectRepository.findByProjectName(projectName));
    }

    public List<Project> findByProject(Integer projectId)
    {
        return (List<Project>) projectRepository.findByProjectId(projectId);
    }

    public List<User> getUsersForProject(int projectId)
    {
        return userRepository.findByProjectIdAndRole(projectId,"user");
    }

    public List<ProjectDetails> fetchAdminProjects(RegionClass regionClass) {
        return getProjectDetailsList(projectRepository.findAllByCreatedBy(userUtilService.findUserId(regionClass.getCreatedBy())));
    }

    public List<ProjectDetails> fetchManagerProjects(RegionClass regionClass) {
        return getProjectDetailsList(projectRepository.findAllByAssignedFor(userUtilService.findUserId(regionClass.getAssignedFor())));
    }
}
