package com.bau.taskportal.repository;

import com.bau.taskportal.entity.ChangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangeHistoryRepository extends JpaRepository<ChangeHistory, Integer> {

}
