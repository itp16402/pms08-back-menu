package org.pms.sammenu.dto.forms.essential_size.base;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.pms.sammenu.utils.FlexibleDoubleSerializer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialityBaseDto {

    private Long materialityBaseId;
    private Long lineId;
    private String lineDescription;

    @JsonSerialize(using = FlexibleDoubleSerializer.class)
    private Double amount;
}
