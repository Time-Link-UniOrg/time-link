package com.timelink.time_link.service;

import com.timelink.time_link.exception.ResourceNotFoundException;
import com.timelink.time_link.model.Course;
import com.timelink.time_link.model.Teacher;
import com.timelink.time_link.repository.CourseRepository;
import com.timelink.time_link.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherQualificationService {

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public Teacher addQualification(Integer teacherId, Integer courseId) {
        Teacher teacher = getTeacherOrThrow(teacherId);
        Course course = getCourseOrThrow(courseId);

        if (teacher.getQualifiedCourses().contains(course)) {
            throw new IllegalStateException(
                    "Teacher " + teacher.getName() +
                            " is already qualified for course " + course.getName()
            );
        }


        teacher.addQualifiedCourse(course);

        return teacherRepository.save(teacher);
    }

    @Transactional
    public Teacher removeQualification(Integer teacherId, Integer courseId) {
        Teacher teacher = getTeacherOrThrow(teacherId);
        Course course = getCourseOrThrow(courseId);

        if (!teacher.getQualifiedCourses().contains(course)) {
            throw new IllegalStateException(
                    "Teacher " + teacher.getName() +
                            " is not qualified for course " + course.getName()
            );
        }

        teacher.removeQualifiedCourse(course);

        return teacherRepository.save(teacher);
    }

    @Transactional
    public Teacher addMultipleQualifications(Integer teacherId, List<Integer> courseIds) {
        Teacher teacher = getTeacherOrThrow(teacherId);

        for (Integer courseId : courseIds) {
            Course course = getCourseOrThrow(courseId);
            if (!teacher.getQualifiedCourses().contains(course)) {
                teacher.addQualifiedCourse(course);
            }
        }

        return teacherRepository.save(teacher);
    }

    @Transactional
    public Teacher removeAllQualifications(Integer teacherId) {
        Teacher teacher = getTeacherOrThrow(teacherId);

        Set<Course> coursesToRemove = Set.copyOf(teacher.getQualifiedCourses());

        for (Course course : coursesToRemove) {
            teacher.removeQualifiedCourse(course);
        }

        return teacherRepository.save(teacher);
    }

    public Set<Course> getTeacherQualifications(Integer teacherId) {
        Teacher teacher = getTeacherOrThrow(teacherId);
        return teacher.getQualifiedCourses();
    }

    public Set<Teacher> getQualifiedTeachersForCourse(Integer courseId) {
        Course course = getCourseOrThrow(courseId);
        return course.getQualifiedTeachers();
    }

    public boolean isTeacherQualified(Integer teacherId, Integer courseId) {
        Teacher teacher = getTeacherOrThrow(teacherId);
        Course course = getCourseOrThrow(courseId);

        return teacher.isQualifiedFor(course);
    }

    public int getQualificationCount(Integer teacherId) {
        Teacher teacher = getTeacherOrThrow(teacherId);
        return teacher.getQualifiedCourses().size();
    }

    public int getQualifiedTeacherCount(Integer courseId) {
        Course course = getCourseOrThrow(courseId);
        return course.getQualifiedTeacherCount();
    }

    @Transactional
    public Teacher replaceQualifications(Integer teacherId, List<Integer> newCourseIds) {
        Teacher teacher = removeAllQualifications(teacherId);
        return addMultipleQualifications(teacherId, newCourseIds);
    }

    private Teacher getTeacherOrThrow(Integer id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Teacher.class, id));
    }

    private Course getCourseOrThrow(Integer id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Course.class, id));
    }
}