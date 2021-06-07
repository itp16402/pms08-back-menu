package org.pms.sammenu.dto.forms.essential_size.responses;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.pms.sammenu.utils.FlexibleDoubleSerializer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EssentialSizePerformanceDto {

    private Long id;
    private String year;
    @JsonSerialize(using = FlexibleDoubleSerializer.class)
    private Double overAmount;
    @JsonSerialize(using = FlexibleDoubleSerializer.class)
    private Double percentage;
    @JsonSerialize(using = FlexibleDoubleSerializer.class)
    private Double perAmount;
    @JsonSerialize(using = FlexibleDoubleSerializer.class)
    private Double taxPerAmount;
}
