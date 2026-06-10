package org.alunosufg.personalfinancespring.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "budget")
@Table(name = "budgets")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BudgetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @NonNull
    private Integer budgetLimit;

    @NonNull
    private Integer month;

}
