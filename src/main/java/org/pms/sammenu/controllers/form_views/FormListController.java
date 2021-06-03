package org.pms.sammenu.controllers.form_views;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.dto.form_views.FormListDto;
import org.pms.sammenu.dto.form_views.FormStatusRequestDto;
import org.pms.sammenu.enums.FormStatus;
import org.pms.sammenu.services.form_views.FormListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/form-lists")
@RestController
@Slf4j
public class FormListController {

    @Autowired
    private FormListService formListService;

    /**
     * Retrieves FormLists
     *
     * @return all formLists
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<FormListDto> fetchFormLists() {

        log.info("Fetch FormLists");

        return formListService.findAll();
    }

    /**
     * Retrieves FormList
     *
     * @param formName the FormList formName
     * @return the formList specified by given formName
     */
    @GetMapping(value = "/{formName}", produces = MediaType.APPLICATION_JSON_VALUE)
    FormListDto findFormListByFormName(@PathVariable String formName) {

        log.info("Fetch FormList {}", formName);

        return formListService.fetchByFormName(formName);
    }

    @PostMapping(value = "/status/{formListId}/{projectId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity saveStatus(@PathVariable Long formListId,
                              @PathVariable Long projectId,
                              @RequestBody FormStatusRequestDto formStatusRequestDto,
                              HttpServletRequest request) {
        log.info("Save Status");
        formListService.saveStatus(projectId, formStatusRequestDto.getStatus(), formListId, request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/status/{projectId}/{formListId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    FormStatus getStatus(@PathVariable Long projectId, @PathVariable Long formListId) {
        log.info("Get Status for form[id:{}] in project[id:{}]", formListId, projectId);
        return formListService.getStatus(projectId, formListId);
    }
}
