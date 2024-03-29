package com.freebills.repositories;

import com.freebills.gateways.entities.ForgetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgetPasswordRepository extends JpaRepository<ForgetPassword, String> {

    Optional<ForgetPassword> findByEmail(String email);

}
