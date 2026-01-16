package com.timelink.time_link.controller;

import com.timelink.time_link.service.StudentRelationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.timelink.time_link.dto.Group.GroupResponseDTO;
import com.timelink.time_link.dto.Parent.ParentResponseDTO;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentRelationsController {

    private final StudentRelationsService studentRelationsService;

    @GetMapping("/{studentId}/group")
    public ResponseEntity<GroupResponseDTO> getStudentGroup(@PathVariable Integer studentId) {
        return ResponseEntity.ok(studentRelationsService.getStudentGroup(studentId));
    }

    @GetMapping("/{studentId}/parents")
    public ResponseEntity<List<ParentResponseDTO>> getStudentParents(@PathVariable Integer studentId) {
        return ResponseEntity.ok(studentRelationsService.getStudentParents(studentId));
    }
    @PutMapping("/{studentId}/group/{groupId}")
    public ResponseEntity<Void> assignGroup(@PathVariable Integer studentId,
                                            @PathVariable Integer groupId) {
        studentRelationsService.assignStudentToGroup(studentId, groupId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{studentId}/group")
    public ResponseEntity<Void> removeGroup(@PathVariable Integer studentId) {
        studentRelationsService.removeStudentFromGroup(studentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{studentId}/parents/{parentId}")
    public ResponseEntity<Void> addParent(@PathVariable Integer studentId,
                                          @PathVariable Integer parentId) {
        studentRelationsService.addParentToStudent(studentId, parentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{studentId}/parents/{parentId}")
    public ResponseEntity<Void> removeParent(@PathVariable Integer studentId,
                                             @PathVariable Integer parentId) {
        studentRelationsService.removeParentFromStudent(studentId, parentId);
        return ResponseEntity.noContent().build();
    }
}
