package ru.test.proj.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.test.proj.config.JwtTokenProvider;
import ru.test.proj.dto.LoginDto;
import ru.test.proj.model.UsersEntity;
import ru.test.proj.repository.UsersEntityRepository;
import ru.test.proj.service.LoginService;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UsersEntityRepository userRepository;

    @Override
    public String login(LoginDto loginDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            UsersEntity user = userRepository.findByEmailOrPhone(loginDto.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            String token = jwtTokenProvider.generateToken(user.getId());

            log.info("User {} has logged in", loginDto.getUsername());
            return token;
        } catch (AuthenticationException e) {
            log.error("User {} has not logged in", loginDto.getUsername(), e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}