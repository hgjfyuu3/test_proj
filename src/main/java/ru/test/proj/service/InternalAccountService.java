package ru.test.proj.service;

import ru.test.proj.model.AccountEntity;

import java.math.BigDecimal;

public interface InternalAccountService {

    void performTransfer(AccountEntity fromUser, AccountEntity toUser, BigDecimal amount);

}