package org.pms.sammenu.domain;

import lombok.*;
import org.pms.sammenu.domain.form_views.FormList;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"formList", "userRole"})
@ToString(exclude = {"formList", "userRole"})
@Entity
@Table(name = "formrole")
public class FormRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "formlistid", nullable = false)
    private FormList formList;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "userroleid", nullable = false)
    private UserRole userRole;

    @Column(name = "state")
    private Short state;
}
