package org.pms.sammenu.domain;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
