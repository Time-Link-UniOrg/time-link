package com.timelink.time_link.service;

import com.timelink.time_link.dto.CourseDTO;
import com.timelink.time_link.mapper.CourseMapper;
import com.timelink.time_link.model.Course;
import com.timelink.time_link.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseDTO createCourse(CourseDTO dto) {
        if (courseRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Course with this name already exists: " + dto.getName());
        }
        Course course = courseMapper.toEntity(dto);
        Course saved = courseRepository.save(course);
        return courseMapper.toDTO(saved);
    }

    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courseMapper.toDTOList(courses);
    }

    public CourseDTO getCourseById(Integer id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        return courseMapper.toDTO(course);
    }

    public CourseDTO getCourseByName(String name) {
        Course course = courseRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Course not found with name: " + name));
        return courseMapper.toDTO(course);
    }

    public CourseDTO updateCourse(Integer id, CourseDTO dto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        courseMapper.updateEntity(course, dto);
        Course updated = courseRepository.save(course);
        return courseMapper.toDTO(updated);
    }

    public void deleteCourse(Integer id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }
}