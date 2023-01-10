package com.freebills.security.services;


import com.freebills.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
        final var user = userRepository.findByLoginIgnoreCase(login)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with login: " + login));

        final var authorityListAdmin = AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");
        final var authorityListUser = AuthorityUtils.createAuthorityList("ROLE_USER");

        return UserDetailsImpl.build(user, user.isAdmin() ? authorityListAdmin : authorityListUser);
    }
}
