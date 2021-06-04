package org.pms.sammenu.controllers;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.dto.UserRoleRequestDto;
import org.pms.sammenu.dto.UserRoleResponseDto;
import org.pms.sammenu.services.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/user-roles")
@RestController
@Slf4j
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    /**
     * Retrieves Set of Member and their roles specified by given projectId
     *
     * @param projectId
     * @return Set of MemberRoles specified by given projectId
     */
    @GetMapping("/{projectId}")
    Set<UserRoleResponseDto> fetchUserRolesByProjectId(@PathVariable Long projectId) {
        log.info("Fetch UserRoles[orderId: {}]", projectId);
        return userRoleService.fetchUserRolesByProjectId(projectId);
    }

    /**
     * Inserts new MemberRoles and remove previous roles
     *
     * @param userRoleRequestDtos the MemberRole object to be saved
     * @param projectId
     */
    @PostMapping(value = "/{projectId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity addMembers(@RequestBody List<UserRoleRequestDto> userRoleRequestDtos,
                              @PathVariable Long projectId) {
        log.info("Save MemberRole[{}]", userRoleRequestDtos.toString());
        userRoleService.save(userRoleRequestDtos, projectId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Remove a member
     *
     * @param memberId
     * @param projectId
     *
     */
    @DeleteMapping("/remove/member/{memberId}/{projectId}")
    public ResponseEntity removeMember(@PathVariable Long memberId, @PathVariable Long projectId) {

        log.info("Remove member {} from project {}", memberId, projectId);

        userRoleService.removeMemberFromGroup(memberId, projectId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Assign new admin and delete previous one
     *
     * @param userId
     * @param projectId
     */
    @PostMapping(value = "/assign-admin/{userId}/{projectId}")
    ResponseEntity assignAdmin(@PathVariable Long userId, @PathVariable Long projectId) {
        log.info("Assign Admin");
        userRoleService.assignAdmin(userId, projectId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Remove admin
     *
     * @param userId
     * @param projectId
     *
     */
    @DeleteMapping("/remove/admin/{userId}/{projectId}")
    public ResponseEntity removeAdmin(@PathVariable Long userId, @PathVariable Long projectId) {

        log.info("Remove admin {} from project {}", userId, projectId);

        userRoleService.removeAdmin(userId, projectId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
