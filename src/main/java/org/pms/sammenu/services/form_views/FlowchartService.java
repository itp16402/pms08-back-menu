package org.pms.sammenu.services.form_views;

import org.pms.sammenu.dto.form_views.FlowchartParentDto;
import org.pms.sammenu.enums.Locale;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FlowchartService {

    List<FlowchartParentDto> fetchFlowchart(Locale language, Long projectId);
}
