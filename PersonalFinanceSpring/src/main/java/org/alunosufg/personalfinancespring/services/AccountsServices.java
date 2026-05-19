package org.alunosufg.personalfinancespring.services;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.alunosufg.personalfinancespring.dto.transactions.UserTransactionDTO;
import org.alunosufg.personalfinancespring.entities.AccountEntity;
import org.alunosufg.personalfinancespring.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountsServices {

    private final AccountRepository accountRepository;

    public AccountsServices(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public AccountEntity getAccount(@Valid @NotNull String email){
        System.out.println("Getting account");
        return accountRepository.getAccount(email)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public void updateBalance(@Valid @NotNull UserTransactionDTO dto){
        System.out.println("Updating account balance");
        var account = getAccount(dto.email());
        account.setAccountBalance(account.getAccountBalance() + dto.value());
        accountRepository.save(account);
    }
}
