package org.pms.sammenu.services;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.domain.Authority;
import org.pms.sammenu.domain.Project;
import org.pms.sammenu.domain.UserRole;
import org.pms.sammenu.dto.ProjectResponseDto;
import org.pms.sammenu.enums.AuthorityType;
import org.pms.sammenu.repositories.ProjectRepository;
import org.pms.sammenu.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ProjectServiceImpl implements ProjectService {

    private UserRoleRepository userRoleRepository;
    private ProjectRepository projectRepository;
    private ConversionService conversionService;

    @Autowired
    public ProjectServiceImpl(UserRoleRepository userRoleRepository,
                              ProjectRepository projectRepository,
                              ConversionService conversionService) {
        this.userRoleRepository = userRoleRepository;
        this.projectRepository = projectRepository;
        this.conversionService = conversionService;
    }

    @Override
    public List<ProjectResponseDto> fetchProjectsByUsername(String username) {

        log.info("Find projects from user {} process starts", username);

        if (username.equals("god"))
            return new ArrayList<>();

        List<UserRole> userRoles = userRoleRepository.findByUser_Username(username);

        Set<Project> projects = userRoles.stream().map(UserRole::getProject).collect(Collectors.toSet());
        Set<Authority> authorities = userRoles.stream().map(UserRole::getAuthority).collect(Collectors.toSet());

        List<ProjectResponseDto> projectResponseDtoList = projects.stream()
                .map(project -> {

                    ProjectResponseDto projectResponseDto = conversionService.convert(project, ProjectResponseDto.class);
                    if (!ObjectUtils.isEmpty(projectResponseDto))
                        projectResponseDto.setRoles(authorities.stream()
                                .map(authority -> AuthorityType.fromCode(authority.getId()))
                                .collect(Collectors.toSet()));
                    return projectResponseDto;
                })
                .collect(Collectors.toList());

        log.info("Find projects from user {} process end", username);

        return projectResponseDtoList;
    }

    @Override
    public ProjectResponseDto fetchProjectById(String username, Long id) {

        log.info("Find project by id[{}] process starts", id);

        Optional<Project> projectOptional = projectRepository.findById(id);
        List<UserRole> userRoles = userRoleRepository.findByUser_Username(username);
        Set<Authority> authorities = userRoles.stream().map(UserRole::getAuthority).collect(Collectors.toSet());

        ProjectResponseDto projectResponseDto = projectOptional
                .map(project -> {

                    ProjectResponseDto responseDto = conversionService.convert(project, ProjectResponseDto.class);
                    if (!ObjectUtils.isEmpty(responseDto))
                        responseDto.setRoles(authorities.stream()
                                .map(authority -> AuthorityType.fromCode(authority.getId()))
                                .collect(Collectors.toSet()));

                    return responseDto;
                })
                .orElse(new ProjectResponseDto());

        log.info("Find project by id[{}] process end", id);

        return projectResponseDto;
    }
}
