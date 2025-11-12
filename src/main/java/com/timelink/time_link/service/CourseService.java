package com.timelink.time_link.service;

import com.timelink.time_link.model.Course;
import com.timelink.time_link.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public Course createCourse(Course course) {
        // Провери дали курс с това име вече съществува
        if (courseRepository.existsByName(course.getName())) {
            throw new RuntimeException("Course with this name already exists: " + course.getName());
        }
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Integer id) {
        return courseRepository.findById(id);
    }

    public Optional<Course> getCourseByName(String name) {
        return courseRepository.findByName(name);
    }

    public Course updateCourse(Integer id, Course courseDetails) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        course.setName(courseDetails.getName());
        course.setTeachers(courseDetails.getTeachers());
        course.setLessons(courseDetails.getLessons());
        course.setPrice(courseDetails.getPrice());
        course.setAge(courseDetails.getAge());
        course.setTimePeriod(courseDetails.getTimePeriod());

        return courseRepository.save(course);
    }

    public void deleteCourse(Integer id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }
}