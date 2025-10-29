package com.timelink.time_link.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "student_table")
public class Student {

    @Id
    @SequenceGenerator(name = "student_sequence", allocationSize = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "date_birth")
    private LocalDate dateBirth;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "history", columnDefinition = "jsonb")
    private String history;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    /*
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;
    */
}