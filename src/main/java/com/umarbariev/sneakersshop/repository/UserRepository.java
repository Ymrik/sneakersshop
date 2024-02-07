package com.umarbariev.sneakersshop.repository;

import com.umarbariev.sneakersshop.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
