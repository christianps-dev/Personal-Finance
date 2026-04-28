package org.alunosufg.personalfinancespring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.util.Date;

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
    @Length(min = 8, message = "Password must be 8-16 characters")
    private String password;

    @NotBlank
    @NonNull
    @Column(unique = true)
    @Email(message = "E-mail must be valid")
    private String email;

    @NonNull
    private Date created;

}
