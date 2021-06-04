package org.pms.sammenu.services;

import org.pms.sammenu.domain.User;
import org.pms.sammenu.dto.UserRoleRequestDto;
import org.pms.sammenu.dto.UserRoleResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface UserRoleService {

    Set<UserRoleResponseDto> fetchUserRolesByProjectId(Long projectId);

    void save(List<UserRoleRequestDto> userRoleRequestDtos, Long projectId);

    void removeMemberFromGroup(Long userId, Long projectId);

    void assignAdmin(Long userId, Long projectId);

    void removeAdmin(Long userId, Long projectId);
}
