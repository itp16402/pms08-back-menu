package org.pms.sammenu.utils;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.config.security.JwtTokenProvider;
import org.pms.sammenu.domain.*;
import org.pms.sammenu.domain.form_views.FormList;
import org.pms.sammenu.domain.form_views.FormView;
import org.pms.sammenu.enums.AuthorityType;
import org.pms.sammenu.enums.FormStatus;
import org.pms.sammenu.enums.FormType;
import org.pms.sammenu.enums.Locale;
import org.pms.sammenu.exceptions.AssignedUserException;
import org.pms.sammenu.exceptions.ResourceNotFoundException;
import org.pms.sammenu.exceptions.UnacceptableActionException;
import org.pms.sammenu.repositories.FormRoleRepository;
import org.pms.sammenu.repositories.UserRepository;
import org.pms.sammenu.repositories.UserRoleRepository;
import org.pms.sammenu.repositories.form_views.FormListRepository;
import org.pms.sammenu.repositories.form_views.FormViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class FormUtils {

    private FormRoleRepository formRoleRepository;
    private UserRoleRepository userRoleRepository;
    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;
    private FormListRepository formListRepository;
    private FormViewRepository formViewRepository;

    @Autowired
    public FormUtils(FormRoleRepository formRoleRepository,
                     UserRoleRepository userRoleRepository,
                     JwtTokenProvider jwtTokenProvider,
                     UserRepository userRepository,
                     FormListRepository formListRepository,
                     FormViewRepository formViewRepository) {
        this.formRoleRepository = formRoleRepository;
        this.userRoleRepository = userRoleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.formListRepository = formListRepository;
        this.formViewRepository = formViewRepository;
    }

    public Long getFormId(String formName){

        Optional<FormList> formList = formListRepository.findByFormName(formName);

        return formList.map(FormList::getId).orElse(null);
    }

    public FormList getForm(Long formId){

        Optional<FormList> formList = formListRepository.findById(formId);

        if (!formList.isPresent())
            throw new UnacceptableActionException("There is not form with id: " + formId);

        return formList.get();
    }

    public String getFormName(String formName, Locale language, FormType formType){

        final String TITLE = "TITLE";

        Optional<FormView> formView = formViewRepository
                .findByTableNameAndLanguageAndFormTypeAndTypos(formName, language.code(),
                        formType.code(), TITLE);

        if (formView.isPresent())
            return formView.get().getOnoma();

        return formName;
    }

    public FormStatus viewFormStatus(Long projectId, Long formListId) {

        Optional<UserRole> manager = userRoleRepository.findByProjectAndAuthority(Project.builder().id(projectId).build(),
                Authority.builder().id(AuthorityType.MANAGER.code()).build());

        if (!manager.isPresent())
            return null;

        Optional<FormRole> formRole = formRoleRepository
                .findByFormListAndUserRole(FormList.builder().id(formListId).build(), manager.get());

        if (!formRole.isPresent())
            throw new ResourceNotFoundException(MessageFormat
                    .format("Form-list [id:{0}] in project [id:{1}] not assign", formListId, projectId));

        return !ObjectUtils.isEmpty(formRole.get().getState()) ? FormStatus.fromValue(formRole.get().getState()) : null;
    }

    public void changeFormStatus(HttpServletRequest request, Long projectId, Long formListId, FormStatus formStatus) {

        log.info("Change form[id:{}] to status[{}] process begins", formListId, formStatus.name());

        Long UserId = getUserIdFromHeader(request);

        List<FormRole> formRoles = buildFormRoles(UserId, projectId, formListId);

        formRoles.forEach(formRole -> {

            formRole.setState(formStatus.code());

            formRoleRepository.save(formRole);
        });

        log.info("Change form[id:{}] to status[{}] process end", formListId, formStatus.name());
    }

    private List<FormRole> buildFormRoles(Long UserId, Long projectId, Long formListId){

        UserRole user = buildUserRole(UserId, projectId);

        List<UserRole> UserRoles = Stream.of(user).collect(Collectors.toList());

        if (!(user.getAuthority().getId() == AuthorityType.MANAGER.code())){

            Optional<UserRole> manager = userRoleRepository.findByProjectAndAuthority(Project.builder().id(projectId).build(),
                    Authority.builder().id(AuthorityType.MANAGER.code()).build());

            if (!manager.isPresent())
                throw new AssignedUserException(MessageFormat
                        .format("Problem. In form :{}, at projects :{} manager not assigned.",
                                formListId, projectId));

            UserRoles.add(manager.get());
        }

        List<FormRole> formRoles = formRoleRepository
                .findByFormListAndUserRoleIn(FormList.builder().id(formListId).build(), UserRoles);

        if (ObjectUtils.isEmpty(formRoles) || formRoles.isEmpty())
            throw new ResourceNotFoundException(MessageFormat
                    .format("Form-list [id:{0}] in project [id:{1}] not assign", formListId, projectId));

        return formRoles;
    }

    private UserRole buildUserRole(Long userId, Long projectId){

        Set<UserRole> UserRoles = userRoleRepository
                .findByUser_IdAndProject(userId, Project.builder().id(projectId).build());

        if (UserRoles.isEmpty())
            throw new ResourceNotFoundException(MessageFormat
                    .format("User [id:{0}] in project [id:{1}] does not exist", userId, projectId));

        if (UserRoles.size() > 1)
            UserRoles = UserRoles.stream().filter(UserRole -> UserRole.getAuthority().getId() == AuthorityType.MANAGER.code())
                    .collect(Collectors.toSet());

        return UserRoles.iterator().next();
    }

    private Long getUserIdFromHeader(HttpServletRequest request){

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            String  jwt = authorizationHeader.substring(7);

            String username = jwtTokenProvider.extractUsername(jwt);

            User User = buildUser(userRepository.findByUsername(username));

            return User.getId();
        }

        return null;
    }

    private User buildUser(Optional<User> User){

        if (!User.isPresent())
            throw new ResourceNotFoundException("User does not exist");

        return User.get();
    }
}
