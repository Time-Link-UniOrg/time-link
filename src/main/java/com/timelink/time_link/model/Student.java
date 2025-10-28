package com.timelink.time_link.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Boolean active;
    private LocalDate dateBirth;

    private String username;
    private String password;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    /*
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;
    */

    // Getters and Setters
}