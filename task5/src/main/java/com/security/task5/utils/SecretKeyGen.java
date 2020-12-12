package com.security.task5.utils;

import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class SecretKeyGen {

    private final Base32 base32 = new Base32();
    private final SecureRandom random = new SecureRandom();

    public String generateSecretKey() {
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return base32.encodeToString(bytes);
    }

    public String getTOTPCode(final String secretKey) {
        byte[] bytes = base32.decode(secretKey);
        final String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }
}
