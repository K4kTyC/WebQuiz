package com.java.engine.webquiz.model;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class UserDto {

    @NotNull
    @Pattern(regexp = "^([A-Za-z0-9_\\-.])+@([A-Za-z0-9_\\-.])+\\.([A-Za-z]{2,4})$")
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=\\S+$).{5,20}$")
    private String password;
}
