package org.pms.sammenu.services;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.domain.Authority;
import org.pms.sammenu.domain.FormRole;
import org.pms.sammenu.domain.Project;
import org.pms.sammenu.domain.UserRole;
import org.pms.sammenu.domain.form_views.FormList;
import org.pms.sammenu.enums.AuthorityType;
import org.pms.sammenu.repositories.FormRoleRepository;
import org.pms.sammenu.repositories.ProjectRepository;
import org.pms.sammenu.repositories.UserRoleRepository;
import org.pms.sammenu.repositories.form_views.FormListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class BehindScenesServiceImpl implements BehindScenesService {

    private ProjectRepository projectRepository;
    private FormListRepository formListRepository;
    private UserRoleRepository userRoleRepository;
    private FormRoleRepository formRoleRepository;

    @Autowired
    public BehindScenesServiceImpl(ProjectRepository projectRepository, FormListRepository formListRepository,
                                   UserRoleRepository userRoleRepository, FormRoleRepository formRoleRepository) {
        this.projectRepository = projectRepository;
        this.formListRepository = formListRepository;
        this.userRoleRepository = userRoleRepository;
        this.formRoleRepository = formRoleRepository;
    }

    @Override
    public void assignToManagerAllForms() {

        log.info("Assign all new forms to manager process begins");

        List<Project> projects = projectRepository.findAll();

        List<FormList> formList = formListRepository.findAll();

        projects.forEach(project -> {

            Optional<UserRole> manager = userRoleRepository
                    .findByProjectAndAuthority(project, Authority.builder().id(AuthorityType.MANAGER.code()).build());

            if (!manager.isPresent())
                return;

            formList.forEach(form -> {

                Optional<FormRole> formRole = formRoleRepository.findByFormListAndUserRole(form, manager.get());

                if (!formRole.isPresent())
                    formRoleRepository.save(FormRole.builder()
                            .formList(form)
                            .userRole(manager.get())
                            .build());
            });
        });

        log.info("Assign all new forms to manager process end");
    }
}
