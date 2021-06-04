//package org.pms.sammenu.utils;
//
//import org.pms.sammenu.domain.Authority;
//import org.pms.sammenu.domain.Project;
//import org.pms.sammenu.domain.User;
//import org.pms.sammenu.domain.UserRole;
//import org.pms.sammenu.dto.AssignRolesDto;
//import org.pms.sammenu.enums.AuthorityType;
//import org.pms.sammenu.repositories.ProjectRepository;
//import org.pms.sammenu.repositories.UserRepository;
//import org.pms.sammenu.repositories.UserRoleRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@Component
//public class RolesUtils {
//
//    private UserRoleRepository userRoleRepository;
//    private UserRepository userRepository;
//    private ProjectRepository projectRepository;
//
//    @Autowired
//    public RolesUtils(UserRoleRepository userRoleRepository, UserRepository userRepository, ProjectRepository projectRepository) {
//        this.userRoleRepository = userRoleRepository;
//        this.userRepository = userRepository;
//        this.projectRepository = projectRepository;
//    }
//
//    public AssignRolesDto assignRoles(Set<Long> memberTypes, Long projectId, Long userId){
//
//        Set<Authority> roles;
//        List<UserRole> userRoles;
//
//        boolean existsDifferentAdmin = existsDifferentAdmin(projectId, userId);
//
//        if (!memberTypes.contains(MemberType.SIGNER.code())) {
//
//            roles = Stream.of(Roles.OWNER, Roles.PARTNER)
//                    .collect(Collectors.toCollection(HashSet::new));
//
//            userRoles = Stream.of(
//                    buildMemberRole(projectId, userId, Roles.OWNER.code()),
//                    buildMemberRole(projectId, userId, Roles.PARTNER.code())
//            )
//                    .collect(Collectors.toCollection(ArrayList::new));
//
//            if (!existsDifferentAdmin){
//                roles.add(Roles.MANAGER);
//                userRoles.add(buildMemberRole(projectId, userId, Roles.MANAGER.code()));
//            }
//        }
//        else {
//
//            Long memberType = orderRepository.memberType(projectId, userId);
//
//            if (memberType.equals(MemberType.SIGNER.code())){
//
//                roles = Stream.of(Roles.PARTNER)
//                        .collect(Collectors.toCollection(HashSet::new));
//
//                userRoles = Stream.of(
//                        buildMemberRole(projectId, userId, Roles.PARTNER.code())
//                )
//                        .collect(Collectors.toCollection(ArrayList::new));
//            }
//            else {
//
//                roles = Stream.of(Roles.OWNER)
//                        .collect(Collectors.toCollection(HashSet::new));
//
//                userRoles = Stream.of(
//                        buildMemberRole(projectId, userId, Roles.OWNER.code())
//                )
//                        .collect(Collectors.toCollection(ArrayList::new));
//
//                if (!existsDifferentAdmin){
//                    roles.add(Roles.MANAGER);
//                    userRoles.add(buildMemberRole(projectId, userId, Roles.MANAGER.code()));
//                }
//            }
//        }
//
//        return AssignRolesDto.builder()
//                .roles(roles)
//                .memberRoles(userRoles)
//                .build();
//    }
//
//    private boolean existsDifferentAdmin(Long projectId, Long userId){
//
//        Optional<UserRole> userRole = userRoleRepository
//                .findByProjectAndAuthority(Project.builder().id(projectId).build(),
//                        Authority.builder().id(AuthorityType.MANAGER.code()).build());
//
//        return userRole.isPresent() && !userId.equals(userRole.get().getUser().getId());
//    }
//
//    private UserRole buildUserRole(Long projectId, Long userId, short roleId){
//
//        Optional<Project> project = projectRepository.findById(projectId);
//        if (!project.isPresent())
//            return new UserRole();
//
//        Optional<User> user = userRepository.findById(userId);
//
//        return UserRole.builder()
//                .user(user.orElse(null))
//                .project(project.get())
//                .authority(Authority.builder().id((int) roleId).build())
//                .build();
//    }
//}
