package org.alunosufg.personalfinancespring.repository;

import org.alunosufg.personalfinancespring.dto.TransactionDTO;
import org.alunosufg.personalfinancespring.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("SELECT t.value AS value, t.transactionTime AS date, t.category AS category FROM transaction t WHERE t.account.user.id =:userId ORDER BY t.transactionTime ASC")
    List<TransactionDTO> getAllByUserId(Long userId);

    @Query("SELECT t.value AS value, t.transactionTime AS date, t.category AS category FROM transaction t WHERE t.account.user.id =:userId ORDER BY t.transactionTime DESC LIMIT :qtd")
    List<TransactionDTO> getLastQtdByUserId(Long userId, Integer qtd);



}
