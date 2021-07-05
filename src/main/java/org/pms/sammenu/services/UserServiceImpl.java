package org.pms.sammenu.services;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.domain.User;
import org.pms.sammenu.dto.UserDto;
import org.pms.sammenu.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ConversionService conversionService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ConversionService conversionService) {
        this.userRepository = userRepository;
        this.conversionService = conversionService;
    }

    @Override
    public List<UserDto> fetchUsers(String username) {

        log.info("Fetch Users process starts");

        List<User> users = userRepository.findAll();

        List<UserDto> userDtoList = users.stream()
                .filter(user -> !user.getUsername().equals(username) && !user.getUsername().equals("god"))
                .map(user -> conversionService.convert(user, UserDto.class))
                .collect(Collectors.toList());

        log.info("Fetch Users process end");

        return userDtoList;
    }

    @Override
    public List<UserDto> search(String username, String lastName) {

        log.info("Search Users by starting lastName[{}] process starts", lastName);

        List<User> users = userRepository.findByLastNameStartingWithIgnoreCase(lastName);

        List<UserDto> userDtoList = users.stream()
                .filter(user -> !user.getUsername().equals(username) && !user.getUsername().equals("god"))
                .map(user -> conversionService.convert(user, UserDto.class))
                .collect(Collectors.toList());

        log.info("Search Users by starting lastName[{}] process end", lastName);

        return userDtoList;
    }

    @Override
    public UserDto fetchByUsername(String username) {

        log.info("Fetch User[{}] process starts", username);

        Optional<User> optionalUser = userRepository.findByUsername(username);

        UserDto userDto = optionalUser
                .map(user -> conversionService.convert(user, UserDto.class))
                .orElse(new UserDto());

        log.info("Fetch User[{}] process end", username);

        return userDto;
    }
}
