package com.timelink.time_link.repository;

import com.timelink.time_link.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
