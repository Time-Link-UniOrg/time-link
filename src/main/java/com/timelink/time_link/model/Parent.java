package com.timelink.time_link.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parents")
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column
    private String phone;

    @Column(unique = true)
    private String email;

    @Column
    private boolean paid;

    @Column
    private String username;

    @Column
    private String password;

    @ManyToMany(mappedBy = "parents", fetch = FetchType.LAZY)
    private Set<Student> children = new HashSet<>();

    public void addChild(Student student) {
        if (student == null) {
            return;
        }
        this.children.add(student);
        student.getParents().add(this);
    }

    public void removeChild(Student student) {
        if (student == null) {
            return;
        }
        this.children.remove(student);
        student.getParents().remove(this);
    }

    public boolean hasChild(Student student) {
        if (student == null) {
            return false;
        }
        return this.children.contains(student);
    }

    public int getChildrenCount() {
        return this.children != null ? this.children.size() : 0;
    }

    @Deprecated
    public String getChild() {
        if (children == null || children.isEmpty()) {
            return null;
        }
        return children.stream()
                .map(Student::getName)
                .collect(Collectors.joining(", "));
    }

    @Deprecated
    public void setChild(String childName) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parent)) return false;
        Parent parent = (Parent) o;
        return id != null && id.equals(parent.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}