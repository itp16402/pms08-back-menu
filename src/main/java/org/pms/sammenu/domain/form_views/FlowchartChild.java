package org.pms.sammenu.domain.form_views;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"flowchart"})
@ToString(exclude = {"flowchart"})
@Entity
@Table(name = "flowchartchild")
public class FlowchartChild {

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

    @Column(name = "state")
    private String state;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "flowchartid", nullable = false)
    private Flowchart flowchart;
}
