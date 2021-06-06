package org.pms.sammenu.services.forms;


import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.domain.Project;
import org.pms.sammenu.domain.forms.BasicInfo;
import org.pms.sammenu.dto.forms.BasicInfoDto;
import org.pms.sammenu.exceptions.ResourceNotFoundException;
import org.pms.sammenu.repositories.BasicInfoRepository;
import org.pms.sammenu.utils.FormUtils;
import org.pms.sammenu.utils.ProjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class BasicInfoServiceImpl implements BasicInfoService {
    
    private BasicInfoRepository basicInfoRepository;

    private ConversionService conversionService;

    private ProjectUtils projectUtils;

    private FormUtils formUtils;

    @Autowired
    public BasicInfoServiceImpl(BasicInfoRepository basicInfoRepository,
                                ConversionService conversionService,
                                ProjectUtils projectUtils,
                                FormUtils formUtils) {
        this.basicInfoRepository = basicInfoRepository;
        this.conversionService = conversionService;
        this.projectUtils = projectUtils;
        this.formUtils = formUtils;
    }

    @Override
    public BasicInfoDto fetchBasicInfoByProjectId(Long projectId, Long formListId) {
        
        log.info("Fetch BasicInfo by project-sam[id: {}] start", projectId);

        Optional<BasicInfo> optionalBasicInfo = basicInfoRepository.findBasicInfoByProject_Id(projectId);

        BasicInfoDto basicInfoDto = optionalBasicInfo
                .map(basicInfo -> conversionService.convert(basicInfo, BasicInfoDto.class))
                .orElse(new BasicInfoDto());

        if (!ObjectUtils.isEmpty(basicInfoDto)){
            basicInfoDto.setStatus(formUtils.viewFormStatus(projectId, formListId));
        }

        log.info("Fetch BasicInfo by project-sam[id: {}] end", projectId);

        return basicInfoDto;
    }

    @Override
    public void save(Long projectId, BasicInfoDto basicInfoDto, Long formListId, HttpServletRequest request) {

        log.info("Save new BasicInfo start");

        if (ObjectUtils.isEmpty(basicInfoDto))
            throw new ResourceNotFoundException("BasicInfo to be saved is empty");

        BasicInfo basicInfo = conversionService.convert(basicInfoDto, BasicInfo.class);

        Project project = projectUtils.fetchProject(projectId);

        basicInfo.setProject(project);

        BasicInfo savedBasicInfo = basicInfoRepository.save(basicInfo);

        formUtils.changeFormStatus(request, projectId, formListId, basicInfoDto.getStatus());

        log.info("Save new BasicInfo[id: {}] end", savedBasicInfo.getId());
    }
}
