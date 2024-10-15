package ru.test.proj.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.test.proj.dto.AccountTransferDto;
import ru.test.proj.service.AccountService;
import ru.test.proj.util.PrincipalUtil;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/transfer")
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<String> transferMoney(Principal principal, @Valid @RequestBody AccountTransferDto accountTransferDto) {
        accountService.transferMoney(PrincipalUtil.getIdFromPrincipal(principal),
                accountTransferDto.getRecipientId(),
                accountTransferDto.getAmount());
        return ResponseEntity.ok("Transfer successful");
    }

}