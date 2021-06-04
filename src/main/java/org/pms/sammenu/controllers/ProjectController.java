package org.pms.sammenu.controllers;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.dto.ProjectResponseDto;
import org.pms.sammenu.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/projects")
@RestController
@Slf4j
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping(value = "/{username}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<ProjectResponseDto> findProjectsByMemberUsername(
            @PathVariable String username,
            Principal principal) {

        log.info("Fetch Projects");

        return projectService.fetchProjectsByUsername(username);
    }
}
