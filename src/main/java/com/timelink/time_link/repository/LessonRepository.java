package com.timelink.time_link.repository;

import com.timelink.time_link.model.Lesson;
import com.timelink.time_link.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    @Query("SELECT l FROM Lesson l WHERE " +
            "(l.originalTeacher.id = :teacherId OR l.actualTeacher.id = :teacherId) " +
            "AND l.lessonDate = :lessonDate " +
            "ORDER BY l.startTime")
    List<Lesson> findLessonsByTeacherAndDate(
            @Param("teacherId") Integer teacherId,
            @Param("lessonDate") LocalDate lessonDate
    );

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Lesson l WHERE " +
            "(l.originalTeacher.id = :teacherId OR l.actualTeacher.id = :teacherId) " +
            "AND l.lessonDate = :lessonDate " +
            "AND l.id != :excludeLessonId " +
            "AND (" +
            "  (l.startTime < :endTime AND l.endTime > :startTime)" +
            ")")
    boolean hasTimeConflict(
            @Param("teacherId") Integer teacherId,
            @Param("lessonDate") LocalDate lessonDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludeLessonId") Integer excludeLessonId
    );

    @Query("SELECT l FROM Lesson l WHERE l.group.id = :groupId AND l.lessonDate = :lessonDate ORDER BY l.startTime")
    List<Lesson> findLessonsByGroupAndDate(
            @Param("groupId") Integer groupId,
            @Param("lessonDate") LocalDate lessonDate
    );

    @Query("SELECT l FROM Lesson l WHERE l.course.id = :courseId ORDER BY l.lessonDate, l.startTime")
    List<Lesson> findLessonsByCourse(@Param("courseId") Integer courseId);

    @Query("SELECT l FROM Lesson l WHERE " +
            "l.status = 'SUBSTITUTE_PENDING' " +
            "AND l.lessonDate >= CURRENT_DATE " +
            "ORDER BY l.lessonDate, l.startTime")
    List<Lesson> findLessonsNeedingSubstitute();

    @Query("SELECT l FROM Lesson l WHERE " +
            "(l.originalTeacher.id = :teacherId OR l.actualTeacher.id = :teacherId) " +
            "AND l.lessonDate BETWEEN :startDate AND :endDate " +
            "ORDER BY l.lessonDate, l.startTime")
    List<Lesson> findLessonsByTeacherAndDateRange(
            @Param("teacherId") Integer teacherId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT l FROM Lesson l WHERE " +
            "l.actualTeacher.id = :teacherId " +
            "AND l.actualTeacher.id != l.originalTeacher.id")
    List<Lesson> findSubstituteLessons(@Param("teacherId") Integer teacherId);
}
