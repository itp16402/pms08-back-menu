package org.pms.sammenu.domain;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"authorities"})
@ToString(exclude = {"authorities"})
@Entity
@Table(name = "project")
public class Project {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "orderid", unique = true)
    private Long orderId;

    @Column(name = "year")
    private String year;

    @Column(name = "recorddate")
    private Instant recordDate;

    @Column(name = "assignmentcontrolhours")
    private Integer assignmentControlHours;

    @Column(name = "customername")
    private String customerName;

    @Column(name = "status")
    private String status;

    @Column(name = "ordertypedescription")
    private String orderTypeDescription;

    @Column(name = "ordertypecomments")
    private String orderTypeComments;

    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name = "userrole", joinColumns =
    @JoinColumn(name = "projectid"), inverseJoinColumns = @JoinColumn(name = "roleid")
    )
    private List<Authority> authorities;
}
