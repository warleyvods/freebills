package com.freebills.repositories;


import com.freebills.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.login LIKE %:login%")
    User findByLogin(String login);

    List<User> findByLoginIgnoreCase(String login);
}
