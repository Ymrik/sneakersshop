package com.umarbariev.sneakersshop.repository;

import com.umarbariev.sneakersshop.model.entity.ClientEntity;
import com.umarbariev.sneakersshop.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    Optional<ClientEntity> findByUser(UserEntity user);
}
