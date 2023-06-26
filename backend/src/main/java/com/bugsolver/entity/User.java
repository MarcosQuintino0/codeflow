package com.bugsolver.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "user.username.not-blank")
    private String username;

    @Email(message = "user.email.invalid")
    @NotBlank(message = "user.email.not-blank")
    private String email;

    @JsonProperty(access = WRITE_ONLY)
    @NotBlank(message = "user.password.not-blank")
    @Size(min = 8, message = "user.password.too-short")
    private String password;
}
