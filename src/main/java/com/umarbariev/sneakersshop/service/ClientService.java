package com.umarbariev.sneakersshop.service;

import com.umarbariev.sneakersshop.mapper.ClientMapper;
import com.umarbariev.sneakersshop.model.dto.ClientDto;
import com.umarbariev.sneakersshop.model.entity.ClientEntity;
import com.umarbariev.sneakersshop.model.entity.UserEntity;
import com.umarbariev.sneakersshop.model.entity.dictionary.SexEntity;
import com.umarbariev.sneakersshop.repository.ClientRepository;
import com.umarbariev.sneakersshop.repository.UserRepository;
import com.umarbariev.sneakersshop.repository.dictionary.DSexRepository;
import com.umarbariev.sneakersshop.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final UserDetailsServiceImpl userDetailsService;
    private final DSexRepository sexRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;

    @Transactional
    public void saveNewClient(ClientDto clientDto) {
        ClientEntity entity = fillCommonFromDto(clientDto);
        UserEntity userEntity = userDetailsService.saveNewClientUser(clientDto.getUsername(), clientDto.getPassword());
        entity.setUser(userEntity);
        clientRepository.save(entity);
    }

    public ClientDto getClient(String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException(String.format("Не найден пользователь %s", username));
        }
        ClientEntity client = clientRepository.findByUser(user).orElseThrow(() -> new UsernameNotFoundException(String.format("Не найден пользователь %s", username)));

        return clientMapper.fromEntity(client);
    }


    private ClientEntity fillCommonFromDto(ClientDto clientDto) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setName(clientDto.getName());
        clientEntity.setLastName(clientDto.getLastName());

        Optional<SexEntity> sexOpt = sexRepository.findByCode(clientDto.getSex());
        if (sexOpt.isEmpty()) {
            throw new IllegalArgumentException(String.format("Неверно указан пол: %s", clientDto.getSex()));
        }

        clientEntity.setSex(sexOpt.get());
        clientEntity.setBirthdate(new Date(clientDto.getBirthdate().getTime()));
        clientEntity.setPhoneNumber(clientDto.getPhoneNumber());
        clientEntity.setEmail(clientDto.getEmail());
        clientEntity.setPreferredDeliveryAddress(clientDto.getDeliveryAddress());
        return clientEntity;
    }
}
