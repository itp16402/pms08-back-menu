package org.pms.sammenu.dto.form_roles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pms.sammenu.dto.UserRoleResponseDto;
import org.pms.sammenu.dto.form_views.FormListDto;
import org.pms.sammenu.enums.FormStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormRoleDto {

    private FormListDto formList;
    private UserRoleResponseDto userRole;
    private FormStatus status;
}
