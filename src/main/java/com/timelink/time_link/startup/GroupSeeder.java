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
        Group group1 = new Group("Math", true, LocalDateTime.now(), LocalDateTime.now().plusMonths(3));
        groupRepository.save(group1);
    }
}
