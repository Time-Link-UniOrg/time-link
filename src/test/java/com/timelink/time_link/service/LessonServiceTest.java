package com.timelink.time_link.service;

import com.timelink.time_link.exception.ResourceNotFoundException;
import com.timelink.time_link.exception.TimeConflictException;
import com.timelink.time_link.model.*;
import com.timelink.time_link.repository.CourseRepository;
import com.timelink.time_link.repository.GroupRepository;
import com.timelink.time_link.repository.LessonRepository;
import com.timelink.time_link.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("LessonService Unit Tests")
class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private LessonService lessonService;

    private Teacher teacher;
    private Group group;
    private Course course;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1);
        teacher.setName("John Smith");
        teacher.setEmail("john@example.com");

        group = new Group();
        group.setId(1);
        group.setName("Class 10A");

        course = new Course();
        course.setId(1);
        course.setName("Mathematics");

        lesson = new Lesson();
        lesson.setLessonDate(LocalDate.of(2024, 3, 15));
        lesson.setStartTime(LocalTime.of(9, 0));
        lesson.setEndTime(LocalTime.of(10, 30));
        lesson.setOriginalTeacher(teacher);
        lesson.setGroup(group);
        lesson.setCourse(course);
        lesson.setStatus(LessonStatus.SCHEDULED);
    }

    @Test
    @DisplayName("Should create lesson successfully")
    void testCreateLesson_Success() {
        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(groupRepository.findById(1)).thenReturn(Optional.of(group));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(lessonRepository.hasTimeConflict(anyInt(), any(), any(), any(), anyInt()))
                .thenReturn(false); // No conflict
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        Lesson createdLesson = lessonService.createLesson(lesson);

        assertThat(createdLesson).isNotNull();
        assertThat(createdLesson.getLessonDate()).isEqualTo(LocalDate.of(2024, 3, 15));
        assertThat(createdLesson.getStartTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(createdLesson.getEndTime()).isEqualTo(LocalTime.of(10, 30));
        assertThat(createdLesson.getStatus()).isEqualTo(LessonStatus.SCHEDULED);

        verify(teacherRepository).findById(1);
        verify(groupRepository).findById(1);
        verify(courseRepository).findById(1);
        verify(lessonRepository).hasTimeConflict(1,
                LocalDate.of(2024, 3, 15),
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                -1);
        verify(lessonRepository).save(lesson);
    }

    @Test
    @DisplayName("Should set default status to SCHEDULED when creating lesson")
    void testCreateLesson_DefaultStatus() {
        lesson.setStatus(null); // No status set

        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(groupRepository.findById(1)).thenReturn(Optional.of(group));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(lessonRepository.hasTimeConflict(anyInt(), any(), any(), any(), anyInt()))
                .thenReturn(false);
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> {
            Lesson savedLesson = invocation.getArgument(0);
            savedLesson.setId(1);
            return savedLesson;
        });

        Lesson createdLesson = lessonService.createLesson(lesson);

        assertThat(createdLesson.getStatus()).isEqualTo(LessonStatus.SCHEDULED);
    }

    @Test
    @DisplayName("Should throw exception when teacher not found")
    void testCreateLesson_TeacherNotFound() {
        when(teacherRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.createLesson(lesson))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Teacher")
                .hasMessageContaining("1");

        verify(lessonRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when group not found")
    void testCreateLesson_GroupNotFound() {
        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(groupRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.createLesson(lesson))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Group")
                .hasMessageContaining("1");

        verify(lessonRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when course not found")
    void testCreateLesson_CourseNotFound() {
        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(groupRepository.findById(1)).thenReturn(Optional.of(group));
        when(courseRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.createLesson(lesson))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Course")
                .hasMessageContaining("1");

        verify(lessonRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when end time is before start time")
    void testCreateLesson_InvalidTimes() {
        lesson.setStartTime(LocalTime.of(10, 30));
        lesson.setEndTime(LocalTime.of(9, 0)); // End before start!

        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(groupRepository.findById(1)).thenReturn(Optional.of(group));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(lessonRepository.hasTimeConflict(anyInt(), any(), any(), any(), anyInt()))
                .thenReturn(false);

        assertThatThrownBy(() -> lessonService.createLesson(lesson))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("End time must be after start time");

        verify(lessonRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw TimeConflictException when teacher has conflicting lesson")
    void testCreateLesson_TimeConflict() {
        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(groupRepository.findById(1)).thenReturn(Optional.of(group));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(lessonRepository.hasTimeConflict(
                1,
                LocalDate.of(2024, 3, 15),
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                -1
        )).thenReturn(true); // Conflict exists!

        assertThatThrownBy(() -> lessonService.createLesson(lesson))
                .isInstanceOf(TimeConflictException.class)
                .hasMessageContaining("Teacher has a conflicting lesson")
                .hasMessageContaining("2024-03-15")
                .hasMessageContaining("09:00")
                .hasMessageContaining("10:30");

        verify(lessonRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should detect time conflict - exact overlap")
    void testTimeConflict_ExactOverlap() {
        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(groupRepository.findById(1)).thenReturn(Optional.of(group));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(lessonRepository.hasTimeConflict(1,
                LocalDate.of(2024, 3, 15),
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                -1))
                .thenReturn(true);

        assertThatThrownBy(() -> lessonService.createLesson(lesson))
                .isInstanceOf(TimeConflictException.class);
    }

    @Test
    @DisplayName("Should detect time conflict - partial overlap (start)")
    void testTimeConflict_PartialOverlapStart() {
        lesson.setStartTime(LocalTime.of(8, 0));
        lesson.setEndTime(LocalTime.of(9, 30));

        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(groupRepository.findById(1)).thenReturn(Optional.of(group));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(lessonRepository.hasTimeConflict(1,
                LocalDate.of(2024, 3, 15),
                LocalTime.of(8, 0),
                LocalTime.of(9, 30),
                -1))
                .thenReturn(true);

        assertThatThrownBy(() -> lessonService.createLesson(lesson))
                .isInstanceOf(TimeConflictException.class);
    }

    @Test
    @DisplayName("Should detect time conflict - partial overlap (end)")
    void testTimeConflict_PartialOverlapEnd() {
        lesson.setStartTime(LocalTime.of(10, 0));
        lesson.setEndTime(LocalTime.of(11, 0));

        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(groupRepository.findById(1)).thenReturn(Optional.of(group));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(lessonRepository.hasTimeConflict(1,
                LocalDate.of(2024, 3, 15),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                -1))
                .thenReturn(true);

        assertThatThrownBy(() -> lessonService.createLesson(lesson))
                .isInstanceOf(TimeConflictException.class);
    }

    @Test
    @DisplayName("Should detect time conflict - new lesson contains existing")
    void testTimeConflict_NewContainsExisting() {
        lesson.setStartTime(LocalTime.of(9, 0));
        lesson.setEndTime(LocalTime.of(11, 0));

        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(groupRepository.findById(1)).thenReturn(Optional.of(group));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(lessonRepository.hasTimeConflict(1,
                LocalDate.of(2024, 3, 15),
                LocalTime.of(9, 0),
                LocalTime.of(11, 0),
                -1))
                .thenReturn(true);

        assertThatThrownBy(() -> lessonService.createLesson(lesson))
                .isInstanceOf(TimeConflictException.class);
    }

    @Test
    @DisplayName("Should NOT detect conflict when lessons are back-to-back")
    void testNoTimeConflict_BackToBack() {
        lesson.setStartTime(LocalTime.of(10, 30));
        lesson.setEndTime(LocalTime.of(12, 0));

        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(groupRepository.findById(1)).thenReturn(Optional.of(group));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(lessonRepository.hasTimeConflict(1,
                LocalDate.of(2024, 3, 15),
                LocalTime.of(10, 30),
                LocalTime.of(12, 0),
                -1))
                .thenReturn(false); // No conflict

        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        Lesson createdLesson = lessonService.createLesson(lesson);

        assertThat(createdLesson).isNotNull();
        verify(lessonRepository).save(lesson);
    }

    @Test
    @DisplayName("Should NOT detect conflict on different dates")
    void testNoTimeConflict_DifferentDates() {
        lesson.setLessonDate(LocalDate.of(2024, 3, 16)); // Different day

        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(groupRepository.findById(1)).thenReturn(Optional.of(group));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(lessonRepository.hasTimeConflict(1,
                LocalDate.of(2024, 3, 16),
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                -1))
                .thenReturn(false); // No conflict (different date)

        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        Lesson createdLesson = lessonService.createLesson(lesson);

        assertThat(createdLesson).isNotNull();
        verify(lessonRepository).save(lesson);
    }

    @Test
    @DisplayName("Should delete lesson successfully")
    void testDeleteLesson_Success() {
        when(lessonRepository.existsById(1)).thenReturn(true);

        lessonService.deleteLesson(1);

        verify(lessonRepository).existsById(1);
        verify(lessonRepository).deleteById(1);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent lesson")
    void testDeleteLesson_NotFound() {

        when(lessonRepository.existsById(999)).thenReturn(false);

        assertThatThrownBy(() -> lessonService.deleteLesson(999))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Lesson")
                .hasMessageContaining("999");

        verify(lessonRepository, never()).deleteById(anyInt());
    }
}