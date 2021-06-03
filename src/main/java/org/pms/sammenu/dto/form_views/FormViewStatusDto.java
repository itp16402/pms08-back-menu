package org.pms.sammenu.dto.form_views;

import lombok.*;
import org.pms.sammenu.enums.FormStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormViewStatusDto {

    private Long id;
    private String tableName;
    private String onoma;
    private FormStatus status;
    private boolean isDisabled;
}
