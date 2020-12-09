package com.security.subtask3.command;

import com.security.subtask3.process.GeneticAlgorithmProcess;
import com.security.util.ExampleCommand;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.security.util.Constants.THREAD_POOL_SIZE;

// Write a code to attack some simple substitution cipher. To reduce the complexity of this one we will use only
// uppercase letters, so the keyspace is only 26! To get this one right automatically you will probably need to use some
// sort of genetic algorithm (which worked the best last year), simulated annealing or gradient descent. Seriously,
// write it right now, you will need it to decipher the next one as well. Bear in mind, theres no spaces.
// https://docs.google.com/document/d/1AWywcUIMoGr_cjOMaqjqeSyAyzK93icQE4W-6bDELfQ

public class SubstitutionExampleCommand implements ExampleCommand {

    @Override
    public void execute() throws InterruptedException {
        System.out.println(decipher());
    }

    private String decipher() throws InterruptedException {
        StringBuffer deciphered = new StringBuffer();

        List<GeneticAlgorithmProcess> processes = createThreads();

        processes.forEach(Thread::start);
        Thread.sleep(6000);
        processes.forEach(x -> deciphered.append("\nkey: ")
                .append(x.getKey())
                .append("\nresult: ")
                .append(x.getResult()));

        return deciphered.toString();
    }

    private List<GeneticAlgorithmProcess> createThreads() {
        return IntStream.range(0, THREAD_POOL_SIZE)
                .mapToObj(x -> new GeneticAlgorithmProcess())
                .collect(Collectors.toList());
    }



}
