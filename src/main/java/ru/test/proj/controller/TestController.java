package ru.test.proj.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.test.proj.dto.UsersDto;
import ru.test.proj.service.UserService;

@RestController
@RequestMapping("/test")
@Profile("test")
@RequiredArgsConstructor
public class TestController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UsersDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUsersById(id));
    }

}