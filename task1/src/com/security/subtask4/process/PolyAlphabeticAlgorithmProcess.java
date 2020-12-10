package com.security.subtask4.process;

import com.security.subtask4.entities.Individual;
import com.security.subtask4.entities.Population;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.security.util.CiphersUtils.ENG_TRIGRAM_FREQUENCY_MAP;
import static com.security.util.Constants.*;

public class PolyAlphabeticAlgorithmProcess extends Thread {

    private String result;
    private String ciphered;
    private List<String> keys;

    @Override
    public void run() {

        int keyLength = findKeyLength();
        List<Population> populationGroup = getInitialPopulationGroup(keyLength);

        for (int i = 0; i < MAX_GENERATION_SIZE; i++) {
            List<Population> fittestPopulationGroup = new ArrayList<>();
            for (int j = 0; j < populationGroup.size(); i++) {
                fittestPopulationGroup.add(getNextPopulation(populationGroup.get(j)));
            }
            populationGroup = fittestPopulationGroup;
            if (i % SHARE_PERIOD == 0) {
                shareKeys();
            }
        }

        keys = populationGroup.get(0).getKeyGroup().stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        result = decodeBySubstitutionGrouped(SUBTASK4_CIPHERED, keys);

    }

    private int findKeyLength() {
        int keyLength = 0;
        Map<Character, Double> monogramToFrequencyMap = populateMonogramToFrequencyMap();

        return keyLength;
    }

    private Map<Character, Double> populateMonogramToFrequencyMap() {
        Map<Character, Double> monogramToFrequencyMap = new HashMap<>();
        Map<Character, Integer> monogramToCountMap = new HashMap<>();
        IntStream.range(0, SUBTASK4_CIPHERED.length())
                .mapToObj(x -> ALPHABET.toUpperCase().charAt(x))
                .filter(x -> ALPHABET.toUpperCase().contains(x.toString()))
                .forEach(x -> {
                    if (monogramToCountMap.containsKey(x)) {
                        Integer count = monogramToCountMap.get(x);
                        monogramToCountMap.put(x, count + 1);
                    } else {
                        monogramToCountMap.put(x, 1);
                    }
                });

        monogramToCountMap.forEach((k, v) -> monogramToFrequencyMap.put(k, (double) v / SUBTASK4_CIPHERED.length()));
        return monogramToFrequencyMap;
    }

    private void shareKeys() {

    }

    private Population getNextPopulation(Population parentPopulation) {

        Population nextPopulation = Population.empty();
        nextPopulation.setGroupIndex(parentPopulation.getGroupIndex());
        nextPopulation.setKeyGroup(parentPopulation.getKeyGroup());

        for (int i = checkElitism(parentPopulation, nextPopulation); i < parentPopulation.getIndividuals().size(); i++) {
            Individual father = selectionTournament(parentPopulation);
            Individual mother = selectionTournament(parentPopulation);

            List<Individual> offspring = crossover(father, mother);
            offspring.forEach(this::performMutation);


            offspring.forEach(x -> {
                setFitness(getFitness(x.getKey().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining())));
            });

            offspring.forEach(nextPopulation::addIndividual);
        }

        return nextPopulation;
    }

    private int checkElitism(Population parentPopulation, Population nextPopulation) {
        if (IS_ELITISM) {
            nextPopulation.addIndividual(getFittestFromPopulation(parentPopulation));
            return 1;
        }
        return 0;
    }

    public double getFitness(String key) {
        double fitness;
        String decoded = decodeBySubstitution(SUBTASK4_CIPHERED, key);

        Map<String, Double> decodedTrigramToFrequencyMap = new HashMap<>(decoded.length());

        populateDecodedTrigramMap(decoded, decodedTrigramToFrequencyMap);

        fitness = decodedTrigramToFrequencyMap.entrySet().stream()
                .filter(entry -> ENG_TRIGRAM_FREQUENCY_MAP.containsKey(entry.getKey()))
                .mapToDouble(entry -> {
                    Double frequencyInConstant = ENG_TRIGRAM_FREQUENCY_MAP.get(entry.getKey());
                    Double frequencyInDecoded = entry.getValue();

                    return frequencyInDecoded * (Math.log10(frequencyInConstant) / Math.log10(2.0d));
                })
                .sum();

        return fitness;
    }

    private void populateDecodedTrigramMap(String decoded, Map<String, Double> decodedTrigramMap) {
        Map<String, Integer> trigramToCountMap = new HashMap<>();
        IntStream.range(0, decoded.length() - 3)
                .mapToObj(i -> decoded.substring(i, i + 3))
                .forEach(trigram -> {
                    if (trigramToCountMap.containsKey(trigram)) {
                        Integer count = trigramToCountMap.get(trigram);
                        trigramToCountMap.put(trigram, count + 1);
                    } else {
                        trigramToCountMap.put(trigram, 1);
                    }
                });

        trigramToCountMap.forEach((key, value) -> decodedTrigramMap.put(key, value / (double) (decoded.length() - 2)));
    }

    private List<Individual> crossover(Individual father, Individual mother) {
        List<Individual> offspring = new ArrayList<>();

        Individual leftChild = getChild(true, father, mother);
        Individual rightChild = getChild(false, father, mother);

        offspring.add(leftChild);
        offspring.add(rightChild);

        return offspring;
    }

    private Individual getChild(boolean directionAscending, Individual father, Individual mother) {
        Individual child = Individual.empty();
        List<Character> childKey = new ArrayList<>();
        if (directionAscending) {
            for (int i = 0; i < ALPHABET.length(); i++) {
                Character fatherCurrent = father.getKey().get(i);
                Character motherCurrent = mother.getKey().get(i);

                Double fatherCharacterFrequency = ENGLISH_LETTERS_FREQUENCY.get(Character.toLowerCase(fatherCurrent));
                Double motherCharacterFrequency = ENGLISH_LETTERS_FREQUENCY.get(Character.toLowerCase(motherCurrent));

                Character mostFrequent;
                Character leastFrequent;

                if (fatherCharacterFrequency >= motherCharacterFrequency) {
                    mostFrequent = fatherCurrent;
                    leastFrequent = motherCurrent;
                } else {
                    mostFrequent = motherCurrent;
                    leastFrequent = fatherCurrent;
                }

                if (childKey.contains(mostFrequent)) {
                    if (childKey.contains(leastFrequent)) {
                        childKey.add(chooseRandom(childKey));
                    } else {
                        childKey.add(leastFrequent);
                    }
                } else {
                    childKey.add(mostFrequent);
                }
            }
        } else {
            for (int i = ALPHABET.length(); i > 0; i--) {
                Character fatherCurrent = father.getKey().get(i);
                Character motherCurrent = mother.getKey().get(i);

                Double fatherCharacterFrequency = ENGLISH_LETTERS_FREQUENCY.get(Character.toLowerCase(fatherCurrent));
                Double motherCharacterFrequency = ENGLISH_LETTERS_FREQUENCY.get(Character.toLowerCase(motherCurrent));

                Character mostFrequent;
                Character leastFrequent;

                if (fatherCharacterFrequency <= motherCharacterFrequency) {
                    mostFrequent = motherCurrent;
                    leastFrequent = fatherCurrent;
                } else {
                    mostFrequent = fatherCurrent;
                    leastFrequent = motherCurrent;
                }

                if (childKey.contains(leastFrequent)) {
                    if (childKey.contains(mostFrequent)) {
                        childKey.add(0, chooseRandom(childKey));
                    } else {
                        childKey.add(0, leastFrequent);
                    }
                } else {
                    childKey.add(0, leastFrequent);
                }
            }
        }

        child.setKey(childKey);

        return child;
    }

    private Character chooseRandom(List<Character> childKey) {
        List<Character> spareCharacters = Arrays.stream(ALPHABET.toUpperCase().split(""))
                .map(x -> x.charAt(0))
                .filter(x -> !childKey.contains(x))
                .collect(Collectors.toList());
        return spareCharacters.get((int) (Math.random() * spareCharacters.size()));
    }

    private List<Character> decodeParent(Individual father) {
        String decodedString = decodeBySubstitution(SUBTASK4_CIPHERED, father.getKey().stream()
                .map(String::valueOf)
                .collect(Collectors.joining()));

        return decodedString.chars()
                .mapToObj(decodedString::charAt)
                .collect(Collectors.toList());
    }

    private void performMutation(final Individual child) {

        List<Character> key = child.getKey();

        IntStream.range(0, ALPHABET.length())
                .forEach(index -> {
                    if (Math.random() <= MUTATION) {
                        swapChars(key);
                    }
                });
    }

    private void swapChars(List<Character> key) {
        final int firstPosition = (int) (Math.random() * ALPHABET.length());
        final int secondPosition = (int) (Math.random() * ALPHABET.length());

        char temp = key.get(firstPosition);
        key.set(firstPosition, key.get(secondPosition));
        key.set(secondPosition, temp);
    }

    private Individual selectionTournament(Population populationToProcess) {
        Population selection = Population.empty();
        populateSelection(populationToProcess, selection, TOURNAMENT_SELECTION_SIZE);

        return getFittestFromPopulation(selection);
    }

    private void populateSelection(Population populationToProcess, Population selection, int sizeOfSelection) {
        List<Individual> existingIndividuals = new ArrayList<>(List.copyOf(populationToProcess.getIndividuals()));
        for (int i = 0; i < sizeOfSelection; i++) {
            int randomId = (int) (Math.random() * populationToProcess.getIndividuals().size());
            selection.addIndividual(existingIndividuals.get(randomId));
            existingIndividuals.remove(randomId);
        }
    }

    private String decodeBySubstitution(String cipher, String key) {
        Map<Character, Character> alphabetCharacterToKeyCharacterMap = new HashMap<>();
        populateAlphabetCharacterToKeyCharacterMap(ALPHABET.toUpperCase(), key, alphabetCharacterToKeyCharacterMap);

        return Arrays.stream(cipher.split(""))
                .map(x -> x.toUpperCase().charAt(0))
                .map(alphabetCharacterToKeyCharacterMap::get)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private String decodeBySubstitutionGrouped(String cipher, List<String> groupedKeys) {
        String decoded = "";

        return decoded;
    }

    private List<Population> getInitialPopulationGroup(int keyLength) {
        return IntStream.range(0, keyLength)
                .mapToObj(this::getInitialPopulation)
                .collect(Collectors.toList());
    }

    private Population getInitialPopulation(int populationIndex) {

        final List<List<Character>> randomKeys = new ArrayList<>();
        populateRandomKeys(randomKeys);

        List<Individual> individuals = randomKeys.stream()
                .map(characters -> {
                    String key = characters.stream().map(String::valueOf).collect(Collectors.joining());
                    return new Individual(characters, getFitness(key));
                }).collect(Collectors.toList());

        return new Population(populationIndex, individuals);
    }

    private void populateAlphabetCharacterToKeyCharacterMap(String alphabet, String key,
                                                            Map<Character, Character> alphabetCharacterToKeyCharacterMap) {
        for (int i = 0; i < alphabet.length(); i++) {
            alphabetCharacterToKeyCharacterMap.put(alphabet.charAt(i), key.charAt(i));
        }
    }

    private Individual getFittestFromPopulation(Population population) {
        return population.getIndividuals().stream()
                .max(Comparator.comparingDouble(Individual::getFitness))
                .orElseThrow();
    }

    private void populateRandomKeys(List<List<Character>> randomKeys) {
        List<Character> alphabet = getAlphabetCharList();

        for (int i = 0; i < alphabet.size(); ) {
            Collections.shuffle(alphabet);
            if (!randomKeys.contains(alphabet)) {
                randomKeys.add(new ArrayList<>(alphabet));
                i++;
            }
        }
    }

    private List<Character> getAlphabetCharList() {
        return Arrays.stream(ALPHABET
                .toUpperCase()
                .split(""))
                .map(x -> x.charAt(0))
                .collect(Collectors.toList());
    }


    public String getCiphered() {
        return ciphered;
    }

    public void setCiphered(String ciphered) {
        this.ciphered = ciphered;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<String> getKeys() {
        return keys;
    }
}
