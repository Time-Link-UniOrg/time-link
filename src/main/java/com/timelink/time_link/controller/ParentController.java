package com.timelink.time_link.controller;

import com.timelink.time_link.mapper.ParentMapper;
import com.timelink.time_link.model.Parent;
import com.timelink.time_link.service.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/parents")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;


    @PostMapping
    public ResponseEntity<ParentDTO> createParent(@RequestBody Parent parent) {
        Parent created = parentService.createParent(parent);
        ParentDTO dto = ParentMapper.toDTO(created);

        return ResponseEntity
                .created(URI.create("/api/parents/" + created.getId()))
                .body(dto);
    }


    @GetMapping
    public ResponseEntity<List<ParentDTO>> getAllParents() {
        List<ParentDTO> dtos = parentService.getAllParents()
                .stream()
                .map(ParentMapper::toDTO)
                .toList();

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ParentDTO> getParentById(@PathVariable Integer id) {
        return parentService.getParentById(id)
                .map(parent -> new ResponseEntity<>(ParentMapper.toDTO(parent), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @GetMapping("/username/{username}")
    public ResponseEntity<ParentDTO> getParentByUsername(@PathVariable String username) {
        return parentService.getByUsername(username)
                .map(parent -> new ResponseEntity<>(ParentMapper.toDTO(parent), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ParentDTO> updateParent(@PathVariable Integer id,
                                                  @RequestBody Parent parent) {
        try {
            Parent updated = parentService.updateParent(id, parent);
            ParentDTO dto = ParentMapper.toDTO(updated);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParent(@PathVariable Integer id) {
        try {
            parentService.deleteParent(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
