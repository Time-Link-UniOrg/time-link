package com.timelink.time_link.mapper;
import com.timelink.time_link.dto.Teacher.TeacherRequestDTO;
import com.timelink.time_link.dto.Teacher.TeacherResponseDTO;
import com.timelink.time_link.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TeacherMapper {

    TeacherResponseDTO toTeacherResponseDTO(Teacher teacher);

    List<TeacherResponseDTO> toTeacherResponseDTOList(List<Teacher> teachers);

    @Mapping(target = "id", ignore = true)
    Teacher toTeacher(TeacherRequestDTO teacherRequestDTO);
}