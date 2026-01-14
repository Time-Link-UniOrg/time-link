package com.timelink.time_link.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teacher")
@Data 
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String phone;
    private String email;

    @Column(columnDefinition = "text")
    private String students;

    @Column(columnDefinition = "text")
    private String groups;

    @Column(columnDefinition = "text")
    private String coursesId;

    @ManyToMany
    @JoinTable(
            name = "teacher_qualified_courses",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> qualifiedCourses = new HashSet<>();

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
}