package org.pms.sammenu.services;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.domain.*;
import org.pms.sammenu.domain.form_views.FormList;
import org.pms.sammenu.dto.UserRoleRequestDto;
import org.pms.sammenu.dto.UserRoleResponseDto;
import org.pms.sammenu.enums.AuthorityType;
import org.pms.sammenu.exceptions.AuthorizationFailedException;
import org.pms.sammenu.exceptions.ResourceNotFoundException;
import org.pms.sammenu.repositories.FormRoleRepository;
import org.pms.sammenu.repositories.UserRepository;
import org.pms.sammenu.repositories.UserRoleRepository;
import org.pms.sammenu.repositories.form_views.FormListRepository;
import org.pms.sammenu.utils.ProjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UserRoleServiceImpl implements UserRoleService {

    private final Set<Short> ROLE_IDS = Stream
            .of(AuthorityType.OWNER.code(), AuthorityType.MANAGER.code(), AuthorityType.PARTNER.code())
            .collect(Collectors.toSet());

    private UserRoleRepository userRoleRepository;
    private UserRepository userRepository;
    private FormRoleRepository formRoleRepository;
    private FormListRepository formListRepository;
    private ProjectUtils projectUtils;
    private ConversionService conversionService;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository, UserRepository userRepository,
                               FormRoleRepository formRoleRepository, FormListRepository formListRepository,
                               ProjectUtils projectUtils, ConversionService conversionService) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.formRoleRepository = formRoleRepository;
        this.formListRepository = formListRepository;
        this.projectUtils = projectUtils;
        this.conversionService = conversionService;
    }

    @Override
    public Set<UserRoleResponseDto> fetchUserRolesByProjectId(Long projectId) {

        log.info("Fetch User-Roles for order[{}] process starts", projectId);

        Set<Long> userIds = userRoleRepository.findUserIds(projectId);

        Set<UserRoleResponseDto> userRoleResponseDtoSet = new HashSet<>();

        userIds.forEach(userId -> {

            Set<UserRole> userRoles = userRoleRepository
                    .findByUser_IdAndProject(userId, Project.builder().id(projectId).build());

            UserRoleResponseDto userRoleResponseDto = conversionService.convert(userRoles, UserRoleResponseDto.class);

            userRoleResponseDtoSet.add(userRoleResponseDto);
        });

        log.info("Fetch User-Roles for order[{}] process end", projectId);

        return userRoleResponseDtoSet;
    }

    @Override
    public void save(List<UserRoleRequestDto> userRoleRequestDtos, Long projectId) {

        log.info("Save new UserRoles start");

        Project project = projectUtils.fetchProject(projectId);

        userRoleRequestDtos.forEach(userRoleRequestDto -> {

            deletePreviousRoles(userRoleRequestDto.getUserId(), projectId);

            if (ROLE_IDS.contains(userRoleRequestDto.getRoleId()))
                throw new AuthorizationFailedException("You can assign only a member or reviewer.");

            Optional<User> user = userRepository.findById(userRoleRequestDto.getUserId());

            UserRole userRole = UserRole.builder()
                    .user(user.orElse(null))
                    .project(project)
                    .authority(Authority.builder().id(userRoleRequestDto.getRoleId()).build())
                    .build();

            userRoleRepository.save(userRole);

            User savedUser = userRole.getUser();
            savedUser.setActive((short) 1);
            userRepository.save(savedUser);
        });

        log.info("Save new MemberRoles end");
    }

    @Override
    public void removeMemberFromGroup(Long userId, Long projectId) {

        log.info("Remove user {} from project {} process begins", userId, projectId);

        Set<UserRole> userRoles = userRoleRepository
                .findByUser_IdAndProject(userId, Project.builder().id(projectId).build());

        userRoles.forEach(userRole -> {

            if (ROLE_IDS.contains(userRole.getAuthority().getId()))
                throw new AuthorizationFailedException("You can remove only a member or a reviewer.");

            formRoleRepository.deleteFormRolesByUserRoleId(userRole.getId());
            userRoleRepository.delete(userRole);
            inActivateUser(userId);
        });

        log.info("Remove member {} from order {} process end", userId, projectId);
    }

    @Override
    public void assignAdmin(Long userId, Long projectId) {

        log.info("Assign as Admin user with id: {} for project: {} process starts", userId, projectId);

        deletePreviousRoles(userId, projectId);

        Optional<UserRole> userRole = userRoleRepository.findByProjectAndAuthority(Project.builder().id(projectId).build(),
                Authority.builder().id(AuthorityType.MANAGER.code()).build());

        userRole.ifPresent(oldAdmin -> {

            formRoleRepository.deleteFormRolesByUserRoleId(oldAdmin.getId());

            userRoleRepository.delete(oldAdmin);

            Optional<User> user = userRepository.findById(userId);

            UserRole newAdmin = userRoleRepository.save(UserRole.builder()
                    .user(user.orElse(null))
                    .authority(Authority.builder().id(AuthorityType.MANAGER.code()).build())
                    .project(oldAdmin.getProject())
                    .build());

            saveFormRoles(newAdmin);
            activateUser(newAdmin);
        });

        userRole.orElseThrow(() -> new ResourceNotFoundException("Problem. In this project there is not admin"));

        log.info("Assign as Admin user with id: {} for project: {} process end", userId, projectId);
    }

    @Override
    public void removeAdmin(Long userId, Long projectId) {

        log.info("Remove Admin {} from project {} process begins", userId, projectId);

        Set<UserRole> userRoles = preventToRemoveOwnerOrPartner(userId, projectId);

        userRoles = userRoles.stream()
                .filter(userRole -> userRole.getAuthority().getId() == AuthorityType.MANAGER.code())
                .collect(Collectors.toSet());

        if (ObjectUtils.isEmpty(userRoles) || userRoles.isEmpty())
            throw new ResourceNotFoundException("There is not admin with userId: " + userId);

        UserRole oldAdmin = userRoles.iterator().next();

        formRoleRepository.deleteFormRolesByUserRoleId(oldAdmin.getId());

        userRoleRepository.delete(oldAdmin);

        inActivateUser(userId);

        Optional<UserRole> userRole = userRoleRepository
                .findByProjectAndAuthority(Project.builder().id(projectId).build(),
                        Authority.builder().id(AuthorityType.OWNER.code()).build());

        if (userRole.isPresent()){

            UserRole newAdmin = userRoleRepository.save(UserRole.builder()
                    .user(userRole.get().getUser())
                    .project(userRole.get().getProject())
                    .authority(Authority.builder().id(AuthorityType.MANAGER.code()).build())
                    .build());

            saveFormRoles(newAdmin);
        }

        log.info("Remove Admin {} from project {} process end", userId, projectId);
    }

    private void activateUser(UserRole userRole){

        User savedUser = userRole.getUser();
        savedUser.setActive((short) 1);
        userRepository.save(savedUser);
    }

    private void inActivateUser(Long userId){

        Optional<User> userOptional = userRepository.findById(userId);

        userOptional.ifPresent(user -> {

            boolean userBelongsToAProject = checkIfUserBelongsToProject(user);
            if (userBelongsToAProject){

                user.setActive((short) 0);
                userRepository.save(user);
            }
        });
    }

    private boolean checkIfUserBelongsToProject(User user){

        List<UserRole> userRoles = userRoleRepository.findByUser_Id(user.getId());

        return !userRoles.isEmpty();
    }

    private void saveFormRoles(UserRole newAdmin){

        List<FormList> formLists = formListRepository.findAll();

        formLists.forEach(formList -> formRoleRepository.save(FormRole.builder()
                .formList(formList)
                .userRole(newAdmin)
                .build()));
    }

    private void deletePreviousRoles(Long userId, Long projectId){

        Set<UserRole> userRoles = preventToRemoveOwnerOrPartner(userId, projectId);

        if (!userRoles.isEmpty()){

            UserRole userRole = userRoles.iterator().next();

            formRoleRepository.deleteFormRolesByUserRoleId(userRole.getId());

            userRoleRepository.deleteUserRoleByUserIdAndProjectId(userId, projectId);
        }
    }

    private Set<UserRole> preventToRemoveOwnerOrPartner(Long userId, Long projectId){

        Set<Short> unauthorizedRoleIds = Stream.of(AuthorityType.OWNER.code(), AuthorityType.PARTNER.code()).collect(Collectors.toSet());

        Set<UserRole> userRoles = userRoleRepository
                .findByUser_IdAndProject(userId, Project.builder().id(projectId).build());

        userRoles.forEach(userRole -> {
            if (unauthorizedRoleIds.contains(userRole.getAuthority().getId()))
                throw new AuthorizationFailedException("Owner or Signer of an Order cannot be removed.");
        });

        return userRoles;
    }
}
