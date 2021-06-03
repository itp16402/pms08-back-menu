package org.pms.sammenu.convert.form_views.form_list;

import org.pms.sammenu.domain.form_views.FormList;
import org.pms.sammenu.dto.form_views.FormListDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FormListToFormListDtoConverter implements Converter<FormList, FormListDto> {

    @Override
    public FormListDto convert(FormList formList) {
        return FormListDto.builder()
                .id(formList.getId())
                .formName(formList.getFormName())
                .build();
    }
}
