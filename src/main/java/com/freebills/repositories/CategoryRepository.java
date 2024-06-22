package com.freebills.repositories;

import com.freebills.gateways.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Query(value = """
                   SELECT c.* FROM categories c
                   INNER JOIN users u
                   ON c.user_id = u.id
                   WHERE u.login = :username
            """, nativeQuery = true)
    List<CategoryEntity> findAllCategoryByUser(String username);

    @Query(value = """
                SELECT c.* FROM categories c
                INNER JOIN users u
                ON c.user_id = u.id
                WHERE u.login = :username
                AND c.id = :id
            """, nativeQuery = true)
    Optional<CategoryEntity> findByIdWithUser(Long id, String username);

    void deleteCategoryEntityByIdAndUser_Login(Long id, String username);

}
