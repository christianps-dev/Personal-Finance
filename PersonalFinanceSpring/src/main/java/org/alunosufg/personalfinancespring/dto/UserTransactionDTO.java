package org.alunosufg.personalfinancespring.dto;

public record UserTransactionDTO(String email, Integer value, String category, String description) {
}
