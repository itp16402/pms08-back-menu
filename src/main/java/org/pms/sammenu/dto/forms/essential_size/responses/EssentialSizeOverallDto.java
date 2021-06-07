package org.pms.sammenu.dto.forms.essential_size.responses;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.pms.sammenu.dto.forms.essential_size.base.BaseDto;
import org.pms.sammenu.utils.FlexibleDoubleSerializer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EssentialSizeOverallDto {

    private Long id;
    private BaseDto base;
    @JsonSerialize(using = FlexibleDoubleSerializer.class)
    private Double interimBaseAmount;
    @JsonSerialize(using = FlexibleDoubleSerializer.class)
    private Double minLimit;
    @JsonSerialize(using = FlexibleDoubleSerializer.class)
    private Double maxLimit;
    @JsonSerialize(using = FlexibleDoubleSerializer.class)
    private Double overAmount;
    @JsonSerialize(using = FlexibleDoubleSerializer.class)
    private Double percentage;
}
