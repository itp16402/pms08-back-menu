package org.pms.sammenu.convert.forms.essential_size.requests;

import org.pms.sammenu.domain.forms.essential_size.EssentialSizePerformance;
import org.pms.sammenu.dto.forms.essential_size.requests.EssentialSizePerformanceRequestDto;
import org.pms.sammenu.exceptions.UnacceptableActionException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class EssentialSizePerformanceRequestDtoToEssentialSizePerformanceConverter
        implements Converter<EssentialSizePerformanceRequestDto, EssentialSizePerformance> {

    @Override
    public EssentialSizePerformance convert(EssentialSizePerformanceRequestDto essentialSizePerformanceRequestDto) {

        if (essentialSizePerformanceRequestDto.getPercentage() < 50 || essentialSizePerformanceRequestDto.getPercentage() > 80)
            throw new UnacceptableActionException(MessageFormat.format("Amount {0}% is not inside [50%, 80%]" ,
                    essentialSizePerformanceRequestDto.getPercentage()));

        return EssentialSizePerformance.builder()
                .id(essentialSizePerformanceRequestDto.getId())
                .year(essentialSizePerformanceRequestDto.getYear())
                .overAmount(essentialSizePerformanceRequestDto.getOverAmount())
                .percentage(essentialSizePerformanceRequestDto.getPercentage())
                .perAmount(essentialSizePerformanceRequestDto.getOverAmount() * (essentialSizePerformanceRequestDto.getPercentage()  / 100))
                .taxPerAmount(essentialSizePerformanceRequestDto.getTaxPerAmount())
                .build();
    }
}
