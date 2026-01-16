package com.timelink.time_link.repository;

import com.timelink.time_link.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    boolean existsByUsername(String username);

    Optional<Teacher> findByUsername(String username);

    @Query("""
        SELECT DISTINCT t
        FROM Teacher t
        JOIN t.qualifiedCourses c
        WHERE c.id = :courseId
          AND NOT EXISTS (
              SELECT 1
              FROM Lesson l
              WHERE (l.originalTeacher = t OR l.actualTeacher = t)
                AND l.lessonDate = :lessonDate
                AND (l.startTime < :endTime AND l.endTime > :startTime)
          )
        """)
    List<Teacher> findQualifiedAndAvailableTeachers(
            @Param("courseId") Integer courseId,
            @Param("lessonDate") LocalDate lessonDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
