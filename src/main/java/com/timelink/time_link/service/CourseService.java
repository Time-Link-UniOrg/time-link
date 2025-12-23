package com.timelink.time_link.service;

import com.timelink.time_link.dto.Course.CourseRequestDTO;
import com.timelink.time_link.exception.ResourceNotFoundException;
import com.timelink.time_link.mapper.CourseMapper;
import com.timelink.time_link.model.Course;
import com.timelink.time_link.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Transactional
    public Course saveCourse(CourseRequestDTO courseRequestDTO) {
        Course course = courseMapper.toCourse(courseRequestDTO);
        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Integer id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(Course.class, id);
        }
    }

    public Optional<Course> getCourseById(Integer id) {
        return courseRepository.findById(id);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseByName(String name) {
        return courseRepository.findByName(name);
    }

    public Course getCourseOrThrow(Integer id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Course.class, id));
    }

    public Course getCourseByNameOrCreate(String name) {
        Optional<Course> fetchedCourse = courseRepository.findByName(name);
        return fetchedCourse.orElseGet(() -> {
            Course newCourse = new Course();
            newCourse.setName(name);
            return courseRepository.save(newCourse);
        });
    }
}