package com.umarbariev.sneakersshop.model.entity;

import com.umarbariev.sneakersshop.model.entity.dictionary.ShoeModelEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "client_favourites")
public class ClientFavourite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private ClientEntity clientEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shoe_model_id", referencedColumnName = "id")
    private ShoeModelEntity shoeModelEntity;
}
