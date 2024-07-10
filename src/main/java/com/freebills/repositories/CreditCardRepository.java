package com.freebills.repositories;

import com.freebills.gateways.entities.CreditCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCardEntity, Long> {

    List<CreditCardEntity> findByAccount_User_Login(final String login);

    Optional<CreditCardEntity> findByIdAndAccount_User_Login(Long id, String username);

    Optional<CreditCardEntity> findById(Long id);

    void deleteByIdAndAccount_User_Login(Long id, String username);

}
