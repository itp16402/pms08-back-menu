package org.pms.sammenu.dto;

import lombok.*;
import org.pms.sammenu.enums.AuthorityType;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDto {

    private Long id;
    private String year;
    private Long orderId;
    private Instant recordDate;
    private Integer assignmentControlHours;
    private String customerName;
    private String status;
    private String orderTypeDescription;
    private String orderTypeComments;
    private Set<AuthorityType> roles;
}
