package com.security.subtask2.command;

import com.security.util.ExampleCommand;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.security.util.Constants.ENGLISH_LETTERS_FREQUENCY;
import static com.security.util.Constants.SUBTASK2_CIPHERED;


//Now try a repeating-key XOR cipher. E.g. it should take a string "hello world" and, given the key is "key",
//xor the first letter "h" with "k", then xor "e" with "e", then "l" with "y", and then xor next char "l" with "k" again,
//then "o" with "e" and so on. You may use an index of coincidence, Hamming distance, Kasiski examination, statistical 
//tests or whatever method you feel would show the best result.

//Find key length with IoC then frequency analysis
public class Xor3ExampleCommand implements ExampleCommand {

    @Override
    public void execute() throws DecoderException {
//        System.out.println("KEY LENGTH: " + findKeyLength());
        decipherXorRepeatingKey();
    }

    private String decipherXorRepeatingKey() throws DecoderException {
        final String deciphered;
        final byte[] bytes = Hex.decodeHex(SUBTASK2_CIPHERED.toCharArray());
        final String cipher = new String(bytes, StandardCharsets.UTF_8);
        //Find key length
        final int keyLength = findKeyLength(cipher);
        //Split into groups by key
        final List<StringBuilder> groupedChars = appendGroupedChars(keyLength, cipher);

        for (int i = 0; i < groupedChars.size(); i++) {
            System.out.println(i + ": ===== " + groupedChars.get(i));
        }
        System.out.println("===================================");
        groupedChars.stream()
                .peek(g -> System.out.println("\n" + "\n" + "\n" + g))
                .map(this::calculateFrequencyMap)
                .peek(g -> System.out.println("size: " + g.size()))
                .forEach(System.out::println);
        System.out.println("=======================================");

        ENGLISH_LETTERS_FREQUENCY.keySet().forEach(potentialEncodedChar -> {
            System.out.println(potentialEncodedChar + " ========= ");
            groupedChars.get(0).toString().chars()
                    .map(encoded -> (char) encoded ^ (potentialEncodedChar ^ '.'))
                    .forEach(ch -> System.out.print(Character.toString(ch)));
            System.out.println();
        });

        final String key = "" + (char) ('.' ^ 'e') + (char) ('A' ^ 'r') + (char) ('K' ^ ' ');
        System.out.println(key);
        deciphered = decodeByKey(cipher, key);
        System.out.println(deciphered);
        return deciphered;
    }

    private String decodeByKey(final String cipher, final String key) {
        final StringBuilder result = new StringBuilder(cipher.length());
        final String[] resultArray = new String[cipher.length()];
        for (int i = 0; i < key.length(); i++) {
            for (int j = 0; j < cipher.length(); j++) {
                if (j % key.length() == i) {
                    resultArray[j] = String.valueOf((char) (key.charAt(i) ^ cipher.charAt(j)));
                }
            }
        }
        for (int i = 0; i < cipher.length() - 40; i++) {
            result.append(resultArray[i]);
        }
        return result.toString();
    }

    private List<StringBuilder> appendGroupedChars(final int keyLength, final String cipher) {
        final List<StringBuilder> groups = new ArrayList<>();
        for (int i = 0; i < keyLength; i++) {
            groups.add(new StringBuilder());
        }
        for (int i = 0; i < cipher.length(); i++) {
            groups.get(i % keyLength).append(cipher.charAt(i));
        }
        return groups;
    }

    private Map<Character, Double> calculateFrequencyMap(final StringBuilder stringBuilder) {
        final Map<Character, Double> frequencyMap = new HashMap<>();
        stringBuilder.chars()
                .forEach(letter -> {
                    if (frequencyMap.get((char) letter) == null) {
                        frequencyMap.put((char) letter, 1d);
                    } else {
                        double num = frequencyMap.get((char) letter) + 1;
                        frequencyMap.put((char) letter, num);
                    }
                });
        frequencyMap.replaceAll((k, v) -> v = v / stringBuilder.length());
        return frequencyMap;
    }

    private int findKeyLength(final String cipher) {
        final Map<Integer, Double> orderToFrequencyMap = new LinkedHashMap<>(cipher.length());

        for (int shiftCount = 1; shiftCount < cipher.length() - 1; shiftCount++) {
            double coincidenceCount = getCoincidenceFrequency(shift(shiftCount, cipher), cipher);
            orderToFrequencyMap.put(shiftCount, coincidenceCount);
        }
        return getKeyDiffFromMap(orderToFrequencyMap);
    }

    private String shift(int count, final String cipher) {
        final String leftSubString = cipher.substring(0, count);
        final String rightSubString = cipher.substring(count + 1);

        return rightSubString + leftSubString;
    }

    private double getCoincidenceFrequency(final String shiftedCiphered, final String cipher) {
        return (double) countOccurrences(shiftedCiphered, cipher) / (double) cipher.length();
    }

    private Integer getKeyDiffFromMap(final Map<Integer, Double> orderToFrequencyMap) {
        return filterFrequencyMap(orderToFrequencyMap).stream()
                .map(Map.Entry::getKey)
                .sorted()
                .limit(2)
                .reduce((x, y) -> y - x)
                .orElse(0);
    }

    private Set<Map.Entry<Integer, Double>> filterFrequencyMap(final Map<Integer, Double> orderToFrequencyMap) {
        Set<Map.Entry<Integer, Double>> entries = orderToFrequencyMap.entrySet();
        return entries.stream()
                .filter(x -> x.getValue() > 0.03d)
                .collect(Collectors.toSet());
    }

    private long countOccurrences(final String shifted, final String cipher) {
        int countOfOccurrences = 0;

        for (int i = 0; i < shifted.length(); i++) {
            if (isOnMutualPosition(shifted, i, cipher)) {
                countOfOccurrences++;
            }
        }
        return countOfOccurrences;
    }

    private boolean isOnMutualPosition(final String shifted, int i, final String cipher) {
        return cipher.charAt(i) == shifted.charAt(i);
    }
}