package com.timelink.time_link.service;

import com.timelink.time_link.dto.Substitute.SubstituteOpportunityDTO;
import com.timelink.time_link.exception.ResourceNotFoundException;
import com.timelink.time_link.mapper.SubstituteRequestMapper;
import com.timelink.time_link.model.Course;
import com.timelink.time_link.model.Lesson;
import com.timelink.time_link.model.Teacher;
import com.timelink.time_link.repository.LessonRepository;
import com.timelink.time_link.repository.SubstituteRequestRepository;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SubstituteService Unit Tests")
class SubstituteServiceTest {

    @Mock
    private SubstituteRequestRepository substituteRequestRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private SubstituteRequestMapper substituteRequestMapper;

    @InjectMocks
    private SubstituteService substituteService;

    private Course course;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(1);
        course.setName("Mathematics");

        lesson = new Lesson();
        lesson.setId(1);
        lesson.setCourse(course);
        lesson.setLessonDate(LocalDate.of(2026, 1, 15));
        lesson.setStartTime(LocalTime.of(10, 0));
        lesson.setEndTime(LocalTime.of(11, 0));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when lesson not found")
    void testFindAvailableTeachers_LessonNotFound() {
        when(lessonRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> substituteService.findAvailableTeachers(999))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Lesson")
                .hasMessageContaining("999");

        verify(teacherRepository, never()).findQualifiedAndAvailableTeachers(anyInt(), any(), any(), any());
        verify(substituteRequestMapper, never()).toOpportunityDTOList(any(), any());
    }

    @Test
    @DisplayName("Should find qualified and available teachers and map to OpportunityDTOs")
    void testFindAvailableTeachers_Success() {
        // given
        Teacher t1 = new Teacher();
        t1.setId(1);
        t1.setName("Teacher One");
        t1.setUsername("t1");
        t1.setPassword("pass");

        Teacher t2 = new Teacher();
        t2.setId(2);
        t2.setName("Teacher Two");
        t2.setUsername("t2");
        t2.setPassword("pass");

        List<Teacher> qualifiedAndAvailable = List.of(t1, t2);

        List<SubstituteOpportunityDTO> expectedDtos =
                List.of(
                        mock(SubstituteOpportunityDTO.class),
                        mock(SubstituteOpportunityDTO.class)
                );


        when(lessonRepository.findById(1)).thenReturn(Optional.of(lesson));

        // репото вече "филтрира" qualified + свободни (busy filter е вътре в query)
        when(teacherRepository.findQualifiedAndAvailableTeachers(
                1,
                LocalDate.of(2026, 1, 15),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        )).thenReturn(qualifiedAndAvailable);

        when(substituteRequestMapper.toOpportunityDTOList(lesson, qualifiedAndAvailable))
                .thenReturn(expectedDtos);

        // when
        List<SubstituteOpportunityDTO> result = substituteService.findAvailableTeachers(1);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).isSameAs(expectedDtos);

        verify(lessonRepository).findById(1);
        verify(teacherRepository).findQualifiedAndAvailableTeachers(
                1,
                LocalDate.of(2026, 1, 15),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );
        verify(substituteRequestMapper).toOpportunityDTOList(lesson, qualifiedAndAvailable);
    }

    @Test
    @DisplayName("Should return empty list when no qualified/available teachers exist")
    void testFindAvailableTeachers_NoTeachers() {
        when(lessonRepository.findById(1)).thenReturn(Optional.of(lesson));
        when(teacherRepository.findQualifiedAndAvailableTeachers(
                anyInt(), any(), any(), any()
        )).thenReturn(List.of());

        when(substituteRequestMapper.toOpportunityDTOList(eq(lesson), eq(List.of())))
                .thenReturn(List.of());

        List<SubstituteOpportunityDTO> result = substituteService.findAvailableTeachers(1);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(teacherRepository).findQualifiedAndAvailableTeachers(
                1,
                LocalDate.of(2026, 1, 15),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );
        verify(substituteRequestMapper).toOpportunityDTOList(lesson, List.of());
    }
}
