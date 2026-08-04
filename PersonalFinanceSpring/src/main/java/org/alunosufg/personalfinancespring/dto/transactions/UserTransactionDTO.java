package org.alunosufg.personalfinancespring.dto.transactions;

import java.sql.Date;

public record UserTransactionDTO(Integer value, String category, String description, Date date) {
}
