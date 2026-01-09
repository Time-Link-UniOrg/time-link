package com.timelink.time_link.controller;

import com.timelink.time_link.dto.Parent.ParentRequestDTO;
import com.timelink.time_link.dto.Parent.ParentResponseDTO;
import com.timelink.time_link.mapper.ParentMapper;
import com.timelink.time_link.model.Parent;
import com.timelink.time_link.service.ParentService;
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
@RequestMapping("/parents")
@Validated
public class ParentController {

    private final ParentService parentService;
    private final ParentMapper parentMapper;

    @PostMapping()
    public ResponseEntity<ParentResponseDTO> saveParent(@Valid @RequestBody ParentRequestDTO parentRequestDTO) {
        Parent savedParent = parentService.saveParent(parentRequestDTO);
        return new ResponseEntity<>(parentMapper.toParentResponseDTO(savedParent), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParentResponseDTO> updateParent(
            @PathVariable @Positive Integer id,
            @Valid @RequestBody ParentRequestDTO parentRequestDTO) {
        Parent updatedParent = parentService.updateParent(id, parentRequestDTO);
        return ResponseEntity.ok(parentMapper.toParentResponseDTO(updatedParent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParent(@PathVariable @Positive Integer id) {
        parentService.deleteParent(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParentResponseDTO> getParentById(@PathVariable @Positive Integer id) {
        Parent fetchedParent = parentService.getParentOrThrow(id);
        return ResponseEntity.ok(parentMapper.toParentResponseDTO(fetchedParent));
    }

    @GetMapping()
    public ResponseEntity<List<ParentResponseDTO>> getAllParents() {
        return new ResponseEntity<>(parentMapper.toParentResponseDTOList(parentService.getAllParents()), HttpStatus.OK);
    }
}