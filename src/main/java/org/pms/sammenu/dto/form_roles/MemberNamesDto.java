package org.pms.sammenu.dto.form_roles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberNamesDto {

    private Long memberId;
    private String firstName;
    private String lastName;
}
