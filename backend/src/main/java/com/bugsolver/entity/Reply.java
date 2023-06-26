package com.bugsolver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.ZonedDateTime;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="Reply")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "reply.description.not-blank")
    private String description;

    @JsonProperty(access = READ_ONLY)
    @Column(name="created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @JsonProperty(access = READ_ONLY)
    @Column(name="best_answer")
    private Boolean bestAnswer = false;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonProperty(access = READ_ONLY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bug_id")
    @JsonIgnore
    private Bug bug;
}
