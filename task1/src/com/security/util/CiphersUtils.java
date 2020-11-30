package com.security.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CiphersUtils {
    public static String readFromFile(final String path) {
        String data = "";
        try {
            data = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
