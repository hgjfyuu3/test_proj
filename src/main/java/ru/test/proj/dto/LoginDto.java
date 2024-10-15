package ru.test.proj.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class LoginDto {

    @NotBlank(message = "Username is mandatory")
    @Size(min = 5, max = 200, message = "Username must be between 5 and 200 characters")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 500, message = "Password must be between 8 and 500 characters")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z]).{8,}$", message = "Password must contain at least one letter and one number")
    private String password;

}