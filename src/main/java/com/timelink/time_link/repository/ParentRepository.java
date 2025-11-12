package com.timelink.time_link.repository;

import com.timelink.time_link.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Integer> {
    Parent findByUsername(String username);

@Modifying
@Query(value = "ALTER TABLE parent ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void restartParentIdIdentity();
}
