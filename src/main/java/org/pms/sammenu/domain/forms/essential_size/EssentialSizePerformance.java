package org.pms.sammenu.domain.forms.essential_size;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"essentialSize"})
@ToString(exclude = {"essentialSize"})
@Entity
@Table(name = "add221performance")
public class EssentialSizePerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "year", nullable = false)
    private String year;

    @Column(name = "ovamount")
    private Double overAmount;

    @Column(name = "percentage")
    private Double percentage;

    @Column(name = "peramount")
    private Double perAmount;

    @Column(name = "taxperamount")
    private Double taxPerAmount;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH})
    @JoinColumn(name = "a221id")
    private EssentialSize essentialSize;
}
