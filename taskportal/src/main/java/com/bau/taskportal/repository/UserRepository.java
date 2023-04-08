package com.bau.taskportal.repository;

import com.bau.taskportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findAllByUserName(String userName);

    User findByUserName(String userName);

    User findByUserId(int userId);

    User save(User user);

    List<User> findAllByRole(String role);

    List<User> findAllByProjectId(Integer projectId);

    List<User> findByProjectIdAndRole(Integer projectId, String role);

}
