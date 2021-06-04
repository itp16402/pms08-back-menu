package org.pms.sammenu.dto;

import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleResponseDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private Long projectId;
    private Set<RoleDto> roles;
}
