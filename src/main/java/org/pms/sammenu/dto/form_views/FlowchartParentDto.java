package org.pms.sammenu.dto.form_views;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pms.sammenu.enums.FormType;
import org.pms.sammenu.enums.Locale;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowchartParentDto {

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
    private List<FlowchartDto> flowcharts;
}
