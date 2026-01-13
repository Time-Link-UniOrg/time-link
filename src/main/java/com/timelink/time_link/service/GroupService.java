package com.timelink.time_link.service;

import com.timelink.time_link.dto.Group.GroupRequestDTO;
import com.timelink.time_link.exception.ResourceNotFoundException;
import com.timelink.time_link.mapper.GroupMapper;
import com.timelink.time_link.model.Group;
import com.timelink.time_link.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    @Transactional
    public Group saveGroup(GroupRequestDTO groupRequestDTO) {
        Group group = groupMapper.toGroup(groupRequestDTO);
        return groupRepository.save(group);
    }

    @Transactional
    public void deleteGroup(Integer id) {
        if (groupRepository.existsById(id)) {
            groupRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(Group.class, id);
        }
    }

    public Optional<Group> getGroupById(Integer id) {
        return groupRepository.findById(id);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group getGroupOrThrow(Integer id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Group.class, id));
    }
}