package org.alunosufg.personalfinancespring.services;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.alunosufg.personalfinancespring.entities.AccountEntity;
import org.alunosufg.personalfinancespring.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountsServices {

    private final AccountRepository accountRepository;

    private final UserAuthService userAuthService;

    public AccountsServices(AccountRepository accountRepository, UserAuthService userAuthService){
        this.accountRepository = accountRepository;
        this.userAuthService = userAuthService;
    }

    public AccountEntity getAccount(@Valid @NotNull String email){
        System.out.println("Getting account: " + email);
        return accountRepository.getAccount(userAuthService.getUserId(email))
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public void updateBalance(@Valid @NotNull String email, Integer value){
        System.out.println("Updating account balance");
        var account = getAccount(email);
        account.setAccountBalance(account.getAccountBalance() + value);
        accountRepository.save(account);
    }
}
