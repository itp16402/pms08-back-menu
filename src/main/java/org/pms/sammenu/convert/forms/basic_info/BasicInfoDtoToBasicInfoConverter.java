package org.pms.sammenu.convert.forms.basic_info;

import org.pms.sammenu.domain.forms.BasicInfo;
import org.pms.sammenu.dto.forms.BasicInfoDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BasicInfoDtoToBasicInfoConverter implements Converter<BasicInfoDto, BasicInfo> {

    @Override
    public BasicInfo convert(BasicInfoDto basicInfoDto) {
        return BasicInfo.builder()
                .id(basicInfoDto.getId())
                .isAuditTax(basicInfoDto.getIsAuditTax())
                .folderCopy(basicInfoDto.getFolderCopy())
                .turnover(basicInfoDto.getTurnover())
                .branch(basicInfoDto.getBranch())
                .balanceSheetType(basicInfoDto.getBalanceSheetType())
                .startDate(basicInfoDto.getStartDate())
                .endDate(basicInfoDto.getEndDate())
                .agreementDate(basicInfoDto.getAgreementDate())
                .letterDate(basicInfoDto.getLetterDate())
                .financialStatementsDate(basicInfoDto.getFinancialStatementsDate())
                .appointmentDate(basicInfoDto.getAppointmentDate())
                .reportDate(basicInfoDto.getReportDate())
                .archivingDate(basicInfoDto.getArchivingDate())
                .formType(basicInfoDto.getFormType())
                .consecutiveYears(basicInfoDto.getConsecutiveYears())
                .hours(basicInfoDto.getHours())
                .acceptance(basicInfoDto.getAcceptance())
                .build();
    }
}
