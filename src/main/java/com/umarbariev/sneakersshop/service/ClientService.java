package com.umarbariev.sneakersshop.service;

import com.umarbariev.sneakersshop.mapper.ClientMapper;
import com.umarbariev.sneakersshop.model.dto.ClientDto;
import com.umarbariev.sneakersshop.model.dto.ClientFavouritesDto;
import com.umarbariev.sneakersshop.model.dto.dictionary.DShoeModelDto;
import com.umarbariev.sneakersshop.model.entity.ClientEntity;
import com.umarbariev.sneakersshop.model.entity.ClientFavourite;
import com.umarbariev.sneakersshop.model.entity.UserEntity;
import com.umarbariev.sneakersshop.model.entity.dictionary.SexEntity;
import com.umarbariev.sneakersshop.repository.ClientFavouriteRepository;
import com.umarbariev.sneakersshop.repository.ClientRepository;
import com.umarbariev.sneakersshop.repository.UserRepository;
import com.umarbariev.sneakersshop.repository.dictionary.DSexRepository;
import com.umarbariev.sneakersshop.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final UserDetailsServiceImpl userDetailsService;
    private final DSexRepository sexRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;
    private final ShoeModelService shoeModelService;
    private final ClientFavouriteRepository clientFavouriteRepository;

    @Transactional
    public void saveNewClient(ClientDto clientDto) {
        ClientEntity entity = fillCommonFromDto(clientDto);
        UserEntity userEntity = userDetailsService.saveNewClientUser(clientDto.getUsername(), clientDto.getPassword());
        entity.setUser(userEntity);
        clientRepository.save(entity);
    }

    @Transactional
    public void updateClient(ClientDto clientDto) {
        String username = clientDto.getUsername();
        ClientEntity clientEntity = getClientEntity(username);
        fillCommonFields(clientEntity, clientDto);
        clientRepository.save(clientEntity);
    }

    public ClientDto getClient(String username) {
        ClientEntity client = getClientEntity(username);

        return clientMapper.fromEntity(client);
    }

    public List<ClientDto> getAllClients() {
        return clientRepository.findAll().stream().map(clientMapper::fromEntity).collect(Collectors.toList());
    }

    public ClientEntity getClientEntity(String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException(String.format("Не найден пользователь %s", username));
        }

        return clientRepository.findByUser(user).orElseThrow(() -> new UsernameNotFoundException(String.format("Не найден пользователь %s", username)));
    }


    private ClientEntity fillCommonFromDto(ClientDto clientDto) {
        ClientEntity clientEntity = new ClientEntity();
        fillCommonFields(clientEntity, clientDto);
        return clientEntity;
    }

    private void fillCommonFields(ClientEntity entity, ClientDto clientDto) {
        entity.setName(clientDto.getName());
        entity.setLastName(clientDto.getLastName());

        Optional<SexEntity> sexOpt = sexRepository.findByCode(clientDto.getSex());
        if (sexOpt.isEmpty()) {
            throw new IllegalArgumentException(String.format("Неверно указан пол: %s", clientDto.getSex()));
        }

        entity.setSex(sexOpt.get());
        entity.setBirthdate(new Date(clientDto.getBirthdate().getTime()));
        entity.setPhoneNumber(clientDto.getPhoneNumber());
        entity.setEmail(clientDto.getEmail());
        entity.setPreferredDeliveryAddress(clientDto.getDeliveryAddress());
    }

    public void addShoeToFavourite(String username, Long shoeId) {
        if (clientFavouriteRepository.existsByClientEntityUserUsernameAndShoeModelEntityId(username, shoeId)) {
            return;
        }
        ClientFavourite clientFavourite = new ClientFavourite();
        clientFavourite.setClientEntity(getClientEntity(username));
        clientFavourite.setShoeModelEntity(shoeModelService.getShoeModelEntity(shoeId));
        clientFavouriteRepository.save(clientFavourite);
    }

    @Transactional
    public void deleteShoeFromFavourites(String username, Long shoeId) {
        clientFavouriteRepository.deleteByClientEntityUserUsernameAndShoeModelEntityId(username, shoeId);
    }

    public ClientFavouritesDto getClientFavourites(String username) {
        List<ClientFavourite> clientFavourites = clientFavouriteRepository.findAllByClientEntityUserUsername(username);
        ClientFavouritesDto clientFavouritesDto = new ClientFavouritesDto(username, new ArrayList<>());

        clientFavourites.forEach(clientFavourite -> {
            DShoeModelDto shoeModelDto = shoeModelService.getShoeById(clientFavourite.getShoeModelEntity().getId());
            clientFavouritesDto.getShoeModels().add(shoeModelDto);
        });

        return clientFavouritesDto;
    }
}
