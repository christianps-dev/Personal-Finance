package org.alunosufg.personalfinancespring.repository;

import org.alunosufg.personalfinancespring.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> getAllByUserId(Long userId);
}
