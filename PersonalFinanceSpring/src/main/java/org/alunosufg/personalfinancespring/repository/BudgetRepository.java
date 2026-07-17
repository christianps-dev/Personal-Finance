package org.alunosufg.personalfinancespring.repository;

import jakarta.transaction.Transactional;
import org.alunosufg.personalfinancespring.dto.budgets.BudgetDTO;
import org.alunosufg.personalfinancespring.dto.budgets.BudgetLimitDTO;
import org.alunosufg.personalfinancespring.entities.AccountEntity;
import org.alunosufg.personalfinancespring.entities.BudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {

    @Query("SELECT b FROM budget b WHERE b.account = :acc AND b.category.id = :ctg AND b.month = :bMonth")
    Optional<BudgetEntity> existsBudget(AccountEntity acc, Long ctg, Integer bMonth);

    @Query("SELECT new org.alunosufg.personalfinancespring.dto.budgets.BudgetDTO(b.category.category, b.budgetLimit, b.month) " +
            "FROM budget b WHERE b.account = :acc AND b.month = :bMonth")
    List<BudgetDTO> getBudgets(AccountEntity acc, Integer bMonth);

    @Modifying
    @Transactional
    @Query("UPDATE budget b SET b.budgetLimit = :value WHERE b.account = :acc AND b.category.id = :ctgId AND b.month = :month")
    void updateBudget(AccountEntity acc, Long ctgId, Integer month, Integer value);

    @Query("SELECT new org.alunosufg.personalfinancespring.dto.budgets.BudgetLimitDTO(" +
            "b.category.category, b.budgetLimit, SUM(t.value)) " +
            "FROM budget b " +
            "LEFT JOIN transaction t ON b.account = t.account " +
            "    AND b.category = t.category AND t.value < 0" +
            "    AND t.transactionTime BETWEEN :firstDay AND :lastDay " +
            "WHERE b.account = :acc " +
            "GROUP BY b.category.category, b.budgetLimit " +
            "ORDER BY b.budgetLimit")
    List<BudgetLimitDTO> getBudgetsLimitsAndSpends(AccountEntity acc, Date firstDay, Date lastDay);


}
