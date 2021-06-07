package org.pms.sammenu.dto.form_views;

import lombok.*;
import org.pms.sammenu.enums.FormType;
import org.pms.sammenu.enums.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowchartChildDto {

    private Long id;
    private Short phase;
    private String name;
    private String formName;
    private Locale language;
    private FormType formType;
    private float sOrder;
    private String type;
    private String icon;
    private String css;
    private String state;
}
