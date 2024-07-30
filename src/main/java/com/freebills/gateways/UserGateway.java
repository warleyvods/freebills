package com.freebills.gateways;

import com.freebills.domain.User;
import com.freebills.exceptions.InvalidCredentialsException;
import com.freebills.exceptions.UserNotFoundException;
import com.freebills.gateways.mapper.UserGatewayMapper;
import com.freebills.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserGateway {

    private final UserRepository userRepository;
    private final UserGatewayMapper userGatewayMapper;

    public User save(final User user) {
        return userGatewayMapper.toDomain(userRepository.save(userGatewayMapper.toEntity(user)));
    }

    public Page<User> getAll(final Pageable pageable) {
        return userRepository.findAll(pageable).map(userGatewayMapper::toDomain);
    }

    public List<User> findAll() {
        return userRepository.findAll().stream().map(userGatewayMapper::toDomain).toList();
    }

    public User findById(final Long id) {
        return userGatewayMapper.toDomain(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
    }

    public User findByLogin(final String login) {
        return userGatewayMapper.toDomain(userRepository.findByLoginIgnoreCase(login).orElseThrow(InvalidCredentialsException::new));
    }

    public User findByEmail(final String email) {
        return userGatewayMapper.toDomain(userRepository.findByEmailIgnoreCase(email).orElseThrow(InvalidCredentialsException::new));
    }

    public User update(final User user) {
        return userGatewayMapper.toDomain(userRepository.save(userGatewayMapper.toEntity(user)));
    }

    public void deleteById(final Long id) {
        userRepository.deleteById(id);
    }

    public void deleteByIds(final List<Long> id) {
        userRepository.deleteAllById(id);
    }

    public Boolean existsByLogin(final String login) {
        return userRepository.existsByLoginIgnoreCase(login);
    }

    public Boolean existsByEmail(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }
}
