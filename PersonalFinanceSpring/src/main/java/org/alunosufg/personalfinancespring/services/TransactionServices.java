package org.alunosufg.personalfinancespring.services;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.alunosufg.personalfinancespring.dto.TransactionDTO;
import org.alunosufg.personalfinancespring.dto.UserGenericDTO;
import org.alunosufg.personalfinancespring.dto.UserTransactionDTO;
import org.alunosufg.personalfinancespring.entities.TransactionEntity;
import org.alunosufg.personalfinancespring.repository.AccountRepository;
import org.alunosufg.personalfinancespring.repository.TransactionRepository;
import org.alunosufg.personalfinancespring.repository.UserAuthRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
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

    public String saveTransaction(@Valid @NotNull UserTransactionDTO transactionDTO){
        TransactionEntity transactionEntity = new TransactionEntity();

        transactionEntity.setValue(transactionDTO.value());
        transactionEntity.setAccount(accountRepository.getAccount(transactionDTO.email()).orElse(null));
        transactionEntity.setCategory(transactionDTO.category());
        transactionEntity.setDescription(transactionDTO.description());
        transactionEntity.setTransactionTime(Date.from(Instant.now()));

        transactionRepository.save(transactionEntity);
        return "Ok";
    }

    public List<TransactionDTO> getAllTransactions(@Valid @NotNull UserGenericDTO user){
        return transactionRepository.getAllByUserId(userAuthRepository.findIdByEmail(user.email()));

    }

    public List<TransactionDTO> getLastQtdTransactions(@Valid @NotNull UserGenericDTO user, @Valid Integer qtd){
        return transactionRepository.getLastQtdByUserId(userAuthRepository.findIdByEmail(user.email()), qtd);

    }

}
