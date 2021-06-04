package org.pms.sammenu.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleRequestDto {

    private Long userId;
    private Short roleId;
}
