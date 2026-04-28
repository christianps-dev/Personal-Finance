package org.alunosufg.personalfinancespring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "transaction")
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @NonNull
    private Integer value;

    @NonNull
    private Date transactionTime;

    @NonNull
    @NotBlank
    private String category;

    private String description;

}


