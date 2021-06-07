package org.pms.sammenu.dto.forms.important_accounts;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.pms.sammenu.dto.forms.AbstractFormDto;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ImportantAccountRequestDto extends AbstractFormDto {

    private List<ImportantAccountAddRequestDto> importantAccountAddRequestDtoList;
}
