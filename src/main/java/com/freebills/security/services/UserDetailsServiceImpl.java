package com.freebills.security.services;


import com.freebills.usecases.FindUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final FindUser findUser;

    @Override
    public UserDetails loadUserByUsername(final String loginOrEmail) throws UsernameNotFoundException {
        final var user = findUser.byLoginOrEmail(loginOrEmail);

        final var authorityListAdmin = AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");
        final var authorityListUser = AuthorityUtils.createAuthorityList("ROLE_USER");

        return UserDetailsImpl.build(user, Boolean.TRUE.equals(user.getAdmin()) ? authorityListAdmin : authorityListUser);
    }
}
