package com.security.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public final class CiphersUtils {

    public static final Map<String, Double> ENG_TRIGRAM_FREQUENCY_MAP = getTrigrams();

    public static String readFromFile(final String path) {
        String data = "";
        try {
            data = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static double getTotal() {
        double total = 0d;
        try (BufferedInputStream ignored = new BufferedInputStream(new FileInputStream("resources/english_trigrams.txt"))) {
            total = Files.lines(Path.of("resources/english_trigrams.txt"))
                    .map(s -> Double.parseDouble(s.substring(4)))
                    .mapToDouble(frequency -> frequency)
                    .sum();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total;
    }

    private static Map<String, Double> getTrigrams() {
        Map<String, Double> trigrams = new HashMap<>();
        double total = getTotal();
        try (BufferedInputStream ignored = new BufferedInputStream(new FileInputStream("resources/english_trigrams.txt"))) {
            Files.lines(Path.of("resources/english_trigrams.txt"))
                    .forEach(s -> {
                        String trigram = s.substring(0, 3);
                        double frequency = Double.parseDouble(s.substring(4)) / total;
                        trigrams.put(trigram, frequency);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return trigrams;
    }
}
