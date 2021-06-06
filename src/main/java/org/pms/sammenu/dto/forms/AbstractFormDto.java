package org.pms.sammenu.dto.forms;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.pms.sammenu.enums.FormStatus;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AbstractFormDto {

    protected Long id;
    protected FormStatus status;
}
