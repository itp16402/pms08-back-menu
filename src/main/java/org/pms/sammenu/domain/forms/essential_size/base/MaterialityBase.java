package org.pms.sammenu.domain.forms.essential_size.base;

import lombok.*;
import org.pms.sammenu.domain.forms.BalanceSheetDictionary;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"base", "add", "subtract"})
@ToString(exclude = {"base", "add", "subtract"})
@Entity
@Table(name = "materiality_base")
public class MaterialityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "baseid")
    private Base base;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "add")
    private BalanceSheetDictionary add;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "subtract")
    private BalanceSheetDictionary subtract;
}
