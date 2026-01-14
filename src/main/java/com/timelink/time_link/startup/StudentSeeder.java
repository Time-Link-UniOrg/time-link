package com.timelink.time_link.startup;

import com.timelink.time_link.model.Group;
import com.timelink.time_link.model.Student;
import com.timelink.time_link.repository.GroupRepository;
import com.timelink.time_link.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Order(1) // Runs after GroupSeeder
public class StudentSeeder implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    @Override
    public void run(String... args) {

        Optional<Group> groupOpt = groupRepository.findById(1);
        if (groupOpt.isEmpty()) {
            System.out.println("Group not found. Skipping student seeding.");
            return;
        }

        Group group = groupOpt.get();

        // ---- STUDENT 1: Mila ----
        if (!studentRepository.existsByUsername("mila")) {
            Student student1 = new Student(
                    null,
                    "Mila",
                    true,
                    LocalDate.of(2005, 10, 29),
                    "mila",
                    "1234",
                    group
            );
            studentRepository.save(student1);
            System.out.println("Seeded student: Mila");
        } else {
            System.out.println("Student Mila already exists, skipping.");
        }

        // ---- STUDENT 2: Ivan ----
        if (!studentRepository.existsByUsername("ivan1")) {
            Student student2 = new Student(
                    null,
                    "Ivan",
                    true,
                    LocalDate.of(2004, 12, 28),
                    "ivan1",
                    "1234i",
                    group
            );
            studentRepository.save(student2);
            System.out.println("Seeded student: Ivan");
        } else {
            System.out.println("Student Ivan already exists, skipping.");
        }
    }
}
