package org.pms.sammenu.services;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.domain.FormRole;
import org.pms.sammenu.domain.Project;
import org.pms.sammenu.domain.UserRole;
import org.pms.sammenu.domain.form_views.Flowchart;
import org.pms.sammenu.domain.form_views.FlowchartChild;
import org.pms.sammenu.domain.form_views.FlowchartParent;
import org.pms.sammenu.domain.form_views.FormList;
import org.pms.sammenu.dto.form_roles.*;
import org.pms.sammenu.enums.AuthorityType;
import org.pms.sammenu.enums.FormType;
import org.pms.sammenu.enums.Locale;
import org.pms.sammenu.exceptions.AuthorizationFailedException;
import org.pms.sammenu.exceptions.ResourceNotFoundException;
import org.pms.sammenu.repositories.FormRoleRepository;
import org.pms.sammenu.repositories.UserRoleRepository;
import org.pms.sammenu.repositories.form_views.FlowchartParentRepository;
import org.pms.sammenu.repositories.form_views.FormListRepository;
import org.pms.sammenu.utils.BasicInfoUtils;
import org.pms.sammenu.utils.FormUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class FormRoleServiceImpl implements FormRoleService {

    private final Set<Short> ROLE_IDS = Stream
            .of(AuthorityType.OWNER.code(), AuthorityType.MANAGER.code(), AuthorityType.PARTNER.code())
            .collect(Collectors.toSet());

    private FormRoleRepository formRoleRepository;
    private FlowchartParentRepository flowchartParentRepository;
    private UserRoleRepository userRoleRepository;
    private FormListRepository formListRepository;
    private BasicInfoUtils basicInfoUtils;
    private ConversionService conversionService;
    private FormUtils formUtils;

    public FormRoleServiceImpl(FormRoleRepository formRoleRepository,
                               FlowchartParentRepository flowchartParentRepository,
                               UserRoleRepository userRoleRepository,
                               FormListRepository formListRepository, BasicInfoUtils basicInfoUtils,
                               ConversionService conversionService, FormUtils formUtils) {
        this.formRoleRepository = formRoleRepository;
        this.flowchartParentRepository = flowchartParentRepository;
        this.userRoleRepository = userRoleRepository;
        this.formListRepository = formListRepository;
        this.basicInfoUtils = basicInfoUtils;
        this.conversionService = conversionService;
        this.formUtils = formUtils;
    }

    @Override
    public void assignWorkToMembers(Long memberId, Long projectId, Set<String> formNames) {

        log.info("Assign work to member [id:{}] process begins", memberId);

        Set<UserRole> userRoles = userRoleRepository
                .findByUser_IdAndProject(memberId, Project.builder().id(projectId).build());

        userRoles.forEach(memberRole -> {
            if (ROLE_IDS.contains(memberRole.getAuthority().getId()))
                throw new AuthorizationFailedException("You must not assign work to owner, partner or admin.");
        });

        UserRole userRole = userRoles.iterator().next();

        List<FormList> formLists = formListRepository.findByFormNameIn(formNames);

        formLists.forEach(formList -> {

            Optional<FormRole> formRole = formRoleRepository.findByFormListAndUserRole(formList, userRole);

            if (!formRole.isPresent())
                formRoleRepository.save(FormRole.builder()
                        .formList(formList)
                        .userRole(userRole)
                        .build());
        });

        log.info("Assign work to member [id:{}] process end", memberId);
    }

    @Override
    public void removeWorkFromMembers(Long memberId, Long projectId, Set<String> formNames) {

        log.info("Remove work from member [id:{}] process begins", memberId);

        UserRole userRole = buildUserRole(memberId, projectId);

        List<FormList> formLists = formListRepository.findByFormNameIn(formNames);

        formLists.forEach(formList -> {

            Optional<FormRole> optionalFormRole = formRoleRepository.findByFormListAndUserRole(formList, userRole);

            optionalFormRole.ifPresent(formRole -> formRoleRepository.delete(formRole));
        });

        log.info("Remove work from member [id:{}] process end", memberId);
    }

    @Override
    public boolean checkIfMemberIsAssignedInForm(Long memberId, Long orderId, Long formListId) {

        log.info("Fetch form-role for member[id:{}] in form-list[id:{}] process begins", memberId, formListId);

        UserRole userRole = buildUserRole(memberId, orderId);

        Optional<FormRole> optionalFormRole = formRoleRepository
                .findByFormListAndUserRole(FormList.builder().id(formListId).build(), userRole);

        log.info("Fetch form-role for member[id:{}] in form-list[id:{}] process end", memberId, formListId);

        return optionalFormRole.isPresent();
    }

    @Override
    public List<FormRoleDto> fetchByMember(Long memberId, Long orderId) {

        log.info("Fetch form-roles for member[id:{}] in order[id:{}] process begins", memberId, orderId);

        UserRole userRole = buildUserRole(memberId, orderId);

        List<FormRole> formRoles = formRoleRepository.findByUserRole(userRole);

        List<FormRoleDto> formRoleDtos = formRoles
                .stream()
                .map(formRole -> conversionService.convert(formRole, FormRoleDto.class))
                .collect(Collectors.toList());

        log.info("Fetch form-roles for member[id:{}] in order[id:{}] process end", memberId, orderId);

        return formRoleDtos;
    }

    @Override
    public List<ParentPhaseDto> fetchFlowchartWithAssignedMembers(Long memberId, Locale language, Long projectId) {

        log.info("Fetch flowchart with assigned members for order [id:{}] process begins", projectId);

        FormType formType = basicInfoUtils.getFormTypeByProjectId(projectId);

        List<FlowchartParent> flowchartParents;

        if (formType.equals(FormType.OTA))
            flowchartParents = flowchartParentRepository
                    .findByLanguageAndFormTypeOrderBySeqOrderAsc(language.code(), FormType.OTA.code());
        else
            flowchartParents = flowchartParentRepository
                    .findByLanguageAndFormTypeOrFormTypeAndLanguageOrderBySeqOrderAsc(language.code(), FormType.T.code(),
                            FormType.F.code(), language.code());

        List<ParentPhaseDto> parentPhases = flowchartParents.stream()
                .map(flowchartParent -> {

                    List<PhaseDto> phases = buildPhases(flowchartParent.getFlowcharts(), memberId, language, formType,
                            projectId);

                    Set<Boolean> checkedPhases = phases.stream().map(PhaseDto::isChecked).collect(toSet());

                    return ParentPhaseDto.builder()
                            .phase(flowchartParent.getPhase())
                            .name(flowchartParent.getName())
                            .sOrder(flowchartParent.getSeqOrder())
                            .phases(phases)
                            .checked(!checkedPhases.contains(false))
                            .build();
                })
                .collect(Collectors.toList());

        log.info("Fetch flowchart with assigned members for order [id:{}] process end", projectId);

        return parentPhases;
    }

    private List<PhaseDto> buildPhases(List<Flowchart> flowcharts,
                                       Long memberId,
                                       Locale language,
                                       FormType formType,
                                       Long orderId){

        return flowcharts.stream()
                .map(flowchart -> {

                    List<StepDto> steps = buildSteps(flowchart.getFlowchartChildren(), memberId, language, formType, orderId);

                    Set<Boolean> checkedSteps = steps.stream().map(StepDto::isChecked).collect(toSet());

                    return PhaseDto.builder()
                            .phase(flowchart.getPhase())
                            .name(flowchart.getName())
                            .sOrder(flowchart.getSeqOrder())
                            .steps(steps)
                            .checked(!checkedSteps.contains(false))
                            .build();
                })
                .sorted(Comparator.comparing(PhaseDto::getSOrder))
                .collect(Collectors.toList());
    }

    private List<StepDto> buildSteps(List<FlowchartChild> flowchartChildren,
                                     Long memberId,
                                     Locale language,
                                     FormType formType,
                                     Long orderId){

        return flowchartChildren.stream()
                .map(flowchartChild -> {

                    List<FormDto> forms = buildForms(flowchartChild.getFormName(), memberId, language, formType, orderId);

                    Set<Boolean> checkedForms = forms.stream().map(FormDto::isChecked).collect(toSet());

                    return StepDto.builder()
                            .phase(flowchartChild.getPhase())
                            .name(flowchartChild.getName())
                            .sOrder(flowchartChild.getSeqOrder())
                            .forms(forms)
                            .checked(!checkedForms.contains(false))
                            .build();
                })
                .sorted(Comparator.comparing(StepDto::getSOrder))
                .collect(Collectors.toList());
    }

    private List<FormDto> buildForms(String formNames, Long memberId, Locale language, FormType formType, Long orderId){

        List<String> formNameList = Stream
                .of(formNames.split(",", -1))
                .collect(Collectors.toList());

        return formNameList.stream()
                .map(formName -> {

                    Set<UserRole> assignedMembers = userRoleRepository.findAssignedUsersByForm(formName, orderId);

                    Set<Long> assignedMembersIds = assignedMembers.stream().map(userRole -> userRole.getUser().getId())
                            .collect(toSet());

                    return FormDto.builder()
                            .id(formName)
                            .formName(formUtils.getFormName(formName, language, formType))
                            .checked(assignedMembersIds.contains(memberId))
                            .members(buildMembers(assignedMembers))
                            .build();
                })
                .collect(Collectors.toList());
    }

    private Set<MemberNamesDto> buildMembers(Set<UserRole> assignedMembers){

        return assignedMembers.stream()
                .map(assignedMember -> MemberNamesDto.builder()
                        .memberId(assignedMember.getUser().getId())
                        .firstName(assignedMember.getUser().getFirstName())
                        .lastName(assignedMember.getUser().getLastName())
                        .build())
                .collect(Collectors.toSet());
    }

    private UserRole buildUserRole(Long userId, Long projectId){

        Set<UserRole> userRoles = userRoleRepository
                .findByUser_IdAndProject(userId, Project.builder().id(projectId).build());

        if (userRoles.isEmpty())
            throw new ResourceNotFoundException(MessageFormat
                    .format("Member [id:{0}] in Order [id:{1}] does not exist", userId, projectId));

        if (userRoles.size() > 1)
            userRoles = userRoles.stream().filter(memberRole -> memberRole.getAuthority().getId() == AuthorityType.MANAGER.code() ||
                    memberRole.getAuthority().getId() == AuthorityType.MEMBER.code())
                    .collect(Collectors.toSet());

        return !userRoles.isEmpty() ? userRoles.iterator().next() : null;
    }
}
