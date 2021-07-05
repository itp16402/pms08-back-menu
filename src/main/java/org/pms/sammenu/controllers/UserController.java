package org.pms.sammenu.controllers;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.dto.UserDto;
import org.pms.sammenu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/users")
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{username}")
    UserDto fetchUserByUsername(@PathVariable String username, Principal principal) {
        log.info("Fetch User[{}]", username);
        return userService.fetchByUsername(username);
    }

    @GetMapping(value = "/all/{username}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<UserDto> fetchAllUsers(@PathVariable String username, Principal principal) {

        log.info("Fetch All users");

        return userService.fetchUsers(username);
    }

    @GetMapping(value = "/search/{username}{lastName}/",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<UserDto> search(@PathVariable("username") String username,
                         @PathVariable("lastName") String lastName,
                         Principal principal) {

        log.info("Search Users by starting lastName[{}]", lastName);

        return userService.search(username, lastName);
    }
}
