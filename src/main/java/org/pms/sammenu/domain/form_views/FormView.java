package org.pms.sammenu.domain.form_views;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "forms")
public class FormView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "tablename")
    private String tableName;

    @Column(name = "nlslang")
    private String language;

    @Column(name = "formtype")
    private String formType;

    @Column(name = "keli")
    private String keli;

    @Column(name = "onoma")
    private String onoma;

    @Column(name = "typos")
    private String typos;

    @Column(name = "svalues")
    private String sValues;

    @Column(name = "infos")
    private String infos;

    @Column(name = "cell")
    private String cell;

    @Column(name = "sfunction")
    private String sFunction;

    @Column(name = "css")
    private String css;

    @Column(name = "sorder")
    private Float seqOrder;

    @Column(name = "sprint")
    private Integer sPrint;

    @Column(name = "optional")
    private Short optional;

    @Column(name = "comments")
    private String comments;

    @Column(name = "help")
    private String help;

    @Column(name = "upload")
    private String upload;

    @Column(name = "value")
    private Integer value;
}
