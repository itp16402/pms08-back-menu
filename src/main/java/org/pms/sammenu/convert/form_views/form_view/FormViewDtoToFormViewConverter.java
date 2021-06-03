package org.pms.sammenu.convert.form_views.form_view;

import org.pms.sammenu.domain.form_views.FormView;
import org.pms.sammenu.dto.form_views.FormViewDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FormViewDtoToFormViewConverter implements Converter<FormViewDto, FormView> {

    @Override
    public FormView convert(FormViewDto formViewDto) {

        return FormView.builder()
                .id(formViewDto.getId())
                .tableName(formViewDto.getTableName())
                .language(formViewDto.getLanguage().code())
                .formType(formViewDto.getFormType().code())
                .keli(formViewDto.getKeli())
                .onoma(formViewDto.getOnoma())
                .typos(formViewDto.getTypos())
                .sValues(formViewDto.getSValues())
                .infos(formViewDto.getInfos())
                .cell(formViewDto.getCell())
                .sFunction(formViewDto.getSFunction())
                .css(formViewDto.getCss())
                .seqOrder(formViewDto.getSOrder())
                .sPrint(formViewDto.getSPrint())
                .optional(formViewDto.getOptional().code())
                .comments(formViewDto.getComments())
                .help(formViewDto.getHelp())
                .upload(formViewDto.getUpload())
                .value(formViewDto.getValue())
                .build();
    }
}
