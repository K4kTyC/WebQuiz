package com.java.engine.webquiz.model;

import javax.validation.constraints.*;

public class UserDto {

    @NotNull
    @Pattern(regexp = "^([A-Za-z0-9_\\-.])+@([A-Za-z0-9_\\-.])+\\.([A-Za-z]{2,4})$")
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=\\S+$).{5,20}$")
    private String password;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
