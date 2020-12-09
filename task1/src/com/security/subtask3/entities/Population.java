package com.security.subtask3.entities;

import java.util.ArrayList;
import java.util.List;

public class Population {

    private List<Individual> individuals;

    public Population(List<Individual> individuals) {
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
        return new Population(new ArrayList<>());
    }

    @Override
    public String toString() {
        return "Population {" +
                "individuals = " + individuals +
                '}';
    }
}
