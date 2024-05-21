package com.freebills.repositories;


import com.freebills.gateways.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLoginIgnoreCase(final String login);

    Boolean existsByLoginIgnoreCase(final String login);

    Boolean existsByEmailIgnoreCase(final String email);

    Optional<UserEntity> findByEmailIgnoreCase(final String email);

}
