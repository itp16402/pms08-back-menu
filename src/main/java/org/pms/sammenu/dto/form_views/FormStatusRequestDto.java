package org.pms.sammenu.dto.form_views;

import lombok.*;
import org.pms.sammenu.enums.FormStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormStatusRequestDto {

    private FormStatus status;
}
