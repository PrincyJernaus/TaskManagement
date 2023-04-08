package com.bau.taskportal.repository;

import com.bau.taskportal.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Project findByProjectName(String projectName);

    Project save(Project project);

    Project findByProjectId(int projectId);

    Project findByProjectNameAndAssignedFor(String projectName, int assignedFor);

    List<Project> findAllByAssignedFor(int assignedFor);

    List<Project> findAllByCreatedBy(int createdBy);

}
