package com.umarbariev.sneakersshop.repository;

import com.umarbariev.sneakersshop.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);

    @Modifying
    @Query(value = "UPDATE sneakers_shop.users SET is_active=?2 WHERE username=?1", nativeQuery = true)
    void updateUserStatus(String username, boolean isActive);
}
