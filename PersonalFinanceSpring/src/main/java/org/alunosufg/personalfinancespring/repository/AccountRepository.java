package org.alunosufg.personalfinancespring.repository;

import org.alunosufg.personalfinancespring.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {


    @Query("SELECT a FROM account a JOIN user u ON u.email = :email")
    Optional<AccountEntity> getAccount(String email);
}
