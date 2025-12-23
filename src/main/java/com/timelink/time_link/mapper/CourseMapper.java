package com.timelink.time_link.mapper;

import com.timelink.time_link.dto.CourseDTO;
import com.timelink.time_link.model.Course;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    public CourseDTO toDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setTeachers(course.getTeachers());
        dto.setLessons(course.getLessons());
        dto.setPrice(course.getPrice());
        dto.setAge(course.getAge());
        dto.setTimePeriod(course.getTimePeriod());
        return dto;
    }

    public Course toEntity(CourseDTO dto) {
        Course course = new Course();
        course.setName(dto.getName());
        course.setTeachers(dto.getTeachers());
        course.setLessons(dto.getLessons());
        course.setPrice(dto.getPrice());
        course.setAge(dto.getAge());
        course.setTimePeriod(dto.getTimePeriod());
        return course;
    }

    public void updateEntity(Course course, CourseDTO dto) {
        course.setName(dto.getName());
        course.setTeachers(dto.getTeachers());
        course.setLessons(dto.getLessons());
        course.setPrice(dto.getPrice());
        course.setAge(dto.getAge());
        course.setTimePeriod(dto.getTimePeriod());
    }

    public List<CourseDTO> toDTOList(List<Course> courses) {
        return courses.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}