package com.timelink.time_link.mapper;

import com.timelink.time_link.dto.TeacherDTO;
import com.timelink.time_link.model.Teacher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeacherMapper {

    public TeacherDTO toDTO(Teacher teacher) {
        TeacherDTO dto = new TeacherDTO();
        dto.setId(teacher.getId());
        dto.setName(teacher.getName());
        dto.setPhone(teacher.getPhone());
        dto.setEmail(teacher.getEmail());
        dto.setStudents(teacher.getStudents());
        dto.setGroups(teacher.getGroups());
        dto.setCoursesId(teacher.getCoursesId());
        dto.setUsername(teacher.getUsername());
        dto.setPassword(teacher.getPassword());
        return dto;
    }

    public Teacher toEntity(TeacherDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setName(dto.getName());
        teacher.setPhone(dto.getPhone());
        teacher.setEmail(dto.getEmail());
        teacher.setStudents(dto.getStudents());
        teacher.setGroups(dto.getGroups());
        teacher.setCoursesId(dto.getCoursesId());
        teacher.setUsername(dto.getUsername());
        teacher.setPassword(dto.getPassword());
        return teacher;
    }

    public void updateEntity(Teacher teacher, TeacherDTO dto) {
        teacher.setName(dto.getName());
        teacher.setPhone(dto.getPhone());
        teacher.setEmail(dto.getEmail());
        teacher.setStudents(dto.getStudents());
        teacher.setGroups(dto.getGroups());
        teacher.setCoursesId(dto.getCoursesId());
        teacher.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            teacher.setPassword(dto.getPassword());
        }
    }

    public List<TeacherDTO> toDTOList(List<Teacher> teachers) {
        return teachers.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}