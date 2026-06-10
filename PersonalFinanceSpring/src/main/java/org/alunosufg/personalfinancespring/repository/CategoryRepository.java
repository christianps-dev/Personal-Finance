package org.alunosufg.personalfinancespring.repository;

import org.alunosufg.personalfinancespring.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    boolean existsByCategory(String category);

    CategoryEntity getCategoryEntitiesByCategory(String category);

    @Query("SELECT c.id FROM category c WHERE c.category = :ctg")
    Optional<Long> getCategoryId(String ctg);
}
