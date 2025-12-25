package com.timelink.time_link.service;

import com.timelink.time_link.dto.Student.StudentRequestDTO;
import com.timelink.time_link.exception.ResourceNotFoundException;
import com.timelink.time_link.mapper.StudentMapper;
import com.timelink.time_link.model.Group;
import com.timelink.time_link.model.Student;
import com.timelink.time_link.repository.GroupRepository;
import com.timelink.time_link.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final StudentMapper studentMapper;

    @Transactional
    public Student saveStudent(StudentRequestDTO studentRequestDTO) {
        Student student = studentMapper.toStudent(studentRequestDTO);

        if (studentRequestDTO.groupId() != null) {
            Group group = groupRepository.findById(studentRequestDTO.groupId())
                    .orElseThrow(() -> new ResourceNotFoundException(Group.class, studentRequestDTO.groupId()));
            student.setGroup(group);
        }

        return studentRepository.save(student);
    }

    @Transactional
    public Student updateStudent(Long id, StudentRequestDTO studentRequestDTO) {
        Student student = getStudentOrThrow(id);

        student.setName(studentRequestDTO.name());
        student.setActive(studentRequestDTO.active());
        student.setDateBirth(studentRequestDTO.dateBirth());
        student.setUsername(studentRequestDTO.username());

        if (studentRequestDTO.password() != null && !studentRequestDTO.password().isEmpty()) {
            student.setPassword(studentRequestDTO.password());
        }

        if (studentRequestDTO.groupId() != null) {
            Group group = groupRepository.findById(studentRequestDTO.groupId())
                    .orElseThrow(() -> new ResourceNotFoundException(Group.class, studentRequestDTO.groupId()));
            student.setGroup(group);
        } else {
            student.setGroup(null);
        }

        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(Student.class, id);
        }
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Student.class, id));
    }
}