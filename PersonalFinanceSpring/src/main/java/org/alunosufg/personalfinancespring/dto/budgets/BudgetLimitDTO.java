package org.alunosufg.personalfinancespring.dto.budgets;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BudgetLimitDTO {
    private String category;
    private Integer budgetLimit;
    private Long currentSpend;
}
