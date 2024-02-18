package com.umarbariev.sneakersshop.repository;

import com.umarbariev.sneakersshop.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getRoleByName(String name);
}
