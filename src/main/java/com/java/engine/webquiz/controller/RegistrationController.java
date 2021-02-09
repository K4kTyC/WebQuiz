package com.java.engine.webquiz.controller;

import com.java.engine.webquiz.model.UserDto;
import com.java.engine.webquiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping(consumes = "application/json")
    public String register(@RequestBody @Valid UserDto userDto) {
        if (!userService.registerUser(userDto)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with such email is already registered");
        }
        return "You successfully registered";
    }
}
