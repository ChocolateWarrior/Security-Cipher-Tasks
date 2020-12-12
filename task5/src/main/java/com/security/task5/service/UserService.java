package com.security.task5.service;

import com.security.task5.dto.UserDTO;
import com.security.task5.model.User;
import com.security.task5.repository.UserRepository;
import com.security.task5.utils.PasswordEncoder;
import com.security.task5.utils.SecretKeyGen;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.security.task5.utils.Constants.*;

@Log4j2
@Service
public class UserService {

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
        if (Objects.isNull(user) || !passwordEncoder.matches(userDTO.getPassword(), user.getPassword())
                || !code.equals(userDTO.getNumber())) {
            return LOGIN_FAILED;
        }
        if (user.getCompromised()){
            final String key = markUserCompromised(user);
            return YOU_WERE_COMPROMISED + key + PLEASE_RESET_YOUR_PASSWORD;
        }
        return LOGIN_SUCCESS;
    }

    private String markUserCompromised(User user) {
        final String key = secretKeyGen.generateSecretKey();
        user.setCompromised(false);
        user.setKey(key);
        userRepository.save(user);
        return key;
    }

    public String saveNewUser(final UserDTO userDto) {
        final User userFromDb = userRepository.findByLogin(userDto.getLogin());

        if (userFromDb != null) {
            log.warn(LOGIN_NOT_UNIQUE);
            return LOGIN_NOT_UNIQUE;
        }
        final String userPassword = userDto.getPassword();
        final Matcher pwMatcher = pwPattern.matcher(userPassword);

        if (!pwMatcher.matches()) {
            log.warn(PASSWORD_IS_NOT_SECURE);
            return PASSWORD_IS_NOT_SECURE;
        }

        final String hash = passwordEncoder.encode(userPassword);
        final String key = secretKeyGen.generateSecretKey();
        final User user = buildUser(userDto, hash, key);

        userRepository.save(user);
        log.info(USER_WAS_SAVED_USERNAME + user.getLogin());
        return key;
    }

    private User buildUser(UserDTO userDto, String hash, String key) {
        return User.builder()
                .login(userDto.getLogin())
                .password(hash)
                .compromised(false)
                .version(env.getProperty(ENCRYPTION_VERSION))
                .key(key)
                .build();
    }
}
