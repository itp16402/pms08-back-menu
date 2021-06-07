package org.pms.sammenu.dto.forms.essential_size.base;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseDto {

    private Integer id;
    private String name;
    private List<MaterialityBaseDto> materialityBaseDtoList;
}
