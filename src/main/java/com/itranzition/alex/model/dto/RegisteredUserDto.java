package com.itranzition.alex.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RegisteredUserDto implements Serializable {
    private String email;
    private String name;
}
