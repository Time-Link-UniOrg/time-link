package com.timelink.time_link.mapper;

import com.timelink.time_link.dto.Group.GroupRequestDTO;
import com.timelink.time_link.dto.Group.GroupResponseDTO;
import com.timelink.time_link.model.Group;
import com.timelink.time_link.model.Student;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GroupMapper {

    @Mapping(target = "studentIds", expression = "java(mapStudentsToIds(group))")
    GroupResponseDTO toGroupResponseDTO(Group group);

    List<GroupResponseDTO> toGroupResponseDTOList(List<Group> groups);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "students", ignore = true)
    Group toGroup(GroupRequestDTO groupRequestDTO);

    default List<Integer> mapStudentsToIds(Group group) {
        if (group.getStudents() == null) {
            return null;
        }
        return group.getStudents().stream()
                .map(Student::getId)
                .collect(Collectors.toList());
    }
}