package com.security.subtask4.service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.security.util.Constants.SUBTASK4_CIPHERED;

public class KeyLengthSearchService {
    public static final int LENGTH = 2000;

    public long findKeyLength() {
        long keyLength = 0;

        Map<Integer, Double> characterToCountMap = new HashMap<>();
        for (int i = 1; i < SUBTASK4_CIPHERED.length(); i++) {
            populateCharacterToCountMap(characterToCountMap, i);
        }
        double avg = characterToCountMap.values()
                .stream()
                .mapToDouble(value -> value)
                .average()
                .orElseThrow(() -> new IllegalArgumentException("Avg not found"));
        int max = characterToCountMap.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new IllegalArgumentException("Key Not Found"));

        double maxCount = characterToCountMap.get(max);
        double step = (maxCount - avg) / LENGTH;
        Map<Integer, Double> IOCMap = new HashMap<>();
        List<Double> frequencies = new ArrayList<>(characterToCountMap.values());

        IntStream.range(0, characterToCountMap.size())
                .forEach(x -> IOCMap.put(x, frequencies.get(x)));

        Map<Long, Long> lengthsMap = IntStream.range(1, LENGTH)
                .mapToObj(index -> index * step)
                .map(bound -> tryKeyLength(getFilteredIndexes(IOCMap, characterToCountMap.get(max) - bound)))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        keyLength = lengthsMap.entrySet()
                .stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new IllegalArgumentException("Key Not Found"));

        return keyLength;
    }

    private void populateCharacterToCountMap(Map<Integer, Double> letterCoincidencesMap, int offset) {

        String shifted = shift(SUBTASK4_CIPHERED, offset);
        int coincidence = (int) IntStream.range(0, SUBTASK4_CIPHERED.length())
                .filter(index -> SUBTASK4_CIPHERED.charAt(index) == shifted.charAt(index))
                .count();

        letterCoincidencesMap.put(offset, (double) coincidence / SUBTASK4_CIPHERED.length());
    }
    
    private List<Integer> getFilteredIndexes(final Map<Integer, Double> IOCMap,
                                             final double bound) {
        return IOCMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() > bound)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private long tryKeyLength(List<Integer> filteredIndexes) {

        return IntStream.range(1, filteredIndexes.size())
                .mapToObj(index -> filteredIndexes.get(index) - filteredIndexes.get(index - 1))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new IllegalArgumentException("Key Not Found"));
    }

    private String shift(String text, int offset) {
        StringBuilder result = new StringBuilder();
        int charsToShift = text.length() - offset;
        return result.append(text.substring(charsToShift))
                .append(text, 0, charsToShift)
                .toString();
    }

}
