package ru.test.proj.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.test.proj.exception.AccountTransferException;
import ru.test.proj.model.AccountEntity;
import ru.test.proj.repository.AccountEntityRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "spring.liquibase.enabled=false"
})
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private InternalAccountService internalAccountService;

    @Autowired
    private AccountEntityRepository accountEntityRepository;

    @Test
    @Sql(scripts = {"classpath:sql/insert_account_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:sql/truncate_account_test_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void transferMoneySuccessful() {
        Long fromUserId = 9999999998L;
        Long toUserId = 9999999999L;
        BigDecimal amount = new BigDecimal("70.00");

        accountService.transferMoney(fromUserId, toUserId, amount);

        AccountEntity accountFrom = accountEntityRepository.findByUserId(fromUserId).get();
        AccountEntity accountTo = accountEntityRepository.findByUserId(toUserId).get();

        assertEquals(0, new BigDecimal("2000.00").compareTo(accountFrom.getBalance()));
        assertEquals(0, new BigDecimal("70.00").compareTo(accountTo.getBalance()));
    }

    @Test
    @Sql(scripts = {"classpath:sql/insert_account_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:sql/truncate_account_test_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void transferMoneyInsufficientFunds() {
        Long fromUserId = 9999999999L;
        Long toUserId = 9999999998L;
        BigDecimal amount = new BigDecimal("200.00");

        AccountEntity accountFrom = accountEntityRepository.findByUserId(fromUserId).get();
        AccountEntity accountTo = accountEntityRepository.findByUserId(toUserId).get();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            internalAccountService.performTransfer(accountFrom, accountTo, amount);
        });

        assertEquals("Insufficient funds", exception.getMessage());
    }

    @Test
    @Sql(scripts = {"classpath:sql/insert_account_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:sql/truncate_account_test_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void transferMoneySameUser() {
        Long fromUserId = 9999999999L;
        Long toUserId = 9999999999L;
        BigDecimal amount = new BigDecimal("100.00");

        Exception exception = assertThrows(AccountTransferException.class, () -> {
            accountService.transferMoney(fromUserId, toUserId, amount);
        });

        assertEquals("Transfer could not be completed. Please try again later.",
                exception.getMessage());
        assertEquals("Cannot transfer money to yourself",
                exception.getCause().getMessage());
    }

}