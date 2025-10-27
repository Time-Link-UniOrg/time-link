package com.timelink.time_link.service;

import com.timelink.time_link.model.Teacher;
import com.timelink.time_link.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public Teacher createTeacher(Teacher teacher) {
        if (teacherRepository.existsByUsername(teacher.getUsername())) {
            throw new RuntimeException("Username already exists: " + teacher.getUsername());
        }
        return teacherRepository.save(teacher);
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> getTeacherById(Integer id) {
        return teacherRepository.findById(id);
    }

    public Optional<Teacher> getTeacherByUsername(String username) {
        return teacherRepository.findByUsername(username);
    }

    public Teacher updateTeacher(Integer id, Teacher teacherDetails) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));

        teacher.setName(teacherDetails.getName());
        teacher.setPhone(teacherDetails.getPhone());
        teacher.setEmail(teacherDetails.getEmail());
        teacher.setStudents(teacherDetails.getStudents());
        teacher.setGroups(teacherDetails.getGroups());
        teacher.setCoursesId(teacherDetails.getCoursesId());

        if (teacherDetails.getUsername() != null) {
            teacher.setUsername(teacherDetails.getUsername());
        }
        if (teacherDetails.getPassword() != null) {
            teacher.setPassword(teacherDetails.getPassword());
        }

        return teacherRepository.save(teacher);
    }

    public void deleteTeacher(Integer id) {
        if (!teacherRepository.existsById(id)) {
            throw new RuntimeException("Teacher not found with id: " + id);
        }
        teacherRepository.deleteById(id);
    }
}
