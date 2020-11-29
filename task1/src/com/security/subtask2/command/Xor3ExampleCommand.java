package com.security.subtask2.command;

import com.security.util.ExampleCommand;

import java.util.*;
import java.util.stream.Collectors;

import static com.security.util.Constants.*;


//Now try a repeating-key XOR cipher. E.g. it should take a string "hello world" and, given the key is "key",
//xor the first letter "h" with "k", then xor "e" with "e", then "l" with "y", and then xor next char "l" with "k" again,
//then "o" with "e" and so on. You may use an index of coincidence, Hamming distance, Kasiski examination, statistical 
//tests or whatever method you feel would show the best result.

//Find key length with IoC then frequency analysis
public class Xor3ExampleCommand implements ExampleCommand {

    @Override
    public void execute() {
//        System.out.println("KEY LENGTH: " + findKeyLength());
        decipherXorRepeatingKey();
    }

    private String decipherXorRepeatingKey() {
        String deciphered = "";

        //Find key length
        int keyLength = findKeyLength();
        //Split into groups by key
        List<String> groupedChars = appendGroupedChars(keyLength);

        groupedChars.forEach(System.out::println);
        //TODO: add frequency analysis
        return deciphered;
    }

    private List<String> appendGroupedChars(int keyLength) {
        List<String> groups = new ArrayList<>();

        for (int groupKey = 0; groupKey < keyLength; groupKey++) {
            StringBuilder cipheredByMutualChar = new StringBuilder();

            for (int j = 0; j < SUBTASK2_CIPHERED.length(); j++) {

                if (j % (keyLength + groupKey) == 0) { //TODO: revise if works properly, SUGGESTION: might miss first 6 elements
                    cipheredByMutualChar.append(SUBTASK2_CIPHERED.charAt(j));
                }
            }
            groups.add(cipheredByMutualChar.toString());
        }
        return groups;
    }

    private int findKeyLength() {
        Map<Integer, Double> orderToFrequencyMap = new LinkedHashMap<>(SUBTASK2_CIPHERED.length());

        for (int shiftCount = 1; shiftCount < SUBTASK2_CIPHERED.length() - 1; shiftCount++) {
            double coincidenceCount = getCoincidenceFrequency(shift(shiftCount));
            orderToFrequencyMap.put(shiftCount, coincidenceCount);
        }

        return getKeyDiffFromMap(orderToFrequencyMap);
    }

    private String shift(int count) {
        String leftSubString = SUBTASK2_CIPHERED.substring(0, count);
        String rightSubString = SUBTASK2_CIPHERED.substring(count + 1);

        return rightSubString + leftSubString;
    }

    private double getCoincidenceFrequency(String shiftedCiphered) {
        return (double) countOccurrences(shiftedCiphered) / (double) SUBTASK2_CIPHERED.length();
    }

    private Integer getKeyDiffFromMap(Map<Integer, Double> orderToFrequencyMap) {
        return filterFrequencyMap(orderToFrequencyMap).stream()
                .map(Map.Entry::getKey)
                .sorted()
                .limit(2)
                .reduce((x, y) -> y - x)
                .orElse(0);
    }

    private Set<Map.Entry<Integer, Double>> filterFrequencyMap(Map<Integer, Double> orderToFrequencyMap) {
        Set<Map.Entry<Integer, Double>> entries = orderToFrequencyMap.entrySet();
        return entries.stream()
                .filter(x -> x.getValue() > 0.175d)
                .collect(Collectors.toSet());
    }

    private long countOccurrences(String shifted) {
        int countOfOccurrences = 0;

        for (int i = 0; i < shifted.length(); i++) {
            if (isOnMutualPosition(shifted, i)) {
                countOfOccurrences++;
            }
        }
        return countOfOccurrences;
    }

    private boolean isOnMutualPosition(String shifted, int i) {
        return SUBTASK2_CIPHERED.charAt(i) == shifted.charAt(i);
    }

    private List<Character> getChars(String ciphered) {
        return Arrays.stream(ciphered.split(""))
                .map(x -> x.charAt(0))
                .collect(Collectors.toList());
    }
}