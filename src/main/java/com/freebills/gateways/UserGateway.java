package com.freebills.gateways;

import com.freebills.domains.User;
import com.freebills.exceptions.UserNotFoundException;
import com.freebills.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public record UserGateway(UserRepository userRepository) {

    public User save(final User user) {
        return userRepository.save(user);
    }

    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User findById(final Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    public User findByLogin(final String login) {
        return userRepository.findByLogin(login).get();
    }

    public User update(final User user) {
        return userRepository.save(user);
    }

    public void deleteById(final Long id) {
        userRepository.deleteById(id);
    }
}
