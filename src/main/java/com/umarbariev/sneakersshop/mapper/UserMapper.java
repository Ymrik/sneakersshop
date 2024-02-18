package com.umarbariev.sneakersshop.mapper;

import com.umarbariev.sneakersshop.model.dto.UserDto;
import com.umarbariev.sneakersshop.model.entity.Role;
import com.umarbariev.sneakersshop.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "roles", target = "authorities", qualifiedByName = "mapRole")
    UserDto fromEntity(UserEntity user);

    @Named("mapRole")
    default List<SimpleGrantedAuthority> mapRole(List<Role> roles) {
        return roles.stream()
                   .map(role -> new SimpleGrantedAuthority(role.getName()))
                   .collect(Collectors.toList());
    }
}
