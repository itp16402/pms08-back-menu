package org.pms.sammenu.dto.forms.essential_size.responses;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.pms.sammenu.dto.forms.AbstractFormDto;
import org.pms.sammenu.dto.forms.essential_size.base.BaseDto;
import org.pms.sammenu.utils.FlexibleDoubleSerializer;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EssentialSizeDto extends AbstractFormDto {

    private BaseDto base;
    @JsonSerialize(using = FlexibleDoubleSerializer.class)
    private Double overAmount;
    @JsonSerialize(using = FlexibleDoubleSerializer.class)
    private Double taxOverAmount;
    private String documentationBase;
    private List<EssentialSizeOverallDto> essentialSizeOverallDtoList;
    private List<EssentialSizePerformanceDto> essentialSizePerformanceDtoList;
}
