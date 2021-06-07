package org.pms.sammenu.domain.forms.important_accounts;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.pms.sammenu.domain.forms.AbstractForm;

import javax.persistence.*;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"importantAccountAddList"}, callSuper = false)
@ToString(exclude = {"importantAccountAddList"})
@Entity
@Table(name = "a231")
public class ImportantAccount extends AbstractForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "peramount")
    private Double perAmount;

    @OneToMany(mappedBy = "importantAccount", fetch= FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH})
    private List<ImportantAccountAdd> importantAccountAddList;
}
