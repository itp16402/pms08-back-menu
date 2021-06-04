package org.pms.sammenu.services;

import org.pms.sammenu.dto.ProjectResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {

    List<ProjectResponseDto> fetchProjectsByUsername(String username);
}
