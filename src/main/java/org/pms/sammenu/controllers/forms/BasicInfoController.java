package org.pms.sammenu.controllers.forms;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.dto.forms.BasicInfoDto;
import org.pms.sammenu.services.forms.BasicInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/basic-info")
@RestController
@Slf4j
@Api(description = "Basic Info (Βασικές πληροφορίες). FORM-CODE: 111")
public class BasicInfoController {
    
    @Autowired
    private BasicInfoService basicInfoService;

    /**
     * Retrieves the BasicInfo specified by given order-member projectId
     *
     * @param projectId the BasicInfo order-member projectId
     * @return the BasicInfo specified by given order-member projectId
     */
    @GetMapping("/project/{projectId}/{formListId}")
    BasicInfoDto fetchBasicInfoByOrderSamId(@PathVariable("projectId") Long projectId,
                                            @PathVariable("formListId") Long formListId) {
        log.info("Fetch BasicInfo by given order-sam[projectId: {}]", projectId);
        return basicInfoService.fetchBasicInfoByProjectId(projectId, formListId);
    }

    /**
     * Inserts a new BasicInfo in case given BasicInfoDto does not provide an id
     * Updates an existing BasicInfo in case given BasicInfoDto provides an id
     *
     * @param basicInfoDto the BasicInfo object to be saved
     */
    @PostMapping(value = "/{projectId}/{formListId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity saveBasicInfo(@PathVariable("projectId") Long projectId,
                                 @PathVariable("formListId") Long formListId,
                                 @RequestBody BasicInfoDto basicInfoDto,
                                 HttpServletRequest request) {
        log.info("Save BasicInfo[{}]", basicInfoDto.toString());
        basicInfoService.save(projectId, basicInfoDto, formListId, request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
