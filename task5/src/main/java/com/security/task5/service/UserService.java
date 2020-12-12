package com.security.task5.service;

import com.security.task5.dto.UserDTO;
import com.security.task5.model.User;
import com.security.task5.repository.UserRepository;
import com.security.task5.utils.PasswordEncoder;
import com.security.task5.utils.SecretKeyGen;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Service
public class
UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment env;
    private final SecretKeyGen secretKeyGen;
    private static final Pattern pwPattern = Pattern.compile("^(?=.*[0-9])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

    public UserService(final UserRepository userRepository,
                       final PasswordEncoder passwordEncoder,
                       final Environment env,
                       final SecretKeyGen secretKeyGen) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
        this.secretKeyGen = secretKeyGen;
    }


    public String login(final UserDTO userDTO) {
        final User user = userRepository.findByLogin(userDTO.getLogin());
        final String code = secretKeyGen.getTOTPCode(user.getKey());
        if (user == null || !passwordEncoder.matches(userDTO.getPassword(), user.getPassword())
                || !code.equals(userDTO.getNumber())) {
            return "login failed!";
        }
        if (user.getCompromised()){
            final String key = secretKeyGen.generateSecretKey();
            user.setCompromised(false);
            user.setKey(key);
            userRepository.save(user);
            return "You was compromised. Your new secret key is: " + key + ". Please reset your password";
        }
        return "login success!";
    }

    public String saveNewUser(final UserDTO userdto) {
        final User userFromDb = userRepository.findByLogin(userdto.getLogin());

        if (userFromDb != null) {
            log.warn("login not unique!");
            return "login not unique!";
        }
        final String userPassword = userdto.getPassword();
        final Matcher pwMatcher = pwPattern.matcher(userPassword);

        if (!pwMatcher.matches()) {
            log.warn("password is not secure!");
            return "password is not secure!";
        }

        final String hash = passwordEncoder.encode(userPassword);
        final String key = secretKeyGen.generateSecretKey();
        final User user = User
                .builder()
                .login(userdto.getLogin())
                .password(hash)
                .compromised(false)
                .version(env.getProperty("encryption.version"))
                .key(key)
                .build();

        userRepository.save(user);
        log.info("User was saved. Username : " + user.getLogin());
        return key;
    }
}
