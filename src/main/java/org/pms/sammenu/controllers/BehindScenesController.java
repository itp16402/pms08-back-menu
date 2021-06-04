package org.pms.sammenu.controllers;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.exceptions.ErrorResponse;
import org.pms.sammenu.exceptions.ResourceNotFoundException;
import org.pms.sammenu.services.BehindScenesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/behind-scenes")
@RestController
@Slf4j
public class BehindScenesController {

    @Autowired
    private BehindScenesService behindScenesService;

    /**
     * Assign all new forms on form-role table (manager role) which added to form-list
     *
     */
    @PostMapping(value = "/assign-admin/all-forms")
    ResponseEntity assignToManagerAllForms() {
        log.info("Assign all new forms to manager");
        behindScenesService.assignToManagerAllForms();
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
