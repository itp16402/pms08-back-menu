package org.pms.sammenu.domain.forms;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "a111")
public class BasicInfo extends AbstractForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "taxaudit")
    private Short isAuditTax;

    @Column(name = "foldercopy")
    private Integer folderCopy;

    @Column(name = "turnover")
    private Double turnover;

    @Column(name = "branch")
    private String branch;

    @Column(name = "entitytype")
    private Integer balanceSheetType;

    @Column(name = "auditstart")
    private LocalDate startDate;

    @Column(name = "auditend")
    private LocalDate endDate;

    @Column(name = "agreementdate")
    private LocalDate agreementDate;

    @Column(name = "letterdate")
    private LocalDate letterDate;

    @Column(name = "financialstatementsdate")
    private LocalDate financialStatementsDate;

    @Column(name = "appointmentdate")
    private LocalDate appointmentDate;

    @Column(name = "reportdate")
    private LocalDate reportDate;

    @Column(name = "archivingdate")
    private LocalDate archivingDate;

    @Column(name = "statutoryaudit")
    private Short formType;

    @Column(name = "consecutiveyears")
    private Integer consecutiveYears;

    @Column(name = "hours")
    private Integer hours;

    @Column(name = "acceptance")
    private String acceptance;
}
