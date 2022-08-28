package com.freebills.auth.service;

import com.freebills.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String login) {
        final var user = Optional.ofNullable(userRepository.findByLogin(login))
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        final var authorityListAdmin = AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");
        final var authorityListUser = AuthorityUtils.createAuthorityList("ROLE_USER");

        return new User(user.getLogin(), user.getPassword(), user.isAdmin() ? authorityListAdmin : authorityListUser);
    }
}
