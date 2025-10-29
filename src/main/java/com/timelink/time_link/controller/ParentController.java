package com.timelink.time_link.controller;

import com.timelink.time_link.model.Parent;
import com.timelink.time_link.service.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/parents")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    @PostMapping
    public ResponseEntity<Parent> createParent(@RequestBody Parent parent) {
        Parent created = parentService.createParent(parent);
        return ResponseEntity.created(URI.create("/api/parents/" + created.getId()))
                .body(created);
        // или: return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Parent>> getAllParents() {
        return ResponseEntity.ok(parentService.getAllParents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parent> getParentById(@PathVariable Integer id) {
        return parentService.getParentById(id)
                .map(parent -> new ResponseEntity<>(parent, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Parent> getParentByUsername(@PathVariable String username) {
        return parentService.getByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Parent> updateParent(@PathVariable Integer id,
                                               @RequestBody Parent parent) {
        try {
            Parent updated = parentService.updateParent(id, parent);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParent(@PathVariable Integer id) {
        try {
            parentService.deleteParent(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EmptyResultDataAccessException e){
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        //parentService.deleteParent(id);
        //return ResponseEntity.noContent().build();
    }
}
