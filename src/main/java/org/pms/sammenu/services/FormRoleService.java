package org.pms.sammenu.services;

import org.pms.sammenu.dto.form_roles.FormRoleDto;
import org.pms.sammenu.dto.form_roles.ParentPhaseDto;
import org.pms.sammenu.enums.Locale;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface FormRoleService {

    void assignWorkToMembers(Long memberId, Long orderId, Set<String> formNames);

    void removeWorkFromMembers(Long memberId, Long orderId, Set<String> formNames);

    boolean checkIfMemberIsAssignedInForm(Long memberId, Long orderId, Long formListId);

    List<FormRoleDto> fetchByMember(Long memberId, Long orderId);

    List<ParentPhaseDto> fetchFlowchartWithAssignedMembers(Long memberId, Locale language, Long orderId);
}
