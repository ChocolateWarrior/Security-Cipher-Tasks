package com.security.task5.utils;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class PasswordEncoder {
    private final Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(16, 32, 1, 65536, 10);
    private final SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();

    public final String encode(final String password) {
        final byte[] digest = digestSHA3.digest(password.getBytes());
        System.out.println(digest);
        final String hash = encoder.encode(Arrays.toString(digest));
        System.out.println(hash);
        return hash;
    }

    public final boolean matches(final String password, final String dbValue) {
        final byte[] digest = digestSHA3.digest(password.getBytes());
        return encoder.matches(Arrays.toString(digest), dbValue);
    }
}
