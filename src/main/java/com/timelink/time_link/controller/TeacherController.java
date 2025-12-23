package com.timelink.time_link.controller;

import com.timelink.time_link.dto.Teacher.TeacherRequestDTO;
import com.timelink.time_link.dto.Teacher.TeacherResponseDTO;
import com.timelink.time_link.mapper.TeacherMapper;
import com.timelink.time_link.model.Teacher;
import com.timelink.time_link.service.TeacherService;
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
@RequestMapping("/teachers")
@Validated
public class TeacherController {

    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;

    @PostMapping()
    public ResponseEntity<TeacherResponseDTO> saveTeacher(@Valid @RequestBody TeacherRequestDTO teacherRequestDTO) {
        Teacher savedTeacher = teacherService.saveTeacher(teacherRequestDTO);
        return new ResponseEntity<>(teacherMapper.toTeacherResponseDTO(savedTeacher), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable @Positive Integer id) {
        teacherService.deleteTeacher(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> getTeacherById(@PathVariable @Positive Integer id) {
        Teacher fetchedTeacher = teacherService.getTeacherOrThrow(id);
        return ResponseEntity.ok(teacherMapper.toTeacherResponseDTO(fetchedTeacher));
    }

    @GetMapping()
    public ResponseEntity<List<TeacherResponseDTO>> getAllTeachers() {
        return new ResponseEntity<>(teacherMapper.toTeacherResponseDTOList(teacherService.getAllTeachers()), HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<TeacherResponseDTO> getTeacherByUsername(@PathVariable String username) {
        Teacher fetchedTeacher = teacherService.getTeacherByUsernameOrThrow(username);
        return ResponseEntity.ok(teacherMapper.toTeacherResponseDTO(fetchedTeacher));
    }
}