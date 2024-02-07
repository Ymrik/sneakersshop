package com.umarbariev.sneakersshop.security;

import com.umarbariev.sneakersshop.mapper.UserMapper;
import com.umarbariev.sneakersshop.model.entity.UserEntity;
import com.umarbariev.sneakersshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("Not found user with username " + username);
        }

        return userMapper.fromEntity(user);
    }
}
