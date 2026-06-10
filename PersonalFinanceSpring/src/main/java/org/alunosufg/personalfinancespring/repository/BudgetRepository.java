package org.alunosufg.personalfinancespring.repository;

import jakarta.transaction.Transactional;
import org.alunosufg.personalfinancespring.dto.transactions.BudgetDTO;
import org.alunosufg.personalfinancespring.entities.BudgetEntity;
import org.alunosufg.personalfinancespring.entities.CategoryEntity;
import org.alunosufg.personalfinancespring.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {

    @Query("SELECT b FROM budget b WHERE b.user.id = :usr AND b.category.id = :ctg AND b.month = :bMonth")
    Optional<BudgetEntity> existsBudget(Long usr, Long ctg, Integer bMonth);

    @Query("SELECT new org.alunosufg.personalfinancespring.dto.transactions.BudgetDTO(b.category.category, b.budgetLimit, b.month) " +
            "FROM budget b WHERE b.user = :userReq AND b.month = :bMonth")
    List<BudgetDTO> getBudgets(UserEntity userReq, Integer bMonth);

    @Modifying
    @Transactional
    @Query("UPDATE budget b SET b.budgetLimit = :value WHERE b.user.id = :usrId AND b.category.id = :ctgId AND b.month = :mnth")
    void updateBudget(Long usrId, Long ctgId, Integer mnth, Integer value);

}
