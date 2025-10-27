package com.timelink.time_link.repository;

import com.timelink.time_link.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    Optional<Teacher> findByUsername(String username);

    boolean existsByUsername(String username);
}