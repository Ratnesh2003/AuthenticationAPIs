package com.example.authentication.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@NoArgsConstructor
@Getter @Setter
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String token;
    private Date dateCreated;

    public Token(String email, String token, Date dateCreated) {
        this.email = email;
        this.token = token;
        this.dateCreated = dateCreated;
    }
}
