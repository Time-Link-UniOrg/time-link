package com.timelink.time_link.startup;

import com.timelink.time_link.model.Group;
import com.timelink.time_link.repository.GroupRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Order(0)
public class GroupSeeder implements CommandLineRunner {
    private final GroupRepository groupRepository;

    @Override
    public void run(String... args) throws Exception{
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 9, 0);
        Group group1 = new Group("Math", true, start , start.plusMonths(3));
        Group group2 = new Group("Python", true, start, start.plusMonths(1));
        groupRepository.save(group1);
        groupRepository.save(group2);
        System.out.println("Seeded group: Math");
        System.out.println("Seeded group: Python");
    }
}
