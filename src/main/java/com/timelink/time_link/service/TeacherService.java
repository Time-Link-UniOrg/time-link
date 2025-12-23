package com.timelink.time_link.service;

import com.timelink.time_link.dto.Teacher.TeacherRequestDTO;
import com.timelink.time_link.exception.ResourceNotFoundException;
import com.timelink.time_link.mapper.TeacherMapper;
import com.timelink.time_link.model.Teacher;
import com.timelink.time_link.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    @Transactional
    public Teacher saveTeacher(TeacherRequestDTO teacherRequestDTO) {
        if (teacherRepository.existsByUsername(teacherRequestDTO.username())) {
            throw new RuntimeException("Username already exists: " + teacherRequestDTO.username());
        }

        Teacher teacher = teacherMapper.toTeacher(teacherRequestDTO);
        return teacherRepository.save(teacher);
    }

    @Transactional
    public void deleteTeacher(Integer id) {
        if (teacherRepository.existsById(id)) {
            teacherRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(Teacher.class, id);
        }
    }

    public Optional<Teacher> getTeacherById(Integer id) {
        return teacherRepository.findById(id);
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> getTeacherByUsername(String username) {
        return teacherRepository.findByUsername(username);
    }

    public Teacher getTeacherOrThrow(Integer id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Teacher.class, id));
    }

    public Teacher getTeacherByUsernameOrThrow(String username) {
        return teacherRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher with username " + username + " not found"));
    }
}