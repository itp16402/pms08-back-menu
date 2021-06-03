package org.pms.sammenu.controllers.form_views;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.dto.form_views.FormViewDto;
import org.pms.sammenu.dto.form_views.FormViewStatusDto;
import org.pms.sammenu.enums.FormType;
import org.pms.sammenu.enums.Locale;
import org.pms.sammenu.exceptions.ErrorResponse;
import org.pms.sammenu.exceptions.ResourceNotFoundException;
import org.pms.sammenu.services.form_views.FormViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/form-views")
@RestController
@Slf4j
public class FormViewController {

    @Autowired
    private FormViewService formViewService;

    /**
     * Retrieves the forms specified by given tableName
     *
     * @param tableName the forms tableName
     * @param locale the forms locale
     * @param formType the forms formType
     * @return the forms specified by given tableName, locale, formType
     */
    @GetMapping("/form-type/{tableName}/{locale}/{formType}")
    List<FormViewDto> findFormsByTableNameAndFormType(@PathVariable String tableName,
                                                      @PathVariable Locale locale,
                                                      @PathVariable FormType formType) {
        log.info("Fetch forms[table-name: {}]", tableName);
        return formViewService.fetchFormsByTableNameAndFormType(tableName, locale, formType);
    }

    /**
     * Retrieves the forms specified by given tableName
     *
     * @param tableName the forms tableName
     * @param locale the forms locale
     * @param projectId the orderId
     * @return the forms specified by given tableName, locale, orderId
     */
    @GetMapping("/order/{tableName}/{locale}/{projectId}")
    List<FormViewDto> findFormsByTableNameAndOrderId(@PathVariable String tableName,
                                                     @PathVariable Locale locale,
                                                     @PathVariable Long projectId) {

        log.info("Fetch forms[table-name: {}]", tableName);

        return formViewService.fetchFormsByTableNameAndProjectId(tableName, locale, projectId);
    }

    /**
     * Retrieves the forms specified by given flowchartChildId and typo
     *
     * @param flowchartChildId the forms tableName
     * @param locale the forms locale
     * @param projectId the forms orderId
     * @return the forms specified by given tableName, locale, projectId
     */
    @GetMapping("/flowchart-child/{flowchartChildId}/{locale}/{typo}/{projectId}")
    List<FormViewStatusDto> findFormNamesAndStatusByFlowChartChild(@PathVariable Long flowchartChildId,
                                                                   @PathVariable Locale locale,
                                                                   @PathVariable String typo,
                                                                   @RequestParam(value = "formType", required = false) FormType formType,
                                                                   @PathVariable Long projectId) {

        log.info("Fetch forms[flowchartChildId: {}] and typo: {}", flowchartChildId, typo);

        return formViewService.findFormNamesAndStatusByFlowChartChild(flowchartChildId, locale, typo, projectId, formType);
    }

    /**
     * Retrieves the forms specified by given flowchartId and typo
     *
     * @param flowchartId the forms tableName
     * @param locale the forms locale
     * @param projectId the forms projectId
     * @return the forms specified by given tableName, locale, projectId
     */
    @GetMapping("/flowchart/{flowchartId}/{locale}/{typo}/{projectId}")
    List<FormViewStatusDto> findFormNamesAndStatusByFlowChart(@PathVariable Long flowchartId,
                                                              @PathVariable Locale locale,
                                                              @PathVariable String typo,
                                                              @RequestParam(value = "formType", required = false) FormType formType,
                                                              @PathVariable Long projectId) {

        log.info("Fetch forms[flowchartId: {}] and typo: {}", flowchartId, typo);

        return formViewService.findFormNamesAndStatusByFlowChart(flowchartId, locale, typo, projectId, formType);
    }

    /**
     * Retrieves the form specified by given tableName and typo
     *
     * @param tableName the forms tableName
     * @param locale the forms locale
     * @param projectId the forms orderId
     * @param typos the forms typos
     * @return the forms specified by given tableName, locale, projectId
     */
    @GetMapping("/{tableName}/{locale}/{projectId}/{typos}")
    FormViewDto findFormByTableNameAndTypo(@PathVariable String tableName,
                                           @PathVariable Locale locale,
                                           @PathVariable Long projectId,
                                           @PathVariable String typos) {

        log.info("Fetch forms[table-name: {}]", tableName);

        return formViewService.fetchFormByTableNameAndTypos(tableName, locale, projectId, typos);
    }

    /**
     * Retrieves the tableNames of forms
     *
     * @return string of tableNames
     */
    @GetMapping("/table-names")
    List<String> findAllTableNames() {

        log.info("Fetch forms[table-names]");

        return formViewService.fetchAllTableNames();
    }

    /**
     * Check if formType is OTA
     *
     * @return true or false
     */
    @GetMapping("/check/form-type/OTA/{orderId}")
    boolean isFormTypeOta(@PathVariable Long orderId) {

        log.info("Check if formType is OTA in order[id: {}]", orderId);

        return formViewService.isFormTypeOta(orderId);
    }

    /**
     * Updates an existing FormView
     *
     * @param formViewDto the FormView object to be saved
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity saveFormView(@RequestBody FormViewDto formViewDto) {
        log.info("Save FormView[{}]", formViewDto.toString());
        formViewService.save(formViewDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
