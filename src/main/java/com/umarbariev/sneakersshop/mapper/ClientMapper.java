package com.umarbariev.sneakersshop.mapper;

import com.umarbariev.sneakersshop.model.dto.ClientDto;
import com.umarbariev.sneakersshop.model.entity.ClientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Date;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "password", source = "user.password")
    @Mapping(target = "sex", source = "sex.name")
    @Mapping(target = "deliveryAddress", source = "preferredDeliveryAddress")
    @Mapping(target = "isActive", source = "user.isActive")
    ClientDto fromEntity(ClientEntity client);

    default Date fromSqlDate(java.sql.Date sqlDate) {
        return new Date(sqlDate.getTime());
    }
}
