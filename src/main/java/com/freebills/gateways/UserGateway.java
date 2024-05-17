package com.freebills.gateways;

import com.freebills.domain.User;
import com.freebills.gateways.entities.UserEntity;
import com.freebills.exceptions.UserNotFoundException;
import com.freebills.gateways.mapper.UserGatewayMapper;
import com.freebills.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public record UserGateway(UserRepository userRepository, UserGatewayMapper userGatewayMapper) {

    public User save(final User user) {
        final UserEntity entity = userRepository.save(userGatewayMapper.toEntity(user));
        return userGatewayMapper.toDomain(entity);
    }

    public Page<User> getAll(final Pageable pageable) {
        return userRepository.findAll(pageable).map(userGatewayMapper::toDomain);
    }

    public User findById(final Long id) {
        return userGatewayMapper.toDomain(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
    }

    public User findByLogin(final String login) {
        return userGatewayMapper.toDomain(userRepository.findByLoginIgnoreCase(login).orElseThrow(UserNotFoundException::new));
    }

    public Optional<User> findByEmail(final String email) {
        return userRepository.findByEmail(email).map(userGatewayMapper::toDomain);
    }

    public User update(final User user) {
        final UserEntity entity = userRepository.save(userGatewayMapper.toEntity(user));
        return userGatewayMapper.toDomain(entity);
    }

    public void deleteById(final Long id) {
        userRepository.deleteById(id);
    }

    public void deleteByIds(final List<Long> id) {
        userRepository.deleteAllById(id);
    }

    public Boolean existsByLogin(final String login) {
        return userRepository.existsByLogin(login);
    }

    public Boolean existsByEmail(final String email) {
        return userRepository.existsByEmail(email);
    }
}
