package org.pms.sammenu.utils;

import org.pms.sammenu.domain.Project;
import org.pms.sammenu.domain.forms.BasicInfo;
import org.pms.sammenu.enums.Audit;
import org.pms.sammenu.enums.FormType;
import org.pms.sammenu.exceptions.ResourceNotFoundException;
import org.pms.sammenu.repositories.BasicInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class BasicInfoUtils {

    @Autowired
    private BasicInfoRepository basicInfoRepository;

    public FormType getFormTypeByProjectId(Long projectId){

        Optional<BasicInfo> basicInfo = basicInfoRepository
                .findBasicInfoByProject(Project.builder().id(projectId).build());

        if (!basicInfo.isPresent())
            return FormType.T;

        Short formType = basicInfo.get().getFormType();

        if (ObjectUtils.isEmpty(formType))
            return FormType.T;

        if (formType == 1)
            return FormType.OTA;

        return FormType.T;
    }

    public LocalDate getStartDate(Long projectId){

        Optional<BasicInfo> basicInfo = basicInfoRepository
                .findBasicInfoByProject(Project.builder().id(projectId).build());

        return basicInfo.map(BasicInfo::getStartDate).orElse(null);
    }

    public LocalDate getEndDate(Long projectId){

        Optional<BasicInfo> basicInfo = basicInfoRepository
                .findBasicInfoByProject(Project.builder().id(projectId).build());

        return basicInfo.map(BasicInfo::getEndDate).orElse(null);
    }

    public Audit getAudit(Long projectId){

        Optional<BasicInfo> basicInfo = basicInfoRepository
                .findBasicInfoByProject(Project.builder().id(projectId).build());

        if (!basicInfo.isPresent())
            throw new ResourceNotFoundException("Basic for checking audit not found in project: " + projectId);

        if (basicInfo.get().getIsAuditTax() == 1)
            return Audit.TAX;

        return Audit.REGULAR;
    }

    public BasicInfo fetchBasicInfo(Long projectId){

        Optional<BasicInfo> basicInfo = basicInfoRepository.findBasicInfoByProject_Id(projectId);

        return basicInfo.orElse(new BasicInfo());
    }
}
