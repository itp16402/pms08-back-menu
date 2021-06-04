package org.pms.sammenu.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.pms.sammenu.config.security.JwtTokenProvider;
import org.pms.sammenu.domain.*;
import org.pms.sammenu.domain.form_views.FormList;
import org.pms.sammenu.enums.AuthorityType;
import org.pms.sammenu.exceptions.AuthorizationFailedException;
import org.pms.sammenu.exceptions.ResourceNotFoundException;
import org.pms.sammenu.repositories.FormRoleRepository;
import org.pms.sammenu.repositories.UserRepository;
import org.pms.sammenu.repositories.UserRoleRepository;
import org.pms.sammenu.repositories.form_views.FormListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Component
public class AspectUtils {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private FormRoleRepository formRoleRepository;

    @Autowired
    private FormListRepository formListRepository;

    public Object getParameterByName(ProceedingJoinPoint proceedingJoinPoint, String parameterName) {
        MethodSignature methodSig = (MethodSignature) proceedingJoinPoint.getSignature();
        Object[] args = proceedingJoinPoint.getArgs();
        String[] parametersName = methodSig.getParameterNames();
        int idx = Arrays.asList(parametersName).indexOf(parameterName);
        if(args.length > idx) { // parameter exist
            return args[idx];
        } // otherwise your parameter does not exist by given name
        return null;
    }

    public FormRole checkIfAssignedToWork(HttpServletRequest request){

        Optional<FormRole> formRole = getFormRoleOptional(request);

        if (!formRole.isPresent())
            throw new AuthorizationFailedException("Unauthorized for this action. Member has not assigned to form.");

        return formRole.get();
    }

    private Optional<FormRole> getFormRoleOptional(HttpServletRequest request){

        Set<UserRole> userRoles = fetchUserRoles(request)
                .stream()
                .filter(userRole -> userRole.getAuthority().getId() == AuthorityType.MANAGER.code() ||
                        userRole.getAuthority().getId() == AuthorityType.MEMBER.code())
                .collect(toSet());

        if (userRoles.isEmpty())
            throw new AuthorizationFailedException(MessageFormat
                    .format("Unauthorized for this action. User has not any of these roles {0} "
                            , AuthorityType.MANAGER + ", " + AuthorityType.MEMBER));

        UserRole userRole = userRoles.iterator().next();
        FormList formList = getFormList(request);

        return formRoleRepository.findByFormListAndUserRole(formList, userRole);
    }

    public Set<Authority> fetchRoles(HttpServletRequest request){

        Set<UserRole> userRoles = fetchUserRoles(request);

        return userRoles.stream()
                .map(userRole -> Authority.builder().id(userRole.getAuthority()
                        .getId())
                        .description(AuthorityType.fromCode(userRole.getAuthority().getId()).description())
                        .build())
                .collect(toSet());
    }

    public void checkIfRoleIsAuthorized(Set<Authority> roles, Authority ...authorizedRoles){

        Set<Authority> authorizedRoleSet = Stream.of(authorizedRoles).collect(toSet());

        Set<AuthorityType> authorizedRoleDescriptions = authorizedRoleSet
                .stream()
                .map(role -> AuthorityType.fromCode(role.getId()))
                .collect(toSet());

        boolean isAuthorized = authorizedRoleSet.stream()
                .map(Authority::getId)
                .anyMatch(
                        roles.stream()
                                .map(Authority::getId)
                                .collect(toSet())
                                ::contains);

        if (!isAuthorized)
            throw new AuthorizationFailedException(MessageFormat
                    .format("Unauthorized for this action. User has not any of these roles {0} "
                            , authorizedRoleDescriptions));
    }

    private Set<UserRole> fetchUserRoles(HttpServletRequest request){

        Set<UserRole> userRoles = null;

        Long projectId = getProjectId(request);

        Long userId = null;

        if (!ObjectUtils.isEmpty(projectId)){

            userId = getUserIdFromHeader(request);

            userRoles =  userRoleRepository
                    .findByUser_IdAndProject(userId, Project.builder().id(projectId).build());
        }

        if (ObjectUtils.isEmpty(userRoles))
            throw new AuthorizationFailedException(
                    MessageFormat
                            .format("Unauthorized for this action. Member [id: {0}] does not belong to Order [id: {1}]"
                                    , userId, projectId));

        return userRoles;
    }

    private Long getUserIdFromHeader(HttpServletRequest request){

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            String  jwt = authorizationHeader.substring(7);

            String username = jwtTokenProvider.extractUsername(jwt);

            User user = getUser(userRepository.findByUsername(username));

            return user.getId();
        }

        return null;
    }

    private Long getProjectId(HttpServletRequest request){

        Long projectId = null;

        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String orderIdPathVariable = (String) pathVariables.get("projectId");

        if (!ObjectUtils.isEmpty(orderIdPathVariable))
            projectId = Long.valueOf((String) pathVariables.get("projectId"));

        return projectId;
    }

    private FormList getFormList(HttpServletRequest request){

        FormList formList;

        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String formListIdPathVariable = (String) pathVariables.get("formListId");

        if (!ObjectUtils.isEmpty(formListIdPathVariable)){
            Long formListId = Long.valueOf((String) pathVariables.get("formListId"));
            formList = FormList.builder().id(formListId).build();
        }
        else {
            String formName = getFormName(request);
            formList = formListRepository.findByFormName(formName).orElse(new FormList());
        }

        return formList;
    }

    private String getFormName(HttpServletRequest request){

        String formName = null;

        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String formListIdPathVariable = (String) pathVariables.get("formCode");

        if (!ObjectUtils.isEmpty(formListIdPathVariable))
            formName = (String) pathVariables.get("formCode");

        return formName;
    }

    private User getUser(Optional<User> user){

        if (!user.isPresent())
            throw new ResourceNotFoundException("User does not exist");

        return user.get();
    }
}
