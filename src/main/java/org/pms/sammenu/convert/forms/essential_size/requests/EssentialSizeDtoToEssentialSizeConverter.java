package org.pms.sammenu.convert.forms.essential_size.requests;

import org.pms.sammenu.domain.forms.essential_size.EssentialSize;
import org.pms.sammenu.domain.forms.essential_size.EssentialSizeOverall;
import org.pms.sammenu.domain.forms.essential_size.EssentialSizePerformance;
import org.pms.sammenu.domain.forms.essential_size.base.Base;
import org.pms.sammenu.dto.forms.essential_size.requests.EssentialSizeOverallRequestDto;
import org.pms.sammenu.dto.forms.essential_size.requests.EssentialSizePerformanceRequestDto;
import org.pms.sammenu.dto.forms.essential_size.requests.EssentialSizeRequestDto;
import org.pms.sammenu.repositories.BalanceSheetDictionaryRepository;
import org.pms.sammenu.repositories.essential_size.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EssentialSizeDtoToEssentialSizeConverter implements Converter<EssentialSizeRequestDto, EssentialSize> {

    @Autowired
    private BaseRepository baseRepository;

    @Autowired
    private BalanceSheetDictionaryRepository balanceSheetDictionaryRepository;

    @Override
    public EssentialSize convert(EssentialSizeRequestDto essentialSizeDto) {

        Optional<Base> base = baseRepository.findById(essentialSizeDto.getBaseId());

        EssentialSize essentialSize = EssentialSize.builder()
                .id(essentialSizeDto.getId())
                .base(base.orElse(null))
                .overAmount(essentialSizeDto.getOverAmount())
                .taxOverAmount(essentialSizeDto.getTaxOverAmount())
                .documentationBase(essentialSizeDto.getDocumentationBase())
                .build();

        essentialSize.setEssentialSizeOverallList(buildEssentialSizeOverallList(essentialSizeDto
                .getEssentialSizeOverallDtoList(), essentialSize));

        essentialSize.setEssentialSizePerformanceList(buildEssentialSizePerformanceList(essentialSizeDto
                .getEssentialSizePerformanceDtoList(), essentialSize));

        return essentialSize;
    }

    private List<EssentialSizeOverall> buildEssentialSizeOverallList(List<EssentialSizeOverallRequestDto> essentialSizeOverallList,
                                                                     EssentialSize essentialSize){

        return !ObjectUtils.isEmpty(essentialSizeOverallList) ? essentialSizeOverallList.stream()
                .map(essentialSizeOverall -> {

                    Optional<Base> baseOptional = baseRepository.findById(essentialSizeOverall.getBaseId());

                    return EssentialSizeOverall.builder()
                            .id(essentialSizeOverall.getId())
                            .base(baseOptional.orElse(null))
                            .interimBaseAmount(essentialSizeOverall.getInterimBaseAmount())
                            .minLimit(essentialSizeOverall.getMinLimit())
                            .maxLimit(essentialSizeOverall.getMaxLimit())
                            .overAmount(essentialSizeOverall.getOverAmount())
                            .percentage(essentialSizeOverall.getPercentage())
                            .essentialSize(essentialSize)
                        .build();
                })
                .collect(Collectors.toList()) : null;
    }

    private List<EssentialSizePerformance> buildEssentialSizePerformanceList(List<EssentialSizePerformanceRequestDto>
                                                                                           essentialSizePerformanceList,
                                                                             EssentialSize essentialSize){

        return !ObjectUtils.isEmpty(essentialSizePerformanceList) ? essentialSizePerformanceList.stream()
                .map(essentialSizePerformance -> EssentialSizePerformance.builder()
                        .id(essentialSizePerformance.getId())
                        .year(essentialSizePerformance.getYear())
                        .overAmount(essentialSizePerformance.getOverAmount())
                        .percentage(essentialSizePerformance.getPercentage())
                        .perAmount(essentialSizePerformance.getPerAmount())
                        .taxPerAmount(essentialSizePerformance.getTaxPerAmount())
                        .essentialSize(essentialSize)
                        .build())
                .collect(Collectors.toList()) : null;
    }
}
