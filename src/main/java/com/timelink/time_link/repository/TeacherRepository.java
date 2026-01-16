package com.timelink.time_link.repository;

import com.timelink.time_link.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    Optional<Teacher> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<Teacher> findByEmail(String email);

    @Query("SELECT t FROM Teacher t JOIN t.qualifiedCourses c WHERE c.id = :courseId")
    List<Teacher> findQualifiedForCourse(@Param("courseId") Integer courseId);

    @Query("SELECT t FROM Teacher t " +
            "JOIN t.qualifiedCourses c " +
            "WHERE c.id = :courseId " +
            "AND t.id NOT IN (" +
            "  SELECT l.originalTeacher.id FROM Lesson l " +
            "  WHERE l.lessonDate = :lessonDate " +
            "  AND l.startTime < :endTime " +
            "  AND l.endTime > :startTime" +
            ") " +
            "AND t.id NOT IN (" +
            "  SELECT l.actualTeacher.id FROM Lesson l " +
            "  WHERE l.actualTeacher IS NOT NULL " +
            "  AND l.lessonDate = :lessonDate " +
            "  AND l.startTime < :endTime " +
            "  AND l.endTime > :startTime" +
            ")")
    List<Teacher> findAvailableQualifiedTeachers(
            @Param("courseId") Integer courseId,
            @Param("lessonDate") LocalDate lessonDate,
            @Param("startTime") java.time.LocalTime startTime,
            @Param("endTime") java.time.LocalTime endTime
    );
}