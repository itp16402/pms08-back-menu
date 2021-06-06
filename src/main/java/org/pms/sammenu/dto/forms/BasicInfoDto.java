package org.pms.sammenu.dto.forms;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BasicInfoDto extends AbstractFormDto {

    private Short isAuditTax;
    private Integer folderCopy;
    private Double turnover;
    private String branch;
    private Integer balanceSheetType;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate agreementDate;
    private LocalDate letterDate;
    private LocalDate financialStatementsDate;
    private LocalDate appointmentDate;
    private LocalDate reportDate;
    private LocalDate archivingDate;
    private Short formType;
    private Integer consecutiveYears;
    private Integer hours;
    private String acceptance;
}
