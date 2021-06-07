package org.pms.sammenu.dto.forms.important_accounts;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.pms.sammenu.utils.FlexibleDoubleSerializer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportantAccountAddDto {

    private Long id;
    private Long lineId;
    private String lineName;
    @JsonSerialize(using = FlexibleDoubleSerializer.class)
    private Double amount;
    private boolean isEssential;
    private boolean isImportant;
    private Short y;
    private Short pd;
    private Short ak;
    private Short ap;
    private Short dd;
    private Short tp;
    private boolean isAssessment;
    private boolean isImportantRisk;
    private boolean isImportantAssessment;
}
