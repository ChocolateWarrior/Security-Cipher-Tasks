package com.security.subtask4.entities;

import java.util.ArrayList;
import java.util.List;

public class Population {

    private int groupIndex;
    private List<Individual> individuals;
    private List<List<Character>> keyGroup;

    public Population(final int groupIndex, final List<Individual> individuals) {
        this.groupIndex = groupIndex;
        this.individuals = individuals;
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<Individual> individuals) {
        this.individuals = individuals;
    }

    public void addIndividual(Individual individual) {
        this.individuals.add(individual);
    }

    public static Population empty(){
        return new Population(0, new ArrayList<>());
    }

    public int getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
    }

    public void setKeyGroup(List<List<Character>> keyGroup) {
        this.keyGroup = keyGroup;
    }

    public List<List<Character>> getKeyGroup() {
        return keyGroup;
    }

    @Override
    public String toString() {
        return "Population{" +
                "groupIndex=" + groupIndex +
                ", individuals=" + individuals +
                ", keyGroup=" + keyGroup +
                '}';
    }
}
