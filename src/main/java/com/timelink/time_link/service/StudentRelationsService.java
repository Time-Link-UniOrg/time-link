package com.timelink.time_link.service;

import com.timelink.time_link.model.Group;
import com.timelink.time_link.model.Parent;
import com.timelink.time_link.model.Student;
import com.timelink.time_link.repository.GroupRepository;
import com.timelink.time_link.repository.ParentRepository;
import com.timelink.time_link.repository.StudentRepository;
import com.timelink.time_link.dto.Group.GroupResponseDTO;
import com.timelink.time_link.dto.Parent.ParentResponseDTO;
import com.timelink.time_link.mapper.GroupMapper;
import com.timelink.time_link.mapper.ParentMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StudentRelationsService {

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final ParentRepository parentRepository;

    private final GroupMapper groupMapper;
    private final ParentMapper parentMapper;


    @Transactional
    public void assignStudentToGroup(Integer studentId, Integer groupId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found"));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("Group not found"));

        student.setGroup(group);
        studentRepository.save(student);
    }

    @Transactional
    public void removeStudentFromGroup(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found"));

        student.setGroup(null);
        studentRepository.save(student);
    }

    @Transactional
    public void addParentToStudent(Integer studentId, Integer parentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found"));
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new NoSuchElementException("Parent not found"));

        parent.addChild(student); // ти вече го имаш
        studentRepository.save(student);
    }

    @Transactional
    public void removeParentFromStudent(Integer studentId, Integer parentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found"));
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new NoSuchElementException("Parent not found"));

        parent.removeChild(student);
        studentRepository.save(student);
    }
    public GroupResponseDTO getStudentGroup(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Group group = student.getGroup();
        if (group == null) {
            return null; // или throw, ако предпочиташ
        }

        return groupMapper.toGroupResponseDTO(group);
    }

    public List<ParentResponseDTO> getStudentParents(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        return parentMapper.toParentResponseDTOList(student.getParents().stream().toList());
    }
}

