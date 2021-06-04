package org.pms.sammenu.convert;

import org.pms.sammenu.domain.Authority;
import org.pms.sammenu.domain.User;
import org.pms.sammenu.domain.UserRole;
import org.pms.sammenu.dto.RoleDto;
import org.pms.sammenu.dto.UserRoleResponseDto;
import org.pms.sammenu.enums.AuthorityType;
import org.pms.sammenu.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class UserRoleToUserRoleResponseDtoConverter implements Converter<Set<UserRole>, UserRoleResponseDto> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserRoleResponseDto convert(Set<UserRole> userRoles) {

        UserRole userRole = userRoles.iterator().next();

        Optional<User> userOptional = userRepository.findById(userRole.getUser().getId());

        return UserRoleResponseDto.builder()
                .userId(userRole.getUser().getId())
                .firstName(userOptional.map(User::getFirstName).orElse(null))
                .lastName(userOptional.map(User::getLastName).orElse(null))
                .projectId(userRole.getProject().getId())
                .roles(buildRoles(userRoles))
                .build();
    }

    private Set<RoleDto> buildRoles(Set<UserRole> userRoles){

        Set<RoleDto> roleDtoSet = new HashSet<>();

        userRoles.forEach(userRole -> roleDtoSet.add(buildRole(userRole.getAuthority())));

        return roleDtoSet;
    }

    private RoleDto buildRole(Authority role){

        return RoleDto.builder()
                .id(role.getId())
                .description(AuthorityType.fromCode(role.getId()))
                .build();
    }
}
