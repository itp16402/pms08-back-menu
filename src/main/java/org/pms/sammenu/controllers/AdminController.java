package org.pms.sammenu.controllers;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.dto.form_views.FormViewDto;
import org.pms.sammenu.enums.Locale;
import org.pms.sammenu.exceptions.ErrorResponse;
import org.pms.sammenu.exceptions.ResourceNotFoundException;
import org.pms.sammenu.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/admin")
@RestController
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * Assign all new forms on form-role table (manager role) which added to form-list
     *
     */
    @PostMapping(value = "/assign-manager/all-forms")
    ResponseEntity assignToManagerAllForms() {
        log.info("Assign all new forms to manager");
        adminService.assignToManagerAllForms();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/form-names/{tableName}/{locale}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<FormViewDto> findFormNames(@PathVariable String tableName, @PathVariable Locale locale) {
        log.info("Get form-names of form[{}]", tableName);
        return adminService.fetchFormNames(tableName, locale);
    }

    @PostMapping(value = "/form-names", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity saveFormNames(@RequestBody FormViewDto formViewDto) {
        log.info("Save form-names of form[{}]", formViewDto.getTableName());
        adminService.saveFormNames(formViewDto);
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
