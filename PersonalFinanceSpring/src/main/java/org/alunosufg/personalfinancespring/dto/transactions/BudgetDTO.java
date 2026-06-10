package org.alunosufg.personalfinancespring.dto.transactions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BudgetDTO {
    private String category;
    private Integer budgetLimit;
    private Integer month;
}
