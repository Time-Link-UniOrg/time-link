package com.timelink.time_link.controller;

import com.timelink.time_link.dto.Group.GroupRequestDTO;
import com.timelink.time_link.dto.Group.GroupResponseDTO;
import com.timelink.time_link.mapper.GroupMapper;
import com.timelink.time_link.model.Group;
import com.timelink.time_link.service.GroupService;
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
@RequestMapping("/groups")
@Validated
public class GroupController {

    private final GroupService groupService;
    private final GroupMapper groupMapper;

    @PostMapping()
    public ResponseEntity<GroupResponseDTO> saveGroup(@Valid @RequestBody GroupRequestDTO groupRequestDTO) {
        Group savedGroup = groupService.saveGroup(groupRequestDTO);
        return new ResponseEntity<>(groupMapper.toGroupResponseDTO(savedGroup), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable @Positive Long id) {
        groupService.deleteGroup(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponseDTO> getGroupById(@PathVariable @Positive Long id) {
        Group fetchedGroup = groupService.getGroupOrThrow(id);
        return ResponseEntity.ok(groupMapper.toGroupResponseDTO(fetchedGroup));
    }

    @GetMapping()
    public ResponseEntity<List<GroupResponseDTO>> getAllGroups() {
        return new ResponseEntity<>(groupMapper.toGroupResponseDTOList(groupService.getAllGroups()), HttpStatus.OK);
    }
}