package ru.test.proj.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.test.proj.dto.UserCreateDto;
import ru.test.proj.dto.UserSearchDto;
import ru.test.proj.dto.UserUpdateDto;
import ru.test.proj.dto.UsersDto;

import java.security.Principal;

public interface UserService {

    UsersDto getUsersById(Long id);

    UsersDto getUsersById(Principal principal);

    Page<UsersDto> searchUsers(UserSearchDto userSearchDto, Pageable pageable);

    UsersDto updateUser(Principal principal, UserUpdateDto userUpdateDto);

    void addPhoneOrEmail(Principal principal, UserCreateDto userCreateDto);

    void deletePhoneOrEmail(Principal principal, UserCreateDto userCreateDto);

}