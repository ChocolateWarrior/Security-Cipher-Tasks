package com.security.subtask3.process;

import com.security.subtask3.entities.Individual;
import com.security.subtask3.entities.Population;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.security.util.CiphersUtils.ENG_TRIGRAM_FREQUENCY_MAP;
import static com.security.util.Constants.*;

public class GeneticAlgorithmProcess extends Thread {

    private String result;
    private String ciphered;
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

    private Population getNextPopulation(Population parentPopulation) {

        Population nextPopulation = Population.empty();

        for (int i = checkElitism(parentPopulation, nextPopulation); i < parentPopulation.getIndividuals().size(); i++) {
            Individual father = selectionTournament(parentPopulation);
            Individual mother = selectionTournament(parentPopulation);

            Individual offspring = crossover(father, mother);
            performMutation(offspring);

            offspring.setFitness(getFitness(offspring.getKey().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining())));

            nextPopulation.addIndividual(offspring);
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
        String decoded = decodeBySubstitution(SUBTASK3_CIPHERED, key);
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

    private Individual crossover(Individual father, Individual mother) {
        Individual offspring = Individual.empty();

        IntStream.range(0, ALPHABET.length())
                .forEach(index -> {
                    List<Character> offspringKey = offspring.getKey();
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
                    List<Integer> emptyPositions = IntStream.range(0, ALPHABET.length())
                            .filter(index -> offspring.getKey().get(index).equals(' '))
                            .boxed()
                            .collect(Collectors.toList());
                    int randomIndex = (int) (Math.random() * emptyPositions.size());

                    offspring.getKey().set(emptyPositions.get(randomIndex), character);
                });

        return offspring;
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

    private String decodeBySubstitution(String cipher, String key) {
        Map<Character, Character> alphabetCharacterToKeyCharacterMap = new HashMap<>();
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

        List<Individual> individuals = randomKeys.stream()
                .map(characters -> {
                    String key = characters.stream().map(String::valueOf).collect(Collectors.joining());
                    return new Individual(characters, getFitness(key));
                }).collect(Collectors.toList());

        return new Population(individuals);
    }

    private void populateAlphabetCharacterToKeyCharacterMap(String alphabet, String key,
                                                            Map<Character, Character> alphabetCharacterToKeyCharacterMap) {
        for (int i = 0; i < alphabet.length(); i++) {
            alphabetCharacterToKeyCharacterMap.put(alphabet.charAt(i), key.charAt(i));
        }
    }

    private void populateSelection(Population populationToProcess, Population selection, int sizeOfSelection) {
        for (int i = 0; i < sizeOfSelection; i++) {
            int randomId = (int) (Math.random() * populationToProcess.getIndividuals().size());
            selection.addIndividual(populationToProcess.getIndividuals().get(randomId));
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

    public String getKey() {
        return key;
    }
}
