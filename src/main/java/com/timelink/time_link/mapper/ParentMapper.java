package com.timelink.time_link.mapper;

import com.timelink.time_link.dto.Parent.ParentRequestDTO;
import com.timelink.time_link.dto.Parent.ParentResponseDTO;
import com.timelink.time_link.model.Parent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ParentMapper {

    ParentResponseDTO toParentResponseDTO(Parent parent);

    List<ParentResponseDTO> toParentResponseDTOList(List<Parent> parents);

    @Mapping(target = "id", ignore = true)
    Parent toParent(ParentRequestDTO parentRequestDTO);
}