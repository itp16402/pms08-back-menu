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

    @Column(name = "entitytype")
    private Integer balanceSheetType;

    @Column(name = "auditstart")
    private LocalDate startDate;

    @Column(name = "auditend")
    private LocalDate endDate;

    @Column(name = "statutoryaudit")
    private Short formType;
}
