package org.pms.sammenu.convert.form_views.form_view;

import org.pms.sammenu.domain.form_views.FormView;
import org.pms.sammenu.dto.form_views.FormViewDto;
import org.pms.sammenu.enums.FormType;
import org.pms.sammenu.enums.Locale;
import org.pms.sammenu.enums.OptionalStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FormViewToFormViewDtoConverter implements Converter<FormView, FormViewDto> {

    @Override
    public FormViewDto convert(FormView formView) {
        return FormViewDto.builder()
                .id(formView.getId())
                .tableName(formView.getTableName())
                .language(formView.getLanguage().equals("el") ? Locale.el : Locale.en)
                .formType(FormType.valueOf(formView.getFormType()))
                .keli(formView.getKeli())
                .onoma(formView.getOnoma())
                .typos(formView.getTypos())
                .sValues(formView.getSValues())
                .infos(formView.getInfos())
                .cell(formView.getCell())
                .sFunction(formView.getSFunction())
                .css(formView.getCss())
                .sOrder(formView.getSeqOrder())
                .sPrint(formView.getSPrint())
                .optional(OptionalStatus.fromValue(formView.getOptional()))
                .comments(formView.getComments())
                .help(formView.getHelp())
                .upload(formView.getUpload())
                .value(formView.getValue())
                .build();
    }
}
