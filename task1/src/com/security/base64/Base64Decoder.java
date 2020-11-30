package com.security.base64;

import java.util.stream.Stream;

import static com.security.util.Constants.BASE_64_CHART;


public class Base64Decoder {
    public final String decode(final String cipher) {
        final String binary = getBinaryStringFromCipher(cipher);
        final StringBuilder result = new StringBuilder();
        final StringBuilder binaryBuilder = new StringBuilder(binary);

        for (int i = 0; i < binary.length() / 8; i++) {
            String bin = binaryBuilder.substring(0, 8);
            binaryBuilder.delete(0, 8);
            int decimal = Integer.parseInt(bin, 2);
            result.append((char) decimal);
        }

        return result.toString();
    }

    private String getBinaryStringFromCipher(String cipher) {
        return Stream.of(cipher.split(""))
                .map(BASE_64_CHART::get)
                .map(this::getBinary)
                .reduce("", (s1, s2) -> s1 + s2);
    }

    private String getBinary(final Integer number) {
        if (number == 64) {
            return "00";
        }
        final StringBuilder binary = new StringBuilder(Integer.toString(number, 2));
        int length = binary.length();
        if (length != 6) {
            for (int i = 0; i < 6 - length; i++) {
                binary.insert(0, "0");
            }
        }
        return binary.toString();
    }
}

