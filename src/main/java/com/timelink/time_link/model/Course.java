package com.timelink.time_link.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "course")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String teachers;

    @Column(columnDefinition = "text")
    private String lessons;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private Integer age;

    @Column(name = "time_period")
    private Integer timePeriod;
}
