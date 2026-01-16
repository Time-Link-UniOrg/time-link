package com.timelink.time_link.repository;

import com.timelink.time_link.model.SubstituteRequest;
import com.timelink.time_link.model.SubstituteRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
    public interface SubstituteRequestRepository extends JpaRepository<SubstituteRequest, Integer> {

        List<SubstituteRequest> findByLessonIdAndStatus(Integer lessonId, SubstituteRequestStatus status);

        default List<SubstituteRequest> findPendingByLessonId(Integer lessonId) {
            return findByLessonIdAndStatus(lessonId, SubstituteRequestStatus.PENDING);
        }
    }
