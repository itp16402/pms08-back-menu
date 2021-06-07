package org.pms.sammenu.convert.forms.essential_size;

import org.pms.sammenu.domain.forms.essential_size.EssentialSize;
import org.pms.sammenu.domain.forms.essential_size.EssentialSizeOverall;
import org.pms.sammenu.domain.forms.essential_size.EssentialSizePerformance;
import org.pms.sammenu.domain.forms.essential_size.base.Base;
import org.pms.sammenu.dto.forms.essential_size.base.BaseDto;
import org.pms.sammenu.dto.forms.essential_size.responses.EssentialSizeDto;
import org.pms.sammenu.dto.forms.essential_size.responses.EssentialSizeOverallDto;
import org.pms.sammenu.dto.forms.essential_size.responses.EssentialSizePerformanceDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EssentialSizeToEssentialSizeDtoConverter implements Converter<EssentialSize, EssentialSizeDto> {

    @Override
    public EssentialSizeDto convert(EssentialSize essentialSize) {
        return EssentialSizeDto.builder()
                .id(essentialSize.getId())
                .base(buildBase(essentialSize.getBase()))
                .overAmount(essentialSize.getOverAmount())
                .taxOverAmount(essentialSize.getTaxOverAmount())
                .documentationBase(essentialSize.getDocumentationBase())
                .essentialSizeOverallDtoList(buildEssentialSizeOverallDtoList(essentialSize.getEssentialSizeOverallList()))
                .essentialSizePerformanceDtoList(buildEssentialSizePerformanceDtoList(essentialSize
                        .getEssentialSizePerformanceList()))
                .build();
    }

    private List<EssentialSizeOverallDto> buildEssentialSizeOverallDtoList(List<EssentialSizeOverall> essentialSizeOverallList){

        return !ObjectUtils.isEmpty(essentialSizeOverallList) ? essentialSizeOverallList.stream()
                .map(essentialSizeOverall -> EssentialSizeOverallDto.builder()
                        .id(essentialSizeOverall.getId())
                        .base(buildBase(essentialSizeOverall.getBase()))
                        .interimBaseAmount(essentialSizeOverall.getInterimBaseAmount())
                        .minLimit(essentialSizeOverall.getMinLimit())
                        .maxLimit(essentialSizeOverall.getMaxLimit())
                        .overAmount(essentialSizeOverall.getOverAmount())
                        .percentage(essentialSizeOverall.getPercentage())
                        .build())
                .sorted(Comparator.comparing(EssentialSizeOverallDto::getId))
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    private List<EssentialSizePerformanceDto> buildEssentialSizePerformanceDtoList(List<EssentialSizePerformance>
                                                                                           essentialSizePerformanceList){

        return !ObjectUtils.isEmpty(essentialSizePerformanceList) ? essentialSizePerformanceList.stream()
                .map(essentialSizePerformance -> EssentialSizePerformanceDto.builder()
                        .id(essentialSizePerformance.getId())
                        .year(essentialSizePerformance.getYear())
                        .overAmount(essentialSizePerformance.getOverAmount())
                        .percentage(essentialSizePerformance.getPercentage())
                        .perAmount(essentialSizePerformance.getPerAmount())
                        .taxPerAmount(essentialSizePerformance.getTaxPerAmount())
                        .build())
                .sorted(Comparator.comparing(EssentialSizePerformanceDto::getId))
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    private BaseDto buildBase(Base base){

        return !ObjectUtils.isEmpty(base) ? BaseDto.builder()
                .id(base.getId())
                .name(base.getName())
                .build() : null;
    }
}
