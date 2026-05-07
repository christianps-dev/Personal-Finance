package org.alunosufg.personalfinancespring.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.alunosufg.personalfinancespring.dto.transactions.TransactionDTO;
import org.alunosufg.personalfinancespring.dto.transactions.UserTransactionDTO;
import org.alunosufg.personalfinancespring.entities.TransactionEntity;
import org.alunosufg.personalfinancespring.repository.AccountRepository;
import org.alunosufg.personalfinancespring.repository.TransactionRepository;
import org.alunosufg.personalfinancespring.repository.UserAuthRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class TransactionServices {

    private final TransactionRepository transactionRepository;
    private final UserAuthRepository userAuthRepository;
    private final AccountRepository accountRepository;

    public TransactionServices(TransactionRepository transactionRepository, UserAuthRepository userAuthRepository,
                               AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.userAuthRepository = userAuthRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public String saveTransaction(@Valid @NotNull UserTransactionDTO dto) {
        var account = accountRepository.getAccount(dto.email())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        System.out.println("--- Saving new transactions");
        TransactionEntity entity = new TransactionEntity();
        entity.setValue(dto.value());
        entity.setAccount(account);
        entity.setCategory(dto.category());
        entity.setDescription(dto.description());

        entity.setTransactionTime(java.sql.Date.from(Instant.now()));

        transactionRepository.save(entity);
        return "Transaction saved successfully";
    }

    public List<TransactionDTO> getAllTransactions(@Valid @NotNull String user) {
        System.out.println("--- Getting all transactions");
        Long userId = userAuthRepository.findIdByEmail(user);
        if (userId == null) {
            throw new RuntimeException("User not found");
        }
        return transactionRepository.getAllByUserId(userId);
    }

    public List<TransactionDTO> getLastQtdTransactions(@Valid @NotNull String email, @Valid Integer qtd){
        System.out.printf("--- Getting last %d transactions\n", qtd);
        return transactionRepository.getLastQtdByUserId(userAuthRepository.findIdByEmail(email), qtd);

    }

    public List<TransactionDTO> getALlTransactionsByMonth(String email, Integer month){
        System.out.printf("--- Getting all transactions in the month %d \n", month);
        String monthQuery = "2026-" + month + "-";
        Date lastDay = java.sql.Date.valueOf(monthQuery + "31");
        Date firstDay = java.sql.Date.valueOf(monthQuery + "1");

        return transactionRepository.getAllTrasactionsByMonth(firstDay, lastDay,
                userAuthRepository.findIdByEmail(email));
    }


}
