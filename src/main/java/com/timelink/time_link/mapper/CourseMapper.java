package com.timelink.time_link.mapper;

import com.timelink.time_link.dto.Course.CourseRequestDTO;
import com.timelink.time_link.dto.Course.CourseResponseDTO;
import com.timelink.time_link.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseMapper {

    CourseResponseDTO toCourseResponseDTO(Course course);

    List<CourseResponseDTO> toCourseResponseDTOList(List<Course> courses);

    @Mapping(target = "id", ignore = true)
    Course toCourse(CourseRequestDTO courseRequestDTO);
}