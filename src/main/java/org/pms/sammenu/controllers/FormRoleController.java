package org.pms.sammenu.controllers;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.dto.form_roles.FormRoleDto;
import org.pms.sammenu.dto.form_roles.ParentPhaseDto;
import org.pms.sammenu.enums.Locale;
import org.pms.sammenu.services.FormRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/form-roles")
@RestController
@Slf4j
public class FormRoleController {

    @Autowired
    private FormRoleService formRoleService;

    /**
     * Assign work to members, as inserted form-roles
     *
     * @param formNames
     * @param projectId
     * @param userId
     */
    @PostMapping(value = "/assign-work/{userId}/{projectId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity saveFormRoles(@RequestBody Set<String> formNames,
                                 @PathVariable Long projectId,
                                 @PathVariable Long userId) {
        log.info("Assign work to member [id:{}]", userId);
        formRoleService.assignWorkToMembers(userId, projectId, formNames);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Remove work from members, as deleted form-roles
     *
     * @param formNames
     * @param projectId
     * @param userId
     */
    @DeleteMapping(value = "/remove-work/{userId}/{projectId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity removeFormRoles(@RequestBody Set<String> formNames,
                                   @PathVariable Long projectId,
                                   @PathVariable Long userId) {
        log.info("Remove work from member [id:{}]", userId);
        formRoleService.removeWorkFromMembers(userId, projectId, formNames);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retrieves Form role for specific member in specific form
     *
     * @param userId
     * @param projectId
     * @param formListId
     * @return Form-role
     */
    @GetMapping(value = "/{userId}/{projectId}/{formListId}", produces = MediaType.APPLICATION_JSON_VALUE)
    boolean checkIfMemberIsAssignedInForm(@PathVariable Long userId,
                                                @PathVariable Long projectId,
                                                @PathVariable Long formListId) {
        log.info("Fetch form-role for member[id:{}] in form-list[id:{}]", userId, formListId);
        return formRoleService.checkIfMemberIsAssignedInForm(userId, projectId, formListId);
    }

    /**
     * Retrieves Form roles for specific member
     *
     * @param userId
     * @param projectId
     * @return Form-roles
     */
    @GetMapping(value = "/{userId}/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<FormRoleDto> findFormRolesByMember(@PathVariable Long userId,
                                            @PathVariable Long projectId) {
        log.info("Fetch form-roles for member[id:{}] in order[id:{}]", userId, projectId);
        return formRoleService.fetchByMember(userId, projectId);
    }

    /**
     * Retrieves Forms with assigned members
     *
     * @param language
     * @param projectId
     * @return PhaseDtos
     */
    @GetMapping(value = "/forms/{userId}/{language}/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ParentPhaseDto> fetchFlowchartWithAssignedMembers(@PathVariable Long userId,
                                                           @PathVariable Locale language,
                                                           @PathVariable Long projectId) {
        log.info("Fetch flowchart with assigned members for order [id:{}]", projectId);
        return formRoleService.fetchFlowchartWithAssignedMembers(userId, language, projectId);
    }
}
