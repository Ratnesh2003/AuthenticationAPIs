package com.example.authentication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToMany
    private Set<User> users;
    public Role(String name) {
        this.name = name;
    }
}
