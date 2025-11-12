package com.timelink.time_link.mapper;

import com.timelink.time_link.dto.ParentDTO;
import com.timelink.time_link.model.Parent;


public class ParentMapper {

    public static ParentDTO toDTO(Parent parent) {
        if (parent == null) return null;

        ParentDTO dto = new ParentDTO();
        dto.setId(parent.getId());
        dto.setName(parent.getName());
        dto.setPhone(parent.getPhone());
        dto.setEmail(parent.getEmail());
        dto.setChild(parent.getChild());
        dto.setPaid(parent.isPaid());
        dto.setUsername(parent.getUsername());
        // НЯМА password — скриваме го от клиента
        return dto;
    }

    public static Parent toEntity(ParentDTO dto) {
        if (dto == null) return null;

        Parent parent = new Parent();
        parent.setId(dto.getId());
        parent.setName(dto.getName());
        parent.setPhone(dto.getPhone());
        parent.setEmail(dto.getEmail());
        parent.setChild(dto.getChild());
        parent.setPaid(dto.isPaid());
        parent.setUsername(dto.getUsername());
        // password не се подава от DTO
        return parent;
    }
}
