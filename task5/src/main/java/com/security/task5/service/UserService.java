package com.security.task5.service;

import com.security.task5.dto.UserDTO;
import com.security.task5.model.User;
import com.security.task5.repository.UserRepository;
import com.security.task5.utils.PasswordEncoder;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User login(final UserDTO userDTO) {
        final User user = userRepository.findByLogin(userDTO.getLogin());
        if (user == null || !passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            return null;
        }
        return user;
    }

    public boolean saveNewUser(final UserDTO userdto) {
        final User userFromDb = userRepository.findByLogin(userdto.getLogin());

        if (userFromDb != null) {
            log.warn("login not unique!");
            return false;
        }
        final String userPassword = userdto.getPassword();

        final String hash = passwordEncoder.encode(userPassword);

        final User user = User
                .builder()
                .login(userdto.getLogin())
                .password(hash)
                .build();

        userRepository.save(user);
        log.info("User was saved. Username : " + user.getLogin());
        return true;
    }
}
