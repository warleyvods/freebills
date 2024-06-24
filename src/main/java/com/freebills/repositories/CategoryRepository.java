package com.freebills.repositories;

import com.freebills.gateways.entities.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Query(value = """
                   SELECT c.* FROM categories c
                   INNER JOIN users u
                   ON c.user_id = u.id
                   WHERE u.login = :username
                   AND (:keyword IS NULL OR CAST(c.name AS text) ILIKE CAST(CONCAT('%', :keyword, '%') AS text))
                   AND (:categoryType IS NULL OR c.category_type = :categoryType)
            """, nativeQuery = true)
    Page<CategoryEntity> findAllCategoryByUser(final String username,
                                               final String keyword,
                                               final String categoryType,
                                               final Pageable pageable);

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
