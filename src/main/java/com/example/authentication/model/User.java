package com.example.authentication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Role> roles;

    public User(String username, String email, String password, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static User build(User user) {
        return new User(
                user.id,
                user.username,
                user.email,
                user.password,
                user.roles
        );
    }
}
