package org.pms.sammenu.domain.forms.important_accounts;

import lombok.*;
import org.pms.sammenu.domain.forms.BalanceSheetDictionary;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"importantAccount", "balanceSheetDictionary"})
@ToString(exclude = {"importantAccount", "balanceSheetDictionary"})
@Entity
@Table(name = "add231")
public class ImportantAccountAdd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "isologismosdictionaryid")
    private BalanceSheetDictionary balanceSheetDictionary;

    @Column(name = "isimportant")
    private Short important;

    @Column(name = "y")
    private Short y;

    @Column(name = "pd")
    private Short pd;

    @Column(name = "ak")
    private Short ak;

    @Column(name = "ap")
    private Short ap;

    @Column(name = "dd")
    private Short dd;

    @Column(name = "tp")
    private Short tp;

    @Column(name = "assessment")
    private Short assessment;

    @Column(name = "isimportantrisk")
    private Short isImportantRisk;

    @Column(name = "isimportantassessment")
    private Short isImportantAssessment;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH})
    @JoinColumn(name = "a231id")
    private ImportantAccount importantAccount;
}
