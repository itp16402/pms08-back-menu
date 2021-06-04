package org.pms.sammenu.convert.form_roles;

import org.pms.sammenu.domain.Authority;
import org.pms.sammenu.domain.FormRole;
import org.pms.sammenu.domain.UserRole;
import org.pms.sammenu.domain.form_views.FormList;
import org.pms.sammenu.dto.RoleDto;
import org.pms.sammenu.dto.UserRoleResponseDto;
import org.pms.sammenu.dto.form_roles.FormRoleDto;
import org.pms.sammenu.dto.form_views.FormListDto;
import org.pms.sammenu.enums.AuthorityType;
import org.pms.sammenu.enums.FormStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.Set;

@Component
public class FormRoleToFormRoleDtoConverter implements Converter<FormRole, FormRoleDto> {

    @Override
    public FormRoleDto convert(FormRole formRole) {

        return FormRoleDto.builder()
                .formList(buildFormListDto(formRole.getFormList()))
                .userRole(buildMemberRoleResponseDto(formRole.getUserRole()))
                .status(!ObjectUtils.isEmpty(formRole.getState()) ? FormStatus.fromValue(formRole.getState()) : null)
                .build();
    }

    private FormListDto buildFormListDto(FormList formList){

        return FormListDto.builder()
                .id(formList.getId())
                .formName(formList.getFormName())
                .build();
    }

    private UserRoleResponseDto buildMemberRoleResponseDto(UserRole userRole) {

        return UserRoleResponseDto.builder()
                .userId(userRole.getUser().getId())
                .projectId(userRole.getProject().getId())
                .roles(buildRoles(userRole))
                .build();
    }

    private Set<RoleDto> buildRoles(UserRole userRole){

        Set<RoleDto> roleDtoSet = new HashSet<>();

        roleDtoSet.add(buildRole(userRole.getAuthority()));

        return roleDtoSet;
    }

    private RoleDto buildRole(Authority role){

        return RoleDto.builder()
                .id(role.getId())
                .description(AuthorityType.fromCode(role.getId()))
                .build();
    }
}
