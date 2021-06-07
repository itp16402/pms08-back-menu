package org.pms.sammenu.domain.forms.essential_size.base;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"materialityBaseList"})
@ToString(exclude = {"materialityBaseList"})
@Entity
@Table(name = "base")
public class Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "nlslang")
    private String language;

    @OneToMany(mappedBy = "base", fetch= FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH})
    private List<MaterialityBase> materialityBaseList;
}
