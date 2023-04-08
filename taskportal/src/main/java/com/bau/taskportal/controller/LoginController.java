package com.bau.taskportal.controller;

import com.bau.taskportal.bean.member.MemberDetails;
import com.bau.taskportal.bean.project.ProjectDetails;
import com.bau.taskportal.bean.project.RegionClass;
import com.bau.taskportal.bean.user.UserDetails;
import com.bau.taskportal.constant.Constants;
import com.bau.taskportal.entity.User;
import com.bau.taskportal.repository.ProjectRepository;
import com.bau.taskportal.repository.UserRepository;
import com.bau.taskportal.util.project.ProjectUtilService;
import com.bau.taskportal.util.user.UserUtilService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class LoginController {

    Logger logger = Logger.getLogger(LoginController.class.getName());
    private final static String MANAGER_ROLE = "manager";
    private final static String USER_ROLE = "user";
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserUtilService userUtilService;

    @Autowired
    private ProjectUtilService projectUtilService;

    @Autowired
    private ProjectRepository projectRepository;

    private UserDetails userDetails;

    private User user;

    @GetMapping("/loginValidate/{username}")
    public User userValidate(@PathVariable String username) {
        logger.info("username :: " + username);
        User user = userRepository.findAllByUserName(username);
        return user;
    }

    @PostMapping("/register")
    public UserDetails registerUser(@RequestBody @NotNull MemberDetails memberDetails) {
        logger.info("username :: " + memberDetails.getUsername());
        userDetails = null;
        try {
            userDetails = userUtilService.createNewUser(memberDetails);
        } catch (NullPointerException e) {
            logger.warning(Constants.INTERNAL_ERROR_MSG + e.getMessage());
        }
        return userDetails;
    }

    @GetMapping("/managers")
    public List<User> getAllManagers() {
        logger.info("Inside getAllManagers");
        List<User> managersList = (List<User>) userRepository.findAllByRole(MANAGER_ROLE);
        logger.info("managersList ::" + managersList.size());
        return managersList;
    }

    @GetMapping("/users")
    public List<User> getAllMembers() {
        logger.info("Inside getAllMembers");
        List<User> usersList = (List<User>) userRepository.findAllByRole(USER_ROLE);
        logger.info("usersList ::" + usersList.size());
        return usersList;
    }

    @GetMapping("/users/{projectName}")
    public List<User> getUserForProject(@PathVariable String projectName) {
        logger.info("Inside getUserForProject");
        Integer projectId = projectRepository.findByProjectName(projectName).getProjectId();
        logger.info("project Id :: " + projectId);
        List<User> users = projectUtilService.getUsersForProject(projectId);
        logger.info("users assigned ::" + users.size() + "for project :: " + projectName);
        return users;
    }

    @GetMapping("/unassignedUsers")
    public List<User> getUnassignedUsers() {
        logger.info("Inside getUnassignedUsers");
        List<User> unassignedUsers = null;
        List<User> users = userRepository.findAllByRole("user");
        unassignedUsers = users.stream().filter(user -> Objects.isNull(user.getProjectId())).collect(Collectors.toList());
        logger.info("users unassigned ::" + unassignedUsers.size());
        return unassignedUsers;
    }

}
