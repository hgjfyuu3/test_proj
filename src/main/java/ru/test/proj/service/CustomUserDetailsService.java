package ru.test.proj.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.test.proj.model.UsersEntity;
import ru.test.proj.repository.UsersEntityRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersEntityRepository usersEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Optional<UsersEntity> user;
        if (id.matches("\\d+")) {
            Long userId = Long.valueOf(id);
            user = usersEntityRepository.findById(userId);
        } else {
            user = usersEntityRepository.findByEmailOrPhone(id);
        }
        return user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

}