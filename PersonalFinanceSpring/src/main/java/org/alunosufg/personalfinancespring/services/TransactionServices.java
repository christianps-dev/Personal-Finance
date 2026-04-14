package org.alunosufg.personalfinancespring.services;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.alunosufg.personalfinancespring.dto.UserGenericDTO;
import org.alunosufg.personalfinancespring.dto.UserTransactionDTO;
import org.alunosufg.personalfinancespring.entities.Transaction;
import org.alunosufg.personalfinancespring.repository.TransactionRepository;
import org.alunosufg.personalfinancespring.repository.UserAuthRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TransactionServices {

    private final TransactionRepository transactionRepository;
    private final UserAuthRepository userAuthRepository;

    public TransactionServices(TransactionRepository transactionRepository,
                               UserAuthRepository userAuthRepository) {
        this.transactionRepository = transactionRepository;
        this.userAuthRepository = userAuthRepository;
    }

    public String saveTransaction(@Valid @NotNull UserTransactionDTO transactionDTO, String creditOrDebit){
        Transaction transaction = new Transaction();

        if (creditOrDebit.equalsIgnoreCase("Debit"))
            transaction.setValue(negativeNumber(transactionDTO.value()));

        transaction.setValue(transactionDTO.value());
        transaction.setUserId(userAuthRepository.findIdByEmail(transactionDTO.email()));
        transaction.setCategory(transactionDTO.category());
        transaction.setDescription(transactionDTO.description());
        transaction.setTransactionTime(getTime());
        transactionRepository.save(transaction);
        return "Ok";
    }

    public List<Transaction> getTransactions(@NonNull UserGenericDTO user){
        return transactionRepository.getAllByUserId(userAuthRepository.findIdByEmail(user.email()));

    }

    private Integer negativeNumber(Integer num){
        return num * -1;
    }

    private String getTime(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        return localDateTime.format(dateFormatter);

    }

}
