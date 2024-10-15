package ru.test.proj.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.test.proj.exception.AccountTransferException;
import ru.test.proj.model.AccountEntity;
import ru.test.proj.repository.AccountEntityRepository;
import ru.test.proj.service.AccountService;
import ru.test.proj.service.InternalAccountService;

import java.math.BigDecimal;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService, InternalAccountService {

    private final AccountEntityRepository accountEntityRepository;

    private final InternalAccountService internalService;

    private static final BigDecimal MAX_TRANSFER_AMOUNT = new BigDecimal("1000000");

    public AccountServiceImpl(AccountEntityRepository accountEntityRepository,
                              @Lazy InternalAccountService internalService) {
        this.accountEntityRepository = accountEntityRepository;
        this.internalService = internalService;
    }

    @Override
    @Transactional
    public void transferMoney(Long fromUserId, Long toUserId, BigDecimal amount) {
        try {
            validateTransferAmount(amount);

            if (fromUserId.equals(toUserId)) {
                throw new IllegalArgumentException("Cannot transfer money to yourself");
            }

            AccountEntity fromUser = getAccountByUserId(fromUserId);
            AccountEntity toUser = getAccountByUserId(toUserId);

            internalService.performTransfer(fromUser, toUser, amount);

            accountEntityRepository.save(fromUser);
            accountEntityRepository.save(toUser);
        } catch (Exception e) {
            log.error("Transfer failed: from {} to {}, amount {}", fromUserId, toUserId, amount, e);
            throw new AccountTransferException("Transfer could not be completed. Please try again later.", e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void performTransfer(AccountEntity fromUser, AccountEntity toUser, BigDecimal amount) {
        if (fromUser.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        fromUser.withdraw(amount);
        toUser.deposit(amount);
    }

    private void validateTransferAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 || amount.compareTo(MAX_TRANSFER_AMOUNT) > 0) {
            throw new IllegalArgumentException("Invalid transfer amount");
        }
    }

    private AccountEntity getAccountByUserId(Long userId) {
        return accountEntityRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Scheduled(fixedRate = 30_000, initialDelay = 30_000)
    public void increaseBalance() {
        Pageable pageable = PageRequest.of(0, 100);
        long count = accountEntityRepository.count();

        while (count > 0) {
            Page<AccountEntity> accounts = accountEntityRepository.findAll(pageable);
            for (AccountEntity account : accounts) {
                BigDecimal initialBalance = account.getInitialBalance();
                BigDecimal currentBalance = account.getBalance();
                BigDecimal maxBalance = initialBalance.multiply(BigDecimal.valueOf(2.07));
                BigDecimal newBalance = currentBalance.multiply(BigDecimal.valueOf(1.1));

                BigDecimal diff = newBalance.subtract(currentBalance);

                if (newBalance.compareTo(maxBalance) <= 0) {
                    account.deposit(diff);
                    accountEntityRepository.saveAndFlush(account);
                }
            }
            count = count - pageable.getPageSize();
            pageable = PageRequest.of(pageable.getPageNumber() + 1, pageable.getPageSize());
        }
    }

}