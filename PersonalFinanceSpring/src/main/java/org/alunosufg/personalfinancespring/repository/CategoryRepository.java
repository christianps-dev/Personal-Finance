package org.alunosufg.personalfinancespring.repository;

import org.alunosufg.personalfinancespring.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    boolean existsByCategory(String category);

    CategoryEntity getCategoryEntitiesByCategory(String category);
}
