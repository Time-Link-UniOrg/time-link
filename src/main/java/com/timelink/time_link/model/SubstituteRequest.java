package com.timelink.time_link.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "substitute_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class SubstituteRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "original_teacher_id", nullable = false)
    private Teacher originalTeacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_teacher_id")
    private Teacher assignedTeacher;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SubstituteRequestStatus status = SubstituteRequestStatus.PENDING;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();
}
