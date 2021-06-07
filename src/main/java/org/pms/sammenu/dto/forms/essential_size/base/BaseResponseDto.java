package org.pms.sammenu.dto.forms.essential_size.base;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pms.sammenu.utils.FlexibleDoubleSerializer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponseDto {

    private Integer id;
    private String name;
    private String language;

    @JsonSerialize(using = FlexibleDoubleSerializer.class)
    private double amount;
}
