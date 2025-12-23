package com.timelink.time_link.controller;

import com.timelink.time_link.dto.Course.CourseRequestDTO;
import com.timelink.time_link.dto.Course.CourseResponseDTO;
import com.timelink.time_link.mapper.CourseMapper;
import com.timelink.time_link.model.Course;
import com.timelink.time_link.service.CourseService;
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
@RequestMapping("/courses")
@Validated
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;

    @PostMapping()
    public ResponseEntity<CourseResponseDTO> saveCourse(@Valid @RequestBody CourseRequestDTO courseRequestDTO) {
        Course savedCourse = courseService.saveCourse(courseRequestDTO);
        return new ResponseEntity<>(courseMapper.toCourseResponseDTO(savedCourse), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable @Positive Integer id) {
        courseService.deleteCourse(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable @Positive Integer id) {
        Course fetchedCourse = courseService.getCourseOrThrow(id);
        return ResponseEntity.ok(courseMapper.toCourseResponseDTO(fetchedCourse));
    }

    @GetMapping()
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        return new ResponseEntity<>(courseMapper.toCourseResponseDTOList(courseService.getAllCourses()), HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CourseResponseDTO> getCourseByName(@PathVariable String name) {
        Course fetchedCourse = courseService.getCourseByName(name)
                .orElseThrow(() -> new com.timelink.time_link.exception.ResourceNotFoundException("Course with name " + name + " not found"));
        return ResponseEntity.ok(courseMapper.toCourseResponseDTO(fetchedCourse));
    }
}