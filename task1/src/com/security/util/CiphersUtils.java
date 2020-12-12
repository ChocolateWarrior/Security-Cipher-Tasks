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

    public static final Map<String, Double> ENG_TRIGRAM_FREQUENCY_MAP = getNgrams(3, "resources/english_trigrams.txt");
    public static final Map<String, Double> ENG_QUADGRAM_FREQUENCY_MAP = getNgrams(4, "resources/english_quadgrams.txt");

    public static String readFromFile(final String path) {
        String data = "";
        try {
            data = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static double getTotal(final String filename, final int parseCount) {
        double total = 0d;
        try (BufferedInputStream ignored = new BufferedInputStream(new FileInputStream(filename))) {
            total = Files.lines(Path.of(filename))
                    .map(s -> Double.parseDouble(s.substring(parseCount)))
                    .mapToDouble(frequency -> frequency)
                    .sum();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total;
    }

    private static Map<String, Double> getNgrams(final int n, final String filename) {
        final Map<String, Double> ngrams = new HashMap<>();
        final double total = getTotal(filename, n + 1);
        try (BufferedInputStream ignored = new BufferedInputStream(new FileInputStream(filename))) {
            Files.lines(Path.of(filename))
                    .forEach(s -> {
                        String ngram = s.substring(0, n);
                        double frequency = Double.parseDouble(s.substring(n + 1)) / total;
                        ngrams.put(ngram, frequency);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ngrams;
    }
}
