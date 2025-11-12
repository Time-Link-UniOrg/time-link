package com.timelink.time_link.startup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timelink.time_link.model.Group;
import com.timelink.time_link.model.Student;
import com.timelink.time_link.repository.GroupRepository;
import com.timelink.time_link.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Order(1) // Runs after GroupSeeder
public class StudentSeeder implements CommandLineRunner {
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    @Override
    public void run(String... args) throws Exception {
        Optional<Group> groupOpt = groupRepository.findById(1L);
        if(groupOpt.isPresent())
        {
            Group group = groupOpt.get();

            Student student1 = new Student(null, "Mila", true, LocalDate.of(2005, 10, 29),
                    "mila", "1234", group);

            Student student2 = new Student(null, "Ivan", true, LocalDate.of(2004, 12, 28),
                    "ivan1", "1234i", group);

            studentRepository.save(student1);
            studentRepository.save(student2);

            System.out.println("Seeded students: Mila, Ivan");
        } else {
            System.out.println("Group not found");
        }
    }
}
