package org.pms.sammenu.dto.forms.essential_size.requests;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EssentialSizePerformanceRequestDto {

    private Long id;
    private String year;
    private Double overAmount;
    private Double percentage;
    private Double perAmount;
    private Double taxPerAmount;
}
