package org.pms.sammenu.convert.forms.essential_size.requests;


import org.pms.sammenu.domain.forms.essential_size.EssentialSizeOverall;
import org.pms.sammenu.domain.forms.essential_size.base.Base;
import org.pms.sammenu.dto.forms.essential_size.requests.EssentialSizeOverallRequestDto;
import org.pms.sammenu.exceptions.UnacceptableActionException;
import org.pms.sammenu.repositories.essential_size.BaseRepository;
import org.pms.sammenu.utils.NumericUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;

@Component
public class EssentialSizeOverallRequestDtoToEssentialSizeOverallConverter
        implements Converter<EssentialSizeOverallRequestDto, EssentialSizeOverall> {

    @Autowired
    private BaseRepository baseRepository;

    @Override
    public EssentialSizeOverall convert(EssentialSizeOverallRequestDto essentialSizeOverallRequestDto) {

        Optional<Base> baseOptional = baseRepository.findById(essentialSizeOverallRequestDto.getBaseId());

        if (essentialSizeOverallRequestDto.getOverAmount() < essentialSizeOverallRequestDto.getMinLimit() ||
                essentialSizeOverallRequestDto.getOverAmount() > essentialSizeOverallRequestDto.getMaxLimit())
            throw new UnacceptableActionException(MessageFormat.format("Amount {0} is not inside [{1}, {2}]" ,
                    essentialSizeOverallRequestDto.getOverAmount(), essentialSizeOverallRequestDto.getMinLimit(),
                    essentialSizeOverallRequestDto.getMaxLimit()));

        double percentage = NumericUtils.roundNumberToCentimeters((essentialSizeOverallRequestDto.getOverAmount() /
                essentialSizeOverallRequestDto.getInterimBaseAmount()) * 100);

        return EssentialSizeOverall.builder()
                .id(essentialSizeOverallRequestDto.getId())
                .base(baseOptional.orElse(null))
                .interimBaseAmount(essentialSizeOverallRequestDto.getInterimBaseAmount())
                .minLimit(essentialSizeOverallRequestDto.getMinLimit())
                .maxLimit(essentialSizeOverallRequestDto.getMaxLimit())
                .overAmount(essentialSizeOverallRequestDto.getOverAmount())
                .percentage(percentage)
                .build();
    }
}
