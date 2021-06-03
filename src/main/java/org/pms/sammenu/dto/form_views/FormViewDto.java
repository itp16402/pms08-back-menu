package org.pms.sammenu.dto.form_views;

import lombok.*;
import org.pms.sammenu.enums.FormType;
import org.pms.sammenu.enums.Locale;
import org.pms.sammenu.enums.OptionalStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormViewDto {

    private Long id;
    private String tableName;
    private Locale language;
    private FormType formType;
    private String keli;
    private String onoma;
    private String typos;
    private String sValues;
    private String infos;
    private String cell;
    private String sFunction;
    private String css;
    private Float sOrder;
    private Integer sPrint;
    private OptionalStatus optional;
    private String comments;
    private String help;
    private String upload;
    private Integer value;
}
