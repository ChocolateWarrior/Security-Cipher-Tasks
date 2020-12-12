package com.security.subtask3.process;

import com.security.subtask3.entities.Individual;
import com.security.subtask3.entities.Population;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.security.util.CiphersUtils.ENG_QUADGRAM_FREQUENCY_MAP;
import static com.security.util.CiphersUtils.ENG_TRIGRAM_FREQUENCY_MAP;
import static com.security.util.Constants.*;

public class GeneticAlgorithmProcess extends Thread {

    private String result;
    private String key;

    @Override
    public void run() {
        Population population = getInitialPopulation();
//        System.out.println(population);
        for (int i = 0; i < MAX_GENERATION_SIZE; i++) {
            Individual fittest = getFittestFromPopulation(population);
//            System.out.println(" Generation: " + i);
//            System.out.println(" Fitness: " + fittest.getFitness() + " , Key: " + fittest.getKey());
            population = getNextPopulation(population);
        }

        key = getFittestFromPopulation(population).getKey().stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
        result = decodeBySubstitution(SUBTASK3_CIPHERED, key);
    }

    private Population getNextPopulation(final Population parentPopulation) {

        final Population nextPopulation = Population.empty();

        for (int i = checkElitism(parentPopulation, nextPopulation); i < parentPopulation.getIndividuals().size(); i++) {
            final Individual father = selectionTournament(parentPopulation);
            final Individual mother = selectionTournament(parentPopulation);

            final Individual offspring = crossover(father, mother);
            performMutation(offspring);

            offspring.setFitness(getFitnessTotal(offspring.getKey().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining())));

            nextPopulation.addIndividual(offspring);
        }

        return nextPopulation;
    }

    private int checkElitism(final Population parentPopulation, final Population nextPopulation) {
        if (IS_ELITISM) {
            nextPopulation.addIndividual(getFittestFromPopulation(parentPopulation));
            return 1;
        }
        return 0;
    }

    private double getFitnessTotal(final String key) {
        return getFitnessNgram(key, ENG_TRIGRAM_FREQUENCY_MAP, 3)
                + getFitnessNgram(key, ENG_QUADGRAM_FREQUENCY_MAP, 4);
    }


    private double getFitnessNgram(final String key, final Map<String, Double> frequencyMap, final int n) {
        final String decoded = decodeBySubstitution(SUBTASK3_CIPHERED, key);
        final Map<String, Double> decodedNgramToFrequencyMap = new HashMap<>(decoded.length());

        populateDecodedMap(decoded, decodedNgramToFrequencyMap, n);

        return decodedNgramToFrequencyMap.entrySet().stream()
                .filter(entry -> frequencyMap.containsKey(entry.getKey()))
                .mapToDouble(entry -> {
                    final Double frequencyInConstant = frequencyMap.get(entry.getKey());
                    final Double frequencyInDecoded = entry.getValue();
                    return frequencyInDecoded * (Math.log10(frequencyInConstant) / Math.log10(2.0d));
                })
                .sum();
    }

    private void populateDecodedMap(final String decoded,
                                    final Map<String, Double> decodedTrigramMap,
                                    final int n) {
        final Map<String, Integer> trigramToCountMap = new HashMap<>();
        IntStream.range(0, decoded.length() - n)
                .mapToObj(i -> decoded.substring(i, i + n))
                .forEach(ngram -> {
                    if (trigramToCountMap.containsKey(ngram)) {
                        final Integer count = trigramToCountMap.get(ngram);
                        trigramToCountMap.put(ngram, count + 1);
                    } else {
                        trigramToCountMap.put(ngram, 1);
                    }
                });

        trigramToCountMap.forEach((key, value) -> decodedTrigramMap.put(key, value / (double) (decoded.length() - (n - 1))));
    }

    private Individual crossover(final Individual father, final Individual mother) {
        final Individual offspring = Individual.empty();

        IntStream.range(0, ALPHABET.length())
                .forEach(index -> {
                    final List<Character> offspringKey = offspring.getKey();
                    Character characterToAdd;
                    if (Math.random() <= CROSSOVER) {
                        characterToAdd = father.getKey().get(index);
                    } else {
                        characterToAdd = mother.getKey().get(index);
                    }

                    if (!offspringKey.contains(characterToAdd)) {
                        offspringKey.set(index, characterToAdd);
                    }
                });

        getAlphabetCharList().stream()
                .filter(character -> !offspring.getKey().contains(character))
                .forEach(character -> {
                    final List<Integer> emptyPositions = IntStream.range(0, ALPHABET.length())
                            .filter(index -> offspring.getKey().get(index).equals(' '))
                            .boxed()
                            .collect(Collectors.toList());
                    int randomIndex = (int) (Math.random() * emptyPositions.size());

                    offspring.getKey().set(emptyPositions.get(randomIndex), character);
                });

        return offspring;
    }

    private void performMutation(final Individual child) {

        final List<Character> key = child.getKey();

        IntStream.range(0, ALPHABET.length())
                .forEach(index -> {
                    if (Math.random() <= MUTATION) {
                        swapChars(key);
                    }
                });
    }

    private void swapChars(final List<Character> key) {
        final int firstPosition = (int) (Math.random() * ALPHABET.length());
        final int secondPosition = (int) (Math.random() * ALPHABET.length());

        final char temp = key.get(firstPosition);
        key.set(firstPosition, key.get(secondPosition));
        key.set(secondPosition, temp);
    }

    private Individual selectionTournament(final Population populationToProcess) {
        final Population selection = Population.empty();
        populateSelection(populationToProcess, selection, TOURNAMENT_SELECTION_SIZE);

        return getFittestFromPopulation(selection);
    }

    private String decodeBySubstitution(final String cipher, final String key) {
        final Map<Character, Character> alphabetCharacterToKeyCharacterMap = new HashMap<>();
        populateAlphabetCharacterToKeyCharacterMap(ALPHABET.toUpperCase(), key, alphabetCharacterToKeyCharacterMap);

        return Arrays.stream(cipher.split(""))
                .map(x -> x.toUpperCase().charAt(0))
                .map(alphabetCharacterToKeyCharacterMap::get)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private Population getInitialPopulation() {

        final List<List<Character>> randomKeys = new ArrayList<>();
        populateRandomKeys(randomKeys);

        final List<Individual> individuals = randomKeys.stream()
                .map(characters -> {
                    final String key = characters.stream().map(String::valueOf).collect(Collectors.joining());
                    return new Individual(characters, getFitnessTotal(key));
                }).collect(Collectors.toList());

        return new Population(individuals);
    }

    private void populateAlphabetCharacterToKeyCharacterMap(final String alphabet,
                                                            final String key,
                                                            final Map<Character, Character> alphabetCharacterToKeyCharacterMap) {
        for (int i = 0; i < alphabet.length(); i++) {
            alphabetCharacterToKeyCharacterMap.put(alphabet.charAt(i), key.charAt(i));
        }
    }

    private void populateSelection(final Population populationToProcess,
                                   final Population selection,
                                   final int sizeOfSelection) {
        for (int i = 0; i < sizeOfSelection; i++) {
            final int randomId = (int) (Math.random() * populationToProcess.getIndividuals().size());
            selection.addIndividual(populationToProcess.getIndividuals().get(randomId));
        }
    }

    private Individual getFittestFromPopulation(final Population population) {
        return population.getIndividuals().stream()
                .max(Comparator.comparingDouble(Individual::getFitness))
                .orElseThrow();
    }

    private void populateRandomKeys(final List<List<Character>> randomKeys) {
        final List<Character> alphabet = getAlphabetCharList();

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

    public String getResult() {
        return result;
    }

    public String getKey() {
        return key;
    }
}