package ru.test.proj.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
public class UserUpdateDto {

    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email format")
    private String oldEmail;

    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email format")
    private String newEmail;

    @Pattern(regexp = "^79\\d{9}$", message = "Phone must be in format 79XXXXXXXXX")
    private String oldPhone;

    @Pattern(regexp = "^79\\d{9}$", message = "Phone must be in format 79XXXXXXXXX")
    private String newPhone;

}