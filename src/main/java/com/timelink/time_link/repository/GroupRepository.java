package com.timelink.time_link.repository;

import com.timelink.time_link.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    Optional<Group> findByName(String name);
}
