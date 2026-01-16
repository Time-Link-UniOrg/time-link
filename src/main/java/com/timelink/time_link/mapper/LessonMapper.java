package com.timelink.time_link.mapper;

import com.timelink.time_link.dto.Lesson.LessonRequestDTO;
import com.timelink.time_link.dto.Lesson.LessonResponseDTO;
import com.timelink.time_link.model.*;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LessonMapper {

    @Mapping(target = "originalTeacherId", source = "originalTeacher.id")
    @Mapping(target = "originalTeacherName", source = "originalTeacher.name")
    @Mapping(target = "actualTeacherId", source = "actualTeacher.id")
    @Mapping(target = "actualTeacherName", source = "actualTeacher.name")
    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "groupName", source = "group.name")
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseName", source = "course.name")
    @Mapping(target = "hasSubstitute", expression = "java(lesson.hasSubstitute())")
    LessonResponseDTO toLessonResponseDTO(Lesson lesson);

    List<LessonResponseDTO> toLessonResponseDTOList(List<Lesson> lessons);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "originalTeacher", expression = "java(mapTeacher(dto.originalTeacherId()))")
    @Mapping(target = "actualTeacher", expression = "java(mapTeacherNullable(dto.actualTeacherId()))")
    @Mapping(target = "group", expression = "java(mapGroup(dto.groupId()))")
    @Mapping(target = "course", expression = "java(mapCourse(dto.courseId()))")
    @Mapping(target = "status", expression = "java(mapStatus(dto.status()))")
    Lesson toLesson(LessonRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "originalTeacher", expression = "java(mapTeacher(dto.originalTeacherId()))")
    @Mapping(target = "actualTeacher", expression = "java(mapTeacherNullable(dto.actualTeacherId()))")
    @Mapping(target = "group", expression = "java(mapGroup(dto.groupId()))")
    @Mapping(target = "course", expression = "java(mapCourse(dto.courseId()))")
    @Mapping(target = "status", expression = "java(mapStatus(dto.status()))")
    void updateLessonFromDTO(LessonRequestDTO dto, @MappingTarget Lesson lesson);

    default Teacher mapTeacher(Integer teacherId) {
        if (teacherId == null) {
            return null;
        }
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        return teacher;
    }

    default Teacher mapTeacherNullable(Integer teacherId) {
        if (teacherId == null) {
            return null;
        }
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        return teacher;
    }

    default Group mapGroup(Integer groupId) {
        if (groupId == null) {
            return null;
        }
        Group group = new Group();
        group.setId(groupId);
        return group;
    }
    default Course mapCourse(Integer courseId) {
        if (courseId == null) {
            return null;
        }
        Course course = new Course();
        course.setId(courseId);
        return course;
    }

    default LessonStatus mapStatus(LessonStatus status) {
        return status != null ? status : LessonStatus.SCHEDULED;
    }
}