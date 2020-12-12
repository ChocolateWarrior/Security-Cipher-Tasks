package com.security.subtask4.process;

import com.security.subtask4.entities.Individual;
import com.security.subtask4.entities.Population;
import com.security.subtask4.service.KeyLengthSearchService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.security.util.CiphersUtils.*;
import static com.security.util.Constants.*;

public class PolyAlphabeticAlgorithmProcess extends Thread {

    private String result;
    private List<List<Character>> keys;
    private double maxFitness;
    private final KeyLengthSearchService keyLengthSearchService;

    public PolyAlphabeticAlgorithmProcess(KeyLengthSearchService keyLengthSearchService) {
        this.keyLengthSearchService = keyLengthSearchService;
    }

    @Override
    public void run() {

        int keyLength = (int) keyLengthSearchService.findKeyLength();
        List<Population> populationGroup = getRandomInitialPopulation(keyLength);

        maxFitness = populationGroup.get(0).getIndividuals().get(0).getFitness();
        populationGroup.forEach(System.out::println);

        for (int i = 1; i < MAX_GENERATION_SIZE; i++) {
            List<Population> fittestPopulationGroup = new ArrayList<>();

            for (Population population : populationGroup) {
                fittestPopulationGroup.add(getNextPopulation(population));
            }
            populationGroup = fittestPopulationGroup;

            if (i % SHARE_PERIOD == 0) {
                shareKeys(populationGroup);
                System.out.println("Generation: " + i);

            }
        }

//        System.out.println("Population winner: ");
//        populationGroup.forEach(System.out::println);
        keys = populationGroup.get(0).getKeyGroup();
        result = decodeBySubstitutionGrouped(SUBTASK4_CIPHERED, keys);

//        System.out.println("Max fitness: " + maxFitness);
        System.out.println("Key length: " + keyLength);
        System.out.println("Result: " + result);
    }



    private void shareKeys(List<Population> populationGroup) {
        populationGroup.forEach(population -> population.setKeyGroup(getFittestKey(populationGroup)));
    }

    private List<List<Character>> getFittestKey(List<Population> populationGroup) {
        return populationGroup.stream()
                .map(x -> getFittestFromPopulation(x).getKey())
                .collect(Collectors.toList());
    }

    private Population getNextPopulation(Population parentPopulation) {
        Population nextPopulation = Population.empty();

        nextPopulation.setGroupIndex(parentPopulation.getGroupIndex());
        nextPopulation.setKeyGroup(parentPopulation.getKeyGroup());

        for (int i = checkElitism(parentPopulation, nextPopulation); i < parentPopulation.getIndividuals().size(); ) {

            Individual father = selectionTournament(parentPopulation);
            Individual mother = selectionTournament(parentPopulation);

            List<Individual> offspring = crossover(father, mother);
            offspring.forEach(this::performMutation);

            offspring.forEach(x -> x.setFitness(
                    getFitness(decodeBySubstitutionGrouped(SUBTASK4_CIPHERED,
                            appendKey(parentPopulation, x, parentPopulation.getKeyGroup())))));

            offspring.forEach(nextPopulation::addIndividual);

            i += 2;
        }

        return nextPopulation;
    }

    private List<List<Character>> appendKey(Population parentPopulation, Individual x, List<List<Character>> parentKeys) {
        return IntStream.range(0, parentKeys.size())
                .mapToObj(index -> index == parentPopulation.getGroupIndex() ? x.getKey() :
                        parentKeys.get(index))
                .collect(Collectors.toList());
    }

    public double getInitialFitness(String cipheredChunk, String key) {

        String decoded = decodeBySubstitution(cipheredChunk, key);
        return calculateNGramFitness(decoded, 1, ENG_MONOGRAM_FREQUENCY_MAP, MONOGRAM_WEIGHT);
    }

    //  TODO: add fourgram to analysis
    public double getFitness(String decoded) {

        final double monogramFitness = calculateNGramFitness(decoded, 1, ENG_MONOGRAM_FREQUENCY_MAP, MONOGRAM_WEIGHT);
        final double bigramFitness = calculateNGramFitness(decoded, 2, ENG_BIGRAM_FREQUENCY_MAP, BIGRAM_WEIGHT);
        final double trigramFitness = calculateNGramFitness(decoded, 3, ENG_TRIGRAM_FREQUENCY_MAP, TRIGRAM_WEIGHT);

        double totalFitness = monogramFitness + bigramFitness + trigramFitness;
        if (totalFitness >= maxFitness) {
            maxFitness = totalFitness;
        }
        return totalFitness;
    }

    private double calculateNGramFitness(String decoded, int n, Map<String, Double> constantFrequencyMap, double weight) {
        double fitness;
        Map<String, Double> decodedNgramToFrequencyMap = new HashMap<>(decoded.length());
        populateDecodedNgramMap(decoded, decodedNgramToFrequencyMap, n);

        fitness = decodedNgramToFrequencyMap.entrySet().stream()
                .filter(entry -> constantFrequencyMap.containsKey(entry.getKey()))
                .mapToDouble(entry -> {
                    Double sourceLogFreq = constantFrequencyMap.get(entry.getKey());
                    return entry.getValue() * (Math.log(sourceLogFreq) / Math.log(2.0));
                })
                .sum();

        return fitness;
    }

    private void populateDecodedNgramMap(String decoded, Map<String, Double> decodedNgramMap, int n) {
        Map<String, Integer> trigramToCountMap = new HashMap<>();
        IntStream.range(0, decoded.length() - n)
                .mapToObj(i -> decoded.substring(i, i + n))
                .forEach(trigram -> {
                    if (trigramToCountMap.containsKey(trigram)) {
                        Integer count = trigramToCountMap.get(trigram);
                        trigramToCountMap.put(trigram, count + 1);
                    } else {
                        trigramToCountMap.put(trigram, 1);
                    }
                });

        trigramToCountMap.forEach((key, value) -> decodedNgramMap.put(key, value / (double) (decoded.length() - (n - 1))));
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
            for (int i = 0; i < SUBTASK3_ALPHABET.size(); i++) {
                Character fatherCurrent = father.getKey().get(i);
                Character motherCurrent = mother.getKey().get(i);

                Double fatherCharacterFrequency = SUBTASK3_ALPHABET.get(fatherCurrent);
                Double motherCharacterFrequency = SUBTASK3_ALPHABET.get(motherCurrent);

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
                Character fatherCurrent = father.getKey().get(i - 1);
                Character motherCurrent = mother.getKey().get(i - 1);

                Double fatherCharacterFrequency = SUBTASK3_ALPHABET.get(fatherCurrent);
                Double motherCharacterFrequency = SUBTASK3_ALPHABET.get(motherCurrent);

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
        List<Individual> existingIndividuals = new ArrayList<>(populationToProcess.getIndividuals());

        for (int i = 0; i < sizeOfSelection; i++) {
            int randomId = new Random().nextInt(existingIndividuals.size());
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

    public String decodeBySubstitutionGrouped(String cipher, List<List<Character>> groupedKeys) {
        String result;
        List<Character> decoded = new ArrayList<>();
        List<Character> alphabetCharList = getAlphabetCharList();
        List<Character> cipherCharList = cipher.chars()
                .mapToObj(x -> (char) x)
                .collect(Collectors.toList());

        for (int i = 0; i < cipherCharList.size(); i++) {
            int id = groupedKeys.get(i % groupedKeys.size()).indexOf(cipherCharList.get(i));

            if (id >= 0) {
                decoded.add(alphabetCharList.get(id));
            }
        }
        result = decoded.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());

        return result;
    }

    private List<Population> getRandomInitialPopulation(int keyLength) {

        Map<Integer, List<Character>> cipheredWithSameKeyMap =
                populateIndexToCharacterMap(SUBTASK4_CIPHERED.toCharArray(), keyLength);

        List<Population> initialGroup = IntStream.range(0, keyLength).mapToObj(index ->
                getInitialPopulation(index, POPULATION_SIZE,
                        cipheredWithSameKeyMap.get(index).stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining())))
                .collect(Collectors.toList());

        List<List<Character>> initialKeys = initialGroup.stream()
                .map(this::getFittestFromPopulation)
                .map(Individual::getKey)
                .collect(Collectors.toList());

        initialGroup.forEach(population -> population.setKeyGroup(initialKeys));

        initialGroup.forEach(population ->
                population.getIndividuals().forEach(individual ->
                        individual.setFitness(getFitness(decodeBySubstitutionGrouped(SUBTASK4_CIPHERED,
                                appendKey(population, individual, initialKeys))))));

        return initialGroup;
    }

    private Map<Integer, List<Character>> populateIndexToCharacterMap(final char[] characters, final int keyLength) {

        return IntStream.range(0, characters.length)
                .boxed()
                .collect(Collectors.groupingBy(index -> index % keyLength,
                        Collectors.mapping(index -> characters[index], Collectors.toList())));
    }

    private Population getInitialPopulation(int populationIndex, int populationSize, String cipheredWithSameKey) {

        final List<List<Character>> randomKeys = new ArrayList<>();

        populateRandomKeys(randomKeys, populationSize);

        List<Individual> individuals = randomKeys.stream()
                .map(characters -> {
                    String key = characters.stream().map(String::valueOf).collect(Collectors.joining());
                    return new Individual(characters, getInitialFitness(key, cipheredWithSameKey));
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

    private void populateRandomKeys(List<List<Character>> randomKeys, int size) {
        List<Character> alphabet = getAlphabetCharList();

        for (int i = 0; i < size; ) {
            Collections.shuffle(alphabet);
            if (!randomKeys.contains(alphabet)) {
                randomKeys.add(new ArrayList<>(alphabet));
                i++;
            }
        }
    }

    private int checkElitism(Population parentPopulation, Population nextPopulation) {
        if (IS_ELITISM) {
            nextPopulation.addIndividual(getFittestFromPopulation(parentPopulation));
            return 1;
        }
        return 0;
    }

    private List<Character> getAlphabetCharList() {
        return Arrays.stream(ALPHABET
                .toUpperCase()
                .split(""))
                .map(x -> x.charAt(0))
                .collect(Collectors.toList());
    }

    public String getResult() {
        return result;
    }

    public List<List<Character>> getKeys() {
        return keys;
    }
}
