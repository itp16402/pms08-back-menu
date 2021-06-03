package org.pms.sammenu.services.form_views;

import org.pms.sammenu.dto.form_views.FormViewDto;
import org.pms.sammenu.dto.form_views.FormViewStatusDto;
import org.pms.sammenu.enums.FormType;
import org.pms.sammenu.enums.Locale;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FormViewService {

    List<FormViewDto> fetchFormsByTableNameAndFormType(String tableName, Locale locale, FormType formType);

    List<FormViewDto> fetchFormsByTableNameAndProjectId(String tableName, Locale locale, Long projectId);

    FormViewDto fetchFormByTableNameAndTypos(String tableName, Locale locale, Long projectId, String typos);

    List<FormViewStatusDto> findFormNamesAndStatusByFlowChart(Long flowchartId,
                                                              Locale locale,
                                                              String typos,
                                                              Long projectId,
                                                              FormType formType);

    List<FormViewStatusDto> findFormNamesAndStatusByFlowChartChild(Long flowchartChildId,
                                                                   Locale locale,
                                                                   String typos,
                                                                   Long projectId,
                                                                   FormType formType);

    List<String> fetchAllTableNames();

    void save(FormViewDto formViewDto);

    boolean isFormTypeOta(Long projectId);
}
