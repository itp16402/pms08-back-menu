package org.pms.sammenu.convert;

import org.pms.sammenu.domain.Project;
import org.pms.sammenu.dto.ProjectResponseDto;
import org.pms.sammenu.enums.AuthorityType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class ProjectToProjectResponseDtoConverter implements Converter<Project, ProjectResponseDto> {

    @Override
    public ProjectResponseDto convert(Project project) {
        return ProjectResponseDto.builder()
                .id(project.getId())
                .orderId(project.getOrderId())
                .year(project.getYear())
                .recordDate(project.getRecordDate())
                .assignmentControlHours(project.getAssignmentControlHours())
                .customerName(project.getCustomerName())
                .status(project.getStatus())
                .orderTypeDescription(project.getOrderTypeDescription())
                .orderTypeComments(project.getOrderTypeComments())
                .roles(project.getAuthorities().stream()
                        .map(authority -> AuthorityType.fromCode(authority.getId()))
                        .collect(Collectors.toSet()))
                .build();
    }
}
