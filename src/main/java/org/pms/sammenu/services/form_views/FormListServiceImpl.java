package org.pms.sammenu.services.form_views;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.domain.form_views.FormList;
import org.pms.sammenu.dto.form_views.FormListDto;
import org.pms.sammenu.enums.FormStatus;
import org.pms.sammenu.repositories.form_views.FormListRepository;
import org.pms.sammenu.utils.FormUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FormListServiceImpl implements FormListService {

    private FormListRepository formListRepository;
    private ConversionService conversionService;
    private FormUtils formUtils;

    @Autowired
    public FormListServiceImpl(FormListRepository formListRepository, ConversionService conversionService, FormUtils formUtils) {
        this.formListRepository = formListRepository;
        this.conversionService = conversionService;
        this.formUtils = formUtils;
    }

    @Override
    public List<FormListDto> findAll() {

        log.info("Fetch FormList process start");

        List<FormList> formLists = formListRepository.findAll();

        List<FormListDto> formListDtos = formLists
                .stream()
                .map(formList -> conversionService.convert(formList, FormListDto.class))
                .collect(Collectors.toList());

        log.info("Fetch FormList process end");
        
        return formListDtos;
    }

    @Override
    public FormListDto fetchByFormName(String formName) {

        log.info("Fetch FormList by formName[{}] process start", formName);

        Optional<FormList> optionalFormList = formListRepository.findByFormName(formName);

        FormListDto formListDto = optionalFormList
                .map(formList -> conversionService.convert(formList, FormListDto.class))
                .orElse(null);

        log.info("Fetch FormList by formName[{}] process end", formName);
        
        return formListDto;
    }

    @Override
    public void saveStatus(Long projectId, FormStatus status, Long formListId, HttpServletRequest request) {

        log.info("Change Status for form[id:{}] in order[id:{}] process begins", formListId, projectId);

        formUtils.changeFormStatus(request, projectId, formListId, status);

        log.info("Change Status for form[id:{}] in order[id:{}] process end", formListId, projectId);
    }

    @Override
    public FormStatus getStatus(Long projectId, Long formListId) {

        log.info("Get Status for form[id:{}] in order[id:{}] process", formListId, projectId);

        return formUtils.viewFormStatus(projectId, formListId);
    }
}
