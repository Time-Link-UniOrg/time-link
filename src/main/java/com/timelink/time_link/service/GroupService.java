package com.timelink.time_link.service;

import com.timelink.time_link.model.Group;
import com.timelink.time_link.repository.GroupRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group putGroup(Long id, Group newGroup) {
        Group existingGroup = groupRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Group not found"));

        existingGroup.setName(newGroup.getName());
        existingGroup.setActive(newGroup.getActive());
        existingGroup.setStartTime(newGroup.getStartTime());
        existingGroup.setEndTime(newGroup.getEndTime());

        return groupRepository.save(existingGroup);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group getGroupById(Long id) {
        return groupRepository.findById(id).orElse(null);
    }

    public Group saveGroup(Group group) {
        return groupRepository.save(group);
    }

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }
}
