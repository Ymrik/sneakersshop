package com.umarbariev.sneakersshop.security;

import com.umarbariev.sneakersshop.mapper.UserMapper;
import com.umarbariev.sneakersshop.model.dto.UpdateUserPasswordDto;
import com.umarbariev.sneakersshop.model.dto.UserDto;
import com.umarbariev.sneakersshop.model.entity.Role;
import com.umarbariev.sneakersshop.model.entity.UserEntity;
import com.umarbariev.sneakersshop.repository.RoleRepository;
import com.umarbariev.sneakersshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserEntity saveNewClientUser(String username, String password) {
        return saveNewUser(new UserDto(username, password, List.of(new SimpleGrantedAuthority("CLIENT"))));
    }

    public UserEntity saveNewUser(UserDto userDto) {
        UserEntity user = new UserEntity();

        String username = userDto.getUsername();
        UserEntity userEntity = userRepository.findByUsername(username);

        if(userEntity != null) {
            throw new IllegalArgumentException(String.format("Пользователь с именем %s уже существует",
                                                             username));
        }

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        List<Role> roles = userDto.getAuthorities().stream()
                               .map(grantedAuthority -> {
                                   Role role = roleRepository.getRoleByName(grantedAuthority.getAuthority());
                                   if (role == null) {
                                       throw new IllegalArgumentException(String.format("Не существующая роль: %s",
                                                                                        grantedAuthority.getAuthority()));
                                   }

                                   return role;
                               }).toList();

        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("Not found user with username " + username);
        }

        return userMapper.fromEntity(user);
    }

    public UserEntity loadUserEntityByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("Not found user with username " + username);
        }

        return user;
    }

    public void updateUser(UpdateUserPasswordDto updateUserPasswordDto, Principal principal) {
        String oldPassword = updateUserPasswordDto.getOldPassword();
        String newPassword1 = updateUserPasswordDto.getNewPassword();
        String newPassword2 = updateUserPasswordDto.getNewPassword2();
        UserEntity user = loadUserEntityByUsername(principal.getName());
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Неверный старый пароль");
        }
        if (!newPassword1.equals(newPassword2)) {
            throw new RuntimeException("Новые пароли не совпадают");
        }
        user.setPassword(passwordEncoder.encode(newPassword1));
        updateUser(user);
    }

    public void updateUser(UserEntity user) {
        userRepository.save(user);
    }
}
