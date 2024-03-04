package com.umarbariev.sneakersshop.repository;

import com.umarbariev.sneakersshop.model.entity.ClientFavourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientFavouriteRepository extends JpaRepository<ClientFavourite, Long> {
    @Modifying
    void deleteByClientEntityUserUsernameAndShoeModelEntityId(String username, Long shoeId);

    Boolean existsByClientEntityUserUsernameAndShoeModelEntityId(String username, Long shoeId);

    List<ClientFavourite> findAllByClientEntityUserUsername(String username);
}
