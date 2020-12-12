package com.security.task5.controller;

import com.security.task5.dto.UserDTO;
import com.security.task5.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public String saveUser(@RequestBody UserDTO user) {
        return userService.saveNewUser(user);
    }

    @PostMapping("login")
    public String login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }
}
