package com.timelink.time_link.controller;

import com.timelink.time_link.dto.Substitute.SubstituteOpportunityDTO;
import com.timelink.time_link.dto.Substitute.SubstituteRequestDTO;
import com.timelink.time_link.dto.Substitute.SubstituteResponseDTO;
import com.timelink.time_link.mapper.SubstituteRequestMapper;
import com.timelink.time_link.model.SubstituteRequest;
import com.timelink.time_link.model.Teacher;
import com.timelink.time_link.service.SubstituteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/substitute-requests")
@RequiredArgsConstructor
public class SubstituteRequestController {

    private final SubstituteService substituteService;
    private final SubstituteRequestMapper substituteRequestMapper;

    @PostMapping
    public ResponseEntity<SubstituteResponseDTO> createRequest(@Valid @RequestBody SubstituteRequestDTO dto) {
        SubstituteRequest created = substituteService.createRequest(dto.lessonId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(substituteRequestMapper.toSubstituteResponseDTO(created));
    }

    @GetMapping("/available")
    public ResponseEntity<List<SubstituteOpportunityDTO>> getAvailableTeachers(@RequestParam Integer lessonId) {
        return ResponseEntity.ok(substituteService.findAvailableTeachers(lessonId));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<SubstituteResponseDTO> acceptRequest(
            @PathVariable Integer id,
            @RequestParam Integer teacherId
    ) {
        SubstituteRequest accepted = substituteService.acceptRequest(id, teacherId);
        return ResponseEntity.ok(substituteRequestMapper.toSubstituteResponseDTO(accepted));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Integer id) {
        substituteService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }

}

