package com.freebills.repositories;


import com.freebills.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.login LIKE %:login%")
    Optional<User> findByLogin(String login);

    List<User> findByLoginIgnoreCase(String login);

    Boolean existsByLogin(final String login);

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

}
