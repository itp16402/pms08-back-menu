package org.pms.sammenu.domain.form_views;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"flowcharts"})
@ToString(exclude = {"flowcharts"})
@Entity
@Table(name = "flowchartparent")
public class FlowchartParent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "phase")
    private Short phase;

    @Column(name = "name")
    private String name;

    @Column(name = "formname")
    private String formName;

    @Column(name = "nlslang", nullable = false)
    private String language;

    @Column(name = "formtype", nullable = false)
    private String formType;

    @Column(name = "sorder")
    private Float seqOrder;

    @Column(name = "type")
    private String type;

    @Column(name = "icon")
    private String icon;

    @Column(name = "css")
    private String css;

    @OneToMany(mappedBy = "flowchartParent", fetch= FetchType.LAZY)
    private List<Flowchart> flowcharts;
}
