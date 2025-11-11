package com.timelink.time_link.controller;

import com.timelink.time_link.dto.Student.StudentRequestDTO;
import com.timelink.time_link.model.Group;
import com.timelink.time_link.model.Student;
import com.timelink.time_link.repository.GroupRepository;
import com.timelink.time_link.repository.StudentRepository;
import com.timelink.time_link.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final GroupRepository groupRepository;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO> saveStudent(@RequestBody Student student) {
        if (student.getGroup() != null && student.getGroup().getId() != null) {
            Group group = groupRepository.findById(student.getGroup().getId())
                    .orElseThrow(() -> new NoSuchElementException("Group not found"));
            student.setGroup(group);
        }
        return new ResponseEntity<>(studentService.saveStudent(student), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);
        return studentService.saveStudent(student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}

