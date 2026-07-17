package org.alunosufg.personalfinancespring.dto.budgets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BudgetsValueCtgDTO {

    private String category;
    private Long value;
}
