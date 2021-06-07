package org.pms.sammenu.domain.forms.essential_size;

import lombok.*;
import org.pms.sammenu.domain.forms.essential_size.base.Base;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"essentialSize"})
@ToString(exclude = {"essentialSize"})
@Entity
@Table(name = "add221overall")
public class EssentialSizeOverall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH})
    @JoinColumn(name = "baseid")
    private Base base;

    @Column(name = "interimbaseamount")
    private Double interimBaseAmount;

    @Column(name = "minlimit")
    private Double minLimit;

    @Column(name = "maxlimit")
    private Double maxLimit;

    @Column(name = "ovamount")
    private Double overAmount;

    @Column(name = "percentage")
    private Double percentage;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH})
    @JoinColumn(name = "a221id")
    private EssentialSize essentialSize;
}
