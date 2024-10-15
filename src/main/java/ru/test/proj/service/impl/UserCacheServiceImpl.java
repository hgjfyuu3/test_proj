package ru.test.proj.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.test.proj.model.UsersEntity;
import ru.test.proj.repository.UsersEntityRepository;
import ru.test.proj.service.UserCacheService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCacheServiceImpl implements UserCacheService {

    private final UsersEntityRepository usersEntityRepository;

    @Override
    @Cacheable(value = "users", key = "#id")
    public UsersEntity getUserById(Long id) {
        log.info("Fetching user from database with ID: {}", id);
        return usersEntityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

}