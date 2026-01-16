package com.timelink.time_link.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timelink.time_link.dto.Substitute.SubstituteRequestDTO;
import com.timelink.time_link.dto.Substitute.SubstituteResponseDTO;
import com.timelink.time_link.dto.Lesson.LessonRequestDTO;
import com.timelink.time_link.dto.Lesson.LessonResponseDTO;
import com.timelink.time_link.model.*;
import com.timelink.time_link.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Substitute Flow Integration Test")
class SubstituteFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private SubstituteRequestRepository substituteRequestRepository;

    private Teacher originalTeacher;
    private Teacher substituteTeacher;
    private Course course;
    private Group group;

    @BeforeEach
    void setUp() {
        substituteRequestRepository.deleteAll();
        lessonRepository.deleteAll();
        teacherRepository.deleteAll();
        courseRepository.deleteAll();
        groupRepository.deleteAll();

        originalTeacher = new Teacher();
        originalTeacher.setName("John Smith");
        originalTeacher.setEmail("john@school.com");
        originalTeacher.setPhone("123456789");
        originalTeacher.setUsername("john_smith");
        originalTeacher.setPassword("password123");
        originalTeacher = teacherRepository.save(originalTeacher);

        substituteTeacher = new Teacher();
        substituteTeacher.setName("Jane Doe");
        substituteTeacher.setEmail("jane@school.com");
        substituteTeacher.setPhone("987654321");
        substituteTeacher.setUsername("jane_doe");
        substituteTeacher.setPassword("password456");
        substituteTeacher = teacherRepository.save(substituteTeacher);

        course = new Course();
        course.setName("Advanced Mathematics");
        course.setPrice(new BigDecimal("299.99"));
        course.setAge(16);
        course.setTimePeriod(12);
        course = courseRepository.save(course);

        originalTeacher.addQualifiedCourse(course);
        substituteTeacher.addQualifiedCourse(course);
        teacherRepository.save(originalTeacher);
        teacherRepository.save(substituteTeacher);

        group = new Group();
        group.setName("Class 10A");
        group.setActive(true);
        group = groupRepository.save(group);
    }

    @Test
    @DisplayName("Complete Substitute Flow - Should successfully substitute a teacher")
    void testCompleteSubstituteFlow() throws Exception {
        LocalDate lessonDate = LocalDate.now().plusDays(1);
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(10, 30);

        LessonRequestDTO lessonRequest = new LessonRequestDTO(
                lessonDate,
                startTime,
                endTime,
                originalTeacher.getId(),
                null,
                group.getId(),
                course.getId(),
                "Introduction to Calculus",
                LessonStatus.SCHEDULED
        );

        MvcResult lessonResult = mockMvc.perform(post("/api/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lessonRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.originalTeacherId").value(originalTeacher.getId()))
                .andExpect(jsonPath("$.actualTeacherId").doesNotExist())
                .andExpect(jsonPath("$.status").value("SCHEDULED"))
                .andReturn();

        LessonResponseDTO createdLesson = objectMapper.readValue(
                lessonResult.getResponse().getContentAsString(),
                LessonResponseDTO.class
        );
        Integer lessonId = createdLesson.id();

        assertThat(lessonId).isNotNull();
        assertThat(createdLesson.originalTeacherId()).isEqualTo(originalTeacher.getId());
        assertThat(createdLesson.actualTeacherId()).isNull();
        assertThat(createdLesson.status()).isEqualTo(LessonStatus.SCHEDULED);

        SubstituteRequestDTO substituteRequest = new SubstituteRequestDTO(lessonId);

        MvcResult requestResult = mockMvc.perform(post("/api/substitute-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(substituteRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.requestId").exists())
                .andExpect(jsonPath("$.lessonId").value(lessonId))
                .andExpect(jsonPath("$.originalTeacherId").value(originalTeacher.getId()))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn();

        SubstituteResponseDTO createdRequest = objectMapper.readValue(
                requestResult.getResponse().getContentAsString(),
                SubstituteResponseDTO.class
        );
        Integer requestId = createdRequest.requestId();

        assertThat(requestId).isNotNull();
        assertThat(createdRequest.lessonId()).isEqualTo(lessonId);
        assertThat(createdRequest.originalTeacherId()).isEqualTo(originalTeacher.getId());
        assertThat(createdRequest.assignedTeacherId()).isNull();
        assertThat(createdRequest.status()).isEqualTo(SubstituteRequestStatus.PENDING);

        mockMvc.perform(get("/api/substitute-requests/available")
                        .param("lessonId", lessonId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].teacherId").exists())
                .andExpect(jsonPath("$[*].teacherName").exists())
                .andExpect(jsonPath("$[*].courseName").value("Advanced Mathematics"));

        MvcResult acceptResult = mockMvc.perform(post("/api/substitute-requests/{id}/accept", requestId)
                        .param("teacherId", substituteTeacher.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value(requestId))
                .andExpect(jsonPath("$.assignedTeacherId").value(substituteTeacher.getId()))
                .andExpect(jsonPath("$.status").value("ACCEPTED"))
                .andReturn();

        SubstituteResponseDTO acceptedRequest = objectMapper.readValue(
                acceptResult.getResponse().getContentAsString(),
                SubstituteResponseDTO.class
        );

        assertThat(acceptedRequest.requestId()).isEqualTo(requestId);
        assertThat(acceptedRequest.assignedTeacherId()).isEqualTo(substituteTeacher.getId());
        assertThat(acceptedRequest.status()).isEqualTo(SubstituteRequestStatus.ACCEPTED);

        MvcResult verifyResult = mockMvc.perform(get("/api/lessons/{id}", lessonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(lessonId))
                .andExpect(jsonPath("$.originalTeacherId").value(originalTeacher.getId()))
                .andExpect(jsonPath("$.actualTeacherId").value(substituteTeacher.getId()))
                .andExpect(jsonPath("$.status").value("SUBSTITUTE_CONFIRMED"))
                .andExpect(jsonPath("$.hasSubstitute").value(true))
                .andReturn();

        LessonResponseDTO updatedLesson = objectMapper.readValue(
                verifyResult.getResponse().getContentAsString(),
                LessonResponseDTO.class
        );

        assertThat(updatedLesson.id()).isEqualTo(lessonId);
        assertThat(updatedLesson.originalTeacherId()).isEqualTo(originalTeacher.getId());
        assertThat(updatedLesson.originalTeacherName()).isEqualTo("John Smith");
        assertThat(updatedLesson.actualTeacherId()).isEqualTo(substituteTeacher.getId());
        assertThat(updatedLesson.actualTeacherName()).isEqualTo("Jane Doe");
        assertThat(updatedLesson.status()).isEqualTo(LessonStatus.SUBSTITUTE_CONFIRMED);
        assertThat(updatedLesson.hasSubstitute()).isTrue();

        Lesson lessonFromDb = lessonRepository.findById(lessonId).orElseThrow();
        assertThat(lessonFromDb.getOriginalTeacher().getId()).isEqualTo(originalTeacher.getId());
        assertThat(lessonFromDb.getActualTeacher().getId()).isEqualTo(substituteTeacher.getId());
        assertThat(lessonFromDb.getStatus()).isEqualTo(LessonStatus.SUBSTITUTE_CONFIRMED);

        SubstituteRequest requestFromDb = substituteRequestRepository.findById(requestId).orElseThrow();
        assertThat(requestFromDb.getStatus()).isEqualTo(SubstituteRequestStatus.ACCEPTED);
        assertThat(requestFromDb.getAssignedTeacher().getId()).isEqualTo(substituteTeacher.getId());
    }

    @Test
    @DisplayName("Should fail when accepting with unavailable teacher")
    void testAcceptWithUnavailableTeacher() throws Exception {
        LocalDate lessonDate = LocalDate.now().plusDays(1);
        LessonRequestDTO lessonRequest = new LessonRequestDTO(
                lessonDate,
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                originalTeacher.getId(),
                null,
                group.getId(),
                course.getId(),
                null,
                LessonStatus.SCHEDULED
        );

        MvcResult lessonResult = mockMvc.perform(post("/api/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lessonRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        LessonResponseDTO createdLesson = objectMapper.readValue(
                lessonResult.getResponse().getContentAsString(),
                LessonResponseDTO.class
        );
        Integer lessonId = createdLesson.id();

        SubstituteRequestDTO substituteRequest = new SubstituteRequestDTO(lessonId);
        MvcResult requestResult = mockMvc.perform(post("/api/substitute-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(substituteRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        SubstituteResponseDTO createdRequest = objectMapper.readValue(
                requestResult.getResponse().getContentAsString(),
                SubstituteResponseDTO.class
        );

        LessonRequestDTO conflictingLesson = new LessonRequestDTO(
                lessonDate,
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                substituteTeacher.getId(),
                null,
                group.getId(),
                course.getId(),
                null,
                LessonStatus.SCHEDULED
        );

        mockMvc.perform(post("/api/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conflictingLesson)))
                .andExpect(status().isCreated());

        MvcResult availableResult = mockMvc.perform(get("/api/substitute-requests/available")
                        .param("lessonId", lessonId.toString()))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = availableResult.getResponse().getContentAsString();
        assertThat(responseContent).doesNotContain(substituteTeacher.getId().toString());
    }

    @Test
    @DisplayName("Should delete substitute request")
    void testDeleteSubstituteRequest() throws Exception {
        LessonRequestDTO lessonRequest = new LessonRequestDTO(
                LocalDate.now().plusDays(1),
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                originalTeacher.getId(),
                null,
                group.getId(),
                course.getId(),
                null,
                LessonStatus.SCHEDULED
        );

        MvcResult lessonResult = mockMvc.perform(post("/api/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lessonRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        LessonResponseDTO createdLesson = objectMapper.readValue(
                lessonResult.getResponse().getContentAsString(),
                LessonResponseDTO.class
        );

        SubstituteRequestDTO substituteRequest = new SubstituteRequestDTO(createdLesson.id());
        MvcResult requestResult = mockMvc.perform(post("/api/substitute-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(substituteRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        SubstituteResponseDTO createdRequest = objectMapper.readValue(
                requestResult.getResponse().getContentAsString(),
                SubstituteResponseDTO.class
        );

        mockMvc.perform(delete("/api/substitute-requests/{id}", createdRequest.requestId()))
                .andExpect(status().isNoContent());

        assertThat(substituteRequestRepository.findById(createdRequest.requestId())).isEmpty();
    }
}