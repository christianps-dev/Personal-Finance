package org.alunosufg.personalfinancespring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Entity(name = "user")
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank(message = "Username is required")
    @Column(unique = true)
    private String username;

    @NotBlank
    @NonNull
    private String password;

    @NotBlank
    @NonNull
    @Column(unique = true)
    @Email(message = "E-mail must be valid")
    private String email;
}
