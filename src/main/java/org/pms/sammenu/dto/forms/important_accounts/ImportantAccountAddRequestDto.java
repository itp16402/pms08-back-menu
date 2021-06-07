package org.pms.sammenu.dto.forms.important_accounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportantAccountAddRequestDto {

    private Long id;
    private Long lineId;
    private String lineName;
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
