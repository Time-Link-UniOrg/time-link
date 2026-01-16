package com.timelink.time_link.controller;

import com.timelink.time_link.dto.Lesson.LessonRequestDTO;
import com.timelink.time_link.dto.Lesson.LessonResponseDTO;
import com.timelink.time_link.exception.ResourceNotFoundException;
import com.timelink.time_link.exception.TimeConflictException;
import com.timelink.time_link.mapper.LessonMapper;
import com.timelink.time_link.model.Lesson;
import com.timelink.time_link.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;
    private final LessonMapper lessonMapper;

    @PostMapping
    public ResponseEntity<LessonResponseDTO> createLesson(
            @Valid @RequestBody LessonRequestDTO requestDTO) {

        try {
            Lesson lesson = lessonMapper.toLesson(requestDTO);
            Lesson createdLesson = lessonService.createLesson(lesson);
            LessonResponseDTO responseDTO = lessonMapper.toLessonResponseDTO(createdLesson);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

        } catch (TimeConflictException e) {
            throw new TimeConflictException(
                    "Cannot create lesson: " + e.getMessage()
            );
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(
                    "Cannot create lesson: " + e.getMessage()
            );
        }
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<LessonResponseDTO>> getLessonsByTeacher(
            @PathVariable Integer teacherId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        LocalDate searchDate = date != null ? date : LocalDate.now();
        List<Lesson> lessons = lessonService.getLessonsByTeacherAndDate(teacherId, searchDate);
        List<LessonResponseDTO> responseDTOs = lessonMapper.toLessonResponseDTOList(lessons);

        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonResponseDTO> updateLesson(
            @PathVariable Integer id,
            @Valid @RequestBody LessonRequestDTO requestDTO) {

        try {
            Lesson lesson = lessonMapper.toLesson(requestDTO);
            Lesson updatedLesson = lessonService.updateLesson(id, lesson);
            LessonResponseDTO responseDTO = lessonMapper.toLessonResponseDTO(updatedLesson);

            return ResponseEntity.ok(responseDTO);

        } catch (TimeConflictException e) {
            throw new TimeConflictException(
                    "Cannot update lesson: " + e.getMessage()
            );
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(
                    "Lesson with id " + id + " not found"
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Integer id) {
        try {
            lessonService.deleteLesson(id);
            return ResponseEntity.noContent().build();

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(
                    "Lesson with id " + id + " not found"
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonResponseDTO> getLessonById(@PathVariable Integer id) {
        Lesson lesson = lessonService.getLessonOrThrow(id);
        LessonResponseDTO responseDTO = lessonMapper.toLessonResponseDTO(lesson);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<LessonResponseDTO>> getAllLessons() {
        List<Lesson> lessons = lessonService.getAllLessons();
        List<LessonResponseDTO> responseDTOs = lessonMapper.toLessonResponseDTOList(lessons);
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<LessonResponseDTO>> getLessonsByGroup(
            @PathVariable Integer groupId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<Lesson> lessons = lessonService.getLessonsByGroupAndDate(groupId, date);
        List<LessonResponseDTO> responseDTOs = lessonMapper.toLessonResponseDTOList(lessons);

        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/{id}/substitute")
    public ResponseEntity<LessonResponseDTO> assignSubstitute(
            @PathVariable Integer id,
            @RequestParam Integer substituteTeacherId) {

        try {
            Lesson updatedLesson = lessonService.assignSubstitute(id, substituteTeacherId);
            LessonResponseDTO responseDTO = lessonMapper.toLessonResponseDTO(updatedLesson);
            return ResponseEntity.ok(responseDTO);

        } catch (TimeConflictException e) {
            throw new TimeConflictException(
                    "Substitute teacher has time conflict: " + e.getMessage()
            );
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<LessonResponseDTO> cancelLesson(@PathVariable Integer id) {
        Lesson cancelledLesson = lessonService.cancelLesson(id);
        LessonResponseDTO responseDTO = lessonMapper.toLessonResponseDTO(cancelledLesson);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<LessonResponseDTO> completeLesson(@PathVariable Integer id) {
        Lesson completedLesson = lessonService.completeLesson(id);
        LessonResponseDTO responseDTO = lessonMapper.toLessonResponseDTO(completedLesson);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/needing-substitute")
    public ResponseEntity<List<LessonResponseDTO>> getLessonsNeedingSubstitute() {
        List<Lesson> lessons = lessonService.getLessonsNeedingSubstitute();
        List<LessonResponseDTO> responseDTOs = lessonMapper.toLessonResponseDTOList(lessons);
        return ResponseEntity.ok(responseDTOs);
    }
}