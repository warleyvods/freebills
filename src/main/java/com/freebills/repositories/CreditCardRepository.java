package com.freebills.repositories;

import com.freebills.gateways.entities.CreditCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCardEntity, Long> {

    @Query(value = """
            select cc.* from credit_card cc
            inner join public.accounts a
                on cc.account_id = a.id
            inner join users u
                on a.user_id = u.id
            where u.login = :username
            and cc.archived = :archived
            """, nativeQuery = true)
    List<CreditCardEntity> findAllCcByLoginAndStatus(boolean archived, String username);

    Optional<CreditCardEntity> findByIdAndAccount_User_Login(Long id, String username);

    void deleteByIdAndAccount_User_Login(Long id, String username);

}
