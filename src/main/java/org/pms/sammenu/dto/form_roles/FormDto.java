package org.pms.sammenu.dto.form_roles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormDto {

    private String id;
    private String formName;
    private boolean checked;
    private Set<MemberNamesDto> members;
}
