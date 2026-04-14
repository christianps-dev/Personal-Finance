package org.alunosufg.personalfinancespring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "transaction")
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @JoinColumn( name = "user.id", nullable = false)
    private Long userId;

    @NonNull
    private Integer value;

    @NonNull
    @NotBlank
    private String transactionTime;

    @NonNull
    @NotBlank
    private String category;

    private String description;

}


