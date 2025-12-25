package com.timelink.time_link.controller;

import com.timelink.time_link.dto.Student.StudentRequestDTO;
import com.timelink.time_link.dto.Student.StudentResponseDTO;
import com.timelink.time_link.mapper.StudentMapper;
import com.timelink.time_link.model.Student;
import com.timelink.time_link.service.StudentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
@Validated
public class StudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;

    @PostMapping()
    public ResponseEntity<StudentResponseDTO> saveStudent(@Valid @RequestBody StudentRequestDTO studentRequestDTO) {
        Student savedStudent = studentService.saveStudent(studentRequestDTO);
        return new ResponseEntity<>(studentMapper.toStudentResponseDTO(savedStudent), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable @Positive Long id,
            @Valid @RequestBody StudentRequestDTO studentRequestDTO) {
        Student updatedStudent = studentService.updateStudent(id, studentRequestDTO);
        return ResponseEntity.ok(studentMapper.toStudentResponseDTO(updatedStudent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable @Positive Long id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable @Positive Long id) {
        Student fetchedStudent = studentService.getStudentOrThrow(id);
        return ResponseEntity.ok(studentMapper.toStudentResponseDTO(fetchedStudent));
    }

    @GetMapping()
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        return new ResponseEntity<>(studentMapper.toStudentResponseDTOList(studentService.getAllStudents()), HttpStatus.OK);
    }
}