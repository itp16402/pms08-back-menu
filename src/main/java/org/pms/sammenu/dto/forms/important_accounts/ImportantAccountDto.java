package org.pms.sammenu.dto.forms.important_accounts;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.pms.sammenu.dto.forms.AbstractFormDto;
import org.pms.sammenu.utils.FlexibleDoubleSerializer;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ImportantAccountDto extends AbstractFormDto {

    @JsonSerialize(using = FlexibleDoubleSerializer.class)
    private Double perAmount;
    private List<ImportantAccountAddDto> importantAccountAddDtoList;
}
