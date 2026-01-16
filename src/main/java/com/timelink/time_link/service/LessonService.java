package com.timelink.time_link.service;

import com.timelink.time_link.exception.ResourceNotFoundException;
import com.timelink.time_link.exception.TimeConflictException;
import com.timelink.time_link.model.*;
import com.timelink.time_link.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LessonService {

    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public Lesson createLesson(Lesson lesson) {
        validateLessonReferences(lesson);

        checkTimeConflict(
                lesson.getOriginalTeacher().getId(),
                lesson.getLessonDate(),
                lesson.getStartTime(),
                lesson.getEndTime(),
                null
        );

        validateLessonTimes(lesson);

        if (lesson.getStatus() == null) {
            lesson.setStatus(LessonStatus.SCHEDULED);
        }

        return lessonRepository.save(lesson);
    }

    @Transactional
    public Lesson updateLesson(Integer id, Lesson updatedLesson) {
        Lesson existingLesson = getLessonOrThrow(id);

        validateLessonReferences(updatedLesson);

        Integer teacherId = updatedLesson.getOriginalTeacher() != null
                ? updatedLesson.getOriginalTeacher().getId()
                : existingLesson.getOriginalTeacher().getId();

        checkTimeConflict(
                teacherId,
                updatedLesson.getLessonDate(),
                updatedLesson.getStartTime(),
                updatedLesson.getEndTime(),
                id
        );


        validateLessonTimes(updatedLesson);


        existingLesson.setLessonDate(updatedLesson.getLessonDate());
        existingLesson.setStartTime(updatedLesson.getStartTime());
        existingLesson.setEndTime(updatedLesson.getEndTime());
        existingLesson.setOriginalTeacher(updatedLesson.getOriginalTeacher());
        existingLesson.setActualTeacher(updatedLesson.getActualTeacher());
        existingLesson.setGroup(updatedLesson.getGroup());
        existingLesson.setCourse(updatedLesson.getCourse());
        existingLesson.setNotes(updatedLesson.getNotes());
        existingLesson.setStatus(updatedLesson.getStatus());

        return lessonRepository.save(existingLesson);
    }

    @Transactional
    public void deleteLesson(Integer id) {
        if (!lessonRepository.existsById(id)) {
            throw new ResourceNotFoundException(Lesson.class, id);
        }
        lessonRepository.deleteById(id);
    }

    public Optional<Lesson> getLessonById(Integer id) {
        return lessonRepository.findById(id);
    }

    public Lesson getLessonOrThrow(Integer id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Lesson.class, id));
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    public List<Lesson> getLessonsByTeacherAndDate(Integer teacherId, LocalDate date) {
        return lessonRepository.findLessonsByTeacherAndDate(teacherId, date);
    }

    public List<Lesson> getLessonsByGroupAndDate(Integer groupId, LocalDate date) {
        return lessonRepository.findLessonsByGroupAndDate(groupId, date);
    }

    public List<Lesson> getLessonsNeedingSubstitute() {
        return lessonRepository.findLessonsNeedingSubstitute();
    }

    @Transactional
    public Lesson assignSubstitute(Integer lessonId, Integer substituteTeacherId) {
        Lesson lesson = getLessonOrThrow(lessonId);
        Teacher substitute = teacherRepository.findById(substituteTeacherId)
                .orElseThrow(() -> new ResourceNotFoundException(Teacher.class, substituteTeacherId));

        checkTimeConflict(
                substituteTeacherId,
                lesson.getLessonDate(),
                lesson.getStartTime(),
                lesson.getEndTime(),
                lessonId
        );

        lesson.setActualTeacher(substitute);
        lesson.setStatus(LessonStatus.SUBSTITUTE_CONFIRMED);

        return lessonRepository.save(lesson);
    }

    @Transactional
    public Lesson cancelLesson(Integer lessonId) {
        Lesson lesson = getLessonOrThrow(lessonId);
        lesson.setStatus(LessonStatus.CANCELLED);
        return lessonRepository.save(lesson);
    }

    @Transactional
    public Lesson completeLesson(Integer lessonId) {
        Lesson lesson = getLessonOrThrow(lessonId);
        lesson.setStatus(LessonStatus.COMPLETED);
        return lessonRepository.save(lesson);
    }

    private void checkTimeConflict(
            Integer teacherId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            Integer excludeLessonId
    ) {
        Integer excludeId = excludeLessonId != null ? excludeLessonId : -1;

        boolean hasConflict = lessonRepository.hasTimeConflict(
                teacherId,
                date,
                startTime,
                endTime,
                excludeId
        );

        if (hasConflict) {
            throw new TimeConflictException(
                    "Teacher has a conflicting lesson on " + date +
                            " between " + startTime + " and " + endTime
            );
        }
    }

    private void validateLessonTimes(Lesson lesson) {
        if (lesson.getEndTime().isBefore(lesson.getStartTime()) ||
                lesson.getEndTime().equals(lesson.getStartTime())) {
            throw new IllegalArgumentException(
                    "End time must be after start time"
            );
        }
    }

    private void validateLessonReferences(Lesson lesson) {
        if (lesson.getOriginalTeacher() != null && lesson.getOriginalTeacher().getId() != null) {
            teacherRepository.findById(lesson.getOriginalTeacher().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            Teacher.class, lesson.getOriginalTeacher().getId()
                    ));
        }

        if (lesson.getGroup() != null && lesson.getGroup().getId() != null) {
            groupRepository.findById(lesson.getGroup().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            Group.class, lesson.getGroup().getId()
                    ));
        }

        if (lesson.getCourse() != null && lesson.getCourse().getId() != null) {
            courseRepository.findById(lesson.getCourse().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            Course.class, lesson.getCourse().getId()
                    ));
        }
    }
}