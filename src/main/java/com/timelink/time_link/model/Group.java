package com.timelink.time_link.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Boolean active;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    /*@ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    */

    @OneToMany(mappedBy = "group")
    private List<Student> students;

    // Getters and Setters
}
