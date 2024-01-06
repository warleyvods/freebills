package com.freebills.gateways;

import com.freebills.gateways.entities.UserEntity;
import com.freebills.exceptions.UserNotFoundException;
import com.freebills.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public record UserGateway(UserRepository userRepository) {

    public UserEntity save(final UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public Page<UserEntity> getAll(final Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public UserEntity findById(final Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    public UserEntity findByLogin(final String login) {
        return userRepository.findByLoginIgnoreCase(login).orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    public Optional<UserEntity> findByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity update(final UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public void deleteById(final Long id) {
        userRepository.deleteById(id);
    }
}
