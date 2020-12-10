package com.security.subtask4.entities;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.security.util.Constants.ALPHABET;

public class Individual {

    private List<Character> key;
    private double fitness;

    public Individual(List<Character> key, double fitness) {
        this.key = key;
        this.fitness = fitness;
    }

    public List<Character> getKey() {
        return key;
    }

    public double getFitness() {
        return fitness;
    }

    public void setKey(List<Character> key) {
        this.key = key;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public static Individual empty() {
        List<Character> emptyCharacterList = IntStream.range(0, ALPHABET.length())
                .mapToObj(index -> ' ')
                .collect(Collectors.toList());
        return new Individual(emptyCharacterList, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Individual that = (Individual) o;
        return Double.compare(that.fitness, fitness) == 0 &&
                Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, fitness);
    }

    @Override
    public String toString() {
        return "Individual {" +
                "key=" + key +
                ", fitness=" + fitness +
                '}';
    }
}
