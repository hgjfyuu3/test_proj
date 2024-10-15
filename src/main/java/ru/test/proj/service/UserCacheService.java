package ru.test.proj.service;

import ru.test.proj.model.UsersEntity;

public interface UserCacheService {

    UsersEntity getUserById(Long id);

}