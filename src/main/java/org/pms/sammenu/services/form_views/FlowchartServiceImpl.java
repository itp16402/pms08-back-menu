package org.pms.sammenu.services.form_views;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.domain.form_views.FlowchartParent;
import org.pms.sammenu.dto.form_views.FlowchartParentDto;
import org.pms.sammenu.enums.FormType;
import org.pms.sammenu.enums.Locale;
import org.pms.sammenu.repositories.form_views.FlowchartParentRepository;
import org.pms.sammenu.utils.BasicInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class FlowchartServiceImpl implements FlowchartService {

    private FlowchartParentRepository flowchartParentRepository;
    private BasicInfoUtils basicInfoUtils;
    private ConversionService conversionService;

    @Autowired
    public FlowchartServiceImpl(FlowchartParentRepository flowchartParentRepository,
                                BasicInfoUtils basicInfoUtils,
                                ConversionService conversionService) {
        this.flowchartParentRepository = flowchartParentRepository;
        this.basicInfoUtils = basicInfoUtils;
        this.conversionService = conversionService;
    }

    @Override
    public List<FlowchartParentDto> fetchFlowchart(Locale language, Long projectId) {

        log.info("Fetch flowchart process start");

        FormType formType = basicInfoUtils.getFormTypeByProjectId(projectId);

        List<FlowchartParent> flowchartParents;

        if (formType.equals(FormType.OTA))
            flowchartParents = flowchartParentRepository
                    .findByLanguageAndFormTypeOrderBySeqOrderAsc(language.code(), FormType.OTA.code());
        else
            flowchartParents = flowchartParentRepository
                    .findByLanguageAndFormTypeOrFormTypeAndLanguageOrderBySeqOrderAsc(language.code(), FormType.T.code(),
                            FormType.F.code(), language.code());

        List<FlowchartParentDto> flowchartParentDtoList = flowchartParents.stream()
                .map(flowchartParent -> conversionService.convert(flowchartParent, FlowchartParentDto.class))
                .collect(Collectors.toList());

        log.info("Fetch flowchart process end");

        return flowchartParentDtoList;
    }
}
