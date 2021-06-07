package org.pms.sammenu.controllers.form_views;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.dto.form_views.FlowchartParentDto;
import org.pms.sammenu.enums.Locale;
import org.pms.sammenu.services.form_views.FlowchartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/flowcharts")
@RestController
@Slf4j
public class FlowchartController {

    @Autowired
    private FlowchartService flowchartService;

    /**
     * Retrieves flowchart
     *
     * @param locale the flowchart locale
     * @return the flowchart specified by given locale, formType
     */
    @GetMapping(value = "/{locale}/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<FlowchartParentDto> findFlowchart(@PathVariable Locale locale, @PathVariable Long projectId) {

        log.info("Fetch flowchart");

        return flowchartService.fetchFlowchart(locale, projectId);
    }
}
