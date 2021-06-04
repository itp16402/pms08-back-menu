package org.pms.sammenu.dto;

import lombok.*;
import org.pms.sammenu.domain.Authority;
import org.pms.sammenu.domain.UserRole;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignRolesDto {

    private Set<Authority> roles;
    private List<UserRole> userRoles;
}
