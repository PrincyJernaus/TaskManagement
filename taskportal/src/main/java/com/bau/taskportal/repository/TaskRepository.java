package com.bau.taskportal.repository;

import com.bau.taskportal.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAllByAssignedTo(int assignedTo);

    List<Task> findAllByProjectId(int projectId);

    List<Task> findAllByProjectIdAndAssignedTo(int projectId, int assignedTo);

    List<Task> findAllByProjectIdAndActiveInd(int projectId, String activeInd);

    List<Task> findAllByProjectIdAndAssignedToAndActiveInd(int projectId, int assignedTo, String activeInd);

    Task findByProjectIdAndTaskDescriptionAndTaskCategoryAndAssignedToAndActiveInd(int projectId, String taskDescription, String taskCategory, int assignedTo, String ActiveInd);

    Task findByProjectIdAndTaskDescriptionAndTaskCategoryAndAssignedToAndAssignedByAndActiveInd(int projectId, String taskDescription, String taskCategory, int assignedTo, int assignedBy, String ActiveInd);

    Task findByProjectIdAndTaskDescriptionAndTaskCategoryAndAssignedToAndAssignedBy(int projectId, String taskDescription, String taskCategory, int assignedTo, int assignedBy);

    Task findByProjectIdAndTaskId(int projectId, int taskId);

}
