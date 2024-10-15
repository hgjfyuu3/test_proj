package ru.test.proj.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UsersDto implements Serializable {

    private String name;

    private LocalDate birthday;

}