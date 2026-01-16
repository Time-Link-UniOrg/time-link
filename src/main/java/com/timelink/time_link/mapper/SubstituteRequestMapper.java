package com.timelink.time_link.mapper;

import com.timelink.time_link.dto.Substitute.SubstituteOpportunityDTO;
import com.timelink.time_link.dto.Substitute.SubstituteResponseDTO;
import com.timelink.time_link.model.Lesson;
import com.timelink.time_link.model.SubstituteRequest;
import com.timelink.time_link.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubstituteRequestMapper {

    default SubstituteResponseDTO toSubstituteResponseDTO(SubstituteRequest request) {
        if (request == null) return null;

        Integer lessonId = request.getLesson() != null ? request.getLesson().getId() : null;
        Integer originalTeacherId = request.getOriginalTeacher() != null ? request.getOriginalTeacher().getId() : null;
        Integer assignedTeacherId = request.getAssignedTeacher() != null ? request.getAssignedTeacher().getId() : null;

        return new SubstituteResponseDTO(
                request.getId(),
                lessonId,
                originalTeacherId,
                assignedTeacherId,
                request.getStatus(),
                request.getCreateAt()
        );
    }

    default List<SubstituteOpportunityDTO> toOpportunityDTOList(Lesson lesson, List<Teacher> teachers) {
        return teachers.stream()
                .map(t -> new SubstituteOpportunityDTO(
                        lesson.getId(),
                        lesson.getLessonDate(),
                        lesson.getStartTime(),
                        lesson.getEndTime(),
                        lesson.getCourse().getId(),
                        lesson.getCourse().getName(),
                        lesson.getGroup().getId(),
                        lesson.getGroup().getName(),
                        t.getId(),
                        t.getName()
                ))
                .toList();
    }
}
