package org.pms.sammenu.dto.forms.essential_size.requests;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.pms.sammenu.dto.forms.AbstractFormDto;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EssentialSizeRequestDto extends AbstractFormDto {

    private Integer baseId;
    private Double overAmount;
    private Double taxOverAmount;
    private String documentationBase;
    private List<EssentialSizeOverallRequestDto> essentialSizeOverallDtoList;
    private List<EssentialSizePerformanceRequestDto> essentialSizePerformanceDtoList;
}
