package com.security.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.security.util.Constants.SUBTASK3_ALPHABET;

public final class CiphersUtils {

    public static final Map<String, Double> ENG_TRIGRAM_FREQUENCY_MAP = getNgrams(3, "resources/english_trigrams.txt");
    public static final Map<String, Double> ENG_BIGRAM_FREQUENCY_MAP = getNgrams(2, "resources/english_bigrams.txt");
    public static final Map<String, Double> ENG_MONOGRAM_FREQUENCY_MAP = getMonoGrams();

    public static String readFromFile(final String path) {
        String data = "";
        try {
            data = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static double getTotal(String filename, int parseCount) {
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

    private static Map<String, Double> getNgrams(int n, String filename) {
        Map<String, Double> ngrams = new HashMap<>();
        double total = getTotal(filename, n + 1);
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

    private static Map<String, Double> getMonoGrams () {
        Map<Character, Double> constantMap = SUBTASK3_ALPHABET;
        List<Character> characters = new ArrayList<>(constantMap.keySet());
        Map<String, Double> monograms = new HashMap<>();

        IntStream.range(0, constantMap.size())
                .forEach(x -> monograms.put(String.valueOf(characters.get(x)), constantMap.get(characters.get(x))));
        return monograms;
    }

}
