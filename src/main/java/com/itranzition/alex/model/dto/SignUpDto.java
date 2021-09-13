package com.itranzition.alex.model.dto;

import lombok.Data;

@Data
public class SignUpDto {
    private String email;
    private String name;
    private String password;
    private String confirmPassword;
    private String surname;
}
