package org.pms.sammenu.convert.forms;

import org.pms.sammenu.domain.forms.BasicInfo;
import org.pms.sammenu.dto.forms.BasicInfoDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BasicInfoToBasicInfoDtoConverter implements Converter<BasicInfo, BasicInfoDto> {

    @Override
    public BasicInfoDto convert(BasicInfo basicInfo) {
        return BasicInfoDto.builder()
                .id(basicInfo.getId())
                .isAuditTax(basicInfo.getIsAuditTax())
                .folderCopy(basicInfo.getFolderCopy())
                .turnover(basicInfo.getTurnover())
                .branch(basicInfo.getBranch())
                .balanceSheetType(basicInfo.getBalanceSheetType())
                .startDate(basicInfo.getStartDate())
                .endDate(basicInfo.getEndDate())
                .agreementDate(basicInfo.getAgreementDate())
                .letterDate(basicInfo.getLetterDate())
                .financialStatementsDate(basicInfo.getFinancialStatementsDate())
                .appointmentDate(basicInfo.getAppointmentDate())
                .reportDate(basicInfo.getReportDate())
                .archivingDate(basicInfo.getArchivingDate())
                .formType(basicInfo.getFormType())
                .consecutiveYears(basicInfo.getConsecutiveYears())
                .hours(basicInfo.getHours())
                .acceptance(basicInfo.getAcceptance())
                .build();
    }
}
