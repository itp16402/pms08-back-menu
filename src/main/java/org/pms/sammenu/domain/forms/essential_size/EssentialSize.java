package org.pms.sammenu.domain.forms.essential_size;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.pms.sammenu.domain.forms.AbstractForm;
import org.pms.sammenu.domain.forms.essential_size.base.Base;

import javax.persistence.*;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"base", "essentialSizeOverallList", "essentialSizePerformanceList"}, callSuper = false)
@ToString(exclude = {"base", "essentialSizeOverallList", "essentialSizePerformanceList"})
@Entity
@Table(name = "a221")
public class EssentialSize extends AbstractForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH})
    @JoinColumn(name = "baseid")
    private Base base;

    @Column(name = "ovamount")
    private Double overAmount;

    @Column(name = "taxovamount")
    private Double taxOverAmount;

    @Column(name = "documentationbase")
    private String documentationBase;

    @OneToMany(mappedBy = "essentialSize", fetch= FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH})
    private List<EssentialSizeOverall> essentialSizeOverallList;

    @OneToMany(mappedBy = "essentialSize", fetch= FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH})
    private List<EssentialSizePerformance> essentialSizePerformanceList;
}
