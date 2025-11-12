package com.timelink.time_link.service;

import com.timelink.time_link.model.Parent;
import com.timelink.time_link.repository.ParentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParentService {

    private final ParentRepository parentRepository;

    public List<Parent> getAllParents() {
        return parentRepository.findAll();
    }

    public Parent createParent(Parent parent) {
        parent.setId(null);
        return parentRepository.save(parent);
    }


    public Optional<Parent> getParentById(Integer id) {
        return parentRepository.findById(id);
    }


    public Optional<Parent> getByUsername(String username) {
        return Optional.ofNullable(parentRepository.findByUsername(username));
    }

    public Parent updateParent(Integer id, Parent updated) {
        Parent existing = parentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parent not found: " + id));

        existing.setName(updated.getName());
        existing.setPhone(updated.getPhone());
        existing.setEmail(updated.getEmail());
        existing.setChild(updated.getChild());
        existing.setPaid(updated.isPaid());
        existing.setUsername(updated.getUsername());
        existing.setPassword(updated.getPassword());

        return parentRepository.save(existing);
    }

    @Transactional
    public void deleteParent(Integer id) {
        if (!parentRepository.existsById(id)) {
            throw new RuntimeException("Parent not found: " + id);
        }

        parentRepository.deleteById(id);
        parentRepository.flush();

        if (parentRepository.count() == 0) {
            parentRepository.restartParentIdIdentity();
        }
    }

}
