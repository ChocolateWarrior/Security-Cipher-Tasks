package com.security.task5.service;

import com.security.task5.dto.UserDTO;
import com.security.task5.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public boolean login(final UserDTO userDTO) {
        return true;
    }

    public boolean saveNewUser(final UserDTO userdto) {
        return true;
    }
}
