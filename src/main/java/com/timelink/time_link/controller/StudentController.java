package com.timelink.time_link.controller;

import com.timelink.time_link.dto.Student.StudentRequestDTO;
import com.timelink.time_link.dto.Student.StudentResponseDTO;
import com.timelink.time_link.mapper.StudentMapper;
import com.timelink.time_link.model.Group;
import com.timelink.time_link.model.Student;
import com.timelink.time_link.repository.GroupRepository;
import com.timelink.time_link.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final GroupRepository groupRepository;
    private final StudentMapper studentMapper;

    @GetMapping
    public List<StudentResponseDTO> getAllStudents() {
        return studentMapper.toStudentResponseDTOList(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Integer id) {
        Student student = studentService.getStudentById(id);
        if (student == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(studentMapper.toStudentResponseDTO(student));
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO> saveStudent(@Valid @RequestBody StudentRequestDTO dto)
    {
        Student student = studentMapper.toStudent(dto);

        if (dto.groupId() != null) {
            Group group = groupRepository.findById(dto.groupId())
                    .orElseThrow(() -> new NoSuchElementException("Group not found"));
            student.setGroup(group);
        }

        Student savedStudent = studentService.saveStudent(student);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentMapper.toStudentResponseDTO(savedStudent));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable Integer id,
            @Valid @RequestBody StudentRequestDTO dto
    ) {
        Student existing = studentService.getStudentById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        existing.setName(dto.name());
        existing.setActive(dto.active());
        existing.setDateBirth(dto.dateBirth());
        existing.setUsername(dto.username());
        existing.setPassword(dto.password());

        if (dto.groupId() != null) {
            Group group = groupRepository.findById(dto.groupId())
                    .orElseThrow(() -> new NoSuchElementException("Group not found"));
            existing.setGroup(group);
        } else {
            existing.setGroup(null);
        }

        Student saved = studentService.saveStudent(existing);
        return ResponseEntity.ok(studentMapper.toStudentResponseDTO(saved));
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
    }
}

