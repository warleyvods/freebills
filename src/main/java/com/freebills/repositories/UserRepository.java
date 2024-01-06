package com.freebills.repositories;


import com.freebills.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginIgnoreCase(final String login);

    Boolean existsByLogin(final String login);

    Boolean existsByEmail(final String email);

    Optional<User> findByEmail(final String email);

}
