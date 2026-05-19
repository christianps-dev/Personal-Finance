package org.alunosufg.personalfinancespring.dto.transactions;

import java.util.Date;

public record TransactionFullDTO(Integer value, Date date, String category, String description) {
}
