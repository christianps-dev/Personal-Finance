package org.alunosufg.personalfinancespring.dto;

import java.util.Date;

public record TransactionDTO(Integer value, Date date, String category)  {
}
