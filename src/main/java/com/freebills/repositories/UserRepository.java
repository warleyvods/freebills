package com.freebills.repositories;


import com.freebills.gateways.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLoginIgnoreCase(final String login);

    Boolean existsByLogin(final String login);

    Boolean existsByEmail(final String email);

    UserEntity findByEmail(final String email);

}
