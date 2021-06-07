package org.pms.sammenu.controllers.forms;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.dto.forms.essential_size.base.BaseDto;
import org.pms.sammenu.dto.forms.essential_size.base.BaseResponseDto;
import org.pms.sammenu.dto.forms.essential_size.requests.EssentialSizeOverallRequestDto;
import org.pms.sammenu.dto.forms.essential_size.requests.EssentialSizePerformanceRequestDto;
import org.pms.sammenu.dto.forms.essential_size.requests.EssentialSizeRequestDto;
import org.pms.sammenu.dto.forms.essential_size.responses.EssentialSizeDto;
import org.pms.sammenu.dto.forms.essential_size.responses.EssentialSizePerformanceDto;
import org.pms.sammenu.enums.Locale;
import org.pms.sammenu.exceptions.ErrorResponse;
import org.pms.sammenu.exceptions.UnacceptableActionException;
import org.pms.sammenu.services.forms.EssentialSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/essential-sizes")
@RestController
@Slf4j
@Api(description = "Essential Size (Ουσιώδες Μέγεθος). FORM-CODE: 221")
public class EssentialSizeController {
    
    @Autowired
    private EssentialSizeService essentialSizeService;

    /**
     * Retrieves the EssentialSize specified by given order-member id
     *
     * @param id the EssentialSize order-member id
     * @return the EssentialSize specified by given order-member id
     */
    @GetMapping("/order/{projectId}/{formListId}/{locale}")
    EssentialSizeDto fetchEssentialSizeByOrderSamId(@PathVariable("projectId") Long id,
                                                    @PathVariable("formListId") Long formListId,
                                                    @PathVariable("locale") Locale locale) {
        log.info("Fetch EssentialSize by given order-sam[id: {}]", id);
        return essentialSizeService.fetchEssentialSizeByProjectId(id, formListId, locale);
    }

    /**
     * Retrieves Base-list specified by given order-member id
     *
     * @return the Base-list specified by given order-member id
     */
    @GetMapping("/base/{projectId}/{locale}")
    List<BaseResponseDto> fetchBaseList(@PathVariable("projectId") Long id,
                                        @PathVariable("locale") Locale locale) {
        log.info("Fetch Base-list by order-sam[id: {}] start", id);
        return essentialSizeService.fetchBaseList(id, locale);
    }

    /**
     * Retrieves Base specified by given base id
     *
     * @return the Base specified by given base id
     */
    @GetMapping("/base/{baseId}/{projectId}/{locale}")
    BaseDto fetchBase(@PathVariable("projectId") Long projectId,
                      @PathVariable("baseId") Integer baseId,
                      @PathVariable("locale") Locale locale) {
        log.info("Fetch Base [id: {}]", baseId);
        return essentialSizeService.fetchBase(baseId, projectId, locale);
    }

    /**
     * Inserts a new EssentialSize in case given essentialSizeDto does not provide an id
     * Updates an existing EssentialSize in case given essentialSizeDto provides an id
     *
     * @param essentialSizeDto the EssentialSize object to be saved
     */
    @PostMapping(value = "/{projectId}/{formListId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity saveEssentialSize(@PathVariable("projectId") Long projectId,
                                     @PathVariable("formListId") Long formListId,
                                     @RequestBody EssentialSizeRequestDto essentialSizeDto,
                                     HttpServletRequest request) {
        log.info("Save EssentialSize[{}]", essentialSizeDto.toString());
        essentialSizeService.save(projectId, essentialSizeDto, formListId, request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/overall/{projectId}/{formListId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity saveEssentialSizeOverall(@PathVariable("projectId") Long projectId,
                                            @PathVariable("formListId") Long formListId,
                                            @RequestBody EssentialSizeOverallRequestDto essentialSizeOverallRequestDto) {
        log.info("Save EssentialSizeOverall[{}]", essentialSizeOverallRequestDto.toString());
        essentialSizeService.save(projectId, essentialSizeOverallRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/performance/{projectId}/{formListId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity saveEssentialSizePerformance(@PathVariable("projectId") Long projectId,
                                                @PathVariable("formListId") Long formListId,
                                                @RequestBody EssentialSizePerformanceRequestDto essentialSizePerformanceRequestDto) {
        log.info("Save EssentialSizePerformance[{}]", essentialSizePerformanceRequestDto.toString());
        essentialSizeService.save(projectId, essentialSizePerformanceRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Retrieves Performance Materiality
     *
     * @return List of Performance Materiality current and previous year
     */
    @GetMapping("/performance-materiality/{projectId}")
    List<EssentialSizePerformanceDto> fetchPerformanceMateriality(@PathVariable("projectId") Long projectId) {
        log.info("Fetch Performance Materiality for order[id:{}]", projectId);
        return essentialSizeService.fetchPerformanceMateriality(projectId);
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
