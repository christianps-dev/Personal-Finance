package org.alunosufg.personalfinancespring.repository;

import org.alunosufg.personalfinancespring.dto.transactions.TransactionDTO;
import org.alunosufg.personalfinancespring.dto.transactions.TransactionFullDTO;
import org.alunosufg.personalfinancespring.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("SELECT t.value AS value, t.transactionTime AS date, t.category.category AS category FROM transaction t" +
            " WHERE t.account.user.id =:userId ORDER BY t.transactionTime ASC")
    List<TransactionDTO> getAllByUserId(Long userId);

    @Query("SELECT t.value AS value, t.transactionTime AS date, t.category.category AS category FROM transaction t" +
            " WHERE t.account.user.id =:userId ORDER BY t.transactionId DESC LIMIT :qtd")
    List<TransactionDTO> getLastQtdByUserId(Long userId, Integer qtd);

    @Query("SELECT t.value AS value, t.transactionTime AS date, t.category.category AS category FROM transaction t" +
            " WHERE t.account.user.id =:userId AND t.transactionTime BETWEEN :firstDay AND :lastDay ")
    List<TransactionDTO> getAllTransactionsByMonth(Date firstDay, Date lastDay, Long userId);

    @Query("SELECT t.value AS value, t.transactionTime AS date, t.category.category AS category, t.description AS description " +
            "FROM transaction t WHERE t.account.user.id =:userId AND t.transactionTime BETWEEN :firstDay AND :lastDay ")
    List<TransactionFullDTO> getAllFullTransactionsByMonth(Date firstDay, Date lastDay, Long userId);
}
