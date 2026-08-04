package org.alunosufg.personalfinancespring.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.alunosufg.personalfinancespring.dto.transactions.TransactionDTO;
import org.alunosufg.personalfinancespring.dto.transactions.TransactionFullDTO;
import org.alunosufg.personalfinancespring.dto.transactions.UserTransactionDTO;
import org.alunosufg.personalfinancespring.entities.TransactionEntity;
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
    private final CategoriesService categoriesService;
    private final AccountsServices accountsServices;


    public TransactionServices(TransactionRepository transactionRepository, UserAuthRepository userAuthRepository,
                               CategoriesService categoriesService, AccountsServices accountsServices) {
        this.transactionRepository = transactionRepository;
        this.userAuthRepository = userAuthRepository;
        this.categoriesService = categoriesService;
        this.accountsServices = accountsServices;
    }

    @Transactional
    public Boolean saveTransaction(@Valid @NotNull UserTransactionDTO dto, String email) {
        var account = accountsServices.getAccount(email);

        System.out.println("--- Saving new transactions");
        TransactionEntity entity = new TransactionEntity();
        entity.setValue(dto.value());
        entity.setAccount(account);
        entity.setCategory(categoriesService.getCategory(dto.category()));
        entity.setDescription(dto.description());

        accountsServices.updateBalance(email, dto.value());

        if ( dto.date() != null) {
            System.out.println("Date null: " + dto.date());
            entity.setTransactionTime(dto.date());
        }
        else
            entity.setTransactionTime(java.sql.Date.from(Instant.now()));

        transactionRepository.save(entity);
        return true;

    }

    public List<TransactionDTO> getAllTransactions(@Valid @NotNull String email) {
        System.out.println("--- Getting all transactions");
        Long userId = userAuthRepository.findIdByEmail(email);
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

        return transactionRepository.getAllTransactionsByMonth(firstDay, lastDay,
                userAuthRepository.findIdByEmail(email));
    }

    public List<TransactionFullDTO> getFullTransactionsByMonth(String email, Integer month){
        System.out.printf("--- Getting all full transactions in the month %d \n", month);
        String monthQuery = "2026-" + month + "-";
        Date lastDay = java.sql.Date.valueOf(monthQuery + "31");
        Date firstDay = java.sql.Date.valueOf(monthQuery + "1");

        return transactionRepository.getAllFullTransactionsByMonth(firstDay, lastDay,
                userAuthRepository.findIdByEmail(email));
    }

}
