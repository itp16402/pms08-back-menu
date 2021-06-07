package org.pms.sammenu.controllers.forms;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.dto.forms.important_accounts.ImportantAccountDto;
import org.pms.sammenu.dto.forms.important_accounts.ImportantAccountRequestDto;
import org.pms.sammenu.exceptions.ErrorResponse;
import org.pms.sammenu.exceptions.UnacceptableActionException;
import org.pms.sammenu.services.forms.ImportantAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/important-accounts")
@RestController
@Slf4j
@Api(description = "Important Account (Σημαντικοί Λογαριασμοί και Σχετικοί Ισχυρισμοί). FORM-CODE: 231")
public class ImportantAccountController {

    @Autowired
    private ImportantAccountService importantAccountService;

    /**
     * Retrieves the ImportantAccount specified by given order-member id
     *
     * @param projectId the ImportantAccount order-member id
     * @return the ImportantAccount specified by given order-member id
     */
    @GetMapping("/order/{projectId}/{formListId}")
    ImportantAccountDto fetchImportantAccountByProjectId(@PathVariable("projectId") Long projectId,
                                                         @PathVariable("formListId") Long formListId) {
        log.info("Fetch ImportantAccount by given order[id: {}]", projectId);
        return importantAccountService.fetchImportantAccountByProjectId(projectId, formListId);
    }

    /**
     * Inserts a new ImportantAccount in case given importantAccountRequestDto does not provide an id
     * Updates an existing ImportantAccount in case given importantAccountRequestDto provides an id
     *
     * @param importantAccountRequestDto the ImportantAccount object to be saved
     */
    @PostMapping(value = "/{projectId}/{formListId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity saveImportantAccount(@PathVariable("projectId") Long projectId,
                                        @PathVariable("formListId") Long formListId,
                                        @RequestBody ImportantAccountRequestDto importantAccountRequestDto,
                                        HttpServletRequest request) {
        log.info("Save ImportantAccount[{}]", importantAccountRequestDto.toString());
        importantAccountService.save(projectId, importantAccountRequestDto, formListId, request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ExceptionHandler(UnacceptableActionException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(HttpStatus.NOT_ACCEPTABLE.value())
                .status(HttpStatus.NOT_ACCEPTABLE)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }
}
