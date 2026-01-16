package com.timelink.time_link.service;

import com.timelink.time_link.exception.ResourceNotFoundException;
import com.timelink.time_link.model.Lesson;
import com.timelink.time_link.model.SubstituteRequest;
import com.timelink.time_link.model.SubstituteRequestStatus;
import com.timelink.time_link.model.Teacher;
import com.timelink.time_link.repository.LessonRepository;
import com.timelink.time_link.repository.SubstituteRequestRepository;
import com.timelink.time_link.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.timelink.time_link.mapper.SubstituteRequestMapper;
import com.timelink.time_link.dto.Substitute.SubstituteOpportunityDTO;


import java.util.List;

@Service
@RequiredArgsConstructor
public class SubstituteService {

    private final SubstituteRequestRepository substituteRequestRepository;
    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final SubstituteRequestMapper substituteRequestMapper;


    @Transactional
    public SubstituteRequest createRequest(Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException(Lesson.class, lessonId));

        SubstituteRequest request = new SubstituteRequest();
        request.setLesson(lesson);
        request.setOriginalTeacher(lesson.getOriginalTeacher());
        request.setStatus(SubstituteRequestStatus.PENDING);

        return substituteRequestRepository.save(request);
    }

    @Transactional(readOnly = true)
    public List<SubstituteOpportunityDTO> findAvailableTeachers(Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException(Lesson.class, lessonId));

        List<Teacher> teachers = teacherRepository.findQualifiedAndAvailableTeachers(
                lesson.getCourse().getId(),
                lesson.getLessonDate(),
                lesson.getStartTime(),
                lesson.getEndTime()
        );

        return substituteRequestMapper.toOpportunityDTOList(lesson, teachers);
    }
    @Transactional
    public SubstituteRequest acceptRequest(Integer requestId, Integer teacherId) {
        SubstituteRequest request = substituteRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("SubstituteRequest not found: " + requestId));

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found: " + teacherId));

        request.setAssignedTeacher(teacher);
        request.setStatus(SubstituteRequestStatus.ACCEPTED);

        Lesson lesson = request.getLesson();
        lesson.setActualTeacher(teacher);
        lesson.setStatus(com.timelink.time_link.model.LessonStatus.SUBSTITUTE_CONFIRMED);

        return substituteRequestRepository.save(request);
    }
    @Transactional
    public void deleteRequest(Integer requestId) {
        if (!substituteRequestRepository.existsById(requestId)) {
            throw new RuntimeException("SubstituteRequest not found: " + requestId);
        }
        substituteRequestRepository.deleteById(requestId);
    }

}
