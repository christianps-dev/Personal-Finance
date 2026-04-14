package org.alunosufg.personalfinancespring.repository;

import lombok.NonNull;
import org.alunosufg.personalfinancespring.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserAuthRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    boolean existsUserEntityByEmail(@NonNull String email);

    boolean existsUserEntityByUsername(@NonNull String username);

    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT u.id FROM user u WHERE u.email = :email")
    Long findIdByEmail(String email);
}
