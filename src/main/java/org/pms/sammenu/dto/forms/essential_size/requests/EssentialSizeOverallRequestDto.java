package org.pms.sammenu.dto.forms.essential_size.requests;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EssentialSizeOverallRequestDto {

    private Long id;
    private Integer baseId;
    private Double interimBaseAmount;
    private Double minLimit;
    private Double maxLimit;
    private Double overAmount;
    private Double percentage;
}
