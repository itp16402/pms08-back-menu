package org.pms.sammenu.services.form_views;


import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.domain.Authority;
import org.pms.sammenu.domain.FormRole;
import org.pms.sammenu.domain.Project;
import org.pms.sammenu.domain.UserRole;
import org.pms.sammenu.domain.form_views.FormList;
import org.pms.sammenu.domain.form_views.FormView;
import org.pms.sammenu.dto.form_views.FormViewDto;
import org.pms.sammenu.dto.form_views.FormViewStatusDto;
import org.pms.sammenu.enums.AuthorityType;
import org.pms.sammenu.enums.FormStatus;
import org.pms.sammenu.enums.FormType;
import org.pms.sammenu.enums.Locale;
import org.pms.sammenu.exceptions.ResourceNotFoundException;
import org.pms.sammenu.repositories.FormRoleRepository;
import org.pms.sammenu.repositories.UserRoleRepository;
import org.pms.sammenu.repositories.form_views.FormListRepository;
import org.pms.sammenu.repositories.form_views.FormViewRepository;
import org.pms.sammenu.utils.BasicInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FormViewServiceImpl implements FormViewService {

    private FormViewRepository formViewRepository;
    private FormRoleRepository formRoleRepository;
    private FormListRepository formListRepository;
    private UserRoleRepository userRoleRepository;
    private BasicInfoUtils basicInfoUtils;
    private ConversionService conversionService;

    @Autowired
    public FormViewServiceImpl(FormViewRepository formViewRepository,
                               FormRoleRepository formRoleRepository,
                               FormListRepository formListRepository,
                               UserRoleRepository userRoleRepository,
                               BasicInfoUtils basicInfoUtils,
                               ConversionService conversionService) {
        this.formViewRepository = formViewRepository;
        this.formRoleRepository = formRoleRepository;
        this.formListRepository = formListRepository;
        this.userRoleRepository = userRoleRepository;
        this.basicInfoUtils = basicInfoUtils;
        this.conversionService = conversionService;
    }

    @Override
    public List<FormViewDto> fetchFormsByTableNameAndFormType(String tableName, Locale locale, FormType formType) {
        log.info("Find form[table-name: {}]  process start", tableName);
        List<FormView> formViews = formViewRepository
                .findByTableNameAndLanguageAndFormTypeOrderBySeqOrderAsc(tableName, locale.code(), formType.code());
        if (formViews.isEmpty())
            throw new ResourceNotFoundException(MessageFormat.
                    format("There is no form with table-name {0}",
                            tableName));
        List<FormViewDto> formViewDtoList = formViews
                .stream()
                .map(formView -> conversionService.convert(formView, FormViewDto.class))
                .collect(Collectors.toList());
        log.info("Find form[table-name: {}] process end", tableName);
        return formViewDtoList;
    }

    @Override
    public List<FormViewDto> fetchFormsByTableNameAndProjectId(String tableName, Locale locale, Long projectId) {

        log.info("Find form[table-name: {}]  process start", tableName);

        FormType formType = basicInfoUtils.getFormTypeByProjectId(projectId);

        List<FormView> formViews = formViewRepository
                .findByTableNameAndLanguageAndFormTypeOrderBySeqOrderAsc(tableName, locale.code(), formType.code());

        if (formViews.isEmpty())
            throw new ResourceNotFoundException(MessageFormat.
                    format("There is no form with table-name {0}",
                            tableName));

        List<FormViewDto> formViewDtoList = formViews
                .stream()
                .map(formView -> conversionService.convert(formView, FormViewDto.class))
                .collect(Collectors.toList());

        log.info("Find form[table-name: {}] process end", tableName);

        return formViewDtoList;
    }

    @Override
    public FormViewDto fetchFormByTableNameAndTypos(String tableName, Locale locale, Long projectId, String typos) {

        log.info("Find form by [table-name: {}] and [typo: {}]  process start", tableName,typos);

        FormType formType = basicInfoUtils.getFormTypeByProjectId(projectId);

        Optional<FormView> optionalFormView = formViewRepository
                .findByTableNameAndLanguageAndFormTypeAndTypos(tableName, locale.code(), formType.code(), typos);

        FormViewDto formViewDto = optionalFormView
                .map(formView -> conversionService.convert(formView, FormViewDto.class))
                .orElse(null);

        log.info("Find form by [table-name: {}] and [typo: {}]  process end", tableName,typos);

        return formViewDto;
    }

    @Override
    public List<FormViewStatusDto> findFormNamesAndStatusByFlowChart(Long flowchartId,
                                                                     Locale locale,
                                                                     String typos,
                                                                     Long projectId, FormType formType) {

        log.info("Find form by [flowchartId: {}] and [typo: {}]  process start", flowchartId, typos);

        if (ObjectUtils.isEmpty(formType))
            formType = basicInfoUtils.getFormTypeByProjectId(projectId);

        List<FormView> formViews = formViewRepository
                .findByFlowchartIdAndLanguageAndFormTypeAndTypos(flowchartId, typos, locale.code(), formType.code());

        List<FormViewStatusDto> formViewStatusDtos = new ArrayList<>(formViews.size());

        formViews.forEach(formView -> {

            FormViewStatusDto formViewStatusDto = FormViewStatusDto.builder()
                    .id(formView.getId())
                    .tableName(formView.getTableName())
                    .onoma(formView.getOnoma())
                    .build();

            formViewStatusDtos.add(formViewStatusDto);
        });

        log.info("Find form by [flowchartId: {}] and [typo: {}]  process end", flowchartId, typos);

        return formViewStatusDtos;
    }

    @Override
    public List<FormViewStatusDto> findFormNamesAndStatusByFlowChartChild(Long flowchartChildId,
                                                                          Locale locale,
                                                                          String typos,
                                                                          Long projectId,
                                                                          FormType formType) {

        log.info("Find form by [flowchartChildId: {}] and [typo: {}]  process start", flowchartChildId, typos);

        if (ObjectUtils.isEmpty(formType))
            formType = basicInfoUtils.getFormTypeByProjectId(projectId);

        List<FormView> formViews = formViewRepository
                .findByFlowchartChildIdAndLanguageAndFormTypeAndTypos(flowchartChildId, typos, locale.code(), formType.code());

        List<FormViewStatusDto> formViewStatusDtos = new ArrayList<>(formViews.size());

        Optional<UserRole> userRole = userRoleRepository
                .findByProjectAndAuthority(Project.builder().id(projectId).build(),
                        Authority.builder().id(AuthorityType.MANAGER.code()).build());

        formViews.forEach(formView -> {

                    FormViewStatusDto formViewStatusDto = FormViewStatusDto.builder()
                        .id(formView.getId())
                        .tableName(formView.getTableName())
                        .onoma(formView.getOnoma())
                        .status(getFormStatus(formView.getTableName(), userRole.orElse(null)))
                        .build();

                    formViewStatusDtos.add(formViewStatusDto);
                });

        log.info("Find form by [flowchartChildId: {}] and [typo: {}]  process end", flowchartChildId, typos);

        return formViewStatusDtos;
    }

    @Override
    public List<FormViewDto> fetchAllTableNames(Locale locale) {

        log.info("Find all table names of forms process start");

        List<FormView> tableNames = formViewRepository.findAllTableNames(locale.code());

        List<FormViewDto> formViewDtoList = tableNames.stream()
                .map(formView -> conversionService.convert(formView, FormViewDto.class))
                .collect(Collectors.toList());

        log.info("Find all table names of forms process end. Size {}", tableNames.size());

        return formViewDtoList;
    }

    @Override
    public void save(FormViewDto formViewDto) {
        log.info("Save FormView start");

        if (ObjectUtils.isEmpty(formViewDto))
            throw new ResourceNotFoundException("FormView to be saved is empty");

        FormView formView = conversionService.convert(formViewDto, FormView.class);

        FormView savedFormView = formViewRepository.save(formView);

        log.info("Save new FormView[id: {}] end", savedFormView.getId());
    }

    @Override
    public boolean isFormTypeOta(Long projectId) {

        log.info("Check if formType is OTA in order[id: {}] process begins", projectId);

        FormType formType = basicInfoUtils.getFormTypeByProjectId(projectId);

        if (formType.equals(FormType.OTA))
            return true;

        log.info("Check if formType is OTA in order[id: {}] process end", projectId);

        return false;
    }

    private FormStatus getFormStatus(String tableName, UserRole userRole){

        FormStatus formStatus = null;

        Optional<FormList> formList = formListRepository.findByFormName(tableName);

        if (!formList.isPresent())
            throw new ResourceNotFoundException("There is not formlist with table-name: " + tableName);

        Optional<FormRole> formRole = formRoleRepository.findByFormListAndUserRole(formList.get(), userRole);

        if (formRole.isPresent())
            formStatus = !ObjectUtils.isEmpty(formRole.get().getState()) ?
                    FormStatus.fromValue(formRole.get().getState()) : null;

        return formStatus;

    }
}
