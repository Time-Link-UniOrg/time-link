package com.timelink.time_link.mapper;

import com.timelink.time_link.dto.Student.StudentRequestDTO;
import com.timelink.time_link.dto.Student.StudentResponseDTO;
import com.timelink.time_link.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StudentMapper {

    @Mapping(target = "groupId", expression = "java(mapGroupToId(student))")
    StudentResponseDTO toStudentResponseDTO(Student student);

    List<StudentResponseDTO> toStudentResponseDTOList(List<Student> students);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    Student toStudent(StudentRequestDTO studentRequestDTO);

    default Long mapGroupToId(Student student) {
        if (student.getGroup() == null) {
            return null;
        }
        return student.getGroup().getId();
    }
}