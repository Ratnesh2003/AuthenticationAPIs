package com.example.authentication.repository;

import com.example.authentication.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByNameIgnoreCase(String name);
    Boolean existsRoleByNameIgnoreCase(String name);
}
