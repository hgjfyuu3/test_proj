package ru.test.proj.dto;

import lombok.Value;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Value
public class AccountTransferDto {

    @NotNull(message = "Recipient id cannot be null")
    Long recipientId;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.00", inclusive = false)
    @DecimalMax(value = "1000000.00", inclusive = false)
    @Digits(integer = 7, fraction = 2)
    BigDecimal amount;

}