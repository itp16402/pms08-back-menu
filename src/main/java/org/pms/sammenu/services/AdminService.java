package org.pms.sammenu.services;

import org.pms.sammenu.dto.form_views.FormViewDto;
import org.pms.sammenu.enums.Locale;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {

    void assignToManagerAllForms();

    List<FormViewDto> fetchFormNames(String tableName, Locale locale);

    void saveFormNames(FormViewDto formViewDto);
}
