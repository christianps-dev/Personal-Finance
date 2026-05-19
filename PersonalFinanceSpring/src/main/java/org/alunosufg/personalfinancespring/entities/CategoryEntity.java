package org.alunosufg.personalfinancespring.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "category")
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String category;

    @NonNull
    private String type;



}
