package org.pms.sammenu.services;

import org.pms.sammenu.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    List<UserDto> fetchUsers(String username);

    List<UserDto> search(String username, String lastName);

    UserDto fetchByUsername(String username);
}
