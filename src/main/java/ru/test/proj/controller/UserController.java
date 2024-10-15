package ru.test.proj.controller;

import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.test.proj.dto.UserCreateDto;
import ru.test.proj.dto.UserSearchDto;
import ru.test.proj.dto.UserUpdateDto;
import ru.test.proj.dto.UsersDto;
import ru.test.proj.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<UsersDto> getUserById(Principal principal) {
        return ResponseEntity.ok(userService.getUsersById(principal));
    }

    @PostMapping("/search")
    public ResponseEntity<Page<UsersDto>> searchUsers(
            @Validated @RequestBody UserSearchDto userSearchDto,
            @ParameterObject Pageable pageable,
            BindingResult ignored) {
        return ResponseEntity.ok(userService.searchUsers(userSearchDto, pageable));
    }

    @PutMapping()
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<UsersDto> updateUser(Principal principal, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        return ResponseEntity.ok(userService.updateUser(principal, userUpdateDto));
    }

    @PostMapping()
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<Void> addPhoneOrEmail(Principal principal, @Valid @RequestBody UserCreateDto userCreateDto) {
        userService.addPhoneOrEmail(principal, userCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping()
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<Void> deletePhone(Principal principal, @Valid @RequestBody UserCreateDto userCreateDto) {
        userService.deletePhoneOrEmail(principal, userCreateDto);
        return ResponseEntity.ok().build();
    }

}