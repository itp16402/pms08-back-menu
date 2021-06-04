package org.pms.sammenu.dto;

import lombok.*;
import org.pms.sammenu.enums.AuthorityType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private short id;
    private AuthorityType description;
}
