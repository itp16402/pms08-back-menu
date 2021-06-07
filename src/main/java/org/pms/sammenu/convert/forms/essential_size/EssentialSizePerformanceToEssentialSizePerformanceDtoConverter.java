package org.pms.sammenu.convert.forms.essential_size;

import org.pms.sammenu.domain.forms.essential_size.EssentialSizePerformance;
import org.pms.sammenu.dto.forms.essential_size.responses.EssentialSizePerformanceDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EssentialSizePerformanceToEssentialSizePerformanceDtoConverter
        implements Converter<EssentialSizePerformance, EssentialSizePerformanceDto> {

    @Override
    public EssentialSizePerformanceDto convert(EssentialSizePerformance essentialSizePerformance) {
        return EssentialSizePerformanceDto.builder()
                .id(essentialSizePerformance.getId())
                .year(essentialSizePerformance.getYear())
                .overAmount(essentialSizePerformance.getOverAmount())
                .percentage(essentialSizePerformance.getPercentage())
                .perAmount(essentialSizePerformance.getPerAmount())
                .taxPerAmount(essentialSizePerformance.getTaxPerAmount())
                .build();
    }
}
