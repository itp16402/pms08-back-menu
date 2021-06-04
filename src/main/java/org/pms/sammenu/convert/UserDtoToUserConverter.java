package org.pms.sammenu.convert;

import org.pms.sammenu.domain.Authority;
import org.pms.sammenu.domain.User;
import org.pms.sammenu.dto.UserDto;
import org.pms.sammenu.enums.AuthorityType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, User> {

    @Override
    public User convert(UserDto userDto) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .active((short) 0)
                .authorities(buildAuthorities())
                .build();
    }

    private List<Authority> buildAuthorities(){

        List<Authority> authorities = new ArrayList<>();

        authorities.add(Authority.builder()
                .id((short) 3)
                .description(AuthorityType.MEMBER.description())
                .build());

        return authorities;
    }
}
